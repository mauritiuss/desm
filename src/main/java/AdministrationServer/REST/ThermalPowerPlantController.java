package AdministrationServer.REST;

import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    Controller = intermediario tra il client ed il livello di servizio
    •	Riceve richieste HTTP (es. GET, POST, ecc.).
    •	Interpreta e valida gli input (es. parametri, corpo della richiesta).
    •	Chiama il service appropriato per eseguire la logica applicativa.
    •	Restituisce una risposta HTTP al client, con eventuali dati o codici di stato.
 */

@RestController
@RequestMapping("/thermalPowerPlants")
public class ThermalPowerPlantController {

    private final ThermalPowerPlantService thermalPowerPlantService;

    @Autowired
    public ThermalPowerPlantController (ThermalPowerPlantService thermalPowerPlantService) {
        this.thermalPowerPlantService = thermalPowerPlantService;
    }

    // Ritorna la lista delle Thermal Power Plant
    @GetMapping
    public ResponseEntity<List<ThermalPowerPlant>> getThermalPowerPlantList() {
        System.out.println(">>>[Controller] getThermalPowerPlantList called\n");
        return ResponseEntity.ok(thermalPowerPlantService.getThermalPowerPlantList());
    }

    // Aggiunge una nuova Thermal Power Plant nel body della richiesta
    @PostMapping("/add")
    public ResponseEntity<Void> addThermalPowerPlant(@RequestBody ThermalPowerPlant thermalPowerPlant) {
        System.out.println(">>>[Controller] addThermalPowerPlant called with: " + thermalPowerPlant);
        thermalPowerPlantService.add(thermalPowerPlant);
        return ResponseEntity.ok().build();
    }

    // Ritorna una Thermal Power Plant tramite l'ID
    @GetMapping("/get/{id}")
    public ResponseEntity<ThermalPowerPlant> getById(@PathVariable String id) {
        ThermalPowerPlant thermalPowerPlant = thermalPowerPlantService.getById(id);
        System.out.println(">>>[Controller] getById called with: " + id);
        if (thermalPowerPlant != null)
            return ResponseEntity.ok(thermalPowerPlant);
        else
            return ResponseEntity.notFound().build();
    }


}

