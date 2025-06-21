package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.REST.ThermalPowerPlantRESTConnector;
import ThermalPowerPlant.ThermalPowerPlant;

import java.io.IOException;
import java.util.Random;

public class ThermalPowerPlantGRPCConnector {
    private static ThermalPowerPlant thermalPowerPlant;
    private static final Random random = new Random();

    public ThermalPowerPlantGRPCConnector() {
        /*thermalPowerPlant = registerThermalPowerPlant();

        if (thermalPowerPlant == null) {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Impossibile registrare la ThermalPowerPlant.");
            System.exit(0);
        }*/

        //1. Avvia il server gRPC sulla porta della ThermalPowerPlant
        startGRPCServer();

        //2. Recupera la centrale successiva
        ThermalPowerPlant nextPlant = thermalPowerPlant.getNextThermalPowerPlant();

        //3. Invia il messaggio di presentazione via gRPC
        if (nextPlant != null && !nextPlant.getId().equals(thermalPowerPlant.getId())) {
            ThermalPowerPlantClient client = new ThermalPowerPlantClient(nextPlant.getAddress(), nextPlant.getPort());
            client.sendPresentation(
                    thermalPowerPlant.getId(),
                    thermalPowerPlant.getAddress(),
                    thermalPowerPlant.getPort()
            );
            client.shutdown();
        } else {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Nessun destinatario valido per il messaggio.");
        }

    }

    private void startGRPCServer() {
        new Thread(() -> {
            try {
                ThermalPowerPlantServer server = new ThermalPowerPlantServer(thermalPowerPlant.getPort());
                server.start();
            } catch (IOException e) {
                System.err.println(">>>[GRPCConnector] Errore durante l'avvio del server gRPC: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Recupera la prima ThermalPowerPlant registrata, se esiste e la mette come next --> Situazione ad anello.
     */
    private static void getNextThermalPowerPlant() {
        ThermalPowerPlant[] plants = ThermalPowerPlantRESTConnector.getInstance().getThermalPowerPlants();

        if (plants != null && plants.length > 0) {
            ThermalPowerPlant firstPlant = plants[0];
            thermalPowerPlant.setNextThermalPowerPlant(firstPlant);
            System.out.println(">>>[ThermalPowerPlantRunner] Prima ThermalPowerPlant registrata: " + firstPlant);
        } else {
            System.out.println(">>>[ThermalPowerPlantRunner] Nessuna ThermalPowerPlant trovata.");
        }
    }
}
