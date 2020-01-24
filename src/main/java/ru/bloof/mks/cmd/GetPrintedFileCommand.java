package ru.bloof.mks.cmd;

import org.pmw.tinylog.Logger;
import ru.bloof.mks.PrinterCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetPrintedFileCommand implements PrinterCommand<GetPrintedFileResult> {
    private static final Pattern RESULT_PATTERN = Pattern.compile("^(M994)? \\d+:(?<name>[^;]*)(;.*)?$");
    private static final String COMMAND_ID = "M994";

    @Override
    public String getCommand() {
        return COMMAND_ID;
    }

    @Override
    public GetPrintedFileResult parseResult(String output) {
        Matcher m = RESULT_PATTERN.matcher(output);
        if (!m.matches()) {
            Logger.info("Result {} not matched with pattern", output);
            return null;
        }
        return new GetPrintedFileResult(m.group("name"));
    }
}
