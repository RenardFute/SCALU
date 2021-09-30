package fr.renardfute.scalu.utils;

import fr.renardfute.scalu.SCALU;

import java.io.*;
import java.util.function.Consumer;

public class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private OutputStream outputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, OutputStream outputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        if(SCALU.IS_SERVERS_OUTPUT_PRINTED) new BufferedReader(new InputStreamReader(inputStream)).lines()
            .forEach(consumer);
    }


    public void write(String cmd) throws IOException {
        Writer writer = new OutputStreamWriter(this.outputStream);
        writer.write(cmd + "\n");
        writer.flush();
    }
}
