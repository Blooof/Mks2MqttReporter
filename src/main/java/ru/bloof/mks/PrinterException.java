package ru.bloof.mks;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class PrinterException extends RuntimeException {
    public PrinterException() {
    }

    public PrinterException(String message) {
        super(message);
    }

    public PrinterException(String message, Throwable cause) {
        super(message, cause);
    }
}