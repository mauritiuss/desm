package AdministrationClient;

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
                    //TODO: mostrare la media dei livelli di emissione di CO₂
                    menu.displayAverageBetweenTimestamp();

                    break;
                default:
                    System.out.println("Scelta non valida");
            }
            System.out.println("\n Premere invio per continuare...");
            new java.util.Scanner(System.in).nextLine();
        }
    }
}
