package fr.renardfute.scalu.blueprints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.renardfute.scalu.SCALU;
import fr.renardfute.scalu.blueprints.errors.BlueprintNotFindException;
import fr.renardfute.scalu.blueprints.errors.MultipleBlueprintException;
import fr.renardfute.scalu.errors.SCALUNotInstanced;
import fr.renardfute.scalu.utils.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A blueprint is a pre-configured server that is ready to be copied and launched. <br>
 * @author renardfute
 * @since 1.0
 */
public class Blueprint {

    public BlueprintConfiguration config;

    /**
     * Will get the matching blueprint with the given name
     * @param name The name of the blueprint you want
     * @throws Exception Name are not unique, so it might be multiples blueprints.<br>
     *                   The name you gave might also have no matching blueprints.
     * @author renardfute
     * @since 1.0
     */
    public Blueprint(String name) throws Exception {
        // Get all matching blueprint
        List<Blueprint> matching = getBlueprintsFromName(name);

        // If nothing matches
        if(matching.size() == 0) throw new BlueprintNotFindException(name);

        // If there is more than one
        if(matching.size() > 1) throw new MultipleBlueprintException(name);

        Blueprint blueprint = matching.get(0);

        // Init all variables with the blueprint that has been found
        this.config = blueprint.config;
    }

    /**
     * Will get the matching blueprint with the given name
     * @param uuid The uuid of the blueprint you want
     * @throws Exception ID are unique, but it can happen that there are two blueprints with the same. <br>
     *                  The id you gave might also have no matching blueprints
     * @author renardfute
     * @since 1.0
     */
    public Blueprint(UUID uuid) throws Exception {
        // Get all matching blueprint
        List<Blueprint> matching = getBlueprintsFromId(uuid);

        // If nothing matches
        if(matching.size() == 0) throw new BlueprintNotFindException(uuid);

        // If there is more than one
        if(matching.size() > 1) throw new MultipleBlueprintException(uuid);

        Blueprint blueprint = matching.get(0);

        // Init all variables with the blueprint that has been found
        this.config = blueprint.config;
    }

    /**
     * Get a blueprint that already exist from its config
     * @param config The config of the blueprint
     * @author renardfute
     * @since 1.0
     */
    public Blueprint(BlueprintConfiguration config) {
        this.config = config;
    }

    /**
     * Create a blueprint by giving its full configuration
     * @param config The configuration of the blueprint you want to create
     * @return The created blueprint
     * @author renardfute
     * @since 1.0
     */
    public static Blueprint create(BlueprintConfiguration config) throws Exception {
        Blueprint blueprint = new Blueprint(config);
        blueprint.init();

        return blueprint;
    }

    /**
     * Create a blueprint by giving its full configuration
     * @param name The name you want for the blueprint
     * @param launch The command that will launch the server
     * @param stop The command that will stop the server
     * @return The created blueprint
     * @author renardfute
     * @since 1.0
     */
    public static Blueprint create(String name, String launch, String stop) throws Exception {
        BlueprintConfiguration config = new BlueprintConfiguration();
        config.name = name;
        config.uuid = UUID.randomUUID().toString();
        config.launchCommand = launch;
        config.stopCommand = stop;

        return create(config);
    }

    /**
     * Create a blueprint by only giving its name. Its id will be generated and its folder will be its name. <br>
     * Caution this will set launch and stop commands by default to "launch" & "stop". See {@link}
     * @param name The name you want for the blueprint
     * @return The created blueprint
     * @author renardfute
     * @since 1.0
     */
    public static Blueprint create(String name) throws Exception {
        return create(name, "launch", "stop");
    }

    public void init() throws Exception {

        if(!SCALU.isInstanced()){
            throw new SCALUNotInstanced("creating a blueprint");
        }

        File dir = new File(SCALU.INSTANCE.directory, this.config.name);
        boolean error = dir.mkdirs();
        this.config.directory = dir.getPath();

        File serverDir = new File(this.config.directory, "servers");
        error &= serverDir.mkdirs();
        File templateDir = new File(this.config.directory, "template");
        error &= templateDir.mkdirs();

        File configFile = new File(this.config.directory, "config.scalu");
        error &= configFile.createNewFile();

        if(SCALU.IS_WARNING_PRINTED && !error) System.err.println("⚠ Warning: Something went wrong while initiating the following blueprint => " + this.config.name + "("+this.config.uuid + ")");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileUtils.writeFile(configFile, gson.toJson(this.config));
    }

    /**
     * Get a list of blueprints with the same name. <br>
     * Will write a warning in the standard error output if there is multiple config file in one directory.
     * @param name The name of the blueprints
     * @return An {@link ArrayList} of blueprints with the same name.
     * @throws Exception if SCALU is not instanced or if json deserialization failed
     * @author renardfute
     * @since 1.0
     */
    public static List<Blueprint> getBlueprintsFromName(String name) throws Exception {
        return getBlueprintsMatching(config -> config.name.equals(name));
    }

    public static List<Blueprint> getBlueprintsFromId(UUID uuid) throws Exception {
        return getBlueprintsMatching(config -> config.uuid.equals(uuid.toString()));
    }

    private static List<Blueprint> getBlueprintsMatching(Predicate<BlueprintConfiguration> supplier) throws Exception {
        List<Blueprint> result = new ArrayList<>();

        if(!SCALU.isInstanced()){
            throw new SCALUNotInstanced("getting blueprints");
        }

        File directory = SCALU.INSTANCE.directory;
        File[] subdirs = directory.listFiles(File::isDirectory);

        assert subdirs != null;
        if(subdirs.length == 0) return result;

        for (File dir : subdirs) {
            // Get all file in dir
            File[] files = dir.listFiles(f -> f.getName().equalsIgnoreCase("config.scalu"));
            assert files != null;

            // Check if this dir is a blueprint directory
            if (files.length == 0) continue;

            // Check if there is only one config
            if (files.length > 1) {
                if(SCALU.IS_WARNING_PRINTED) System.err.println("⚠ Warning: " + dir.getAbsolutePath() + " contains more than one 'config.scalu'. It will not be taken into consideration, please fix it.");
                continue;
            }

            // Read config file
            File configFile = files[0];
            Gson gson = new Gson();
            BlueprintConfiguration config = gson.fromJson(FileUtils.readFile(configFile, StandardCharsets.UTF_8), BlueprintConfiguration.class);
            if(!supplier.test(config)) continue;
            config.directory = dir.getPath();

            result.add(new Blueprint(config));
        }

        return result;
    }

}
