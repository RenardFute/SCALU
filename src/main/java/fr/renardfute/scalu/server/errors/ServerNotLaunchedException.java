package fr.renardfute.scalu.server.errors;

import fr.renardfute.scalu.server.Server;

public class ServerNotLaunchedException extends ServerException {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param server  The server that is related to this error.
     * @param context The context in which the application starts.
     * @author renardfute
     * @since 1.0
     */
    public ServerNotLaunchedException(Server server, String context) {
        super("The server " + server.uuid.toString() + " has not been launched, so you can't " + context, server);
    }
}
