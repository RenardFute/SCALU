package fr.renardfute.scalu.utils;

import fr.renardfute.scalu.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some useful functions for server management
 * @author renardfute
 */
public class ServerUtils {

    /**
     * Use lsof to find the port of the running server
     * @param server The server you want to know the listening port
     * @return The port on which the given server is running
     * @author renardfute
     * @since 1.0
     */
    public static short getPort(Server server){
        short result = -1;
        Process p;
        String command= "lsof -i -P ";
        try{
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                if(line.matches("^.*" + server.process.pid() + ".*TCP.*")){
                    Pattern pattern = Pattern.compile(".*:(\\d*).*");
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.find()){
                        result = Short.parseShort(matcher.group(1));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
