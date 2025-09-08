package ThermalPowerPlant;

import Simulator.Buffer;
import Simulator.PollutionSensor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThermalPowerPlant {
    private String id;
    private String address;
    private int port;

    //Dati della successiva ThermalPowerPlant
    private String nextId;
    private String nextAddress;
    private int nextPort;

    //Flag per capire se la ThermalPowerPlant è occupata
    private boolean busy = false;

    //Cambi per i dati dell'inquinamento
    private Buffer pollutionBuffer;
    private PollutionSensor pollutionSensor;
    private Co2AveragesScheduler averagesScheduler;

    /**
     * Mappa per salvare le richieste e il prezzo proposto
     * String --> Il timestamp della richiesta della RenewableEnergyProvider
     * Double --> Il prezzo proposto dalla ThermalPowerplant
     */
    private Map<String, Double> prices;

    //Singleton
    private static ThermalPowerPlant instance;

    //Costruttore vuoto
    private ThermalPowerPlant() {}

    public ThermalPowerPlant(String id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
        this.prices = prices != null ? prices : new HashMap<>();
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

    // Singleton
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

    // Inizializzazione sicura della mappa
    private Map<String, Double> getPricesMap() {
        if (prices == null) {
            prices = new HashMap<>();
        }
        return prices;
    }

    // Aggiunge o aggiorna un prezzo
    public void addPrice(String key) {
        getPricesMap().put(key, generateRandomPrice());
    }

    // Genera un numero casuale tra [0.1, 0.9] inclusi entrambi
    public static double generateRandomPrice() {
        double price = 0.1 + ThreadLocalRandom.current().nextDouble() * (0.9 - 0.1);
        return Math.round(price * 1000.0) / 1000.0; // arrotondato a 3 decimali
    }

    // Ottiene un prezzo per chiave
    public Double getPrice(String key) {
        return getPricesMap().get(key);
    }

    // Verifica se una chiave esiste
    public boolean hasPrice(String key) {
        return getPricesMap().containsKey(key);
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

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    @Override
    public String toString() {
        return "REST.ThermalPowerPlant{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }


    //Metodi per i dati dell'inquinamento
    //Inizializza e avvia il sensore di inquinamento usando il buffer dato.
    public synchronized void initPollutionMonitoring(Buffer buffer){
        if(buffer == null) throw new IllegalArgumentException("Buffer nullo");
        if(this.pollutionSensor != null && this.pollutionSensor.isAlive()) return;

        this.pollutionBuffer = buffer;
        this.pollutionSensor = new PollutionSensor(buffer);
        this.pollutionSensor.start();
    }

    //Ritorna il buffer del sensore di inquinamento (può servire per leggere le medie).
    public Buffer getPollutionBuffer() {
        return pollutionBuffer;
    }

    //Stop del simulatore
    public synchronized void stopPollutionMonitoring() {
        if(this.pollutionSensor != null){
            this.pollutionSensor.stopMeGently();
            this.pollutionSensor = null;
        }
    }

    public synchronized void startAveragesScheduler() {
        if (this.averagesScheduler != null && this.averagesScheduler.isAlive()) return;
        this.averagesScheduler = new Co2AveragesScheduler(this);
        this.averagesScheduler.start();
    }

    public synchronized void stopAveragesScheduler() {
        if (this.averagesScheduler != null) {
            this.averagesScheduler.stopMeGently();
            this.averagesScheduler = null;
        }
    }

    public Co2AveragesScheduler getAveragesScheduler() {
        return averagesScheduler;
    }

}
