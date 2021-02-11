/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.backend.DAO;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.Date;
import java.util.List;

/**
 * implements {@link dom.jfischer.kaffeekasse.middleTier.Command}.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class CommandImpl implements Command {

    /**
     * the backend.
     */
    private final DAO dao;

    /**
     * channel for frontend to transmit results.
     */
    private final Slave slave;

    /**
     * object for generating and storing pdf files.
     */
    private final TallyPDF tallyPDF;

    /**
     * constructor.
     *
     * @param dao a {@link dom.jfischer.kaffeekasse.backend.DAO} object.
     * @param slave a {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl}
     * object.
     * @param tallyPDF a {@link dom.jfischer.kaffeekasse.middleTier.TallyPDF}
     * object.
     */
    public CommandImpl(
            final DAO dao,
            final Slave slave,
            final TallyPDF tallyPDF) {
        this.dao = dao;
        this.slave = slave;
        this.tallyPDF = tallyPDF;
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#addParticipant}.
     */
    @Override
    public void addParticipant(final String name) {
        this.dao.open();

        this.dao.addParticipant(name);

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#clear}.
     */
    @Override
    public void clear() {
        this.dao.open();

        this.dao.clear();

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#coffeeIn}.
     */
    @Override
    public void coffeeIn(final String name, final int amount) {
        Date timestamp = new Date();
        this.dao.open();

        Participant participant = this.dao.getParticipant(name);
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            participant.coffeeIn(amount, timestamp);
            participant.save();
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#coffeeOut}.
     */
    @Override
    public void coffeeOut(final String name, final int nr) {
        Date timestamp = new Date();
        this.dao.open();

        Participant participant = this.dao.getParticipant(name);
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            participant.coffeeOut(nr, timestamp);
            participant.save();
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#deleteEntry}.
     */
    @Override
    public void deleteEntry(final String id) {
        this.dao.open();

        AccountPeriod currentAccountPeriod = this.dao.getCurrentAccountPeriod();
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            AccountEntry entry = currentAccountPeriod.searchId(id);
            err = this.dao.getBackendError();
            if (err == BackendError.OK) {
                currentAccountPeriod.deleteEntry(entry);
            }
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#getBankDeposit}.
     */
    @Override
    public void getBankDeposit() {
        this.dao.open();

        int retval = this.dao.getBankDeposit();

        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            this.slave.processJSON(JSONizer.jsonize(retval));
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#getDeposit}.
     */
    @Override
    public void getDeposit(final String name) {
        this.dao.open();

        Participant participant = this.dao.getParticipant(name);
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            this.slave.processJSON(JSONizer.jsonize(participant.getDeposit()));
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#getNrCups}.
     */
    @Override
    public void getNrCups(final String name) {
        this.dao.open();

        Participant participant = this.dao.getParticipant(name);
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            int retval = participant.getNrCups();
            this.slave.processJSON(JSONizer.jsonize(retval));
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#getPrice}.
     */
    @Override
    public void getPrice() {
        this.dao.open();

        AccountPeriod period = this.dao.getCurrentAccountPeriod();
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            int price = period.getPrice();
            this.slave.processJSON(JSONizer.jsonize(price));
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#inactivateParticipant}.
     */
    @Override
    public void inactivateParticipant(final String name) {
        this.dao.open();

        Participant participant = this.dao.getParticipant(name);
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            participant.inactivate();
            participant.save();
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#listEntries}.
     */
    @Override
    public void listEntries() {
        this.dao.open();

        AccountPeriod period = this.dao.getCurrentAccountPeriod();
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            this.slave.processJSON(
                    JSONizer.jsonizeAccountEntryList(period.listEntries()));
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#listParticipants}.
     */
    @Override
    public void listParticipants() {
        this.dao.open();

        List<Participant> participants = this.dao.listActiveParticipants();
        this.slave.processJSON(JSONizer.jsonizeParticipantList(participants));

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#listPeriods}.
     */
    @Override
    public void listPeriods() {
        this.dao.open();

        List<AccountPeriod> periods = this.dao.listPeriods();
        this.slave.processJSON(JSONizer.jsonizeAccountPeriodList(periods));

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#pay}.
     */
    @Override
    public void pay(final String name, final int amount) {
        Date timestamp = new Date();
        this.dao.open();

        Participant participant = this.dao.getParticipant(name);
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            participant.pay(amount, timestamp);
            participant.save();
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#setPrice}.
     */
    @Override
    public void setPrice(final int price) {
        this.dao.open();

        AccountPeriod accountPeriod = this.dao.getCurrentAccountPeriod();
        BackendError err = this.dao.getBackendError();
        if (err == BackendError.OK) {
            accountPeriod.setPrice(price);
            accountPeriod.save();
        }

        this.dao.close();
    }

    /**
     * {@inheritDoc}
     *
     * implements {@link Command#storeTally}.
     */
    @Override
    public void storeTally(final String filename) {
        this.tallyPDF.storeTally(filename);
    }

}
