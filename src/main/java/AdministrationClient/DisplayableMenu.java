package AdministrationClient;

/**
 * Questa interfaccia contiene i metodi necessari per il ClientMenu
 */
public interface DisplayableMenu {

    /**
     * Questo metodo mostra il menù e ritorna la scelta dell'utente
     * @return la scelta dell'utente
     */
    public int getChoice();


    /**
     * Questo metodo mostra tutte le Thermal Power Plants attive
     */
    public void displayThermalPowerPlants();
}
