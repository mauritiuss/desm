package AdministrationServer.REST;

/**
 * Questa classe contiene le informazioni di base del server
 */
public class RESTServerProperties {

    public static final String HOST = "localhost";
    public static final String PORT = "8080";
    public static final String BASE_URL = String.format("http://%s:%s/thermalPowerPlants/", HOST, PORT);

    //Costruttore vuoto
    private RESTServerProperties() {}
}
