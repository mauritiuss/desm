package ThermalPowerPlant;

import ThermalPowerPlant.MQTT.ThermalPowerPlantMQTTConnector;
import ThermalPowerPlant.REST.ThermalPowerPlantRESTConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

/**
 * Questa classe è usata per far partire una ThermalPowerPlant
 */
public class ThermalPowerPlantRunner {

    public ThermalPowerPlantRunner() { ThermalPowerPlantRESTConnector.getInstance(); }

    private static ThermalPowerPlant thermalPowerPlant;
    private static Random random = new Random();

    public static void main(String[] args) {
        String id = String.valueOf(random.nextInt(Integer.MAX_VALUE - 1) + 1); // id > 0
        String address = "localhost";
        int port = getFreePort();
        thermalPowerPlant = new ThermalPowerPlant(id, address, port);

        ThermalPowerPlantRESTConnector.getInstance().addThermalPowerPlant(thermalPowerPlant);











        //new ThermalPowerPlantMQTTConnector();
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
