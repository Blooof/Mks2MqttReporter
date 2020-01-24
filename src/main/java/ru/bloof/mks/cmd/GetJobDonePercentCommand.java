package ru.bloof.mks.cmd;

import org.pmw.tinylog.Logger;
import ru.bloof.mks.PrinterCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetJobDonePercentCommand implements PrinterCommand<GetJobDonePercentResult> {
    private static final Pattern RESULT_PATTERN = Pattern.compile("^M27 (?<pc>\\d+)$");
    private static final String COMMAND_ID = "M27";

    @Override
    public String getCommand() {
        return COMMAND_ID;
    }

    @Override
    public GetJobDonePercentResult parseResult(String output) {
        Matcher m = RESULT_PATTERN.matcher(output);
        if (!m.matches()) {
            Logger.info("Result {} not matched with pattern", output);
            return null;
        }
        int percent = Integer.parseInt(m.group("pc"));
        return new GetJobDonePercentResult(percent);
    }
}