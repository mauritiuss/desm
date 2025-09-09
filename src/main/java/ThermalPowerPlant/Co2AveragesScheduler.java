package ThermalPowerPlant;

import Simulator.Buffer;
import Simulator.Measurement;
import common.model.Co2AveragesPacket;
import static common.log.Log.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread periodico che, ogni 10 secondi:
 * - legge le medie pronte dal Buffer (readAllAndClean)
 * - se presenti, crea un Co2AveragesPacket e lo accoda come "in attesa di invio".
 */
public class Co2AveragesScheduler extends Thread{

    private static final long PERIOD_IN_MS = 10_000L;
    private boolean stop = false;

    // Coda dei pacchetti generati e non ancora inviati
    private final List<Co2AveragesPacket> pendingPackets = new ArrayList<>();

    private final ThermalPowerPlant thermalPowerPlant;

    public Co2AveragesScheduler(ThermalPowerPlant thermalPowerPlant) {
        if (thermalPowerPlant == null) {
            throw new IllegalArgumentException(">>>[Co2AveragesScheduler] La ThermalPowerPlant non può essere null");
        }
        this.thermalPowerPlant = thermalPowerPlant;
        setName("Co2AveragesScheduler");
        setDaemon(true);
    }

    public void stopMeGently(){
        stop = true;
        this.interrupt();
    }

    //Ritorna e svuota tutti i pacchetti accumulati finora
    public synchronized List<Co2AveragesPacket> fetchAndClearPendingPackets(){
        List<Co2AveragesPacket> out = new ArrayList<>(pendingPackets);
        pendingPackets.clear();
        return out;
    }

    @Override
    public void run(){
        while(!stop){
            try{
                Thread.sleep(PERIOD_IN_MS);
                Buffer buffer = thermalPowerPlant.getPollutionBuffer();
                if(buffer == null)  continue;

                // Legge le medie pronte dal buffer (può essere lista vuota)
                List<Measurement> averages = buffer.readAllAndClean();
                if(averages == null || averages.isEmpty()) continue;

                long ts = System.currentTimeMillis();
                Co2AveragesPacket packet = new Co2AveragesPacket(thermalPowerPlant.getId(), ts, averages);

                synchronized (this) {
                    pendingPackets.add(packet);
                }

                //printMeasurements(averages, ts);

                i("Co2AveragesScheduler","Creato packet: " + packet);

                Thread.sleep(10_000); // In modo da pubblicare un pacchetto ogni 10 secondi

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e){
                e("Co2AveragesScheduler","Errore: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        i("Co2AveragesScheduler","Terminato.");
    }

    private void printMeasurements(List<Measurement> averages, long ts) {
        i("Co2AveragesScheduler","Pacchetto creato alle " + ts + " con " + averages.size() + " medie:");
        for (Measurement m : averages) {
            i("Co2AveragesScheduler","   -> Media: " + m.getValue() + " g, timestamp: " + m.getTimestamp());
        }
    }
}
