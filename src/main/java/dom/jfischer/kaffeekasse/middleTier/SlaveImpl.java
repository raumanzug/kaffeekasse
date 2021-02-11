/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.backend.BackendErrorState;

/**
 * notify frontend about results of command execution.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public abstract class SlaveImpl
        implements BackendErrorState, Slave {

    /**
     * backend error code currently stored in this object.
     */
    private BackendError state = BackendError.OK;

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
    public BackendError getState() {
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
        this.state = BackendError.OK;
        this.errorMode = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(final BackendError error) {
        this.state = error;
        if (error != BackendError.OK) {
            this.errorMode = true;
        }
        processBackendError(error);
    }

}
