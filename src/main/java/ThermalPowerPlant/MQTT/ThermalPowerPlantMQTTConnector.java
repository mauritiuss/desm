package ThermalPowerPlant.MQTT;

import common.MQTT.MQTTConnector;
import common.MQTT.MQTTProperties;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static common.MQTT.MQTTProperties.ENERGY_REQUEST_TOPIC;

/**
 * Questa classe è l'MQTTConnector per la ThermalPowerPlant
 * Contiene tutti i metodi per leggere le comunicazioni
 */
public class ThermalPowerPlantMQTTConnector extends MQTTConnector {

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        super.connectComplete(reconnect, serverURI);

        try{
            client.subscribe(MQTTProperties.ENERGY_REQUEST_TOPIC, MQTTProperties.QOS);
            System.out.println(">>>[ThermalPowerPlantMQTTConnector] Iscritto al topic: " + MQTTProperties.ENERGY_REQUEST_TOPIC);
        }catch (MqttException e){
            System.out.println(">>>[ThermalPowerPlantMQTTConnector] Errore nella sottoscrizione: "+e.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message){
        String payload = new String(message.getPayload());
        System.out.println(">>>[ThermalPowerPlantMQTTConnector] Richiesta di energia ricevuta: ");
        System.out.println(">>>[ThermalPowerPlantMQTTConnector] Topic: "+topic);
        System.out.println(">>>[ThermalPowerPlantMQTTConnector] Payload: "+payload + "\n");



        // Simula decisione (50% di probabilità di accettare la richiesta)
        if (Math.random() < 0.5) {
            System.out.println(">>>[ThermalPowerPlantMQTTConnector] Richiesta ACCETTATA da questa TPP: ");
            // Qui eventualmente potresti pubblicare una risposta o loggare
        } else {
            System.out.println(">>>[ThermalPowerPlantMQTTConnector] Richiesta IGNORATA da questa TPP");
        }

        //TODO --> Cosa deve fare quando riceve una richiesta di energia
    }
}
