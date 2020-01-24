package ru.bloof.mks.cmd;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class PrintStatusResult extends AResult {
    private String result;

    public PrintStatusResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public boolean isPrinting() {
        return "PRINTING".equals(result);
    }
}