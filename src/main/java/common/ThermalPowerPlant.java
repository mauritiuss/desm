package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThermalPowerPlant {
    private String id;
    private String address;
    private int port;

    //Costruttore vuoto
    public ThermalPowerPlant() {}

    public ThermalPowerPlant(String id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
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

    @Override
    public String toString() {
        return "REST.ThermalPowerPlant{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
