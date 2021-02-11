/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.frontend.cmdline;

/**
 * Exception dedicated to command line interpreter who throws this exception if
 * third party's argparse4j module was incorrectly configured.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class CommandDoesNotExistException extends RuntimeException {

    /**
     * Creates a new instance of <code>CommandDoesNotExistException</code>
     * without detail message.
     */
    public CommandDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>CommandDoesNotExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CommandDoesNotExistException(final String msg) {
        super(msg);
    }
}
