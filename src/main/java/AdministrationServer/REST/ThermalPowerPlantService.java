package AdministrationServer.REST;

import common.ThermalPowerPlant;
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

    public synchronized List<ThermalPowerPlant> getThermalPowerPlantList() {
        System.out.println(">>>[Serice] Return all ThermalPowerPlantList\n");
        return new ArrayList<>(thermalPowerPlantList);
    }

    public synchronized void setThermalPowerPlantList(List<ThermalPowerPlant> thermalPowerPlants) {
        thermalPowerPlantList.clear();
        thermalPowerPlantList.addAll(thermalPowerPlants);
    }

    public synchronized void add(ThermalPowerPlant thermalPowerPlant) {
        System.out.println(">>>[Serice] Added thermal power plant with ID: " + thermalPowerPlant.getId()+"\n");
        thermalPowerPlantList.add(thermalPowerPlant);
    }

    public synchronized ThermalPowerPlant getById(String id) {
        System.out.println(">>>[Serice] Trying to return thermal power plant with ID: " + id);
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            if (thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                System.out.println(">>>[Serice] Return thermal power plant with ID: " + id);
                return thermalPowerPlant;
            }
        }
        System.out.println(">>>[Serice] Not found Thermal power plant with ID: " + id);
        return null; // Not found
    }


}
