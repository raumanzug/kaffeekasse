/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database;

import java.util.Date;
import javax.persistence.EntityManager;

/**
 * manages the state of the program system. In particular, manages the bank
 * balance and determines the current accounting period.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class State {

    /**
     * initial price valid til setPrice subcommand set another price.
     */
    private final int DEFAULT_PRICE = 12;

    /**
     * primary key in STATE table which holds state of kaffeekasse's program
     * state.
     */
    private final int STATE_ID = 1;

    /**
     * required entity manager due to using java persistence api.
     */
    private final EntityManager entityManager;

    /**
     * raw object related to ACCOUNT_PERIOD table in database.
     */
    private dom.jfischer.kaffeekasse.backend.database.entities.State rawState = null;

    /**
     * constructur.
     *
     * @param entityManager a {@link javax.persistence.EntityManager} object.
     */
    public State(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * add an entry to the current acounting period in the ledger.
     *
     * @param amount credited amount / ct
     * @param nrCups nr of cups pulled
     * @param postingText a {@link java.lang.String} object.
     * @param timestamp a {@link java.util.Date} object.
     * @param rawParticipant a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.Participant}
     * object.
     */
    public void addAccountEntry(
            final int amount,
            final int nrCups,
            final String postingText,
            final Date timestamp,
            final dom.jfischer.kaffeekasse.backend.database.entities.Participant rawParticipant) {
        dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry rawAccountEntry
                = new dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry();
        rawAccountEntry.setAmount(amount);
        rawAccountEntry.setNrCups(nrCups);
        rawAccountEntry.setPostingText(postingText);
        rawAccountEntry.setTimestamp(timestamp);
        rawAccountEntry.setParticipantId(rawParticipant);
        rawAccountEntry.setAccountPeriodid(getCurrentRawAccountPeriod());
        this.entityManager.persist(rawAccountEntry);
    }

    /**
     * credit an amount onto the bank.
     *
     * @param amount credited amount / ct
     */
    public void addBankDeposit(final int amount) {
        int bankDeposit = getBankDeposit();
        bankDeposit += amount;
        this.rawState.setBankDeposit(bankDeposit);
    }

    /**
     * ends up the current account period and creates a new one.
     */
    public void clear() {
        int price = getPrice();
        createRawAccountPeriod(price);
    }

    /**
     * create the next row in ACCOUNT_PERIOD database table and mark it as
     * current in database table STATE.
     *
     * @param price
     */
    private void createRawAccountPeriod(final int price) {
        dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod rawAccountPeriod
                = new dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod();
        rawAccountPeriod.setPrice(price);
        rawAccountPeriod.setTimestamp(new Date());
        this.entityManager.persist(rawAccountPeriod);
        this.rawState.setAccountPeriodid(rawAccountPeriod);
    }

    /**
     * get deposit in the bank.
     *
     * @return deposit in the bank.
     */
    public int getBankDeposit() {
        load();
        return this.rawState.getBankDeposit();
    }

    /**
     * get raw table row related to current accounting period.
     *
     * @return raw table row related to current accounting period.
     */
    public dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod getCurrentRawAccountPeriod() {
        load();
        return this.rawState.getAccountPeriodid();
    }

    /**
     * get valid price for a cup of coffee / ct.
     *
     * @return price for a cup of coffee / ct.
     */
    public int getPrice() {
        dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod currentRawAccountPeriod
                = getCurrentRawAccountPeriod();
        return currentRawAccountPeriod.getPrice();
    }

    /**
     * initializes database structure in case of first use.
     *
     * do nothing if database structure is already set up.
     */
    private void load() {
        if (this.rawState == null) {

            // if case of first use of method <code>load</code>.
            this.rawState
                    = this.entityManager.find(
                            dom.jfischer.kaffeekasse.backend.database.entities.State.class, STATE_ID);
            if (this.rawState == null) {

                // if database structure does not yet exist,
                // initialize database structure.
                this.rawState
                        = new dom.jfischer.kaffeekasse.backend.database.entities.State(STATE_ID);
                this.rawState.setBankDeposit(0);
                createRawAccountPeriod(DEFAULT_PRICE);
                this.entityManager.persist(this.rawState);
            }
        }
    }

    /**
     * set price of cup of coffee / ct.
     *
     * @param price a int.
     */
    public void setPrice(final int price) {
        dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod currentRawAccountPeriod
                = getCurrentRawAccountPeriod();
        currentRawAccountPeriod.setPrice(price);
    }

}
