package ru.bloof.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.pmw.tinylog.Logger;
import ru.bloof.conf.AppConfig;

import java.io.Closeable;

/**
 * @author <a href="mailto:blloof@gmail.com">Oleg Larionov</a>
 */
public class MqttProxy implements Closeable {
    private final AppConfig appConfig;
    private final MqttClient mqttClient;

    public MqttProxy(AppConfig config) throws MqttException {
        this.appConfig = config;

        mqttClient = new MqttClient(config.mqttServer, config.mqttPublisher);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setMaxReconnectDelay(30000);
        mqttClient.connect(options);
    }

    public void send(String statId, Object value) throws MqttException {
        String topic = appConfig.topicPrefix + statId;
        Logger.info("Sending {} to topic {}", value, topic);
        mqttClient.publish(topic, value.toString().getBytes(), 0, false);
    }

    @Override
    public void close() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            Logger.info(e, "Exception on mqtt disconnect");
        }
        try {
            mqttClient.close();
        } catch (MqttException e) {
            Logger.info(e, "Exception on mqtt close");
        }
    }
}