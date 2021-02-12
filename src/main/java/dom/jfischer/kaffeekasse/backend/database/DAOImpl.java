/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.DAO;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriodComparator;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import dom.jfischer.kaffeekasse.backend.ar.ParticipantComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import dom.jfischer.kaffeekasse.backend.BackendRetcodeState;

/**
 * implements {@link dom.jfischer.kaffeekasse.backend.DAO} or database backend.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class DAOImpl implements DAO {

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
     * constructor.
     *
     * @param entityManager a {@link javax.persistence.EntityManager} object.
     * @param backendErrorState a
     * {@link dom.jfischer.kaffeekasse.backend.BackendRetcodeState} object.
     */
    public DAOImpl(
            final EntityManager entityManager,
            final BackendRetcodeState backendErrorState) {
        this.entityManager = entityManager;
        this.backendErrorState = backendErrorState;
        this.state
                = new State(entityManager);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#addParticipant}.
     */
    @Override
    public void addParticipant(final String name) {

        boolean isParticipantDoesExist = false;

        // check if participant with name <code>name</code> already exist.
        try {
            this.entityManager.createNamedQuery(
                    "Participant.findByName",
                    dom.jfischer.kaffeekasse.backend.database.entities.Participant.class)
                    .setParameter("name", name)
                    .getSingleResult();
            isParticipantDoesExist = true;
        } catch (NoResultException exc) {
        }

        if (isParticipantDoesExist) {

            // ParticipantImpl with name <code>name</code> already exists. Set error code.
            this.backendErrorState.setState(BackendRetcode.PARTICIPANT_ALREADY_EXISTS);
        } else {

            // participant does not exist yet. add a participant with
            // name <code>name</code>.
            dom.jfischer.kaffeekasse.backend.database.entities.Participant rawParticipant
                    = new dom.jfischer.kaffeekasse.backend.database.entities.Participant();
            rawParticipant.setIsActive(true);
            rawParticipant.setName(name);
            rawParticipant.setNrCups(0);
            rawParticipant.setDeposit(0);
            this.entityManager.persist(rawParticipant);
        }
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#clear}.
     */
    @Override
    public void clear() {
        this.state.clear();
        List<Participant> participantList
                = listActiveParticipants();
        for (Participant participant : participantList) {
            participant.clear();
        }
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#close}.
     */
    @Override
    public void close() {
        this.entityManager.getTransaction().commit();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#getBackendRetcode}.
     */
    @Override
    public BackendRetcode getBackendRetcode() {
        return this.backendErrorState.getState();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#getBankDeposit}.
     */
    @Override
    public int getBankDeposit() {
        return this.state.getBankDeposit();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.DAO#getCurrentAccountPeriod}.
     */
    @Override
    public AccountPeriod getCurrentAccountPeriod() {
        dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod rawCurrentAccountPeriod
                = this.state.getCurrentRawAccountPeriod();
        return new AccountPeriodImpl(
                this.entityManager,
                this.backendErrorState,
                this.state,
                rawCurrentAccountPeriod);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#getParticipant}.
     */
    @Override
    public Participant getParticipant(final String name) {
        ParticipantImpl retval = null;

        try {
            dom.jfischer.kaffeekasse.backend.database.entities.Participant rawParticipant
                    = this.entityManager.createNamedQuery(
                            "Participant.findByName",
                            dom.jfischer.kaffeekasse.backend.database.entities.Participant.class)
                            .setParameter("name", name)
                            .getSingleResult();
            retval = new ParticipantImpl(
                    this.backendErrorState,
                    this.state,
                    rawParticipant);
        } catch (NoResultException e) {
            this.backendErrorState.setState(BackendRetcode.PARTICIPANT_NOT_FOUND);
        }

        return retval;
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.DAO#listActiveParticipants}.
     */
    @Override
    public List<Participant> listActiveParticipants() {
        List<Participant> retList
                = new ArrayList<>();
        List<dom.jfischer.kaffeekasse.backend.database.entities.Participant> rawParticipantList
                = this.entityManager.createNamedQuery(
                        "Participant.findByIsActive",
                        dom.jfischer.kaffeekasse.backend.database.entities.Participant.class)
                        .setParameter("isActive", true)
                        .getResultList();

        // maps all entries of <code>rawParticipantList</code> into
        // <code>retList</code>.
        rawParticipantList
                .stream()
                .map((rawParticipant) -> new ParticipantImpl(
                this.backendErrorState,
                this.state,
                rawParticipant))
                .forEachOrdered((participant) -> {
                    retList.add(participant);
                });

        Comparator<Participant> comparator
                = new ParticipantComparator();
        retList.sort(comparator);
        return retList;
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#listPeriods}.
     */
    @Override
    public List<AccountPeriod> listPeriods() {
        List<AccountPeriod> retList
                = new ArrayList<>();
        List<dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod> rawAccountPeriodList
                = this.entityManager.createNamedQuery(
                        "AccountPeriod.findAll",
                        dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod.class)
                        .getResultList();

        // maps elements of <code>rawAccountPeriodList</code> into
        // <code>retList</code>.
        rawAccountPeriodList
                .stream()
                .map((rawAccountPeriod) -> new AccountPeriodImpl(
                this.entityManager,
                this.backendErrorState,
                this.state,
                rawAccountPeriod))
                .forEachOrdered((accountPeriod) -> {
                    retList.add(accountPeriod);
                });

        Comparator<AccountPeriod> comparator
                = new AccountPeriodComparator(this);
        retList.sort(comparator);
        return new ArrayList<>(retList);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.DAO#open}.
     */
    @Override
    public void open() {
        this.entityManager.getTransaction().begin();
    }

}
