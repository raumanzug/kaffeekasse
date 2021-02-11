/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import dom.jfischer.kaffeekasse.backend.BackendError;

/**
 * notify frontend about results of command execution.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface Slave {

    /**
     * whether error has occured.
     *
     * @return <code>true</code>: error has occured, <code>false</code>
     * otherwise
     */
    boolean isErrorMode();

    /**
     * notifying errors appeared in the backend's site.
     *
     * what should happen if an error at the backend's site has appeared?
     *
     * @param error a {@link dom.jfischer.kaffeekasse.backend.BackendError}
     * object.
     */
    void processBackendError(BackendError error);

    /**
     * notifying about regular results of command execution.
     *
     * @param jsonObject a
     * {@link dom.jfischer.kaffeekasse.middleTier.JSONObject} object.
     */
    void processJSON(JSONObject jsonObject);

    /**
     * notifying errors appeared in tally PDF.
     *
     * @param message a {@link java.lang.String} object.
     */
    void processTallyPDFError(final String message);

    /**
     * reset error state so that next error can be stored in this object.
     */
    void resetState();

}
