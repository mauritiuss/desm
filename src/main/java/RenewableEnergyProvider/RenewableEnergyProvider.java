package RenewableEnergyProvider;

import common.MQTT.MQTTConnector;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static common.MQTT.MQTTProperties.ENERGY_REQUEST_TOPIC;

public class RenewableEnergyProvider extends MQTTConnector {

    private static final int PUBLISH_INTERVAL_MS = 10_000;
    private final Random random = new Random();

    public RenewableEnergyProvider() {
        super(); //Richiama il costruttore di MQTTConnector
        startPublishing();
    }

    private void startPublishing() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long timestamp = System.currentTimeMillis();
                int energyRequested = 5000 + random.nextInt(10001);

                String payload = String.format(
                        "{\"timestamp\": %d, \"energyRequested\": %d}",
                        timestamp, energyRequested
                );

                publish(ENERGY_REQUEST_TOPIC, payload); //Utilizza il metodo della superclasse
                System.out.println(">>>[RenewableEnergyProvider] Inviata richiesta per payload: " + payload);
            }
        }, 0, PUBLISH_INTERVAL_MS);
    }


    public static void main(String[] args) {
        new RenewableEnergyProvider();
    }

}
