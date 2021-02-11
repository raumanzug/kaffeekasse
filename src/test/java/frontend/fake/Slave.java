/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package frontend.fake;

import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.middleTier.JSONObject;

/**
 * extends abstract class {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl}.
 *
 * @author jfischer
 */
public class Slave extends dom.jfischer.kaffeekasse.middleTier.SlaveImpl {

    /**
     * storage for tally PDF;s error messages.
     */
    private String tallyPDFError;

    /**
     * storage for json objects outputted by some commands.
     */
    private JSONObject jsonObject;

    /**
     * constructor.
     */
    public Slave() {
        this.tallyPDFError = null;
        this.jsonObject = null;
    }

    /**
     * get error from tally PDF.
     *
     * @return
     */
    public String getTallyPDFError() {
        return this.tallyPDFError;
    }

    /**
     * get json object.
     *
     * @return json object.
     */
    public JSONObject getJSONObject() {
        return this.jsonObject;
    }

    /**
     * implements abstract method
     * {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl#processBackendError}.
     *
     * @param error
     */
    @Override
    public void processBackendError(BackendError error) {
    }

    /**
     * extends method
     * {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl#processTallyPDFError}.
     *
     * @param message
     */
    @Override
    public void processTallyPDFError(String message) {
        super.processTallyPDFError(message);
        this.tallyPDFError = message;
    }

    /**
     * implements abstract method {@link dom.jfischer.kaffeekasse.merrorMode}.
     *
     * @param jsonObject
     */
    @Override
    public void processJSON(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

}
