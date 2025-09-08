package common.MQTT;

import ThermalPowerPlant.ThermalPowerPlantRunner;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Questa classe astratta contiene tutti i metodi per la comunicazione MQTT
 */
public abstract class MQTTConnector implements MqttCallbackExtended {
    protected MqttClient client;
    protected MqttConnectOptions options;

    /**
     * Costruttore per MQTTConnector
     * Inizializza il client con tutte le opzioni necessarie
     * e si connette al broker MQTT
     */
    protected MQTTConnector() {
        try {
            client = new MqttClient(
                    MQTTProperties.MQTT_BROKER_URL,
                    MqttClient.generateClientId(),
                    new MemoryPersistence());

            options = new MqttConnectOptions();
            options.setKeepAliveInterval(MQTTProperties.MQTT_KEEP_ALIVE);
            options.setCleanSession(true); //Opzione per una connessione pulita
            options.setAutomaticReconnect(true); //Opzione per riconnettersi automaticamente se la connessione si interrompe

            client.setCallback(this);
            client.connect(options);

        } catch (MqttException e) {
            System.out.println(">>>[MQTTConnector] Errore creando il client MQTT: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Metodo per disconnettere il client dal MQTT Broker
     */
    public void disconnect() {
        try{
            client.disconnect();
            System.out.println(">>>[MQTTConnector] Disconnessione dal broker MQTT avvenuta con successo");
        }catch (MqttException e){
            System.out.println(">>>[MQTTConnector] Errore disconnessione dal broker MQTT: " + e.getMessage());
        }
    }

    /**
     * Pubblica un messaggio su un argomento dato
     * @param topic l'argomento su cui verrà pubblicato il messaggio
     * @param payload il messaggio da pubblicare
     */
    public void publish(String topic, String payload){
        try{
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MQTTProperties.QOS);
            message.setRetained(true);
            client.publish(topic, message);
        }catch (MqttException e){
            System.out.println(">>>[MQTTConnector] Errore publish: " + e.getMessage());
        }
    }

    /**
     * Pubblica un messaggio su un argomento dato
     * @param topic l'argomento su cui verrà pubblicato il messaggio
     * @param payload il messaggio da pubblicare
     */
    public void publishNonRetained(String topic, String payload) {
        try {
            MqttMessage msg = new MqttMessage(payload.getBytes());
            msg.setQos(MQTTProperties.QOS);
            msg.setRetained(false);
            client.publish(topic, msg);
        } catch (MqttException e) {
            System.out.println(">>>[MQTTConnector] Errore publishNonRetained: " + e.getMessage());
        }
    }


    /**
     * Metodi generati automaticamente
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI){
        if (reconnect) {
            System.out.println(">>>[MQTTConnector] "+ ThermalPowerPlant.ThermalPowerPlant.getInstance().getId() + " Riconnessione al MQTT broker avvenuta");
        }else{
            if (ThermalPowerPlant.ThermalPowerPlant.getInstance().getId() == null){
                System.out.println(">>>[MQTTConnector] RenewableEnergyProvider connessione al MQTT broker avvenuta");
            }else{
                System.out.println(">>>[MQTTConnector] "+ ThermalPowerPlant.ThermalPowerPlant.getInstance().getId() + " Connessione al MQTT broker avvenuta");
            }
        }
    }

    @Override
    public void connectionLost(Throwable cause){
        System.out.println(">>>[MQTTConnector] Connessione persa: " + cause.getMessage());
        if (cause instanceof MqttException) {
            MqttException me = (MqttException) cause;
            System.out.println(">>>[MQTTConnector] Reason code: " + me.getReasonCode());
            System.out.println(">>>[MQTTConnector] Inner cause: " + me.getCause());
            me.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(">>>[MQTTConnector] Topic: " + topic + "+Messaggio: " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println(">>>[MQTTConnector] Messaggio consegnato: "+token.toString());
    }


}
