package ru.bloof.mks.cmd;

import org.pmw.tinylog.Logger;
import ru.bloof.mks.PrinterCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class GetTempReportCommand implements PrinterCommand<GetTempReportResult> {
    private static final Pattern RESULT_PATTERN = Pattern.compile("^T:(?<cet>\\d+) /(?<tet>\\d+) B:(?<cbt>\\d+) /(?<tbt>\\d+).*$");
    private static final String COMMAND_ID = "M105";

    @Override
    public String getCommand() {
        return COMMAND_ID;
    }

    @Override
    public GetTempReportResult parseResult(String output) {
        Matcher m = RESULT_PATTERN.matcher(output);
        if (!m.matches()) {
            Logger.info("Result {} not matched with pattern", output);
            return null;
        }
        int curExtTemp = Integer.parseInt(m.group("cet"));
        int tgtExtTemp = Integer.parseInt(m.group("tet"));
        int curBedTemp = Integer.parseInt(m.group("cbt"));
        int tgtBedTemp = Integer.parseInt(m.group("tbt"));
        return new GetTempReportResult(curExtTemp, tgtExtTemp, curBedTemp, tgtBedTemp);
    }
}