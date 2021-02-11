/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend;

/**
 * stores an backend error which has appeared in the backend so that a frontend
 * can notice from it.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public interface BackendErrorState {

    /**
     * get backend error stored in this object.
     *
     * @return backend error stored in this object.
     */
    BackendError getState();

    /**
     * store an backend error in this object.
     *
     * @param error a {@link dom.jfischer.kaffeekasse.backend.BackendError}
     * object.
     */
    void setState(final BackendError error);

}
