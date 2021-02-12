/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend;

import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.List;

/**
 * data access object.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface DAO {

    /**
     * add a participant with name <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     */
    void addParticipant(String name);

    /**
     * end up a account period and create another one.
     */
    void clear();

    /**
     * close a transaction.
     */
    void close();

    /**
     * get error from backend's site.
     *
     * @return backend's error.
     */
    BackendRetcode getBackendRetcode();

    /**
     * get the deposit at the bank.
     *
     * @return deposit at the bank.
     */
    int getBankDeposit();

    /**
     * get current accounting period.
     *
     * @return current accounting period.
     */
    AccountPeriod getCurrentAccountPeriod();

    /**
     * get the representation of a participant with name <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     * @return participant with name <code>name</code>
     */
    Participant getParticipant(String name);

    /**
     * get a sorted list of all active participants.
     *
     * @return sorted list of all active participants.
     */
    List<Participant> listActiveParticipants();

    /**
     * get a sorted list of all accounting periods.
     *
     * @return sorted list of all accounting periods.
     */
    List<AccountPeriod> listPeriods();

    /**
     * start a transaction.
     *
     * call it before using any other dao's method and after close method.
     */
    void open();

}
