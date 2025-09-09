package AdministrationClient;

/**
 * Questa interfaccia contiene i metodi necessari per il ClientMenu
 */
public interface DisplayableMenu {

    /**
     * Questo metodo mostra il menù e ritorna la scelta dell'utente
     * @return la scelta dell'utente
     */
    int getChoice();


    /**
     * Questo metodo mostra tutte le Thermal Power Plants attive
     */
    void displayThermalPowerPlants();


    /**
     * Questo metodo mostra la media delle emissioni di Co2 tra due timestamp t1 e t2
     */
    void displayAverageBetweenTimestamp();
}
