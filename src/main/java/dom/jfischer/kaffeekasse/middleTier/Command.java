/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

/**
 * methods which are called by command line interpreter. for each subcommand
 * there is a method related to it.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface Command {

    /**
     * add a participant with name <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     */
    void addParticipant(String name);

    /**
     * close current account period an create another one.
     */
    void clear();

    /**
     * credit the price of a pack of coffee given from participant to the
     * accountant.
     *
     * @param name a {@link java.lang.String} object.
     * @param amount a int.
     */
    void coffeeIn(String name, int amount);

    /**
     * count the nr of cups of coffee pulled by participant.
     *
     * @param name a {@link java.lang.String} object.
     * @param nr a int.
     */
    void coffeeOut(String name, int nr);

    /**
     * delete entry related to identifying token.
     *
     * @param id identifying token
     */
    void deleteEntry(String id);

    /**
     * get the deposit of the bank.
     */
    void getBankDeposit();

    /**
     * get the deposit of participant with name <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     */
    void getDeposit(String name);

    /**
     * get the number of cups of coffee pulled by participant <code>name</code>
     * which are not yet accounted.
     *
     * @param name a {@link java.lang.String} object.
     */
    void getNrCups(String name);

    /**
     * get valid price of a cup of coffee.
     */
    void getPrice();

    /**
     * make participant with name <code>name</code> inactive.
     *
     * @param name a {@link java.lang.String} object.
     */
    void inactivateParticipant(String name);

    /**
     * list accounting entries contained in current accounting period.
     */
    void listEntries();

    /**
     * list active participant.
     */
    void listParticipants();

    /**
     * list all accounting periods.
     */
    void listPeriods();

    /**
     * settle deposit of participant with name <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     * @param amount a int.
     */
    void pay(String name, int amount);

    /**
     * set valid price of a cup of coffee.
     *
     * @param price a int.
     */
    void setPrice(int price);

    /**
     * stores a tally sheet onto disk.
     *
     * @param filename a {@link java.lang.String} object.
     */
    void storeTally(String filename);

}
