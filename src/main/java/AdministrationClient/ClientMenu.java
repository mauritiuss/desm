package AdministrationClient;

import AdministrationClient.REST.AdministrationClientRESTConnector;
import common.ThermalPowerPlant;

import java.util.Scanner;

/**
 * Questa classe è la classe che permette di mostrare il menù
 */
public class ClientMenu implements DisplayableMenu {

    private Scanner scanner = new Scanner(System.in);

    public ClientMenu() { AdministrationClientRESTConnector.getInstance(); }

    @Override
    public int getChoice(){
        printMenu();
        System.out.println("Inserisci la tua scelta: ");
        return readControlledInt();
    }

    @Override
    public void displayThermalPowerPlants() {
        ThermalPowerPlant[] plants = AdministrationClientRESTConnector.getInstance().getThermalPowerPlants();

        if(plants.length == 0){
            System.out.println("Nessuna Thermal Power Plant è presente");
        }else{
            System.out.println("Thermal Power Plant:");
            for (ThermalPowerPlant p : plants) {
                System.out.println(p.toString());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n --- Menu Administration Client ---");
        System.out.println("1. Mostra elenco delle Thermal Power Plants");
        System.out.println("2. Mostra statistiche emissioni CO₂");
        System.out.println("0. Esci");
        System.out.println("-----------------------------------\n");
    }

    private int readControlledInt() {
        while (true) {
            if (scanner.hasNextInt()){
                int value = scanner.nextInt();
                if (value <= 2 ){
                    return value;
                }
            }else{
                scanner.next();
                System.out.println("Inserisci un numero valido");
            }
        }
    }
}
