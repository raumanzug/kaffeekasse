/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package backend.fake;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.Date;
import dom.jfischer.kaffeekasse.backend.BackendRetcodeState;

/**
 * implements {@link Participant}.
 *
 * @author jfischer
 */
public class ParticipantImpl implements Participant {

    /**
     * posting text for registering coffee in command.
     */
    private final String postingTextCoffeeIn = "coffee in";

    /**
     * posting text for registering ooffee out command.
     */
    private final String postingTextCoffeeOut = "coffee out";

    /**
     * posting text for registering pay command.
     */
    private final String postingTextPay = "pay";

    /**
     * instance for storing backend's errors.
     */
    private final BackendRetcodeState backendErrorState;

    /**
     * an dao.
     */
    private final DAOImpl dao;

    /**
     * amount of ct in participant's deposit.
     */
    private int deposit = 0;

    /**
     * nr of cups participant pulled and never was accounted yet.
     */
    private int nrCups = 0;

    /**
     * participant's name.
     */
    private final String name;

    /**
     * identifier.
     */
    private final Integer id;

    /**
     * whether participant is still active.
     */
    private boolean isActive = true;

    /**
     * constructor.
     *
     * @param backendErrorState
     * @param dao
     * @param name
     * @param id
     */
    public ParticipantImpl(
            BackendRetcodeState backendErrorState,
            DAOImpl dao,
            String name,
            Integer id) {
        this.backendErrorState = backendErrorState;
        this.dao = dao;
        this.name = name;
        this.id = id;
    }

    /**
     * implements {@link Participant#clear}.
     */
    @Override
    public void clear() {
        AccountPeriod currentAccountPeriod
                = this.dao.getCurrentAccountPeriod();
        int price = currentAccountPeriod.getPrice();
        this.deposit -= this.nrCups * price;
        this.nrCups = 0;
    }

    /**
     * implements {@link Participant#coffeeIn}.
     *
     * @param amount
     * @param timestamp
     */
    @Override
    public void coffeeIn(int amount, Date timestamp) {
        this.deposit += amount;

        this.dao.addAccountEntry(amount,
                0,
                this,
                postingTextCoffeeIn,
                timestamp);
    }

    /**
     * implements {@link Participant#coffeeOut}.
     *
     * @param nrCups
     * @param timestamp
     */
    @Override
    public void coffeeOut(int nrCups, Date timestamp) {
        this.nrCups += nrCups;

        this.dao.addAccountEntry(0,
                nrCups,
                this,
                postingTextCoffeeOut,
                timestamp);
    }

    /**
     * implements {@link Participant#getBackendRetcode}.
     *
     * @return
     */
    @Override
    public BackendRetcode getBackendRetcode() {
        return this.backendErrorState.getState();
    }

    /**
     * implements {@link Participant#getDeposit}.
     *
     * @return
     */
    @Override
    public int getDeposit() {
        return this.deposit;
    }

    /**
     * implements {@link Participant#getId}.
     *
     * @return
     */
    @Override
    public String getId() {
        return this.id.toString();
    }

    /**
     * implements {@link Participant#getName}.
     *
     * @return
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * implements {@link Participant#getNrCups}.
     *
     * @return
     */
    @Override
    public int getNrCups() {
        return this.nrCups;
    }

    /**
     * implements {@link Participant#inactivate}.
     */
    @Override
    public void inactivate() {
        this.isActive = false;
    }

    /**
     * implements {@link Participant#pay}.
     *
     * @param amount
     * @param timestamp
     */
    @Override
    public void pay(int amount, Date timestamp) {
        this.deposit += amount;
        this.dao.addBankDeposit(amount);

        this.dao.addAccountEntry(amount,
                0,
                this,
                postingTextPay,
                timestamp);
    }

    /**
     * implements {@link Participant#save}.
     */
    @Override
    public void save() {
    }

    /**
     * whether participant is still active.
     *
     * @return
     */
    public boolean isActive() {
        return this.isActive;
    }

}
