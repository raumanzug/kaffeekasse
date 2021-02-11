/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database;

import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.backend.BackendErrorState;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntryComparator;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * implements {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod} for
 * database backend.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class AccountPeriodImpl implements
        AccountPeriod {

    /**
     * required entity manager due to using java persistence api.
     */
    private final EntityManager entityManager;

    /**
     * instance for storing backend's errors.
     */
    private final BackendErrorState backendErrorState;

    /**
     * instance for storing system state.
     */
    private final State state;

    /**
     * raw object related to ACCOUNT_PERIOD table in database.
     */
    private final dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod rawAccountPeriod;

    /**
     * constructor.
     *
     * @param entityManager a {@link javax.persistence.EntityManager} object.
     * @param backendErrorState a
     * {@link dom.jfischer.kaffeekasse.backend.BackendErrorState} object.
     * @param state a {@link dom.jfischer.kaffeekasse.backend.database.State}
     * object.
     * @param rawAccountPeriod a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod}
     * object.
     */
    public AccountPeriodImpl(
            final EntityManager entityManager,
            final BackendErrorState backendErrorState,
            final State state,
            final dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod rawAccountPeriod) {
        this.entityManager = entityManager;
        this.backendErrorState = backendErrorState;
        this.state = state;
        this.rawAccountPeriod = rawAccountPeriod;
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#deleteEntry}.
     */
    @Override
    public void deleteEntry(final AccountEntry entry) {
        entry.remove();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#getBackendError}.
     */
    @Override
    public BackendError getBackendError() {
        return this.backendErrorState.getState();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#getId}.
     */
    @Override
    public String getId() {
        return this.rawAccountPeriod.getId().toString();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#getPrice}.
     */
    @Override
    public int getPrice() {
        return this.rawAccountPeriod.getPrice();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#getTimestamp}.
     */
    @Override
    public Date getTimestamp() {
        return this.rawAccountPeriod.getTimestamp();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#listEntries}.
     */
    @Override
    public List<AccountEntry> listEntries() {
        List<AccountEntry> retList
                = new ArrayList<>();
        List<dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry> rawAccountEntryList
                = this.rawAccountPeriod.getAccountEntryList();

        // maps list <code>rawAccountEntryList</code> to <code>retList</code>.
        // apparently, rawAccountEntryList.stream() does not work on jpa queries.
        // that is why we do not use it.
        for (dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry rawAccountEntry : rawAccountEntryList) {
            AccountEntry accountEntry
                    = new AccountEntryImpl(
                            this.entityManager,
                            this.backendErrorState,
                            this.state,
                            rawAccountEntry);
            retList.add(accountEntry);
        }

        Comparator<AccountEntry> comparator
                = new AccountEntryComparator();
        retList.sort(comparator);

        return retList;
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#save}.
     */
    @Override
    public void save() {
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#searchId}.
     */
    @Override
    public AccountEntry searchId(final String id) {
        AccountEntry retval = null;

        try {
            int nr = Integer.parseInt(id);

            // search identifier among the elements of
            // <code>rawAccountEntryList</code>.
            List<dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry> rawAccountEntryList
                    = this.rawAccountPeriod.getAccountEntryList();
            for (dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry entry : rawAccountEntryList) {
                if (nr == entry.getId()) {

                    // found a suitable element make a dto from it.
                    retval = new AccountEntryImpl(
                            this.entityManager,
                            this.backendErrorState,
                            this.state,
                            entry);
                    break;
                }
            }

            if (retval == null) {

                // if entry not found marks error state.
                this.backendErrorState.setState(BackendError.ENTRY_NOT_FOUND);
            }

        } catch (NumberFormatException e) {
            this.backendErrorState
                    .setState(BackendError.NO_VALID_ACCOUNT_ENTRY_KEY);
        }

        return retval;
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod#setPrice}.
     */
    @Override
    public void setPrice(final int price) {
        if (price == 0) {

            // price should never be zero. if so then mark as error.
            this.backendErrorState.setState(BackendError.ZERO_PRICE);
        } else {
            this.rawAccountPeriod.setPrice(price);
        }
    }

}
