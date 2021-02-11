/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.frontend.cmdline;

/**
 * implements {@link dom.jfischer.kaffeekasse.frontend.cmdline.Output}.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class OutputImpl implements Output {

    /**
     * {@inheritDoc}
     *
     * implements {@link Output#stdout}.
     */
    @Override
    public void stdout(final String message) {
        System.out.println(message);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Output#stderr}.
     */
    @Override
    public void stderr(final String message) {
        System.err.println(message);
    }

}
