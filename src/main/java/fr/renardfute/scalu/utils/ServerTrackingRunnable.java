package fr.renardfute.scalu.utils;

import fr.renardfute.scalu.server.Server;
import fr.renardfute.scalu.server.ServerState;

import java.util.TimerTask;

/**
 * This runnable will track a server at a defined timing.
 * @author renardfute
 * @since 1.0
 */
public class ServerTrackingRunnable extends TimerTask {

    private final Server SERVER;

    public ServerTrackingRunnable(Server server){
        this.SERVER = server;
    }


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if(SERVER.process.isAlive()) SERVER.state = ServerState.RUNNING;
        else {
            SERVER.state = ServerState.STOPPED;
            this.cancel();
        }
        if(SERVER.port == -1) SERVER.port = ServerUtils.getPort(SERVER);
    }
}
