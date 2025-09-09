package AdministrationServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Simulator.Measurement;
import static common.log.Log.*;

/**
 * Singleton che rappresenta il server di amministrazione.
 * Tutti i metodi sono sincronizzati per garantire la consistenza dei dati in scenari multi-threaded (es. ricezione concorrente di dati via MQTT).
 */
public class AdministrationServer {

    // Archivio in-memory: plantId -> elenco di misure average ricevute
    private final Map<String, List<Measurement>> storage = new HashMap<>();

    // Singleton
    private static AdministrationServer instance;
    public static synchronized AdministrationServer getInstance() {
        if (instance == null) {
            instance = new AdministrationServer();
        }
        return instance;
    }

    //Costruttore vuoto
    private AdministrationServer() {}


    /**
     * Salva una misura per una {@link ThermalPowerPlant}. Se non esiste ancora una lista associata al plantId, viene creata.
     * @param plantId identificativo della ThermalPowerPlant
     * @param measurement misura da aggiungere
     */
    public synchronized void saveMeasurement(String plantId, Measurement measurement) {
        if (plantId == null || measurement == null) return;

        storage.computeIfAbsent(plantId, k -> new ArrayList<>()).add(measurement);
    }

    /**
     * Stampa sul log lo stato corrente dello storage, raggruppando le misure per ogni {@code plantId}.
     */
    public synchronized void printStorage() {
        i("AdministrationServer", "CO2", "Contenuto dello storage:");
        for (Map.Entry<String, List<Measurement>> entry : storage.entrySet()) {
            String plantId = entry.getKey();
            List<Measurement> measurements = entry.getValue();
            i("AdministrationServer", "CO2","   PlantId = " + plantId);
            for (Measurement m : measurements) {
                i("AdministrationServer", "CO2","      - " + m.toString());
            }
        }
    }

    /**
     * Calcola la media dei valori delle misure il cui timestamp è compreso tra t1 e t2.
     * @param t1 timestamp iniziale (in millisecondi), deve essere minore di t2
     * @param t2 timestamp finale (in millisecondi), deve essere maggiore di t1
     * @return media dei valori nel range temporale specificato, oppure {@code Double.NaN} se non ci sono misure nel range
     * @throws IllegalArgumentException se t1 è maggiore di t2
     */
    public synchronized double getAverageBetweenTimestamp(long t1, long t2){
        if(t1 > t2){
            throw new IllegalArgumentException("t1 deve essere minore di t2");
        }

        double sum = 0;
        int count  = 0;

        for(List<Measurement> list : storage.values()){
            for(Measurement m : list){
                long timestamp = m.getTimestamp();
                if(timestamp >= t1 && timestamp <= t2){
                    sum += m.getValue();
                    count++;
                }
            }
        }
        return count > 0 ? sum/count : Double.NaN;
    }

}
