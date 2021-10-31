package ru.bloof.mks;

import ru.bloof.conf.AppConfig;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class PrinterClient implements Closeable {
    private final InetSocketAddress addr;

    private PrinterConnection cnxn;

    public PrinterClient(AppConfig config) {
        this.addr = new InetSocketAddress(config.printerHost, config.printerPort);
    }

    public synchronized <T> T executeCommand(PrinterCommand<T> command) {
        String cmd = command.getCommand();
        PrinterConnection cnxn;
        try {
            cnxn = getConnection();
            try {
                cnxn.writeLine(cmd);
                String deliveryStatus = cnxn.readLine();
                if (!"ok".equals(deliveryStatus)) {
                    throw new IOException("Printer command receive result not ok: " + deliveryStatus);
                }
                String result = cnxn.readLine();
                T cmdResult = command.parseResult(result);
                return cmdResult;
            } catch (IOException e) {
                closeConnection();
                throw new PrinterException("Cannot execute command " + cmd, e);
            }
        } catch (IOException e) {
            closeConnection();
            throw new PrinterException("Cannot get printer connection for " + cmd, e);
        }
    }

    @Override
    public synchronized void close() {
        closeConnection();
    }

    private PrinterConnection getConnection() throws IOException {
        PrinterConnection cnxn = this.cnxn;
        if (cnxn == null) {
            Socket s = new Socket();
            s.setSoTimeout(5000);
            s.connect(addr, 1000);
            this.cnxn = cnxn = new PrinterConnection(s);
        }
        return cnxn;
    }

    private void closeConnection() {
        PrinterConnection cnxn = this.cnxn;
        if (cnxn != null) {
            this.cnxn = null;
            cnxn.close();
        }
    }

    private PrinterConnection createConnection(InetSocketAddress addr) throws IOException {
        Socket s = new Socket();
        s.setSoTimeout(5000);
        s.connect(addr);
        return new PrinterConnection(s);
    }
}