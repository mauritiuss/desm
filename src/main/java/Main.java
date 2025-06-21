import AdministrationServer.AdministrationServer;
import ThermalPowerPlant.ThermalPowerPlantRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Questa classe è utilizzata per far partire il progetto intero.
 * Fa partire l'AdministrationServer, l'AdministrationClient e le ThermalPowerPlant.
 * È importante aver fatto partire MOSQUITTO MQTT Broker prima di far partire questa classe.
 */
public class Main {
    private static final int WAIT_SECONDS = 60;
    private static final int NUMBER_OF_THERMALPOWERPLANTS = 4;


    /**
     * @param args gli argomenti da riga di comando (non utilizzati).
     * @throws InterruptedException se il thread principale viene interrotto durante il sonno.
     * @throws IOException se un processo non riesce ad avviarsi.
     */
    public static void main(String args[]) throws InterruptedException, IOException {
        List<Process> processes = new ArrayList<>();

        registerShutdownHook(processes);

        //1. Avvia l'AdministrationServer
        processes.add(startJavaProcess("AdministrationServer.AdministrationServer"));
        TimeUnit.SECONDS.sleep(3);

        /**
        //2. Avvia l'AdministrationClient
        processes.add(startJavaProcess("AdministrationClient.AdministrationClient"));
        TimeUnit.SECONDS.sleep(WAIT_SECONDS);
         */

        //3. Avvia 4 ThermalPowerPlant
        for (int i = 0; i < NUMBER_OF_THERMALPOWERPLANTS; i++) {
            processes.add(startJavaProcess("ThermalPowerPlant.ThermalPowerPlantRunner"));
            TimeUnit.SECONDS.sleep(WAIT_SECONDS);
        }

    }


    /**
     * Avvia un nuovo processo Java per eseguire una specifica classe del progetto.
     * L'output del processo verrà mostrato nel terminale di IntelliJ.
     * @param className il nome completo (incluso il package) della classe da eseguire.
     * @return il processo avviato.
     * @throws IOException se si verifica un errore durante l'avvio del processo.
     */
    private static Process startJavaProcess(String className) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + "/bin/java";
        String classpath = System.getProperty("java.class.path");

        ProcessBuilder builder = new ProcessBuilder(
                javaBin,
                "-cp",
                classpath,
                className
        );

        builder.inheritIO(); // Mostra l'output nel terminale IntelliJ
        Process process = builder.start();
        System.out.println(">>>[Main] Partito:" + className);
        return process;
    }

    /**
     * Registra uno shutdown hook che termina tutti i processi avviati quando il programma principale viene interrotto.
     * Questo assicura che il processo AdministrationServer non lasci la porta 8080 occupata.
     * @param processes la lista dei processi avviati da chiudere al termine dell'esecuzione.
     */
    private static void registerShutdownHook(List<Process> processes) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println(">>>[Main] Shutdown Hook attivato. Terminazione dei processi...");
            for (Process process : processes) {
                if (process.isAlive()) {
                    process.destroy();
                    System.out.println(">>>[Main] Processo terminato.");
                }
            }
        }));
    }

}
