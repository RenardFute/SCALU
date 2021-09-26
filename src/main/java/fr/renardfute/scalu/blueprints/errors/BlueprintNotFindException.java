package fr.renardfute.scalu.blueprints.errors;

import java.util.UUID;

/**
 * This error occurs when you try to get a blueprint that doesn't exist
 * @author renardfute
 * @since 1.0
 */
public class BlueprintNotFindException extends BlueprintException {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param name      the name that was given but result in no blueprints.
     * @author renardfute
     * @since 1.0
     */
    public BlueprintNotFindException(String name) {
        // Todo complete the error message
        super("No blueprint find with the name '" + name + "'. Try create one with ", null);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param uuid      the id that was given but result in no blueprints.
     * @author renardfute
     * @since 1.0
     */
    public BlueprintNotFindException(UUID uuid) {
        // Todo complete the error message
        super("No blueprint find with the id '" + uuid.toString() + "'. Try create one with ", null);
    }



}
