package ru.bloof.mks;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class PrinterConnection {
    private static final String LINE_SEPARATOR = "\r\n";

    private final Socket s;
    private final BufferedReader reader;
    private final Writer writer;

    public PrinterConnection(Socket s) throws IOException {
        this.s = s;
        this.reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.writer = new OutputStreamWriter(s.getOutputStream());
    }

    public synchronized void writeLine(String s) throws IOException {
        writer.write(s + LINE_SEPARATOR);
        writer.flush();
    }

    public synchronized String readLine() throws IOException {
        return reader.readLine();
    }

    public synchronized void close() {
        IOUtils.closeQuietly(s);
    }
}