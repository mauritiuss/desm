package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.REST.ThermalPowerPlantRESTConnector;
import ThermalPowerPlant.ThermalPowerPlant;

import java.io.IOException;
import java.util.Random;

public class ThermalPowerPlantGRPCConnector {

    public ThermalPowerPlantGRPCConnector() {

        //1. Avvia il server gRPC sulla porta della ThermalPowerPlant
        startGRPCServer();

        //2. Recupera la prima centrale dell'anello (= la successiva)
        getNextThermalPowerPlant();

        //3. Invia il messaggio di presentazione via gRPC
        sendPresentationMessage();

    }


    /**
     * Avvia il server gRPC sulla porta specificata dalla ThermalPowerPlant.
     */
    private void startGRPCServer() {
        new Thread(() -> {
            try {
                ThermalPowerPlantServer server = new ThermalPowerPlantServer(ThermalPowerPlant.getInstance().getPort());
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
            ThermalPowerPlant.getInstance().setNextId(firstPlant.getId());
            ThermalPowerPlant.getInstance().setNextAddress(firstPlant.getAddress());
            ThermalPowerPlant.getInstance().setNextPort(firstPlant.getPort());
            System.out.println(">>>[ThermalPowerPlantRunner] Prima ThermalPowerPlant dell'anello: " + firstPlant);
        } else {
            System.out.println(">>>[ThermalPowerPlantRunner] Nessuna ThermalPowerPlant trovata.");
        }
    }


    /**
     * Invia un messaggio gRPC di presentazione alla next ThermalPowerPlant nell'anello,
     */
    private void sendPresentationMessage() {
        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

        if (!plant.getId().equals(plant.getNextId()) && plant.getPort() != plant.getNextPort()) {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Invio del messaggio di presentazione.");
            ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                    //Creo il client per collegarmi al successore dell'anello
                    plant.getNextAddress(), plant.getNextPort()
            );
            client.sendPresentation(
                    ThermalPowerPlant.getInstance().getId(),
                    ThermalPowerPlant.getInstance().getAddress(),
                    ThermalPowerPlant.getInstance().getPort(),
                    ThermalPowerPlant.getInstance().getNextId(),
                    ThermalPowerPlant.getInstance().getNextAddress(),
                    ThermalPowerPlant.getInstance().getNextPort()
            );
            client.shutdown();

        } else {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Nessun destinatario valido per il messaggio.");
        }
    }
}
