package AdministrationClient.REST;

import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static AdministrationServer.REST.RESTServerProperties.BASE_URL;

/**
 * Questa classe contiene tutti i metodi che permettono
 * all'AdministrationClient di comunciare con l'AdministrationServer
 */
public class AdministrationClientRESTConnector {
    private static AdministrationClientRESTConnector instance;
    private static final RestTemplate restTemplate = new RestTemplate();

    // Costruttore vuoto
    private AdministrationClientRESTConnector() {}

    /**
     * Questo metodo restituisce l'istanza di AdministrationClientRESTConnector.
     * Se la connessione non è ancora stata inizializzata, la inizializza.
     * @return L'istanza di AdministrationClientRESTConnector.
     */
    public static synchronized AdministrationClientRESTConnector getInstance() {
        if (instance == null)
            initializeConnection();
        return instance;
    }

    /**
     * Questo metodo inizializza la connessione al server.
     */
    private static void initializeConnection() {
        instance = new AdministrationClientRESTConnector();
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
            System.err.println("Errore durante la richiesta al server: " + e.getMessage());
            return new ThermalPowerPlant[0];
        }
    }
}
