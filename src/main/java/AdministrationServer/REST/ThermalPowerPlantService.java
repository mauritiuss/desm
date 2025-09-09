package AdministrationServer.REST;

import AdministrationServer.AdministrationServer;
import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import static common.log.Log.*;

/*
 * Service è il componente centrale della logica applicativa per:
 * - Gestire la collezione di ThermalPowerPlant.
 * - Fornire metodi per aggiungere, leggere, cercare e rimuovere impianti.
 * - Garantire sincronizzazione base per l’accesso concorrente.
 * - Essere usato dal Controller per rispondere alle richieste REST.
*/
@Service
public class ThermalPowerPlantService {
    private final List<ThermalPowerPlant> thermalPowerPlantList = new ArrayList<>();

    /**
     * Restituisce una copia della lista di tutte le {@link ThermalPowerPlant} attualmente presenti.
     * @return una nuova lista contenente tutte le ThermalPowerPlant.
     */
    public synchronized List<ThermalPowerPlant> getThermalPowerPlantList() {
        //System.out.println(">>>[ThermalPowerPlantService] Ritorno di ThermalPowerPlantList");
        return new ArrayList<>(thermalPowerPlantList);
    }

    /**
     * Aggiunge una nuova {@link ThermalPowerPlant} alla lista, se il suo ID non è già presente.
     * @param thermalPowerPlant l'oggetto ThermalPowerPlant da aggiungere (non null).
     * @return true se la ThermalPowerPlant è stata aggiunta correttamente, false se l'ID esiste già.
     */
    public synchronized boolean add(ThermalPowerPlant thermalPowerPlant) {
        String newId = thermalPowerPlant.getId();

        for (ThermalPowerPlant p : thermalPowerPlantList) {
            if (p.getId() != null && p.getId().equalsIgnoreCase(newId)) {
                i("ThermalPowerPlantService", "PRESENTATION","ID già esistente: "+newId);
                return false;
            }
        }

        thermalPowerPlantList.add(thermalPowerPlant);
        i("ThermalPowerPlantService", "PRESENTATION","Aggiunta ThermalPowerPlant con ID: " + newId);
        return true;
    }

    /**
     * Restituisce una {@link ThermalPowerPlant} in base all'ID fornito.
     * @param id l'identificatore della ThermalPowerPlant da cercare (non null).
     * @return l'oggetto ThermalPowerPlant se trovato, null altrimenti.
     */
    public synchronized ThermalPowerPlant getById(String id) {
        i("ThermalPowerPlantService", "OTHER","Cercando di restituire la ThermalPowerPlant con ID: " + id);
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            if (thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                i("ThermalPowerPlantService", "OTHER","Ritorno della ThermalPowerPlant con ID: " + id);
                return thermalPowerPlant;
            }
        }
        i("ThermalPowerPlantService", "OTHER","Non è stata trovata nessuna ThermalPowerPlant con ID: " + id);
        return null; // Not found
    }

    /**
     * Calcola la media delle misurazioni di CO₂ tra due timestamp.
     * La logica di calcolo è delegata a {@link AdministrationServer}.
     * @param t1 timestamp iniziale (ms)
     * @param t2 timestamp finale (ms)
     * @return Double con la media se presente, altrimenti null
     * @throws IllegalArgumentException se t1 > t2
     */
    public synchronized Double getAverageBetweenTimestamp(long t1, long t2) {
        double avg = AdministrationServer.getInstance().getAverageBetweenTimestamp(t1, t2);
        return Double.isNaN(avg) ? null : avg;
    }

    /**
     * Rimuove una {@link ThermalPowerPlant} dalla lista tramite ID.
     * @param id l’identificatore della centrale da rimuovere.
     * @return true se la centrale è stata rimossa, false se non esiste alcuna centrale con quell’ID.
     */
    public synchronized boolean removeThermalPowerPlant(String id) {
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            if( thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                thermalPowerPlantList.remove(thermalPowerPlant);
                i("ThermalPowerPlantService", "EXIT","Rimossa ThermalPowerPlant con ID: " + id);
                return true;
            }
        }
        i("ThermalPowerPlantService", "EXIT","Nessuna ThermalPowerPlant con ID: " + id);
        return false;
    }


}
