package ru.bloof.mks.cmd;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetJobDonePercentResult extends AResult {
    private final int percent;

    public GetJobDonePercentResult(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }
}