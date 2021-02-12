/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.Date;
import javax.persistence.EntityManager;
import dom.jfischer.kaffeekasse.backend.BackendRetcodeState;

/**
 * implements {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry} for
 * database backend.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class AccountEntryImpl implements
        AccountEntry {

    /**
     * required entity manager due to using java persistence api.
     */
    private final EntityManager entityManager;

    /**
     * instance for storing backend's errors.
     */
    private final BackendRetcodeState backendErrorState;

    /**
     * instance for storing system state.
     */
    private final State state;

    /**
     * raw object related to ACCOUNT_ENTRY table in database.
     */
    private final dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry rawAccountEntry;

    /**
     * constructor.
     *
     * @param entityManager a {@link javax.persistence.EntityManager} object.
     * @param backendErrorState a
     * {@link dom.jfischer.kaffeekasse.backend.BackendRetcodeState} object.
     * @param state a {@link dom.jfischer.kaffeekasse.backend.database.State}
     * object.
     * @param rawAccountEntry a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry}
     * object.
     */
    public AccountEntryImpl(
            final EntityManager entityManager,
            final BackendRetcodeState backendErrorState,
            final State state,
            final dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry rawAccountEntry) {
        this.entityManager = entityManager;
        this.backendErrorState = backendErrorState;
        this.state = state;
        this.rawAccountEntry = rawAccountEntry;
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getAmount}.
     */
    @Override
    public int getAmount() {
        return this.rawAccountEntry.getAmount();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getBackendRetcode}.
     */
    @Override
    public BackendRetcode getBackendRetcode() {
        return this.backendErrorState.getState();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getId}.
     */
    @Override
    public String getId() {
        return this.rawAccountEntry.getId().toString();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getNrCups}.
     */
    @Override
    public int getNrCups() {
        return this.rawAccountEntry.getNrCups();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getParticipant}.
     */
    @Override
    public Participant getParticipant() {
        return new ParticipantImpl(
                this.backendErrorState,
                this.state,
                this.rawAccountEntry.getParticipantId());
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getPostingText}.
     */
    @Override
    public String getPostingText() {
        return this.rawAccountEntry.getPostingText();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#getTimestamp}.
     */
    @Override
    public Date getTimestamp() {
        return this.rawAccountEntry.getTimestamp();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#remove}.
     */
    @Override
    public void remove() {
        this.entityManager.remove(this.rawAccountEntry);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry#save}.
     */
    @Override
    public void save() {
    }

}
