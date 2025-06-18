package common.MQTT;

/**
 * Questa classe contiene le informazioni di base del MQTT Broker
 */
public abstract class MQTTProperties {

    public static final String MQTT_BROKER_IP = "localhost";
    public static final String MQTT_BROKER_PORT = "1883";

    public static final String MQTT_BROKER_URL = String.format("tcp://%s:%s", MQTT_BROKER_IP, MQTT_BROKER_PORT);

    public static final int MQTT_KEEP_ALIVE = 120;
    public static final int QOS = 2;

    public static final String BASE_TOPIC = "thermal-power-plant";
    public static final String ENERGY_REQUEST_TOPIC = BASE_TOPIC + "energy/requests";

}
