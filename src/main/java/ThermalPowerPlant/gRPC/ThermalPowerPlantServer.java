package ThermalPowerPlant.gRPC;

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
            System.out.println("Ricevuto messaggio di presentazione da ThermalPowerPlant:");
            System.out.println("ID: " + request.getId());
            System.out.println("Address: " + request.getAddress());
            System.out.println("Port: " + request.getPort());

            // Risposta semplice
            PresentationProto.PresentationAck ack = PresentationProto.PresentationAck.newBuilder()
                    .setMessage("Presentazione ricevuta con successo da " + request.getId())
                    .build();

            responseObserver.onNext(ack);
            responseObserver.onCompleted();
        }
    }


}
