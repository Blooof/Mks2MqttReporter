package ru.bloof.mks.cmd;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetElapsedTimeResult extends AResult {
    private final long seconds;

    public GetElapsedTimeResult(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }
}