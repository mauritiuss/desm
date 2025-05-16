package REST;

public class ThermalPowerPlant {
    private String id;

    //Remember to always add an empty constructor
    public ThermalPowerPlant() {}

    public ThermalPowerPlant(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}
