package ru.bloof.mks;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public interface PrinterCommand<T> {
    String getCommand();

    T parseResult(String output);
}