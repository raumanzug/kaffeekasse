/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

/**
 * generate tally sheet for printing out and hanging out. participants should
 * make a stroke on it for each cup of coffee he pulls.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface TallyPDF {

    /**
     * generates a tally sheet onto disk.
     *
     * @param filename a {@link java.lang.String} object.
     */
    void storeTally(String filename);

}
