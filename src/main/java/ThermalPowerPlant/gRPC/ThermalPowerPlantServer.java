package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.ThermalPowerPlant;
import ThermalPowerPlant.grpc.PresentationProto;
import ThermalPowerPlant.grpc.ThermalPowerPlantServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;

public class ThermalPowerPlantServer {

    private Server server;
    private final int port;

    /**
     * Crea un nuovo server gRPC che ascolta sulla porta specificata.
     * @param port la porta su cui il server deve essere avviato
     */
    public ThermalPowerPlantServer(int port) {
        this.port = port;
    }

    /**
     * Avvia il server gRPC e registra il servizio `ThermalPowerPlantService`.
     * Aggiunge anche un hook per l'arresto sicuro in caso di terminazione del processo.
     * @throws IOException se il server non riesce ad avviarsi
     */
    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new ThermalPowerPlantServiceImpl())
                .build()
                .start();
        System.out.println("Server gRPC avviato sulla porta " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** Shutdown server gRPC");
            ThermalPowerPlantServer.this.stop();
        }));
    }

    /**
     * Ferma l'esecuzione del server
     */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Blocca il thread principale finché il server non viene terminato.
     * Utile per mantenere il server in esecuzione.
     * @throws InterruptedException se il thread viene interrotto durante l'attesa
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Implementazione concreta del servizio `ThermalPowerPlantService` definito nel file `.proto`.
     * Gestisce la logica della chiamata RPC `presentYourself`, utilizzata per registrare le centrali.
     */
    private static class ThermalPowerPlantServiceImpl extends ThermalPowerPlantServiceGrpc.ThermalPowerPlantServiceImplBase {

        /**
         * Gestisce la richiesta `presentYourself` da parte di una ThermalPowerPlant.
         * @param request la richiesta contenente ID, indirizzo e porta della ThermalPowerPlant
         * @param responseObserver l'osservatore usato per inviare la risposta al client
         */
        @Override
        public void presentYourself(PresentationProto.PresentationMessage request,
                                    StreamObserver<PresentationProto.PresentationAck> responseObserver) {

            ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

            System.out.println(">>>[ThermalPowerPlantServer] ThermalPowerPlant: "+plant.toString()+", Ricevuto messaggio di presentazione da ThermalPowerPlant:" +request.getId()+
                    ", "+request.getAddress()+", "+request.getPort());

            //Caso: il messaggio è tornato al mittente → chiusura dell'anello
            if(request.getId().equals(plant.getId())) {
                System.out.println(">>>[ThermalPowerPlantServer] Il messaggio è tornato al mittente. Interrompo la catena.");
            } else {
                // Il messaggio proviene da un'altra ThermalPowerPlant → inoltro o aggiornamento
                ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                        plant.getNextAddress(), plant.getNextPort()
                );

                if (request.getNextId().equals(plant.getNextId())) {
                    // Sono l'ultimo dell'anello: aggiorno il riferimento al prossimo
                    System.out.println(">>> [ThermalPowerPlantServer] Sono l'ultimo dell'anello. Aggiorno il next con chi si presenta.");

                    plant.setNextId(request.getId());
                    plant.setNextPort(request.getPort());
                    plant.setPort(plant.getNextPort());

                }

                // Inoltro il messaggio al prossimo nodo
                client.sendPresentation(
                        request.getId(), // --> Id di chi si presenta
                        request.getAddress(), // --> Address di chi si presenta
                        request.getPort(), // --> Porta di chi si presenta
                        request.getNextId(),
                        request.getNextAddress(),
                        request.getNextPort()
                );

                client.shutdown();
            }

            // Invio ack al chiamante
            PresentationProto.PresentationAck ack = PresentationProto.PresentationAck.newBuilder()
                    .setMessage("Presentazione ricevuta con successo da " + request.getId())
                    .build();

            responseObserver.onNext(ack);
            responseObserver.onCompleted();
        }
    }


}
