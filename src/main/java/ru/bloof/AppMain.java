package ru.bloof;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.pmw.tinylog.Logger;
import ru.bloof.mks.PrinterClient;
import ru.bloof.mks.cmd.*;
import ru.bloof.mqtt.MqttProxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class AppMain {
    private static final String PRINTER_HOST = "";
    private static final int PRINTER_PORT = 8080;
    private static final String MQTT_SERVER = "";
    private static final String MQTT_PUBLISHER = "Mks2MqttReporter/1.0.0";

    public static void main(String[] args) throws MqttException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        try (PrinterClient pc = new PrinterClient(PRINTER_HOST, PRINTER_PORT);
             MqttProxy mqttProxy = new MqttProxy(MQTT_SERVER, MQTT_PUBLISHER)) {
            executor.scheduleWithFixedDelay(() -> sendTempStatus(pc, mqttProxy), 5, 5, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(() -> sendPrintStatus(pc, mqttProxy), 30, 30, TimeUnit.SECONDS);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    executor.awaitTermination(1, TimeUnit.HOURS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
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
        } catch (MqttException e) {
            Logger.warn(e, "Cannot send print status");
        }
    }

    private static void sendTempStatus(PrinterClient pc, MqttProxy mqttProxy) {
        GetTempReportCommand cmd = new GetTempReportCommand();
        GetTempReportResult result = pc.executeCommand(cmd);
        if (result != null) {
            try {
                mqttProxy.send("curExtruderTemp", result.getCurrentExtruderTemp());
                mqttProxy.send("curBedTemp", result.getCurrentBedTemp());
                mqttProxy.send("tgtExtruderTemp", result.getTargetExtruderTemp());
                mqttProxy.send("tgtBedTemp", result.getTargetBedTemp());
            } catch (MqttException e) {
                Logger.warn(e, "Cannot send temp report");
            }
        }
    }
}