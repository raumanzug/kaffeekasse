/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.frontend.cmdline;

/**
 * console output.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface Output {

    /**
     * console output to stderr.
     *
     * @param message a {@link java.lang.String} object.
     */
    void stderr(String message);

    /**
     * console output to stdout.
     *
     * @param message a {@link java.lang.String} object.
     */
    void stdout(String message);

}
