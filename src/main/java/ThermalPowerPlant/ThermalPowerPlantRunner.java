package ThermalPowerPlant;

import ThermalPowerPlant.MQTT.ThermalPowerPlantMQTTConnector;

/**
 * Questa classe è usata per far partire una ThermalPowerPlant
 */
public class ThermalPowerPlantRunner {
    public static void main(String[] args) {
        new ThermalPowerPlantMQTTConnector();
    }
}
