/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.Date;
import dom.jfischer.kaffeekasse.backend.BackendRetcodeState;

/**
 * implements {@link dom.jfischer.kaffeekasse.backend.ar.Participant} for
 * database backend.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class ParticipantImpl implements
        Participant {

    /**
     * posting text documenting coffee In command in ledger.
     */
    private final String postingTextCoffeeIn = "coffee in";

    /**
     * posting text documenting coffee Out command in ledger.
     */
    private final String postingTextCoffeeOut = "coffee out";

    /**
     * posting text documenting pay command in ledger.
     */
    private final String postingTextPay = "pay";

    /**
     * instance for storing backend's errors.
     */
    private final BackendRetcodeState backendErrorState;

    /**
     * instance for storing system state.
     */
    private final State state;

    /**
     * raw object related to PARTICIPANT table in database.
     */
    private final dom.jfischer.kaffeekasse.backend.database.entities.Participant rawParticipant;

    /**
     * constructor.
     *
     * @param backendErrorState a
     * {@link dom.jfischer.kaffeekasse.backend.BackendRetcodeState} object.
     * @param state a {@link dom.jfischer.kaffeekasse.backend.database.State}
     * object.
     * @param rawParticipant a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.Participant}
     * object.
     */
    public ParticipantImpl(
            final BackendRetcodeState backendErrorState,
            final State state,
            final dom.jfischer.kaffeekasse.backend.database.entities.Participant rawParticipant) {
        this.backendErrorState = backendErrorState;
        this.state = state;
        this.rawParticipant = rawParticipant;
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.ar.Participant#clear}.
     */
    @Override
    public void clear() {
        int currentPrice = this.state.getPrice();
        int deposit = getDeposit();
        deposit -= currentPrice * getNrCups();
        this.rawParticipant.setDeposit(deposit);
        this.rawParticipant.setNrCups(0);
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#coffeeIn}.
     */
    @Override
    public void coffeeIn(final int amount, final Date timestamp) {
        int newDeposit = getDeposit();
        newDeposit += amount;
        this.rawParticipant.setDeposit(newDeposit);

        this.state.addAccountEntry(
                amount,
                0,
                this.postingTextCoffeeIn,
                timestamp,
                this.rawParticipant);
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#coffeeOut}.
     */
    @Override
    public void coffeeOut(final int nrCups, final Date timestamp) {
        int newNrCups = getNrCups();
        newNrCups += nrCups;
        this.rawParticipant.setNrCups(newNrCups);

        this.state.addAccountEntry(
                0,
                nrCups,
                this.postingTextCoffeeOut,
                timestamp,
                this.rawParticipant);
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#getBackendRetcode}.
     */
    @Override
    public BackendRetcode getBackendRetcode() {
        return this.backendErrorState.getState();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#getDeposit}.
     */
    @Override
    public int getDeposit() {
        return this.rawParticipant.getDeposit();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.ar.Participant#getId}.
     */
    @Override
    public String getId() {
        return this.rawParticipant.getId().toString();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#getName}.
     */
    @Override
    public String getName() {
        return this.rawParticipant.getName();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#getNrCups}.
     */
    @Override
    public int getNrCups() {
        return this.rawParticipant.getNrCups();
    }

    /**
     * {@inheritDoc}
     *
     * implements
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant#inactivate}.
     */
    @Override
    public void inactivate() {
        this.rawParticipant.setIsActive(false);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.ar.Participant#pay}.
     */
    @Override
    public void pay(final int amount, final Date timestamp) {
        this.state.addBankDeposit(amount);

        int deposit = getDeposit();
        deposit += amount;
        this.rawParticipant.setDeposit(deposit);

        this.state.addAccountEntry(
                amount,
                0,
                this.postingTextPay,
                timestamp,
                this.rawParticipant);
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link dom.jfischer.kaffeekasse.backend.ar.Participant#save}.
     */
    @Override
    public void save() {
    }

}
