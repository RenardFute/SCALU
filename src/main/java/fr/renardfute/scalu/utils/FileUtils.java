package fr.renardfute.scalu.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * This is a little class that will contain every utility methods that are linked to file management.
 * @author renardfute
 * @since 1.0
 */
public class FileUtils {

    /**
     * Give this a file and an encoding and it will return you the content as a String
     * @param file The file you want the content of.
     * @param encoding The encoding of the given file
     * @return The content as a {@link String}
     * @throws IOException Thrown if {@link Files#readAllBytes(Path)} fails.
     * @author SomeoneOnTheInternet //TODO Find who it was
     * @since 1.O
     */
    public static String readFile(File file, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }

    /**
     * Give this a file and a string to write it in.
     * @param file The file to write the string in.
     * @param string The string to write in the given file
     * @throws IOException see {@link Files#write(Path, byte[], OpenOption...)}
     * @author renardfute
     * @since 1.0
     */
    public static void writeFile(File file, String string) throws IOException {
        byte[] strToBytes = string.getBytes();
        Files.write(file.toPath(), strToBytes);
    }

}
