package common.model;

import Simulator.Measurement;

import java.util.List;

/**
 * Rappresenta un pacchetto di medie CO2 pronte per l'invio.
 * Contiene l'ID della ThermalPowerPlant, il timestamp di creazione del pacchetto e la lista delle medie calcolate.
 */
public class Co2AveragesPacket {

    private final String thermalPowerPlantId;
    private final long timestamp;
    private final List<Measurement> averages;

    /**
     * @param thermalPowerPlantId  ID della ThermalPowerPlant
     * @param timestamp epoch millis del momento in cui il pacchetto è stato creato
     * @param averages  lista delle medie calcolate
     */
    public Co2AveragesPacket(String thermalPowerPlantId, long timestamp, List<Measurement> averages) {
        this.thermalPowerPlantId = thermalPowerPlantId;
        this.timestamp = timestamp;
        this.averages = averages;
    }

    public String getThermalPowerPlantId() {
        return thermalPowerPlantId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Measurement> getAverages() {
        return averages;
    }

    @Override
    public String toString() {
        return "Co2AveregesPacket{" +
                "thermalPowerPlantId='" + thermalPowerPlantId + '\'' +
                ", timestamp=" + timestamp +
                ", averages=" + averages +
                '}';
    }
}
