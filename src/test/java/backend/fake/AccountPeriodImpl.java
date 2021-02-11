/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package backend.fake;

import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.backend.BackendErrorState;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * implements {@link AccountPeriod}.
 *
 * @author jfischer
 */
public class AccountPeriodImpl implements AccountPeriod {

    /**
     * instance for storing backend's errors.
     */
    private final BackendErrorState backendErrorState;

    /**
     * price of cup of coffee.
     */
    private int price;

    /**
     * identifier.
     */
    private final Integer id;

    /**
     * timestamp.
     */
    private final Date timestamp;

    /**
     * accounting entries belonging to this accounting period.
     */
    private final List<AccountEntry> data;

    /**
     * constructor.
     *
     * @param backendErrorState
     * @param price
     * @param id
     * @param timestamp
     */
    public AccountPeriodImpl(
            BackendErrorState backendErrorState,
            int price,
            Integer id,
            Date timestamp) {
        this.backendErrorState = backendErrorState;
        this.price = price;
        this.id = id;
        this.timestamp = timestamp;
        data = new ArrayList<>();
    }

    /**
     * implements {@link AccountPeriod#deleteEntry}.
     *
     * @param entry
     */
    @Override
    public void deleteEntry(AccountEntry entry) {
        this.data.remove(entry);
    }

    /**
     * implements {@link AccountPeriod#getBackendError}.
     *
     * @return
     */
    @Override
    public BackendError getBackendError() {
        return this.backendErrorState.getState();
    }

    /**
     * implements {@link AccountPeriod#getId}.
     *
     * @return
     */
    @Override
    public String getId() {
        return this.id.toString();
    }

    /**
     * implements {@link AccountPeriod#getPrice}.
     *
     * @return
     */
    @Override
    public int getPrice() {
        return this.price;
    }

    /**
     * implements {@link AccountPeriod#getTimestamp}.
     *
     * @return
     */
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * implements {@link AccountPeriod#listPeriods}.
     *
     * @return
     */
    @Override
    public List<AccountEntry> listEntries() {
        return this.data;
    }

    /**
     * implements {@link AccountPeriod#save}.
     */
    @Override
    public void save() {
    }

    /**
     * implements {@link AccountPeriod#searchId}.
     *
     * @param id
     * @return
     */
    @Override
    public AccountEntry searchId(String id) {
        AccountEntry retval = null;

        for (AccountEntry accountEntry : this.data) {
            if (accountEntry.getId().equals(id)) {
                retval = accountEntry;
                break;
            }
        }

        if (retval == null) {

            // if entry not found marks error state.
            this.backendErrorState.setState(BackendError.ENTRY_NOT_FOUND);
        }

        return retval;
    }

    /**
     * implements {@link AccountPeriod#setPrice}.
     *
     * @param price
     */
    @Override
    public void setPrice(int price) {
        if (price == 0) {
            this.backendErrorState.setState(BackendError.ZERO_PRICE);
        } else {
            this.price = price;
        }
    }

    /**
     * adds an accounting entry to accounting period represented by this object.
     *
     * @param amount
     * @param nrCups
     * @param participant
     * @param postingText
     * @param timestamp
     */
    public void addAccountEntry(
            int amount,
            int nrCups,
            Participant participant,
            String postingText,
            Date timestamp) {
        AccountEntry accountEntry
                = new AccountEntryImpl(
                        this.backendErrorState,
                        amount,
                        nrCups,
                        this.data.size(),
                        participant,
                        postingText,
                        timestamp,
                        this
                );
        this.data.add(accountEntry);
    }

}
