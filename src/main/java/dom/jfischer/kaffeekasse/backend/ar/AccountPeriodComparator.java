/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.ar;

import dom.jfischer.kaffeekasse.backend.DAO;
import java.util.Comparator;

/**
 * <p>
 * AccountPeriodComparator class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class AccountPeriodComparator implements
        Comparator<AccountPeriod> {

    private final DAO dao;

    /**
     * <p>
     * Constructor for AccountPeriodComparator.</p>
     *
     * @param dao a {@link dom.jfischer.kaffeekasse.backend.DAO} object.
     */
    public AccountPeriodComparator(DAO dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(AccountPeriod arg0, AccountPeriod arg1) {
        AccountPeriod currentAccountPeriod
                = this.dao.getCurrentAccountPeriod();
        String currentId = currentAccountPeriod.getId();

        boolean isFirstIsCurrent = arg0.getId().equals(currentId);
        boolean isSecondIsCurrent = arg1.getId().equals(currentId);

        int retval;
        if (isFirstIsCurrent && isSecondIsCurrent) {
            retval = 0;
        } else if (isFirstIsCurrent) {
            retval = 1;
        } else if (isSecondIsCurrent) {
            retval = -1;
        } else {
            retval = arg0.getTimestamp().compareTo(arg1.getTimestamp());
        }

        return retval;
    }

}
