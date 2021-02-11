/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.ar;

import dom.jfischer.kaffeekasse.backend.BackendError;
import java.util.Date;
import java.util.List;

/**
 * an accounting period.
 *
 * will be closed by clear command line subcommand.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface AccountPeriod {

    /**
     * delete an entry contained in this account period.
     *
     * check backend's error state after using this method!
     *
     * @param entry a {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry}
     * object.
     */
    void deleteEntry(AccountEntry entry);

    /**
     * get backend's error state.
     *
     * @return backend's error state.
     */
    BackendError getBackendError();

    /**
     * get an identifying token of this accounting period.
     *
     * @return identifying token.
     */
    String getId();

    /**
     * get valid price of a cup of coffee.
     *
     * @return valid price of a cup of coffee.
     */
    int getPrice();

    /**
     * get timestamp of starting accounting period.
     *
     * @return timestamp of starting accounting period.
     */
    Date getTimestamp();

    /**
     * list all accounting entries related to this accounting period.
     *
     * @return list of all accounting entries in current accounting period.
     */
    List<AccountEntry> listEntries();

    /**
     * make all setting made in this object valid.
     */
    void save();

    /**
     * get the related accounting entry for an identifying token.
     *
     * check backend's error state after using this method!
     *
     * @param id identifying token.
     * @return related accounting entry for identifying token.
     */
    AccountEntry searchId(String id);

    /**
     * set valid price for a cup of coffee.
     *
     * check backend's error state after using this method!
     *
     * @param price a int.
     */
    void setPrice(int price);

}
