/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend;

/**
 * All the errors which can appear in the backend.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public enum BackendRetcode {

    /**
     * no error.
     */
    OK("OK"),
    /**
     * accounting key not found.
     */
    ENTRY_NOT_FOUND("entry not found."),
    /**
     * account entry key does not exist.
     */
    NO_VALID_ACCOUNT_ENTRY_KEY("invalid key for account entry."),
    /**
     * requested participant not found.
     */
    PARTICIPANT_NOT_FOUND("participant does not exist."),
    /**
     * a participant with requested name already exist.
     */
    PARTICIPANT_ALREADY_EXISTS("participant already exist."),
    /**
     * Price of a cup of coffee must not be zero.
     */
    ZERO_PRICE("price cannot be zero."),
    /**
     * read access from backend failed.
     */
    READ_ERROR("read from backend failed."),
    /**
     * write access to backend failed.
     */
    WRITE_ERROR("write onto backend failed");

    /**
     * error message.
     */
    private final String errorMessage;

    /**
     * constructor.
     *
     * @param errorMessage
     */
    BackendRetcode(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * get message related to respective error.
     *
     * @return error message related to respective error.
     */
    public String getMessage() {
        return this.errorMessage;
    }

}
