#!/bin/bash
java -Xms80M -Xmx80m -Xlog:gc -XX:+UseG1GC -jar Mks2MqttReporter.jar -ms tcp://192.168.1.11:1883 -ph 192.168.1.173 -pp 8080