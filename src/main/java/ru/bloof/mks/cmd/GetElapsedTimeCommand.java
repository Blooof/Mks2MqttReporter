package ru.bloof.mks.cmd;

import org.pmw.tinylog.Logger;
import ru.bloof.mks.PrinterCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetElapsedTimeCommand implements PrinterCommand<GetElapsedTimeResult> {
    private static final String COMMAND_ID = "M992";
    private static final Pattern RESULT_PATTERN = Pattern.compile("^M992 (?<h>\\d+):(?<m>\\d+):(?<s>\\d+)$");

    @Override
    public String getCommand() {
        return COMMAND_ID;
    }

    @Override
    public GetElapsedTimeResult parseResult(String output) {
        Matcher m = RESULT_PATTERN.matcher(output);
        if (!m.matches()) {
            Logger.info("Result {} not matched with pattern", output);
            return null;
        }
        long seconds = Integer.parseInt(m.group("h"));
        seconds = seconds * 60 + Integer.parseInt(m.group("m"));
        seconds = seconds * 60 + Integer.parseInt(m.group("s"));
        return new GetElapsedTimeResult(seconds);
    }
}