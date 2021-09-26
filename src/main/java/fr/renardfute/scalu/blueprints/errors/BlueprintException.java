package fr.renardfute.scalu.blueprints.errors;

import fr.renardfute.scalu.blueprints.Blueprint;

/**
 * Parent class for all exception related to blueprints
 * @author renardfute
 * @since 1.0
 */
public class BlueprintException extends Exception {

    public Blueprint blueprint;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param blueprint the blueprint that caused the error.
     * @author renardfute
     * @since 1.0
     */
    public BlueprintException(String message, Blueprint blueprint) {
        super(message);
        this.blueprint = blueprint;
    }


}
