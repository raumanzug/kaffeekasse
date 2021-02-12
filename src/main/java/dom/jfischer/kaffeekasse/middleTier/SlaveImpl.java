/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.BackendRetcodeState;

/**
 * notify frontend about results of command execution.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public abstract class SlaveImpl
        implements BackendRetcodeState, Slave {

    /**
     * backend error code currently stored in this object.
     */
    private BackendRetcode state = BackendRetcode.OK;

    /**
     * indicates that an error has occured.
     */
    private boolean errorMode = false;

    /**
     * {@inheritDoc}
     *
     * error appearing in the backend's site.
     */
    @Override
    public BackendRetcode getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * whether error has occured.
     */
    @Override
    public boolean isErrorMode() {
        return this.errorMode;
    }

    /**
     * {@inheritDoc}
     *
     * notifying errors appeared in tally PDF.
     */
    @Override
    public void processTallyPDFError(final String message) {
        this.errorMode = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetState() {
        this.state = BackendRetcode.OK;
        this.errorMode = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(final BackendRetcode error) {
        this.state = error;
        if (error != BackendRetcode.OK) {
            this.errorMode = true;
        }
        processBackendRetcode(error);
    }

}
