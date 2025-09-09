package common.MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import static common.log.Log.*;

/**
 * Componente base per la comunicazione MQTT
 * Nota: Usa {@link #publishNonRetained(String, String)} per evitare che il broker mantenga l’ultimo messaggio (utile per stream di misure).
 * Usa {@link #publish(String, String)} (retained=true) solo per messaggi “di stato” che devono essere consegnati ai nuovi subscriber.
 */
public abstract class MQTTConnector implements MqttCallbackExtended {
    protected MqttClient client;
    protected MqttConnectOptions options;

    /**
     * Costruttore per MQTTConnector
     * Inizializza il client con tutte le opzioni necessarie e si connette al broker MQTT.
     * @throws RuntimeException se la connessione non può essere creata (termina il processo).
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
            e("MQTTConnector", "Errore creando il client MQTT: ", e);
            System.exit(0);
        }
    }

    /**
     * Disconnette il client dal broker MQTT.
     */
    public void disconnect() {
        try{
            client.disconnect();
            i("MQTTConnector", "OTHER", "Disconnessione dal broker MQTT avvenuta con successo");
        }catch (MqttException e){
            e("MQTTConnector", "OTHER","Errore disconnessione dal broker MQTT");
        }
    }

    /**
     * Pubblica un messaggio RETAINED su un topic.
     * Il messaggio rimane memorizzato sul broker e verrà consegnato ai futuri subscriber del topic.
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
            e("MQTTConnector", "Errore publish", e);
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
            e("MQTTConnector", "Errore publishNonRetained", e);
        }
    }

    /**
     * Metodi generati automaticamente
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI){
        if (reconnect) {
            i("MQTTConnector", "OTHER",ThermalPowerPlant.ThermalPowerPlant.getInstance().getId() + " Riconnessione al MQTT broker avvenuta");
        }else{
            i("MQTTConnector", "OTHER","Connessione al MQTT broker avvenuta");
        }
    }

    @Override
    public void connectionLost(Throwable cause){
        e("MQTTConnector", "OTHER","Connessione persa: " + cause.getMessage());
        if (cause instanceof MqttException) {
            MqttException me = (MqttException) cause;
            e("MQTTConnector","OTHER", "  - Reason code: " + me.getReasonCode());
            e("MQTTConnector", "  - Inner cause: " + me.getCause(), me);
            me.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        i("MQTTConnector", "OTHER","Topic: " + topic + " | Messaggio: " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //i("MQTTConnector", "Messaggio consegnato: " + token.toString());
    }


}
