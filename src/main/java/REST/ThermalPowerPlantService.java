package REST;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
    Collection of resourses --> List of Thermal power plant
 */
@Service
public class ThermalPowerPlantService {
    private final List<ThermalPowerPlant> thermalPowerPlantList = new ArrayList<>();

    public synchronized List<ThermalPowerPlant> getThermalPowerPlantList() {
        return new ArrayList<>(thermalPowerPlantList);
    }

    public synchronized void setThermalPowerPlantList(List<ThermalPowerPlant> thermalPowerPlants) {
        thermalPowerPlantList.clear();
        thermalPowerPlantList.addAll(thermalPowerPlants);
    }

    public synchronized void add(ThermalPowerPlant thermalPowerPlant) {
        thermalPowerPlantList.add(thermalPowerPlant);
    }

    public synchronized ThermalPowerPlant getById(String id) {
        System.out.println("called");
        for (ThermalPowerPlant thermalPowerPlant : thermalPowerPlantList) {
            System.out.println(thermalPowerPlant.getId());
            System.out.println(id);
            if (thermalPowerPlant.getId() != null && thermalPowerPlant.getId().equalsIgnoreCase(id)) {
                return thermalPowerPlant;
            }
        }
        return null; // Not found
    }


}
