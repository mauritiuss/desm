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

    public ThermalPowerPlantRunner() { ThermalPowerPlantRESTConnector.getInstance(); }

    public static void main(String[] args) {

        //1. Registrazione della ThermalPowerPlant --> REST
        if (!registerThermalPowerPlant()) { //Se non va a buon fine blocca tutto
            System.out.println(">>>[ThermalPowerPlantRunner] Impossibile registrare la ThermalPowerPlant.");
            System.exit(0);
        }else{ //Se va a buon fine

            //2. Inizia ad acquisire dati dal sensore di inquinamento
            new Thread(() -> {

            }).start();

            //3. Crea un collegamento per la comunicazione gRPC
            new Thread(() -> {
                ThermalPowerPlantGRPCConnector.getInstance();
            }).start();

            //4. Crea un collegamento per la comunicazione MQTT
            new Thread(() -> {
                new ThermalPowerPlantMQTTConnector();
            }).start();
        }

    }

    /**
     * Registra una nuova ThermalPowerPlant generando ID, address e porta dinamica.
     * @return la ThermalPowerPlant se registrata correttamente, null altrimenti.
     */
    private static boolean registerThermalPowerPlant() {
        String id = String.valueOf(random.nextInt(Integer.MAX_VALUE - 1) + 1); // id > 0
        String address = "localhost";
        int port = getFreePort();
        ThermalPowerPlant.initialize(id, address, port);

        return ThermalPowerPlantRESTConnector.getInstance().addThermalPowerPlant();
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
