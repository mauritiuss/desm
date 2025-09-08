package AdministrationClient;

import AdministrationClient.REST.AdministrationClientRESTConnector;
import ThermalPowerPlant.ThermalPowerPlant;

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

    private long readControlledLong() {
        while (true) {
            if (scanner.hasNextLong()) {
                return scanner.nextLong();
            } else {
                scanner.next();
                System.out.println("Inserisci un numero long valido (millisecondi).");
            }
        }
    }

    @Override
    public void displayAverageBetweenTimestamp(){
        System.out.println("Inserisci t1 (timestamp in millisecondi): ");
        long t1 = readControlledLong();

        System.out.println("Inserisci t2 (timestamp in millisecondi): ");
        long t2 = readControlledLong();

        try{
            Double avg = AdministrationClientRESTConnector.getInstance().getAverageBetweenTimestamp(t1, t2);

            if(avg == null){
                System.out.println("Nessuna misura trovata nell'intervallo [" + t1 + ", " + t2 + "].");
            } else {
                System.out.println("Media valori nell'intervallo [" + t1 + ", " + t2 + "]: " + avg);
            }
        } catch (IllegalArgumentException e){
            System.out.println("Input non valido: t1 deve essere minore o uguale a t2.");
        } catch (Exception e){
            System.out.println("Errore durante la richiesta: " + e.getMessage());
        }
    }
}
