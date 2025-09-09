package ThermalPowerPlant.MQTT;

import Simulator.Measurement;
import ThermalPowerPlant.ThermalPowerPlant;
import ThermalPowerPlant.gRPC.ThermalPowerPlantGRPCConnector;
import common.MQTT.MQTTConnector;
import common.MQTT.MQTTProperties;
import common.model.Co2AveragesPacket;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import static common.log.Log.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
            i("ThermalPowerPlantMQTTConnector","OTHER",""+ ThermalPowerPlant.getInstance().getId() + " Iscritto al topic: " + MQTTProperties.ENERGY_REQUEST_TOPIC+"\n");
        }catch (MqttException e){
            e("ThermalPowerPlantMQTTConnector","OTHER","Errore nella sottoscrizione: "+e.getMessage());
        }

        //Avvio del publisher delle medie co2
        startCo2Publisher();
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
        i("ThermalPowerPlantMQTTConnector","OTHER","Richiesta di energia ricevuta per il topic: " + topic+": "+payload);

        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

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


    //Metodi per i dati dell'inquinamento
    private boolean stopPublisher = false;
    private Thread publisherThread;

    // Avvia il thread che invia i pacchetti di medie CO2 non appena disponibili
    private synchronized void startCo2Publisher() {
        if (publisherThread != null && publisherThread.isAlive()) return;

        publisherThread = new Thread(() -> {
            i("ThermalPowerPlantMQTTConnector","CO2","Publisher CO2 avviato.");
            while (!stopPublisher && !Thread.currentThread().isInterrupted()) {
                try{
                    ThermalPowerPlant plant = ThermalPowerPlant.getInstance();
                    if(plant == null || plant.getAveragesScheduler() == null){
                        Thread.sleep(1000);
                        continue;
                    }

                    //Prendo i pacchetti accumulati nello scheduler (creati ogni 10s)
                    List<Co2AveragesPacket> packets = plant.getAveragesScheduler().fetchAndClearPendingPackets();

                    if(packets != null && !packets.isEmpty()){
                        for (Co2AveragesPacket packet : packets) {
                            String plantId = packet.getThermalPowerPlantId();
                            String topic = MQTTProperties.co2AveragesTopicFor(plantId); //--> Per pubblicare sul topic dedicato
                            String payload = toJson(packet);
                            publishNonRetained(topic, payload);
                            i("ThermalPowerPlantMQTTConnector","CO2","Pubblicato packet CO2 su MQTT: " + payload);
                        }
                    }else{
                        Thread.sleep(500);
                    }
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }catch (Exception e){
                    e("ThermalPowerPlantMQTTConnector","CO2","Errore publisher: " + e.getMessage());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            i("ThermalPowerPlantMQTTConnector","CO2","Publisher CO2 terminato");
        }, "Co2AveragesPublisher");
        publisherThread.setDaemon(true);
        publisherThread.start();
    }

    // Ferma il publisher
    public synchronized void stopCo2Publisher() {
        stopPublisher = true;
        if (publisherThread != null) {
            publisherThread.interrupt();
            Thread.currentThread().interrupt();
        }
    }

    // Serializza un Co2AveragesPacket in JSON
    private String toJson(Co2AveragesPacket packet) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTs = now.format(formatter);

        // Lista medie come stringa compatta
        StringBuilder averagesJson = new StringBuilder("[");
        List<Measurement> list = packet.getAverages();
        for (int i = 0; i < list.size(); i++) {
            Measurement m = list.get(i);
            averagesJson.append(String.format(
                    "{\"sensorId\": \"%s\", \"type\": \"%s\", \"value\": %.2f, \"timestamp\": %d}",
                    m.getId(), m.getType(), m.getValue(), m.getTimestamp()
            ));
            if (i < list.size() - 1) averagesJson.append(",");
        }
        averagesJson.append("]");

        // Payload finale
        return String.format(
                "{\"plantId\": \"%s\", \"timestamp\": \"%s\", \"averages\": %s}",
                packet.getThermalPowerPlantId(), formattedTs, averagesJson.toString()
        );
    }


}
