package ThermalPowerPlant;

import ThermalPowerPlant.MQTT.ThermalPowerPlantMQTTConnector;
import ThermalPowerPlant.REST.ThermalPowerPlantRESTConnector;
import ThermalPowerPlant.gRPC.ThermalPowerPlantGRPCConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;


/**
 * Questa classe è usata per far partire una ThermalPowerPlant
 */
public class ThermalPowerPlantRunner extends Thread{
    private static final Random random = new Random();
    private static ThermalPowerPlant thermalPowerPlant;

    public ThermalPowerPlantRunner() { ThermalPowerPlantRESTConnector.getInstance(); }

    public static void main(String[] args) {

        //1. Registrazione della ThermalPowerPlant --> REST
        thermalPowerPlant = registerThermalPowerPlant();

        if (thermalPowerPlant == null) { //Se non va a buon fine blocca tutto
            System.out.println(">>>[ThermalPowerPlantRunner] Impossibile registrare la ThermalPowerPlant.");
            System.exit(0);
        }else{ //Se va a buon fine

            //2. Crea un collegamento per la comunicazione gRPC
            new Thread(() -> {
                new ThermalPowerPlantGRPCConnector();
            }).start();

            //3. Crea un collegamento per la comunicazione MQTT
            new Thread(() -> {
                new ThermalPowerPlantMQTTConnector();
            }).start();
        }

    }

    /**
     * Registra una nuova ThermalPowerPlant generando ID, address e porta dinamica.
     * @return la ThermalPowerPlant se registrata correttamente, null altrimenti.
     */
    private static ThermalPowerPlant registerThermalPowerPlant() {
        String id = String.valueOf(random.nextInt(Integer.MAX_VALUE - 1) + 1); // id > 0
        String address = "localhost";
        int port = getFreePort();
        ThermalPowerPlant plant = new ThermalPowerPlant(id, address, port);

        boolean success = ThermalPowerPlantRESTConnector.getInstance().addThermalPowerPlant(plant);

        if (success) {
            System.out.println(">>>[ThermalPowerPlantRunner] Inserimento della ThermalPowerPlant avvenuto correttamente");
            return plant;
        } else {
            System.out.println(">>>[ThermalPowerPlantRunner] Inserimento della ThermalPowerPlant fallito");
            return null;
        }
    }

    /**
     * Restituisce una porta TCP libera assegnata dinamicamente dal sistema operativo.
     * @return Un numero di porta disponibile.
     * @throws RuntimeException se non è possibile ottenere una porta libera.
     */
    private static int getFreePort(){
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }catch (IOException e){
            throw new RuntimeException(">>>[ThermalPowerPlantRunner] Nessuna porta disponibile, errore: "+e);
        }
    }


}
