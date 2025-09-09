package AdministrationClient.REST;

import ThermalPowerPlant.ThermalPowerPlant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static common.log.Log.*;

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
            e("AdministrationClientRESTConnector", "Errore durante la richiesta al server: " + e.getMessage());
            return new ThermalPowerPlant[0];
        }
    }

    /**
     * Richiede al server la media delle misure tra due timestamp.
     * Restituisce:
     *  - Double (200 OK) se presente
     *  - null (204 No Content) se nessuna misura nel range
     *  - lancia IllegalArgumentException se t1 > t2 (400 Bad Request dal server)
     */
    public Double getAverageBetweenTimestamp(long t1, long t2) {
        String url = BASE_URL + "/average?t1={t1}&t2={t2}";
        try {
            ResponseEntity<Double> response = restTemplate.getForEntity(
                    url, Double.class, t1, t2
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody(); // può essere Double
            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return null; // nessun dato
            } else {
                w("AdministrationClientRESTConnector", "Risposta inattesa dal server: " + response.getStatusCode());
                return null;
            }
        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
            // 400 dal server quando t1 > t2
            throw new IllegalArgumentException("t1 deve essere minore o uguale a t2", e);
        } catch (Exception e) {
            e("AdministrationClientRESTConnector", "Errore durante la richiesta al server: " + e.getMessage());
            throw e; // rilancio per far gestire al menu un generico errore
        }
    }
}
