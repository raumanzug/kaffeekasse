/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package backend.fake;

import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.backend.BackendErrorState;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.Date;

/**
 * implements {@link AccountEntry}.
 *
 * @author jfischer
 */
public class AccountEntryImpl implements AccountEntry {

    /**
     * instance for storing backend's errors.
     */
    private final BackendErrorState backendErrorState;

    /**
     * amount of ct added to deposit.
     */
    private final int amount;

    /**
     * number of cups of coffee registered.
     */
    private final int nrCups;

    /**
     * identifier for accounting entry.
     */
    private final Integer id;

    /**
     * participant related to this accounting entry.
     */
    private final Participant participant;

    /**
     * posting text.
     */
    private final String postingText;

    /**
     * timestamp.
     */
    private final Date timestamp;

    /**
     * accounting period this entry belongs to.
     */
    private final AccountPeriod accountPeriod;

    /**
     * constructor.
     *
     * @param backendErrorState
     * @param amount
     * @param nrCups
     * @param id
     * @param participant
     * @param postingText
     * @param timestamp
     * @param accountPeriod
     */
    public AccountEntryImpl(
            BackendErrorState backendErrorState,
            int amount,
            int nrCups,
            Integer id,
            Participant participant,
            String postingText,
            Date timestamp,
            AccountPeriod accountPeriod) {
        this.backendErrorState = backendErrorState;
        this.amount = amount;
        this.nrCups = nrCups;
        this.id = id;
        this.participant = participant;
        this.postingText = postingText;
        this.timestamp = timestamp;
        this.accountPeriod = accountPeriod;
    }

    /**
     * implements {@link AccountEntry#getAmount}.
     *
     * @return
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    /**
     * implements {@link AccountEntry#getBackendError}.
     *
     * @return
     */
    @Override
    public BackendError getBackendError() {
        return this.backendErrorState.getState();
    }

    /**
     * implements {@link AccountEntry#getId}.
     *
     * @return
     */
    @Override
    public String getId() {
        return this.id.toString();
    }

    /**
     * implements {@link AccountEntry#getNrCups}.
     *
     * @return
     */
    @Override
    public int getNrCups() {
        return this.nrCups;
    }

    /**
     * implements {@link AccountEntry#getParticipant}.
     *
     * @return
     */
    @Override
    public Participant getParticipant() {
        return this.participant;
    }

    /**
     * implements {@link AccountEntry#getPostingText}.
     *
     * @return
     */
    @Override
    public String getPostingText() {
        return this.postingText;
    }

    /**
     * implements {@link AccountEntry#getTimestamp}.
     *
     * @return
     */
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * implements {@link AccountEntry#remove}.
     */
    @Override
    public void remove() {
        this.accountPeriod.deleteEntry(this);
    }

    /**
     * implements {@link AccountEntry#save}.
     */
    @Override
    public void save() {
    }

}
