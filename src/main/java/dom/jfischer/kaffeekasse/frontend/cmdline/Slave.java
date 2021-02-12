/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.frontend.cmdline;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.middleTier.JSONObject;

/**
 * specialized abstract class
 * {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl}.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class Slave extends dom.jfischer.kaffeekasse.middleTier.SlaveImpl {

    /**
     * class for output to console.
     */
    private final Output output;

    /**
     * constructor.
     *
     * @param output a {@link dom.jfischer.kaffeekasse.frontend.cmdline.Output}
     * object.
     */
    public Slave(final Output output) {
        this.output = output;
    }

    /**
     * {@inheritDoc}
     *
     * implements abstract method
     * {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl#processBackendRetcode}.
     */
    @Override
    public void processBackendRetcode(final BackendRetcode error) {
        this.output.stderr(error.getMessage());
    }

    /**
     * {@inheritDoc}
     *
     * extends method
     * {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl#processTallyPDFError}.
     *
     * doing so, it outputs error message to console.
     */
    @Override
    public void processTallyPDFError(final String message) {
        super.processTallyPDFError(message);
        this.output.stderr(message);
    }

    /**
     * {@inheritDoc}
     *
     * implements abstract method
     * {@link dom.jfischer.kaffeekasse.middleTier.SlaveImpl#processJSON}.
     */
    @Override
    public void processJSON(final JSONObject jsonObject) {
        this.output.stdout(jsonObject.toString());
    }

}
