package AdministrationServer.REST;

import AdministrationServer.AdministrationServer;
import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import static common.log.Log.*;

/*
    Service è il componente centrale della logica applicativa per:
    •	Aggiungere, leggere, cercare gli impianti
	•	Garantire sincronizzazione base
	•	Essere usato dal controller per rispondere alle richieste REST
	Collection of resourses --> List of Thermal power plant
*/

@Service
public class ThermalPowerPlantService {
    private final List<ThermalPowerPlant> thermalPowerPlantList = new ArrayList<>();

    /**
     * Questo metodo restituisce una copia della lista di tutte le ThermalPowerPlant attualmente presenti.
     * @return una nuova lista contenente tutte le ThermalPowerPlant.
     */
    public synchronized List<ThermalPowerPlant> getThermalPowerPlantList() {
        //System.out.println(">>>[ThermalPowerPlantService] Ritorno di ThermalPowerPlantList");
        return new ArrayList<>(thermalPowerPlantList);
    }

    /**
     * Questo metodo imposta una nuova lista di ThermalPowerPlant, sovrascrivendo quella esistente.
     * @param thermalPowerPlants la nuova lista di ThermalPowerPlant da impostare.
     */
    public synchronized void setThermalPowerPlantList(List<ThermalPowerPlant> thermalPowerPlants) {
        thermalPowerPlantList.clear();
        thermalPowerPlantList.addAll(thermalPowerPlants);
    }

    /**
     * Questo metodo aggiunge una nuova ThermalPowerPlant alla lista, se il suo ID non è già presente.
     * @param thermalPowerPlant l'oggetto ThermalPowerPlant da aggiungere.
     * @return true se la ThermalPowerPlant è stata aggiunta correttamente, false se l'ID esiste già.
     */
    public synchronized boolean add(ThermalPowerPlant thermalPowerPlant) {
        String newId = thermalPowerPlant.getId();

        for (ThermalPowerPlant p : thermalPowerPlantList) {
            if (p.getId() != null && p.getId().equalsIgnoreCase(newId)) {
                i("ThermalPowerPlantService", "ID già esistente: "+newId);
                return false;
            }
        }

        thermalPowerPlantList.add(thermalPowerPlant);
        i("ThermalPowerPlantService", "Aggiunta ThermalPowerPlant con ID: " + newId);
        return true;
    }

    /**
     * Questo metodo restituisce una ThermalPowerPlant in base all'ID fornito.
     * @param id l'identificatore della ThermalPowerPlant da cercare.
     * @return l'oggetto ThermalPowerPlant se trovato, null altrimenti.
     */
    public synchronized ThermalPowerPlant getById(String id) {
        i("ThermalPowerPlantService", "Cercando di restituire la ThermalPowerPlant con ID: " + id);
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            if (thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                i("ThermalPowerPlantService", "Ritorno della ThermalPowerPlant con ID: " + id);
                return thermalPowerPlant;
            }
        }
        i("ThermalPowerPlantService", "Non è stata trovata nessuna ThermalPowerPlant con ID: " + id);
        return null; // Not found
    }

    /**
     * Calcola la media tra due timestamp delegando all'AdministrationServer.
     * @param t1 timestamp iniziale (ms)
     * @param t2 timestamp finale (ms)
     * @return Double con la media se presente, altrimenti null
     * @throws IllegalArgumentException se t1 > t2
     */
    public synchronized Double getAverageBetweenTimestamp(long t1, long t2) {
        double avg = AdministrationServer.getInstance().getAverageBetweenTimestamp(t1, t2);
        return Double.isNaN(avg) ? null : avg;
    }

    public synchronized boolean removeThermalPowerPlant(String id) {
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            if( thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                thermalPowerPlantList.remove(thermalPowerPlant);
                i("ThermalPowerPlantService", "Rimossa ThermalPowerPlant con ID: " + id);
                return true;
            }
        }
        i("ThermalPowerPlantService", "Nessuna ThermalPowerPlant con ID: " + id);
        return false;
    }


}
