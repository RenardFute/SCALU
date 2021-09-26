package fr.renardfute.scalu.errors;

import java.io.File;

public class SCALUNotInstanced extends Exception {

    /**
     * This exception thrown when you try to use methods that require
     * SCALU to be instanced.
     * @see fr.renardfute.scalu.SCALU#SCALU(File)
     * @param cause The thing you where doing when you needed SCALU to be instanced.
     * @author renardfute
     * @since 1.0
     */
    public SCALUNotInstanced(String cause) {
        super("Before " + cause +  " you have to instantiate SCALU see SCALU#SCALU(File)");
    }
}
