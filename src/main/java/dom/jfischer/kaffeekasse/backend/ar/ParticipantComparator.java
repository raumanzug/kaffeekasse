/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.ar;

import java.util.Comparator;

/**
 * <p>
 * ParticipantComparator class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class ParticipantComparator implements
        Comparator<Participant> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Participant arg0, Participant arg1) {
        return arg0.getName().compareTo(arg1.getName());
    }

}
