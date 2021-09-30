package fr.renardfute.scalu.server;

import fr.renardfute.scalu.blueprints.Blueprint;
import fr.renardfute.scalu.server.errors.ServerNotLaunchedException;
import fr.renardfute.scalu.utils.FileUtils;
import fr.renardfute.scalu.utils.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;

import static fr.renardfute.scalu.SCALU.IS_WARNING_PRINTED;


public class Server {

    public Blueprint blueprint;
    public UUID uuid;
    public File directory;
    public Process process;
    public StreamGobbler gobbler;

    public Server(Blueprint blueprint){
        this.blueprint = blueprint;
        uuid = UUID.randomUUID();

        create();
        try {
            launch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
