package AdministrationServer.REST;

import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
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
        System.out.println(">>>[Serice] Ritorno di ThermalPowerPlantList\n");
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
                System.out.println(">>>[Serice] ID già esistente: "+newId);
                return false;
            }
        }

        thermalPowerPlantList.add(thermalPowerPlant);
        System.out.println(">>>[Serice] Aggiunta ThermalPowerPlant con ID: " + newId+"\n");
        return true;
    }

    /**
     * Questo metodo restituisce una ThermalPowerPlant in base all'ID fornito.
     * @param id l'identificatore della ThermalPowerPlant da cercare.
     * @return l'oggetto ThermalPowerPlant se trovato, null altrimenti.
     */
    public synchronized ThermalPowerPlant getById(String id) {
        System.out.println(">>>[Serice] Cercando di restituire la ThermalPowerPlant con ID: " + id);
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            if (thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                System.out.println(">>>[Serice] Ritorno della ThermalPowerPlant con ID: " + id);
                return thermalPowerPlant;
            }
        }
        System.out.println(">>>[Serice] Non è stata trovata nessuna ThermalPowerPlant con ID: " + id);
        return null; // Not found
    }


}
