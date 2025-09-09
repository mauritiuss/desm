package RenewableEnergyProvider;

import common.MQTT.MQTTConnector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import static common.log.Log.*;

import static common.MQTT.MQTTProperties.ENERGY_REQUEST_TOPIC;

public class RenewableEnergyProvider extends MQTTConnector {

    private static final int PUBLISH_INTERVAL_MS = 10_000;
    private final Random random = new Random();

    public RenewableEnergyProvider() {
        super(); //Richiama il costruttore di MQTTConnector
        startPublishing();
    }

    private void startPublishing() {

        testSinglePublishing();
        testSinglePublishing();
        //testSinglePublishing();


        /*Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Ottieni l'orario attuale
                LocalDateTime now = LocalDateTime.now();

                // Crea un formatter con il formato desiderato
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                // Format del timestamp in una stringa
                String timestamp = now.format(formatter);
                int energyRequested = 5000 + random.nextInt(10001);

                String payload = String.format(
                        "{\"timestamp\": \"%s\", \"energyRequested\": %d}",
                        timestamp, energyRequested
                );

                publish(ENERGY_REQUEST_TOPIC, payload); //Utilizza il metodo della superclasse
                i("RenewableEnergyProvider","Inviata richiesta per payload: " + payload);
            }
        }, 0, PUBLISH_INTERVAL_MS);*/
    }

    /*
    Metodo per testare una singola richiesta di energia
     */
    public void testSinglePublishing(){
        //TEST
        // Ottieni l'orario attuale
        LocalDateTime now = LocalDateTime.now();

        // Crea un formatter con il formato desiderato
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Format del timestamp in una stringa
        String timestamp = now.format(formatter);
        int energyRequested = 5000 + random.nextInt(10001);

        String payload = String.format(
                "{\"timestamp\": \"%s\", \"energyRequested\": %d}",
                timestamp, energyRequested
        );

        publish(ENERGY_REQUEST_TOPIC, payload); //Utilizza il metodo della superclasse
        i("RenewableEnergyProvider","OTHER","Inviata richiesta per payload: " + payload);
    }


    public static void main(String[] args) {
        new RenewableEnergyProvider();
    }

}
