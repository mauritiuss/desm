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
     * Restituisce la lista di tutte le {@link ThermalPowerPlant} registrate.
     * @return ResponseEntity contenente una lista di ThermalPowerPlant.
     */
    @GetMapping
    public ResponseEntity<List<ThermalPowerPlant>> getThermalPowerPlantList() {
        //System.out.println(">>>[ThermalPowerPlantController] getThermalPowerPlantList chiamato");
        return ResponseEntity.ok(thermalPowerPlantService.getThermalPowerPlantList());
    }

    /**
     * Aggiunge una nuova {@link ThermalPowerPlant} al sistema.
     * @param thermalPowerPlant l'oggetto ThermalPowerPlant da aggiungere.
     * @return {@link ResponseEntity} con stato 200 (OK) se l'aggiunta ha successo, 409 (CONFLICT) altrimenti.
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
     * Restituisce una {@link ThermalPowerPlant} identificata da un ID specifico.
     * @param id l'identificatore univoco della ThermalPowerPlant.
     * @return {@link ResponseEntity} contenente la ThermalPowerPlant se trovata, 404 (NOT FOUND) altrimenti.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ThermalPowerPlant> getById(@PathVariable String id) {
        ThermalPowerPlant thermalPowerPlant = thermalPowerPlantService.getById(id);
        //System.out.println(">>>[ThermalPowerPlantController] getById called with: " + id);
        if (thermalPowerPlant != null)
            return ResponseEntity.ok(thermalPowerPlant);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * Calcola la media dei valori di CO2 tra due timestamp.
     * @param t1 timestamp iniziale (in millisecondi)
     * @param t2 timestamp finale (in millisecondi)
     * @return {@link ResponseEntity} con stato 200 (OK) con il valore medio, 204 (NO CONTENT) se non ci sono dati, 400 (BAD CONFLICT) se t1 > t2
     */
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageBetweenTimestamp(@RequestParam("t1") long t1, @RequestParam("t2") long t2) {
        try{
            Double avg = thermalPowerPlantService.getAverageBetweenTimestamp(t1, t2);
            if(avg == null){
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(avg); //200
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build(); //400
        }
    }

    /**
     * Rimuove una {@link ThermalPowerPlant} dal sistema tramite ID.
     * @param id l'identificatore univoco della ThermalPowerPlant da rimuovere.
     * @return {@link ResponseEntity} con stato 200 (OK) se la centrale è stata rimossa, 404 (NOT FOUND) se l'ID non esiste.
     */
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeThermalPowerPlant(@PathVariable String id) {
        return thermalPowerPlantService.removeThermalPowerPlant(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}

