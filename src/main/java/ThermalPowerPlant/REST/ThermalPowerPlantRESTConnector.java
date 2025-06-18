package ThermalPowerPlant.REST;

import org.springframework.web.client.RestTemplate;

/**
 * Questa classe contiene tutti i metodi che permettono
 * alla ThermalPowerPlant di comunciare con l'AdministrationServer
 */
public class ThermalPowerPlantRESTConnector {
    private static ThermalPowerPlantRESTConnector instance;
    private static final RestTemplate restTemplate = new RestTemplate();

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


}
