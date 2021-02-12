/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.frontend.cmdline;

import dom.jfischer.kaffeekasse.backend.DAO;
import dom.jfischer.kaffeekasse.backend.database.DAOImpl;
import dom.jfischer.kaffeekasse.middleTier.Command;
import dom.jfischer.kaffeekasse.middleTier.CommandImpl;
import dom.jfischer.kaffeekasse.middleTier.TallyPDF;
import dom.jfischer.kaffeekasse.middleTier.TallyPDFImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

/**
 * main class. contains command line parser.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public final class Main {

    /**
     * program version string.
     *
     * result of <code>kaffeekasse --version</code>.
     */
    private static final String PROGRAM_VERSION = "${prog} 0.2.0";

    /**
     * persistence unit required by java persistence api.
     */
    private static final String PERSISTENCE_UNIT_NAME = "kaffeekassePU";

    /**
     * exit code: syntax error at command line.
     */
    private static final int EXIT_SYNTAX_ERROR_AT_CMDLINE = 1;

    /**
     * exit code: error occured int program execution.
     */
    private static final int EXIT_PROGRAM_EXECUTION_ERROR = 2;

    /**
     * exit code: cannot open database connection.
     */
    private static final int EXIT_CANNOT_OPEN_DATABASE = 5;

    /**
     * required entity manager due to using java persistence api.
     */
    private static EntityManager ENTITY_MANAGER;

    /**
     * class used to generate entity managers required by java persistence api.
     */
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    /**
     * class for output to console.
     */
    private static Output OUTPUT;

    /**
     * stores error state transmitted from middletier.
     */
    private static Slave SLAVE;

    /**
     * middletier's interface.
     */
    private static Command FRONTEND;

    /**
     * close database connection.
     */
    private static void close() {
        ENTITY_MANAGER.close();
        ENTITY_MANAGER_FACTORY.close();
    }

    /**
     * opens database connection.
     *
     * @return <code>false</code>: error has appeared, <code>true</code>:
     * database is now open
     */
    private static boolean open() {
        boolean retval = true;

        OUTPUT = new OutputImpl();
        SLAVE = new Slave(OUTPUT);
        try {
            ENTITY_MANAGER_FACTORY
                    = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            ENTITY_MANAGER = ENTITY_MANAGER_FACTORY.createEntityManager();
            DAO dao = new DAOImpl(ENTITY_MANAGER, SLAVE);
            TallyPDF tallyPDF = new TallyPDFImpl(dao, SLAVE);
            FRONTEND = new CommandImpl(dao, SLAVE, tallyPDF);
        } catch (PersistenceException exc) {
            OUTPUT.stderr("Persistence exception has appeared.  Do you started database?");
            retval = false;
        }

        return retval;
    }

    /**
     * command line interpreter.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        /*
         * configure argument parser
         *
         * we uses third party's argparse4j module, cf.
         * {@link https://argparse4j.github.io/index.html}
         */
        ArgumentParsers.setCJKWidthHack(true);
        ArgumentParsers.setSingleMetavar(true);
        ArgumentParsers.setTerminalWidthDetection(true);

        ArgumentParser argumentParser
                = ArgumentParsers.newArgumentParser("kaffeekasse", true)
                        .version(PROGRAM_VERSION);
        argumentParser.addArgument("--version")
                .action(Arguments.version())
                .help("prints program version");

        Subparsers subparsers
                = argumentParser.addSubparsers()
                        .dest("subcommand")
                        .help("choose one command");

        /*
         * add subcommands with its parameters and their types
         *
         * for each subcommand a java statement block is dedicated.
         */
        {
            // addParticipant subcommand
            Subparser addParticipantCommand
                    = subparsers.addParser("addParticipant")
                            .help("add a new participant");
            addParticipantCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
        }
        {
            // clear subcommand
            Subparser clearCommand
                    = subparsers.addParser("clear")
                            .help("turn to next accounting period");
        }
        {
            // deleteEntry subcommand
            Subparser deleteEntryCommand
                    = subparsers.addParser("deleteEntry")
                            .help("delete entry in a given accounting period");
            deleteEntryCommand.addArgument("E")
                    .type(Integer.class)
                    .help("nr of entry");
        }
        {
            // getBankDeposit subcommand
            Subparser getBankDepositCommand
                    = subparsers.addParser("getBankDeposit")
                            .help("get bank deposit");
        }
        {
            // getDeposit subcommand
            Subparser getDepositCommand
                    = subparsers.addParser("getDeposit")
                            .help("get deposit related to participant");
            getDepositCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
        }
        {
            // getNrCups subcommand
            Subparser getNrCupsCommand
                    = subparsers.addParser("getNrCups")
                            .help("get nr of strokes in tally related to participant");
            getNrCupsCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
        }
        {
            // getPrice subcommand
            Subparser getPriceCommand
                    = subparsers.addParser("getPrice")
                            .help("get price per cup of coffee");
        }
        {
            // in subcommand
            Subparser inCommand
                    = subparsers.addParser("in")
                            .help("register coffee");
            inCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
            inCommand.addArgument("A")
                    .type(Integer.class)
                    .help("amount / ct");
        }
        {
            // inactivateParticipant subcommand
            Subparser inactivateParticipantCommand
                    = subparsers.addParser("inactivateParticipant")
                            .help("make a participant inactive");
            inactivateParticipantCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
        }
        {
            // listEntries subcommand
            Subparser listEntriesCommand
                    = subparsers.addParser("listEntries")
                            .help("list entries in a given accounting period");
        }
        {
            // listParticipants subcommand
            Subparser listParticipantsCommand
                    = subparsers.addParser("listParticipants")
                            .help("list active participants");
        }
        {
            // listPeriods subcommands
            Subparser listPeriodsCommand
                    = subparsers.addParser("listPeriods")
                            .help("list accounting periods");
        }
        {
            // out subcommand
            Subparser outCommand
                    = subparsers.addParser("out")
                            .help("register pulled cup of coffee");
            outCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
            outCommand.addArgument("A")
                    .type(Integer.class)
                    .help("amount");
        }
        {
            // pay subcommand
            Subparser payCommand
                    = subparsers.addParser("pay")
                            .help("pay outstanding amount");
            payCommand.addArgument("P")
                    .type(String.class)
                    .help("participant");
            payCommand.addArgument("A")
                    .type(Integer.class)
                    .help("amount / ct");
        }
        {
            // setPrice subcommand
            Subparser setPriceCommand
                    = subparsers.addParser("setPrice")
                            .help("set price per cup of coffee");
            setPriceCommand.addArgument("A")
                    .type(Integer.class)
                    .help("amount / ct");
        }
        {
            // storeTally subcommand
            Subparser storeTallyCommand
                    = subparsers.addParser("storeTally")
                            .help("get tally sheet on which participants make strokes whenever they pull a cup of coffee");
            storeTallyCommand.addArgument("F")
                    .type(String.class)
                    .help("filename");
        }

        try {
            /*
             * parse command line
             *
             * after doing that <code>subcommandStr</code> will contain
             * subcommand
             */
            Namespace cmdlineArgs
                    = argumentParser.parseArgs(args);
            String subcommandStr = cmdlineArgs.getString("subcommand");

            // opens database connection
            if (!open()) {

                // if errors occure stop kaffeekasse program with error code
                System.exit(EXIT_CANNOT_OPEN_DATABASE);
            }

            /*
             * evaluate subcommand
             *
             * for each subcommand there is a method in middletier's interface
             * which will be called.
             */
            switch (subcommandStr) {
                case "addParticipant":
                    FRONTEND.addParticipant(
                            cmdlineArgs.getString("P"));
                    break;
                case "clear":
                    FRONTEND.clear();
                    break;
                case "deleteEntry":
                    FRONTEND.deleteEntry(
                            cmdlineArgs.getString("E"));
                    break;
                case "getBankDeposit":
                    FRONTEND.getBankDeposit();
                    break;
                case "getDeposit":
                    FRONTEND.getDeposit(
                            cmdlineArgs.getString("P"));
                    break;
                case "getNrCups":
                    FRONTEND.getNrCups(
                            cmdlineArgs.getString("P"));
                    break;
                case "getPrice":
                    FRONTEND.getPrice();
                    break;
                case "in":
                    FRONTEND.coffeeIn(
                            cmdlineArgs.getString("P"),
                            cmdlineArgs.getInt("A"));
                    break;
                case "inactivateParticipant":
                    FRONTEND.inactivateParticipant(
                            cmdlineArgs.getString("P"));
                    break;
                case "listEntries":
                    FRONTEND.listEntries();
                    break;
                case "listParticipants":
                    FRONTEND.listParticipants();
                    break;
                case "listPeriods":
                    FRONTEND.listPeriods();
                    break;
                case "out":
                    FRONTEND.coffeeOut(
                            cmdlineArgs.getString("P"),
                            cmdlineArgs.getInt("A"));
                    break;
                case "pay":
                    FRONTEND.pay(
                            cmdlineArgs.getString("P"),
                            cmdlineArgs.getInt("A"));
                    break;
                case "setPrice":
                    FRONTEND.setPrice(
                            cmdlineArgs.getInt("A"));
                    break;
                case "storeTally":
                    FRONTEND.storeTally(
                            cmdlineArgs.getString("F"));
                    break;
                default:
                    throw new CommandDoesNotExistException("subcommand " + subcommandStr + "does not exist.");
            }

            // close database connection.
            close();

        } catch (ArgumentParserException e) {

            /*
             * third party's argparse4j module handles errors occuring at
             * command line. After doing that stop kaffeekasse program.
             */
            argumentParser.handleError(e);
            System.exit(EXIT_SYNTAX_ERROR_AT_CMDLINE);
        }

        /*
         * check if middletier transmitted error state. if errors have appeared
         * kaffeekasse program stops with error code.
         */
        if (SLAVE.isErrorMode()) {
            System.exit(EXIT_PROGRAM_EXECUTION_ERROR);
        }
    }

    /**
     * forbidden default constructor.
     */
    private Main() {

    }
}
