package ru.bloof.conf;

import org.apache.commons.cli.*;
import org.pmw.tinylog.Logger;

public class ArgsParser {
    private static final String MQTT_PUBLISHER_DEF = "Mks2MqttReporter/1.0.0";
    private static final String TOPIC_PREFIX_DEF = "home/ghost4s/";

    public AppConfig parse(String[] args) {
        Options opts = new Options();
        opts.addRequiredOption("ph", "printer-host", true, "printer network host (ip address). ex: 192.168.1.80");
        opts.addRequiredOption("pp", "printer-port", true, "printer network port. ex: 8080");
        opts.addRequiredOption("ms", "mqtt-server", true, "mqtt server. format scheme://host:port. ex: tcp://192.168.1.200:1883 or ssl://192.168.1.200:8883");
        opts.addOption("p", "publisher-name", true, "mqtt publisher name. default: " + MQTT_PUBLISHER_DEF);
        opts.addOption("t", "topic-prefix", true, "mqtt topic prefix. default: " + TOPIC_PREFIX_DEF);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine parsed = parser.parse(opts, args);
            AppConfig appConfig = new AppConfig();
            appConfig.printerHost = parsed.getOptionValue("ph");
            appConfig.printerPort = Integer.parseInt(parsed.getOptionValue("pp"));
            appConfig.mqttServer = parsed.getOptionValue("ms");
            appConfig.mqttPublisher = parsed.getOptionValue("p", MQTT_PUBLISHER_DEF);
            appConfig.topicPrefix = parsed.getOptionValue("t", TOPIC_PREFIX_DEF);
            return appConfig;
        } catch (ParseException e) {
            Logger.error("Bad parameters provided: " + e.getMessage());
            new HelpFormatter().printHelp("java [some java options, like GC, memory, etc] -jar Mks2MqttReporter.jar", opts, true);
            return null;
        }
    }
}
