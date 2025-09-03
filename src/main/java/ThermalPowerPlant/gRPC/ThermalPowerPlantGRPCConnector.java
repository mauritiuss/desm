package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.REST.ThermalPowerPlantRESTConnector;
import ThermalPowerPlant.ThermalPowerPlant;

import java.io.IOException;

public class ThermalPowerPlantGRPCConnector {

    private static ThermalPowerPlantGRPCConnector instance;

    private ThermalPowerPlantGRPCConnector() {
        //1. Avvia il server gRPC sulla porta della ThermalPowerPlant
        startGRPCServer();
        //2. Recupera la prima centrale dell'anello (= la successiva)
        getNextThermalPowerPlant();
        //3. Invia il messaggio di presentazione via gRPC
        sendPresentationMessage();
    }

    public static synchronized ThermalPowerPlantGRPCConnector getInstance() {
        if (instance == null) {
            instance = new ThermalPowerPlantGRPCConnector();
        }
        return instance;
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
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Prima ThermalPowerPlant dell'anello: " + firstPlant);
        } else {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Nessuna ThermalPowerPlant trovata.");
        }
    }

    /**
     * Invia un messaggio gRPC di presentazione alla next ThermalPowerPlant nell'anello,
     */
    private void sendPresentationMessage() {
        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

        if (!isAloneInRing()) {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] "+plant.getId()+" Invio del messaggio di presentazione al mio next: "+plant.getNextId());
            //Creo il client per collegarmi al successore dell'anello
            ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                    plant.getNextAddress(), plant.getNextPort()
            );
            client.sendPresentation(
                    plant.getId(),
                    plant.getAddress(),
                    plant.getPort(),
                    plant.getNextId(),
                    plant.getNextAddress(),
                    plant.getNextPort()
            );
            client.shutdown();

        } else {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Nessun destinatario valido per il messaggio di presentazione.");
        }
    }

    /**
     * Avvia il processo di elezione secondo l'algoritmo di Chang-Roberts.
     * Questo metodo viene chiamato quando la Thermal Power Plant riceve una richiesta di energia
     * e deve partecipare a un'elezione per stabilire quale nodo fornirà l'energia richiesta.
     * @param requestTimestamp il timestamp della richiesta di energia (identificativo)
     * @param energyRequestKwh la quantità di energia richiesta in kWh
     */
    public void startElection(String requestTimestamp, int energyRequestKwh) {
        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

        if (!isAloneInRing()) {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] "+plant.getId()+" Invio del messaggio di elezione per richiesta: "+requestTimestamp+", energia richiesta: "+energyRequestKwh+" Valore proposto: "+plant.getPrice(requestTimestamp));
            //Creo il client per collegarmi al successore dell'anello
            ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                    plant.getNextAddress(), plant.getNextPort()
            );

            client.sendChangRobertsMessage(
                    true,
                    requestTimestamp,
                    plant.getId(),
                    plant.getPrice(requestTimestamp),
                    energyRequestKwh,
                    null
            );
            client.shutdown();
        } else {
            System.out.println(">>>[ThermalPowerPlantGRPCConnector] Nessun destinatario valido per il messaggio di elezione.");

            //TODO: Sono l'unico nodo nell'anello, quindi soddisfo la richiesta.
        }

    }

    /**
     * Verifica se questo nodo è l'unico presente nell'anello.
     * @return true se il nodo è da solo nell'anello, false altrimenti
     */
    public boolean isAloneInRing(){
        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();
        return plant.getId().equals(plant.getNextId()) && plant.getPort() == plant.getNextPort();
    }
}
