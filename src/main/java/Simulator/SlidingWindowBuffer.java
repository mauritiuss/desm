package Simulator;

import java.util.ArrayList;
import java.util.List;
import static common.log.Log.*;
import static Simulator.BufferProperties.STEP;
import static Simulator.BufferProperties.WINDOW_SIZE;

public class SlidingWindowBuffer implements Buffer{

    private final List<Measurement> window = new ArrayList<>(WINDOW_SIZE);
    private final List<Measurement> readyAvarages = new ArrayList<>();

    /**
     * Aggiunge una nuova misurazione al buffer.
     * Quando la finestra raggiunge WINDOW_SIZE misurazioni, viene calcolata la media dei valori e memorizzata in readyAverages.
     * La finestra viene poi fatta avanzare di STEP elementi.
     * @param m la misurazione da aggiungere (non deve essere null)
     */
    @Override
    public synchronized void addMeasurement(Measurement m) {
        if(m == null) return;

        if(Double.isNaN(m.getValue()) || Double.isInfinite(m.getValue())) return;
        if(m.getTimestamp() <= 0) return;

        window.add(m);

        //Se la finestra è piena, calcolo media e avanzo
        if(window.size() == WINDOW_SIZE) {
            double sum = 0.0;


            for (Measurement measurement : window) {
                sum += measurement.getValue();
            }

            //printValue(window);

            double avg = sum / WINDOW_SIZE;

            //Timestamp della media = timestamp ultima misurazione della finestra
            long ts = window.get(WINDOW_SIZE - 1).getTimestamp();

            //ID e type ereditati dall'ultima misurazione
            String id = window.get(WINDOW_SIZE - 1).getId();
            String type = window.get(WINDOW_SIZE - 1).getType();
            String avgType = (type == null || type.isEmpty()) ? "AVG" : (type + "_AVG");

            readyAvarages.add(new Measurement(id, avgType, avg, ts));

            i("SlidingWindowBuffer","CO2"," Media calcolata: " + avg + " g (su " + WINDOW_SIZE + " valori)\n");

            //Notifica i consumatori che potrebbero essere in wait()
            this.notifyAll();

            //Avanzo la finestra di STEP (50%)
            for (int i =0; i < STEP && !window.isEmpty(); i++) {
                window.remove(0);
            }
        } else if ( window.size() > WINDOW_SIZE) {
            while(window.size() > WINDOW_SIZE) {
                window.remove(0);
            }
        }
    }

    private void printValue(List<Measurement> window) {
        i("SlidingWindowBuffer","CO2"," Calcolo media su finestra:");
        for (Measurement measurement : window) {
            i("SlidingWindowBuffer","CO2","   -> Valore: " + measurement.getValue() + " g, timestamp: " + measurement.getTimestamp());
        }
    }

    /**
     * Restituisce tutte le medie calcolate dall'ultima chiamata e svuota il buffer delle medie.
     * La finestra corrente di misurazioni non viene toccata, in modo che l'overlap continui a funzionare.
     * @return lista delle medie calcolate (può essere vuota, mai null)
     */
    @Override
    public synchronized List<Measurement> readAllAndClean() {
        List<Measurement> result = new ArrayList<>(readyAvarages);
        readyAvarages.clear();
        return result;
    }
}
