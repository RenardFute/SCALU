package fr.renardfute.scalu;

import java.io.File;

/**
 * SCALU stands for <b>S</b>erver <b>C</b>reation <b>A</b>nd <b>L</b>aunching <b>U</b>til. <br>
 * It's better to instantiate it by giving its working directory before doing anything.
 * @see SCALU#SCALU(File)
 * @author renardfute
 * @since 1.0
 */
public class SCALU {

    public File directory;
    public static SCALU INSTANCE;
    public static boolean IS_DEBUGGING = true;
    public static boolean IS_WARNING_PRINTED = true;
    public static boolean IS_SERVERS_OUTPUT_PRINTED = false;

    /**
     * Main constructor to instantiate SCALU
     * @param directory The working directory where there is all SCALU's blueprints
     * @author renardfute
     * @since 1.0
     */
    public SCALU(File directory) {
        if(!directory.exists() || !directory.isDirectory()){
            throw new IllegalArgumentException("The given file is not a directory or it doesn't exist");
        }
        this.directory = directory;

        SCALU.INSTANCE = this;
    }

    /**
     * Check if SCALU has been instanced.
     * @return True if it has been, false if not.
     * @author renardfute
     * @since 1.0
     */
    public static boolean isInstanced(){
        return SCALU.INSTANCE != null;
    }

}
