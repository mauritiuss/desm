package AdministrationClient;

import AdministrationClient.REST.AdministrationClientRESTConnector;
import ThermalPowerPlant.ThermalPowerPlant;
import static common.log.Log.*;
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
        ic( "Inserisci la tua scelta: ");
        return readControlledInt();
    }

    @Override
    public void displayThermalPowerPlants() {
        ThermalPowerPlant[] plants = AdministrationClientRESTConnector.getInstance().getThermalPowerPlants();

        if(plants.length == 0){
            System.out.println("Nessuna Thermal Power Plant è presente");
            i("ClientMenu","OTHER","Nessuna Thermal Power Plant è presente");
        }else{
            i("ClientMenu","OTHER","Thermal Power Plant:");
            for (ThermalPowerPlant p : plants) {
                System.out.println(p.toString());
            }
        }
    }

    private void printMenu() {
        ic("\n --- Menu Administration Client ---");
        ic("1. Mostra elenco delle Thermal Power Plants");
        ic("2. Mostra statistiche emissioni CO₂");
        ic("0. Esci");
        ic("-----------------------------------\n");
    }

    private int readControlledInt() {
        while (true) {
            String input = scanner.nextLine().trim(); // legge tutta la riga
            try {
                int value = Integer.parseInt(input);
                if (value >= 0 && value <= 2) {
                    return value;
                } else {
                    i("ClientMenu", "OTHER","Inserisci un numero valido tra 0 e 2");
                }
            } catch (NumberFormatException e) {
                i("ClientMenu", "OTHER","Inserisci un numero intero valido");
            }
        }
    }

    private long readControlledLong() {
        while (true) {
            if (scanner.hasNextLong()) {
                return scanner.nextLong();
            } else {
                scanner.next();
                i("ClientMenu","CO2","Inserisci un numero long valido (millisecondi).");
            }
        }
    }

    @Override
    public void displayAverageBetweenTimestamp(){
        i("ClientMenu","CO2","Inserisci t1 (timestamp in millisecondi): ");
        long t1 = readControlledLong();

        i("ClientMenu","CO2","Inserisci t2 (timestamp in millisecondi): ");
        long t2 = readControlledLong();

        try{
            Double avg = AdministrationClientRESTConnector.getInstance().getAverageBetweenTimestamp(t1, t2);

            if(avg == null){
                i("ClientMenu","CO2","Nessuna misura trovata nell'intervallo [" + t1 + ", " + t2 + "].");
            } else {
                i("ClientMenu","CO2","Media valori nell'intervallo [" + t1 + ", " + t2 + "]: " + avg);
            }
        } catch (IllegalArgumentException e){
            e("ClientMenu","CO2","Input non valido: t1 deve essere minore o uguale a t2.");
        } catch (Exception e){
            e("ClientMenu","CO2","Errore durante la richiesta: " + e.getMessage());
        }
    }
}
