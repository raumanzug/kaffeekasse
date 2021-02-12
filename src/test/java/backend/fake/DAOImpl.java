/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package backend.fake;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.DAO;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dom.jfischer.kaffeekasse.backend.BackendRetcodeState;

/**
 * implements {@link DAO}.
 *
 * @author jfischer
 */
public class DAOImpl implements DAO {

    /**
     * initial price valid til setPrice subcommand set another price.
     */
    private final int DEFAULT_PRICE = 12;

    /**
     * instance for storing backend's errors.
     */
    private final BackendRetcodeState backendErrorState;

    /**
     * bank deposit.
     */
    private int bankDeposit = 0;

    /**
     * the ledger consisting in all the accounting periods.
     */
    private final List<AccountPeriodImpl> ledger;

    /**
     * all the participants.
     */
    private final List<ParticipantImpl> listParticipant;

    /**
     * constructor.
     *
     * @param backendErrorState
     */
    public DAOImpl(BackendRetcodeState backendErrorState) {
        this.backendErrorState = backendErrorState;

        this.ledger = new ArrayList<>();
        {
            Date timestamp = new Date();
            AccountPeriodImpl firstAccountPeriod
                    = new AccountPeriodImpl(
                            this.backendErrorState,
                            DEFAULT_PRICE,
                            0,
                            timestamp
                    );
            this.ledger.add(firstAccountPeriod);
        }

        this.listParticipant = new ArrayList<>();
    }

    /**
     * implements {@link DAO#addParticipant}.
     *
     * @param name
     */
    @Override
    public void addParticipant(String name) {
        Participant testParticipant = seekParticipant(name);
        if (testParticipant == null) {
            ParticipantImpl participant
                    = new ParticipantImpl(
                            this.backendErrorState,
                            this,
                            name,
                            listParticipant.size()
                    );
            this.listParticipant.add(participant);
        } else {
            this.backendErrorState.setState(BackendRetcode.PARTICIPANT_ALREADY_EXISTS);
        }
    }

    /**
     * implements {@link DAO#clear}.
     */
    @Override
    public void clear() {
        Date timestamp = new Date();
        AccountPeriod currentAccountPeriod = getCurrentAccountPeriod();
        int price = currentAccountPeriod.getPrice();
        AccountPeriodImpl newAccountPeriod
                = new AccountPeriodImpl(
                        this.backendErrorState,
                        price,
                        ledger.size(),
                        timestamp
                );
        this.ledger.add(newAccountPeriod);

        List<Participant> listActiveParticipants
                = listActiveParticipants();
        for (Participant participant : listActiveParticipants) {
            participant.clear();
        }
    }

    /**
     * implements {@link DAO#close}.
     */
    @Override
    public void close() {
    }

    /**
     * implements {@link DAO#getBackendRetcode}.
     *
     * @return
     */
    @Override
    public BackendRetcode getBackendRetcode() {
        return this.backendErrorState.getState();
    }

    /**
     * implements {@link DAO#getBankDeposit}.
     *
     * @return
     */
    @Override
    public int getBankDeposit() {
        return this.bankDeposit;
    }

    /**
     * like {@link #getCurrentAccountPeriod()} but results in
     * {@link AccountPeriodImpl} object.
     *
     * we use this method twice: at first to program {@link #addBankDeposit} an
     * at second to program {@link #getCurrentAccountPeriod}.
     *
     * @return current {@link AccountPeriodImpl} object.
     */
    private AccountPeriodImpl getCurrentConcreteAccountPeriod() {
        int lastIndex = this.ledger.size() - 1;
        return this.ledger.get(lastIndex);
    }

    /**
     * implements {@link DAO#getCurrentAccountPeriod}.
     *
     * @return
     */
    @Override
    public AccountPeriod getCurrentAccountPeriod() {
        return getCurrentConcreteAccountPeriod();
    }

    /**
     * seek participant with name <code>name</code>.
     *
     * @param name
     * @return participant with name <code>name</code>.
     */
    private Participant seekParticipant(String name) {

        Participant retval = null;

        List<Participant> listActiveParticipants
                = listActiveParticipants();

        for (Participant participant : listActiveParticipants) {
            if (participant.getName().equals(name)) {
                retval = participant;
            }
        }

        return retval;
    }

    /**
     * implements {@link DAO#getParticipant}.
     *
     * @param name
     * @return
     */
    @Override
    public Participant getParticipant(String name) {

        Participant retval = seekParticipant(name);

        if (retval == null) {
            this.backendErrorState.setState(BackendRetcode.PARTICIPANT_NOT_FOUND);
        }

        return retval;
    }

    /**
     * implements {@link DAO#listActiveParticipants}.
     *
     * @return
     */
    @Override
    public List<Participant> listActiveParticipants() {

        List<Participant> retList
                = new ArrayList<>();
        this.listParticipant
                .stream()
                .filter((participant) -> (participant.isActive()))
                .forEachOrdered((participant) -> {
                    retList.add(participant);
                });

        return retList;
    }

    /**
     * implements {@link DAO#listPeriods}.
     *
     * @return
     */
    @Override
    public List<AccountPeriod> listPeriods() {
        return new ArrayList<>(this.ledger);
    }

    /**
     * implements {@link DAO#open}.
     */
    @Override
    public void open() {
    }

    /**
     * add an amount of ct to bank deposit.
     *
     * @param amount
     */
    public void addBankDeposit(int amount) {
        this.bankDeposit += amount;
    }

    /**
     * add an accounting entry to the current accounting period.
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
        AccountPeriodImpl currentAccountPeriod
                = getCurrentConcreteAccountPeriod();
        currentAccountPeriod.addAccountEntry(
                amount,
                nrCups,
                participant,
                postingText,
                timestamp);
    }

}
