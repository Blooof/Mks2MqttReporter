package ru.bloof.mks.cmd;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetTempReportResult extends AResult {
    private final int currentExtruderTemp;
    private final int targetExtruderTemp;
    private final int currentBedTemp;
    private final int targetBedTemp;

    public GetTempReportResult(int currentExtruderTemp, int targetExtruderTemp, int currentBedTemp, int targetBedTemp) {
        this.currentExtruderTemp = currentExtruderTemp;
        this.targetExtruderTemp = targetExtruderTemp;
        this.currentBedTemp = currentBedTemp;
        this.targetBedTemp = targetBedTemp;
    }

    public int getCurrentExtruderTemp() {
        return currentExtruderTemp;
    }

    public int getTargetExtruderTemp() {
        return targetExtruderTemp;
    }

    public int getCurrentBedTemp() {
        return currentBedTemp;
    }

    public int getTargetBedTemp() {
        return targetBedTemp;
    }
}