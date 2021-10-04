package fr.renardfute.scalu.server;

import fr.renardfute.scalu.blueprints.Blueprint;
import fr.renardfute.scalu.server.errors.ServerNotLaunchedException;
import fr.renardfute.scalu.utils.FileUtils;
import fr.renardfute.scalu.utils.ServerTrackingRunnable;
import fr.renardfute.scalu.utils.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.Executors;

import static fr.renardfute.scalu.SCALU.IS_WARNING_PRINTED;

/**
 * A server object is a copy of a blueprint that can be launched and tracked
 * @author renardfute
 * @since 1.0
 */
public class Server {

    public Blueprint blueprint;
    public UUID uuid;
    public File directory;
    public Process process;
    public StreamGobbler gobbler;
    public ServerState state;
    public short port = -1;

    public Server(Blueprint blueprint){
        this.blueprint = blueprint;
        uuid = UUID.randomUUID();

        create();
        try {
            this.state = ServerState.STARTING;
            launch();
        } catch (IOException e) {
            this.state = ServerState.STOPPED;
            e.printStackTrace();
        }
    }

    /**
     * This functions copies the template, give it an id and paste it in the server folder
     * @author renardfute
     * @since 1.0
     */
    public void create() {
        File bpDir = new File(blueprint.config.directory, "template");
        this.directory = new File(blueprint.config.directory, "servers/" + uuid.toString());
        boolean error = true;
        try {
            FileUtils.copyDirectory(bpDir, this.directory);
        } catch (IOException e) {
            error = false;
            if(IS_WARNING_PRINTED) e.printStackTrace();
        }

        if(!error && IS_WARNING_PRINTED) System.err.println("⚠ Warning: Something went wrong while copying the server " + uuid.toString());
    }

    /**
     * This will launch the server
     * @throws IOException Thrown if there is any error with the threading
     * @author renardfute
     * @since 1.0
     */
    public void launch() throws IOException {
        new Thread(() -> {
            ProcessBuilder builder = new ProcessBuilder(this.blueprint.config.launchCommand.split(" "));
            builder.directory(this.directory);
            try {
                this.process = builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(this.blueprint.config.launchCommand);
            this.gobbler = new StreamGobbler(process.getInputStream(), process.getOutputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(this.gobbler);
            this.state = ServerState.RUNNING;

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new ServerTrackingRunnable(this), 0, 1000);
        }).start();
    }

    /**
     * This will simply delete the server directory.
     * And will stop it if it was running.
     * @return The success of the deletion
     * @author renardfute
     * @since 1.0
     */
    public boolean delete() {
        if(this.process.isAlive()){
            try {
                this.stop();
            } catch (ServerNotLaunchedException e) {
                e.printStackTrace();
            }
        }
        return FileUtils.deleteDirectory(this.directory);
    }

    /**
     * Will stop the server
     * @throws ServerNotLaunchedException Thrown if the server is already stopped or has never been launched !
     * @author renardfute
     * @since 1.0
     */
    public void stop() throws ServerNotLaunchedException {
        if (!this.process.isAlive()){
            throw new ServerNotLaunchedException(this, "stop the server");
        }

        try {
            this.gobbler.write(this.blueprint.config.stopCommand);

            this.process.waitFor();
            if(!this.process.isAlive()) {
                this.gobbler = null;
            }
            this.state = ServerState.STOPPED;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
