package AdministrationServer.REST;

import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Questo metodo restituisce la lista di tutte le ThermalPowerPlant presenti.
     * @return ResponseEntity contenente una lista di ThermalPowerPlant.
     */
    @GetMapping
    public ResponseEntity<List<ThermalPowerPlant>> getThermalPowerPlantList() {
        System.out.println(">>>[Controller] getThermalPowerPlantList chiamato\n");
        return ResponseEntity.ok(thermalPowerPlantService.getThermalPowerPlantList());
    }

    /**
     * Questo metodo invia una richiesta POST al controller per aggiungere una nuova ThermalPowerPlant.
     * @param thermalPowerPlant l'oggetto ThermalPowerPlant da aggiungere.
     * @return ResponseEntity con stato 200 (OK) se l'aggiunta ha successo, 409 (CONFLICT) altrimenti.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addThermalPowerPlant(@RequestBody ThermalPowerPlant thermalPowerPlant) {
        boolean success = thermalPowerPlantService.add(thermalPowerPlant);

        if (success) {
            return ResponseEntity.ok().build(); // 200 ok
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //409 conflict
        }
    }

    /**
     * Questo metodo restituisce una ThermalPowerPlant identificata da un ID specifico.
     * @param id l'identificatore univoco della ThermalPowerPlant.
     * @return ResponseEntity contenente la ThermalPowerPlant se trovata, 404 (NOT FOUND) altrimenti.
     */
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

