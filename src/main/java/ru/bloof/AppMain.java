package ru.bloof;

import org.pmw.tinylog.Logger;
import ru.bloof.conf.AppConfig;
import ru.bloof.conf.ArgsParser;
import ru.bloof.mks.PrinterClient;
import ru.bloof.mks.cmd.*;
import ru.bloof.mqtt.MqttProxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main class
 */
public class AppMain {
    public static void main(String[] args) throws Exception {
        AppConfig config = new ArgsParser().parse(args);
        if (config == null) {
            return;
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try (PrinterClient pc = new PrinterClient(config);
             MqttProxy mqttProxy = new MqttProxy(config)) {
            executor.scheduleWithFixedDelay(() -> sendTempStatus(pc, mqttProxy), 0, 5, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(() -> sendPrintStatus(pc, mqttProxy), 0, 30, TimeUnit.SECONDS);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    executor.awaitTermination(1, TimeUnit.HOURS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private static void sendPrintStatus(PrinterClient pc, MqttProxy mqttProxy) {
        try {
            PrintStatusCommand cmd = new PrintStatusCommand();
            PrintStatusResult statusResult = pc.executeCommand(cmd);
            if (statusResult != null) {
                mqttProxy.send("status", statusResult.getResult());
            }
            if (statusResult == null || !statusResult.isPrinting()) {
                mqttProxy.send("file", "none");
                mqttProxy.send("elapsed", 0);
                mqttProxy.send("job_percent", 0);
                return;
            }
            GetPrintedFileCommand fileCommand = new GetPrintedFileCommand();
            GetPrintedFileResult fileResult = pc.executeCommand(fileCommand);
            if (fileResult != null) {
                mqttProxy.send("file", fileResult.getFilePath());
            }
            GetElapsedTimeCommand elapsedTimeCommand = new GetElapsedTimeCommand();
            GetElapsedTimeResult elapsedTimeResult = pc.executeCommand(elapsedTimeCommand);
            if (elapsedTimeResult != null) {
                mqttProxy.send("elapsed", elapsedTimeResult.getSeconds());
            }
            GetJobDonePercentCommand jobPercentCommand = new GetJobDonePercentCommand();
            GetJobDonePercentResult jobPercentResult = pc.executeCommand(jobPercentCommand);
            if (jobPercentResult != null) {
                mqttProxy.send("job_percent", jobPercentResult.getPercent());
            }
        } catch (Exception e) {
            Logger.warn(e, "Cannot send print status");
        }
    }

    private static void sendTempStatus(PrinterClient pc, MqttProxy mqttProxy) {
        try {
            GetTempReportCommand cmd = new GetTempReportCommand();
            GetTempReportResult result = pc.executeCommand(cmd);
            if (result != null) {
                mqttProxy.send("curExtruderTemp", result.getCurrentExtruderTemp());
                mqttProxy.send("curBedTemp", result.getCurrentBedTemp());
                mqttProxy.send("tgtExtruderTemp", result.getTargetExtruderTemp());
                mqttProxy.send("tgtBedTemp", result.getTargetBedTemp());
            }
        } catch (Exception e) {
            Logger.warn(e, "Cannot send temp report");
        }
    }
}