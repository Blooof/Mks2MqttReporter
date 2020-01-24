package ru.bloof.mks.cmd;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetPrintedFileResult extends AResult {
    private final String filePath;

    public GetPrintedFileResult(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}