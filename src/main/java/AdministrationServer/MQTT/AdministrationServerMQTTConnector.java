package AdministrationServer.MQTT;

import AdministrationServer.AdministrationServer;
import Simulator.Measurement;
import common.MQTT.MQTTConnector;
import common.MQTT.MQTTProperties;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static common.log.Log.*;

public class AdministrationServerMQTTConnector extends MQTTConnector {

    private AdministrationServer administrationServer = AdministrationServer.getInstance();

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        super.connectComplete(reconnect, serverURI);

        try{
            client.subscribe(MQTTProperties.CO2_AVERAGES_TOPIC, MQTTProperties.QOS);
            i("AdministrationServerMQTTConnector", "OTHER","Iscritto al topic: "+MQTTProperties.CO2_AVERAGES_TOPIC);
        }catch (MqttException e){
            e("AdministrationServerMQTTConnector","OTHER", "Errore nella sottoscrizione");
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message){
        String payload = new String(message.getPayload());
        String thermalPowerPlantId = extractPlantIdFromTopic(topic);
        i("AdministrationServerMQTTConnector", "CO2","Dati di inquinamento ricevuti per il topic: " + topic +": "+payload);

        try{
            List<Measurement> measurements = parseMeasurements(payload);

            for (Measurement m : measurements) {
                i("AdministrationServerMQTTConnector", "CO2","Measurement parsato: " + m.toString() + ", Id Thermal Power Plant: " + thermalPowerPlantId);
                administrationServer.saveMeasurement(thermalPowerPlantId, m);
            }

            administrationServer.printStorage();

        }catch (Exception e){
            e("AdministrationServerMQTTConnector","CO2", "Errore parsing/salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Measurement> parseMeasurements(String json) {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("Payload vuoto");

        // 1) Trova il blocco dell'array averages: [ {...}, {...}, ... ]
        int a = json.indexOf("\"averages\"");
        if (a < 0) throw new IllegalArgumentException("Campo averages mancante");
        int lb = json.indexOf("[", a);
        int rb = json.indexOf("]", lb);
        if (lb < 0 || rb < 0 || rb <= lb) throw new IllegalArgumentException("Array averages non valido");
        String arr = json.substring(lb + 1, rb); // contenuto tra [ e ]

        // 2) Regex che cattura ogni oggetto dell'array con campi:
        //    sensorId (stringa), type (stringa), value (numero con , o .), timestamp (cifre)
        Pattern pObj = Pattern.compile(
                "\\{[^}]*?\"sensorId\"\\s*:\\s*\"([^\"]+)\"[^}]*?"
                        + "\"type\"\\s*:\\s*\"([^\"]+)\"[^}]*?"
                        + "\"value\"\\s*:\\s*(-?\\d+(?:[.,]\\d+)?)\\s*[^}]*?"
                        + "\"timestamp\"\\s*:\\s*(\\d+)",
                Pattern.DOTALL
        );

        Matcher m = pObj.matcher(arr);
        List<Measurement> out = new ArrayList<>();

        while (m.find()) {
            String sensorId = m.group(1).trim();
            String type     = m.group(2).trim();
            String valueStr = m.group(3).trim().replace(",", "."); // normalizza virgola → punto
            String tsStr    = m.group(4).trim();

            double value = Double.parseDouble(valueStr);
            long ts = Long.parseLong(tsStr);

            out.add(new Measurement(sensorId, type, value, ts));
        }

        if (out.isEmpty()) {
            throw new IllegalArgumentException("Nessun elemento valido in averages");
        }
        return out;
    }

    private String extractPlantIdFromTopic(String topic) {
        if (topic == null) return null;
        String[] parts = topic.split("/");
        // atteso: ["thermal-power-plant", "{plantId}", "co2", "averages"]
        if (parts.length >= 4) {
            return parts[1];
        }
        return null;
    }

}
