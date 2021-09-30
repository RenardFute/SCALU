package fr.renardfute.scalu.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;

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

    /**
     * This functions copies and entire directory including sub folders and sub files.
     * @param src The source you want to copy
     * @param dest The place you want to paste the source
     * @throws IOException Error coming from {@link Files#copy(Path, Path, CopyOption...)}
     * @author renardfute
     * @since 1.0
     */
    public static void copyDirectory(File src, File dest) throws IOException {

        if(src.isDirectory()){
            if(!dest.exists()) dest.mkdirs();

            String[] files = src.list();
            assert files != null;

            for (String file : files){
                File subSrc = new File(src, file);
                File subDist = new File(dest, file);

                copyDirectory(subSrc, subDist);
            }
        }
        else {
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }


    }

    /**
     * Will delete a directory recursively. <br>
     * See https://www.baeldung.com/java-delete-directory
     * @param directory The directory to delete
     * @return The success of the operation
     * @author renardfute with Internet
     * @since 1.0
     */
    public static boolean deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File sub : contents) {
                deleteDirectory(sub);
            }
        }
        return directory.delete();
    }

}
