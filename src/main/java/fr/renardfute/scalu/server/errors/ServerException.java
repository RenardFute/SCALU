package fr.renardfute.scalu.server.errors;

import fr.renardfute.scalu.server.Server;

/**
 * Parent class for all exception related to servers
 * @author renardfute
 * @since 1.0
 */
public abstract class ServerException extends Exception {

    public Server server;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param server The server that is related to this error.
     * @author renardfute
     * @since 1.0
     */
    public ServerException(String message, Server server) {
        super(message);
        this.server = server;
    }
}
