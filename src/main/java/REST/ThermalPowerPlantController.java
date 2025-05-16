package REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thermalPowerPlants")
public class ThermalPowerPlantController {

    private final ThermalPowerPlantService thermalPowerPlantService;

    @Autowired
    public ThermalPowerPlantController (ThermalPowerPlantService thermalPowerPlantService) {
        this.thermalPowerPlantService = thermalPowerPlantService;
    }

    // Return list of thermalPowerPlant
    @GetMapping
    public ResponseEntity<List<ThermalPowerPlant>> getThermalPowerPlantList() {
        return ResponseEntity.ok(thermalPowerPlantService.getThermalPowerPlantList());
    }

    // Add a new thermalPowerPlant
    @PostMapping("/add")
    public ResponseEntity<Void> addUser(@RequestBody ThermalPowerPlant thermalPowerPlant) {
        thermalPowerPlantService.add(thermalPowerPlant);
        return ResponseEntity.ok().build();
    }

    // Get thermalPowerPlant by Id
    @GetMapping("/get/{id}")
    public ResponseEntity<ThermalPowerPlant> getById(@PathVariable String id) {
        ThermalPowerPlant thermalPowerPlant = thermalPowerPlantService.getById(id);
        System.out.println(id);
        if (thermalPowerPlant != null)
            return ResponseEntity.ok(thermalPowerPlant);
        else
            return ResponseEntity.notFound().build();
    }


}

