package AdministrationClient;

import static common.log.Log.*;

/**
 * Questa classe è usata per far partire il Client
 */
public class AdministrationClient {

    public static void main(String[] args) {

        DisplayableMenu menu = new ClientMenu();
        while (true) {
            switch (menu.getChoice()){
                case 0:
                    System.exit(0);
                case 1:
                    menu.displayThermalPowerPlants();
                    break;
                case 2:
                    menu.displayAverageBetweenTimestamp();
                    break;
                default:
                    w("AdministrationClient", "Scelta non valida");
            }
            i("AdministrationClient", "Premere invio per continuare...");
            new java.util.Scanner(System.in).nextLine();
        }
    }
}
