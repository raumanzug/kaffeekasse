/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.ar;

import dom.jfischer.kaffeekasse.backend.BackendError;
import java.util.Date;

/**
 * a participant.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface Participant {

    /**
     * account for the cups of coffee and reset the tally.
     */
    void clear();

    /**
     * credit the price of a pack of coffee given from participant to the
     * accountant.
     *
     * @param amount a int.
     * @param timestamp a {@link java.util.Date} object.
     */
    void coffeeIn(int amount, Date timestamp);

    /**
     * count the nr of cups of coffee pulled by participant.
     *
     * @param nrCups a int.
     * @param timestamp a {@link java.util.Date} object.
     */
    void coffeeOut(int nrCups, Date timestamp);

    /**
     * get the deposit of participant.
     *
     * @return deposit.
     */
    int getDeposit();

    /**
     * get backend's error state.
     *
     * @return backend;s error state.
     */
    BackendError getBackendError();

    /**
     * get an identifying token for this participant.
     *
     * @return identifying token.
     */
    String getId();

    /**
     * get mnemonic name of this participant which appears in tally list.
     *
     * @return mnemonic name.
     */
    String getName();

    /**
     * get the number of cups of coffee which is not yet accounted.
     *
     * @return number of cups of coffee.
     */
    int getNrCups();

    /**
     * make participant inactive.
     */
    void inactivate();

    /**
     * settle debt.
     *
     * @param amount a int.
     * @param timestamp a {@link java.util.Date} object.
     */
    void pay(int amount, Date timestamp);

    /**
     * make settings made in this object valid.
     */
    void save();

}
