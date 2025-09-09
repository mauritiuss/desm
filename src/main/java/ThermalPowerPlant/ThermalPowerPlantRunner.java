package ThermalPowerPlant;

import Simulator.Buffer;
import Simulator.SlidingWindowBuffer;
import ThermalPowerPlant.MQTT.ThermalPowerPlantMQTTConnector;
import ThermalPowerPlant.REST.ThermalPowerPlantRESTConnector;
import ThermalPowerPlant.gRPC.ThermalPowerPlantGRPCConnector;
import static common.log.Log.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Questa classe è usata per far partire una ThermalPowerPlant
 */
public class ThermalPowerPlantRunner{
    private static final Random random = new Random();

    public ThermalPowerPlantRunner() { ThermalPowerPlantRESTConnector.getInstance(); }

    // riferimento al connettore MQTT, utile per shutdown
    private static final AtomicReference<ThermalPowerPlantMQTTConnector> MQTT_REF = new AtomicReference<>();

    public static void main(String[] args) {

        //1. Registrazione della ThermalPowerPlant --> REST
        if (!registerThermalPowerPlant()) { //Se non va a buon fine blocca tutto
            w("ThermalPowerPlantRunner","OTHER","Impossibile registrare la ThermalPowerPlant");
            System.exit(0);
        }else{ //Se va a buon fine

            //2. Inizia ad acquisire dati dal sensore di inquinamento
            // Sensore + scheduler
            new Thread(() -> {
                try{
                    Buffer buffer = new SlidingWindowBuffer();
                    ThermalPowerPlant.getInstance().initPollutionMonitoring(buffer);
                    ThermalPowerPlant.getInstance().startAveragesScheduler();
                    i("ThermalPowerPlantRunner","CO2","Sensore CO2 e scheduler avviati.");
                }catch (Exception e){
                    e("ThermalPowerPlantRunner","CO2","Errore avvio sensore/scheduler: " + e.getMessage());
                }
            }, "Starter-Sensor-Scheduler").start();

            //3. Crea un collegamento per la comunicazione gRPC
            // gRPC
            new Thread(() -> {
                ThermalPowerPlantGRPCConnector.getInstance();
            }, "Starter-gRPC").start();

            //4. Crea un collegamento per la comunicazione MQTT
            // MQTT
            new Thread(() -> {
                ThermalPowerPlantMQTTConnector connector = new ThermalPowerPlantMQTTConnector();
                MQTT_REF.set(connector);
            }, "Starter-MQTT").start();

            //5. Console listener per il comando di exit
            startConsoleExitListener();
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

    /**
     * Avvia un thread in background che legge dalla console.
     * Quando l’utente digita 'exit', inizia la procedura di uscita.
     */
    private static void startConsoleExitListener() {
        Thread t = new Thread(() -> {
            i("ThermalPowerPlantRunner","EXIT","Digita 'exit' per uscire dalla rete in modo controllato");
            try(Scanner scanner = new Scanner(System.in)){
                while(true){
                    if(!scanner.hasNextLine()) break;
                    String line = scanner.nextLine();
                    if(line!= null && "exit".equalsIgnoreCase(line.trim())){
                        onExitRequested();
                        break;
                    }
                }
            } catch (Exception e){
                e("ThermalPowerPlantRunner","EXIT"," Errore nel listener della console: "+e.getMessage());
            }
        }, "ConsoleExitListener");
        t.setDaemon(true);
        t.start();
    }

    /**
     * Hook chiamato quando viene richiesto l'uscita (comando 'exit').
     * In questo step stampa soltanto un log; nei prossimi step effettuerà:
     * - notifica agli altri impianti via gRPC,
     * - deregistrazione dal server REST,
     * - shutdown di sensore, scheduler, MQTT, gRPC.
     */
    private static void onExitRequested() {
        i("ThermalPowerPlantRunner","EXIT","Comando di exit ricevuto. Avvio procedura di uscita...");

        // Uscita tramite gRPC
        try{
            ThermalPowerPlantGRPCConnector.getInstance().leaveRing();
        }catch (Exception e){
            e("ThermalPowerPlantRunner","EXIT","Errore leaveRing: " + e.getMessage());
        }

        // Deregistrazione dal server REST
        try{
            boolean ok = ThermalPowerPlantRESTConnector.getInstance().removeThermalPowerPlant();
            i("ThermalPowerPlantRunner","EXIT","Deregistrazione dal server REST: "+ok);
        }catch (Exception e){
            e("ThermalPowerPlantRunner","EXIT","Errore deregistrazione dal server REST: " + e.getMessage());
        }


        // Shutdown locale del sensore, scheduler, MQTT
        try{
            ThermalPowerPlant.getInstance().stopAveragesScheduler();
            ThermalPowerPlant.getInstance().stopPollutionMonitoring();

            ThermalPowerPlantMQTTConnector connector = MQTT_REF.get();
            if (connector != null) {
                connector.stopCo2Publisher();
                connector.disconnect();
            }
        }catch (Exception e){
            e("ThermalPowerPlantRunner","EXIT","Errore shutdown locale: " + e.getMessage());
        }

        i("ThermalPowerPlantRunner","EXIT","Uscita completata.");
        System.exit(0);

    }


}
