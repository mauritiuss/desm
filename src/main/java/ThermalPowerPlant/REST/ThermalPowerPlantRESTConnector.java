package ThermalPowerPlant.REST;

import AdministrationServer.REST.RESTServerProperties;
import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static common.log.Log.*;

import static AdministrationServer.REST.RESTServerProperties.BASE_URL;

/**
 * Questa classe contiene tutti i metodi che permettono
 * alla ThermalPowerPlant di comunciare con l'AdministrationServer
 */
public class ThermalPowerPlantRESTConnector {
    private static ThermalPowerPlantRESTConnector instance;
    private static final RestTemplate restTemplate = new RestTemplate();

    // Costruttore vuoto
    private ThermalPowerPlantRESTConnector() {}

    /**
     * Questo metodo restituisce l'istanza di ThermalPowerPlantRESTConnector.
     * Se la connessione non è ancora stata inizializzata, la inizializza.
     * @return L'istanza di ThermalPowerPlantRESTConnector.
     */
    public static synchronized ThermalPowerPlantRESTConnector getInstance() {
        if (instance == null) {
            initializeConnection();
        }
        return instance;
    }

    /**
     * Questo metodo inizializza la connessione al server.
     */
    private static void initializeConnection() { instance = new ThermalPowerPlantRESTConnector(); }

    /**
     * Questo metodo invia una richiesta POST al controller per aggiungere una nuova ThermalPowerPlant.
     * @return boolean true se la ThermalPowerPlant è stata aggiunta, false altrimenti.
     */
    public boolean addThermalPowerPlant() {
        ThermalPowerPlant thermalPowerPlant = ThermalPowerPlant.getInstance();
        try{
            String url = RESTServerProperties.BASE_URL + "/add";
            ResponseEntity<Void> response = restTemplate.postForEntity(url, thermalPowerPlant, Void.class);

            if(response.getStatusCode().is2xxSuccessful()){
                //System.out.println(">>>[ThermalPowerPlantRESTConnector] ThermalPowerPlant inviata correttamente");
                return true;
            }else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                //System.out.println(">>>[ThermalPowerPlantRESTConnector] ID già esistente: "+thermalPowerPlant.getId());
                return false;
            }else{
                //System.out.println(">>>[ThermalPowerPlantRESTConnector] Risposta inaspettata");
                return false;
            }
        }catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.CONFLICT){
                w("ThermalPowerPlantRESTConnector","OTHER","ID già presente (Errore 409): " + thermalPowerPlant.getId());
                return false;
            }else{
                w("ThermalPowerPlantRESTConnector","OTHER","Errore HTTP: " + e.getStatusCode());
                return false;
            }
        }catch (Exception e) {
            e("ThermalPowerPlantRESTConnector","OTHER","Errore durante l'invio della ThermalPowerPlant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Questo metodo chiama il controller del server per ottenere tutte le informazioni delle Thermal Power Plants.
     * @return Un array di REST.ThermalPowerPlant.
     */
    public ThermalPowerPlant[] getThermalPowerPlants() {
        try {
            ResponseEntity<ThermalPowerPlant[]> response =
                    restTemplate.getForEntity(BASE_URL, ThermalPowerPlant[].class);

            return response.getBody() != null ? response.getBody() : new ThermalPowerPlant[0];
        } catch (Exception e) {
            e("ThermalPowerPlantRESTConnector","OTHER","Errore durante la richiesta al server: " + e.getMessage());
            return new ThermalPowerPlant[0];
        }
    }

    public boolean removeThermalPowerPlant(){
        ThermalPowerPlant plant = ThermalPowerPlant.getInstance();
        try{
            String url = RESTServerProperties.BASE_URL + "/remove/" + plant.getId();
            restTemplate.delete(url);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            e("ThermalPowerPlantRESTConnector","EXIT","ID non trovato sul server: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e("ThermalPowerPlantRESTConnector","EXIT","Errore durante la rimozione: " + e.getMessage());
            return false;
        }
    }

}
