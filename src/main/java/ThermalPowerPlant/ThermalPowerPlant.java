package ThermalPowerPlant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class ThermalPowerPlant {
    private String id;
    private String address;
    private int port;

    /**
     * Timestamp di creazione della ThermalPowerPlant
     * Serve in caso due ThermalPowerPlant propongano lo stesso "prezzo"
    */
    private Timestamp timestamp;

    //Dati della successiva ThermalPowerPlant
    private String nextId;
    private String nextAddress;
    private int nextPort;

    //Singleton
    private static ThermalPowerPlant instance;

    //Costruttore vuoto
    private ThermalPowerPlant() {}

    public ThermalPowerPlant(String id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    //Costruttore a partire da un Json
    @JsonCreator
    public ThermalPowerPlant(@JsonProperty("id") String id,
                             @JsonProperty("address") String address,
                             @JsonProperty("port") String port) {
        this.id = id;
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public static ThermalPowerPlant getInstance() {
        if (instance == null) {
            instance = new ThermalPowerPlant();
        }
        return instance;
    }

    public static void initialize(String id, String address, int port) {
        if (instance == null) {
            instance = new ThermalPowerPlant(id, address, port);
        }
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getNextAddress() {
        return nextAddress;
    }

    public void setNextAddress(String nextAddress) {
        this.nextAddress = nextAddress;
    }

    public int getNextPort() {
        return nextPort;
    }

    public void setNextPort(int nextPort) {
        this.nextPort = nextPort;
    }

    @Override
    public String toString() {
        return "REST.ThermalPowerPlant{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
