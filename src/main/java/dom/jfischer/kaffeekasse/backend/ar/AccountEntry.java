/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.ar;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import java.util.Date;

/**
 * an entry in the ledger.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface AccountEntry {

    /**
     * get the amount / ct of entry.
     *
     * @return amount / ct
     */
    int getAmount();

    /**
     * get error from backend's site.
     *
     * @return error from backend's site.
     */
    BackendRetcode getBackendRetcode();

    /**
     * get identifying token for this entry.
     *
     * @return identifying token.
     */
    String getId();

    /**
     * get the number of cups mentioned in this entry.
     *
     * @return number of cups.
     */
    int getNrCups();

    /**
     * get the participant related to this entry.
     *
     * @return participant.
     */
    Participant getParticipant();

    /**
     * get posting text contained in this entry.
     *
     * @return posting text.
     */
    String getPostingText();

    /**
     * get accounting entry's timestamp.
     *
     * @return accounting entry's timestamp.
     */
    Date getTimestamp();

    /**
     * remove this account entry.
     */
    void remove();

    /**
     * make all settings of this object valid.
     */
    void save();

}
