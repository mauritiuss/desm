package common.model;

import Simulator.Measurement;
import java.util.List;

/**
 * Rappresenta un pacchetto contenente le medie delle emissioni di CO2 calcolate da una {@link ThermalPowerPlant}.
 * Un pacchetto è composto da:
 * - L'ID della ThermalPowerPlant che ha generato i dati.
 * - Il timestamp di creazione del pacchetto.
 * - Una lista di {@link Measurement} che rappresentano le medie calcolate su finestre temporali del sensore di inquinamento.
 * Questi pacchetti vengono generati dal {@link ThermalPowerPlant.Co2AveragesScheduler} e successivamente pubblicati via MQTT.
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
