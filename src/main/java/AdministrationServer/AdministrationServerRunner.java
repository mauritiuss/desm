package AdministrationServer;

import AdministrationServer.MQTT.AdministrationServerMQTTConnector;
import common.MQTT.MQTTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import static common.log.Log.*;

/**
 * Questa classe è usata per far partire il Server
 */
@SpringBootApplication
public class AdministrationServerRunner {

    public static void main(String[] args) {
        SpringApplication.run(AdministrationServerRunner.class, args);
        i("AdministrationServerRunner", "Server running on http://localhost:8080");

        AdministrationServer server = AdministrationServer.getInstance();

        //Crea un collegamento per la comunicazione MQTT
        new Thread(() -> {
            new AdministrationServerMQTTConnector();
        }).start();


    }
}
