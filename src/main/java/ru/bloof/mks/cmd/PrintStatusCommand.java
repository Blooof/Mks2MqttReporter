package ru.bloof.mks.cmd;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bloof.mks.PrinterCommand;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class PrintStatusCommand implements PrinterCommand<PrintStatusResult> {
    private static final String COMMAND_ID = "M997";

    @Override
    public String getCommand() {
        return COMMAND_ID;
    }

    @Override
    public PrintStatusResult parseResult(String output) {
        String prefix = COMMAND_ID + ' ';
        if (StringUtils.startsWith(output, prefix)) {
            output = output.substring(prefix.length());
        }
        return new PrintStatusResult(output);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}