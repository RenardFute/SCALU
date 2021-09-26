package fr.renardfute.scalu.blueprints.errors;

import java.util.UUID;

/**
 * This error occurs when you try to get a blueprint while not being accurate enough.
 * Which leads to a return of multiples blueprints
 * @author renardfute
 * @since 1.0
 */
public class MultipleBlueprintException extends BlueprintException {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param name      the name that was given but result in too many blueprints.
     * @author renardfute
     * @since 1.0
     */
    public MultipleBlueprintException(String name) {
        super("There more than one blueprint with the name '" + name + "'. Try with an id or both id and name.", null);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param uuid      the id that was given but result in too many blueprints.
     * @author renardfute
     * @since 1.0
     */
    public MultipleBlueprintException(UUID uuid) {
        super("This error should never happen, check if you modified any id by hand. If this error is recurring please contact the developer.", null);
    }

}
