package ThermalPowerPlant.MQTT;

import ThermalPowerPlant.ThermalPowerPlant;
import ThermalPowerPlant.gRPC.ThermalPowerPlantGRPCConnector;
import common.MQTT.MQTTConnector;
import common.MQTT.MQTTProperties;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
            System.out.println(">>>[ThermalPowerPlantMQTTConnector] "+ ThermalPowerPlant.getInstance().getId() + " Iscritto al topic: " + MQTTProperties.ENERGY_REQUEST_TOPIC+"\n");
        }catch (MqttException e){
            System.out.println(">>>[ThermalPowerPlantMQTTConnector] Errore nella sottoscrizione: "+e.getMessage());
        }
    }


    /**
     * Callback invocata quando un messaggio MQTT viene ricevuto sul topic sottoscritto.
     * Il messaggio viene ricevuto MQTT ma la comunicazione tra ThermalPowerPlant avviene tramite GRPC.
     * @param topic il topic MQTT su cui è arrivato il messaggio
     * @param message il messaggio MQTT contenente la richiesta di energia (in formato JSON)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message){
        String payload = new String(message.getPayload());
        System.out.println(">>>[ThermalPowerPlantMQTTConnector] Richiesta di energia ricevuta per il topic: " + topic+": "+payload);

        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

        //TODO generare valore + far partire elezione

        //La ThermalPowerPlant propone il valore soltanto se non è occupata
        if(!plant.isBusy()){
            String timestamp = extractionTimestamp(payload);
            int energyRequest = extractionKwh(payload);

            plant.addPrice(timestamp);

            ThermalPowerPlantGRPCConnector.getInstance().startElection(timestamp, energyRequest);
        }
    }

    /**
     * Estrae il valore del campo "timestamp" da una stringa JSON.
     * Se il campo non viene trovato o il formato non è corretto,
     * restituisce {@code null}.
     * @param payload la stringa JSON da cui estrarre il timestamp
     * @return il valore del timestamp come stringa, oppure {@code null} se non trovato
     */
    private String extractionTimestamp(String payload) {
        String key = "\"timestamp\": \"";
        int start = payload.indexOf(key);
        if (start == -1) {
            return null;
        }
        start += key.length(); // sposta l'indice dopo il prefisso
        int end = payload.indexOf("\"", start);
        if (end == -1) {
            return null;
        }
        return payload.substring(start, end);
    }

    /**
     * Estrae il valore del campo "energyRequested" da una stringa JSON.
     * Se il campo non viene trovato o il formato non è corretto, restituisce {@code -1}.
     * @param payload la stringa JSON da cui estrarre il valore
     * @return il valore intero di energyRequested, oppure {@code -1} se non trovato
     */
    private int extractionKwh(String payload) {
        String key = "\"energyRequested\": ";
        int start = payload.indexOf(key);
        if (start == -1) {
            return -1;
        }

        start += key.length(); // posizione dell'inizio del numero
        int end = payload.indexOf("}", start); // oppure payload.indexOf(",", start) se ce ne sono altri campi dopo

        String numberString = payload.substring(start, end).trim();

        try {
            return Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            return -1; // oppure lancia eccezione, a seconda dei tuoi requisiti
        }
    }
}
