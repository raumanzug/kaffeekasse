/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.ar;

import java.util.Comparator;

/**
 * <p>
 * AccountEntryComparator class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class AccountEntryComparator implements
        Comparator<AccountEntry> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(AccountEntry arg0, AccountEntry arg1) {
        return arg0.getTimestamp().compareTo(arg1.getTimestamp());
    }

}
