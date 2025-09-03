package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.ThermalPowerPlant;
import ThermalPowerPlant.grpc.ChangRobertsProto;
import ThermalPowerPlant.grpc.ChangRobertsServiceGrpc;
import ThermalPowerPlant.grpc.PresentationProto;
import ThermalPowerPlant.grpc.ThermalPowerPlantServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.security.Timestamp;

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
                .addService(new ChangRobertsServiceImpl())
                .build()
                .start();
        System.out.println(">>>[ThermalPowerPlantServer] "+ ThermalPowerPlant.getInstance().getId() + " Server gRPC avviato sulla porta " + port);

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

            System.out.println(">>>[ThermalPowerPlantServer] "+plant.getId()+" ha ricevuto messaggio di presentazione da ThermalPowerPlant:" +request.getId()+
                    ", "+request.getAddress()+", "+request.getPort());

            //Caso: il messaggio è tornato al mittente → chiusura dell'anello
            if(request.getId().equals(plant.getId())) {
                System.out.println(">>>[ThermalPowerPlantServer] Il messaggio è tornato al mittente. Interrompo la catena --> "+request.getId()+" = "+plant.getId());
            } else {
                // Il messaggio proviene da un'altra ThermalPowerPlant → inoltro o aggiornamento

                System.out.println(">>>[ThermalPowerPlantServer] Sono: "+plant.getId()+" ed il mio next è: "+plant.getNextId()+ " ed il next della richiesta è: "+request.getNextId());

                if (request.getNextId().equals(plant.getNextId())) {
                    // Sono l'ultimo dell'anello: aggiorno il riferimento al prossimo
                    System.out.println(">>>[ThermalPowerPlantServer] "+plant.getId()+" sono l'ultimo dell'anello. Aggiorno il next con chi si presenta.");

                    plant.setNextId(request.getId());
                    plant.setNextAddress(request.getAddress());
                    plant.setNextPort(request.getPort());

                }

                System.out.println(">>>[ThermalPowerPlantServer] "+plant.getId()+" inoltro il messaggio di presentazione a: "+plant.getNextId());

                ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                        plant.getNextAddress(), plant.getNextPort()
                );

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

    /**
     * Implementazione concreta del servizio `ChangRobertsService` definito nel file `.proto`.
     * Gestisce la logica della chiamata RPC `sendMessage`, utilizzata per l'algoritmo di elezione.
     */
    private static class ChangRobertsServiceImpl extends ChangRobertsServiceGrpc.ChangRobertsServiceImplBase {

        @Override
        public void sendMessage(ChangRobertsProto.ChangRobertsMessage request, StreamObserver<ChangRobertsProto.Ack> responseObserver){

            ChangRobertsProto.ChangRobertsMessage.MessageTypeCase typeCase = request.getMessageTypeCase();

            switch (typeCase) {
                case ELECTION:
                    handleElection(request.getElection());
                    break;
                case ELECTED:
                    handleElected(request.getElected());
                    break;
                case MESSAGETYPE_NOT_SET:
                    System.out.println(">>>[ThermalPowerPlantServer] Messaggio ricevuto senza contenuto!");
                    break;
            }

            // Invio ack al chiamante
            ChangRobertsProto.Ack ack = ChangRobertsProto.Ack.newBuilder()
                    .setMessage("OK")
                    .build();

            responseObserver.onNext(ack);
            responseObserver.onCompleted();
        }

        private void handleElection(ChangRobertsProto.Election election) {
            ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

            ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                    plant.getNextAddress(), plant.getNextPort()
            );

            // STEP 1: Se sono occupato inoltro il messaggio a prescindere
            if(plant.isBusy()){
                client.sendChangRobertsMessage(
                        true, // --> Flag per election
                        election.getTimestamp(),
                        election.getCandidateId(),
                        election.getPrice(),
                        election.getKwh(),
                        null // --> Campo per Elected
                );
                client.shutdown();
                return;
            }

            // STEP 2: Confronto prezzi se non sono occupato
            if(election.getPrice() > plant.getPrice(election.getTimestamp())){
                // Il mio prezzo è migliore --> NON inoltro il messaggio
            } else if (election.getPrice() < plant.getPrice(election.getTimestamp())){
                // Prezzo ricevuto migliore --> inoltro il messaggio
                client.sendChangRobertsMessage(
                        true, // --> Flag per election
                        election.getTimestamp(),
                        election.getCandidateId(),
                        election.getPrice(),
                        election.getKwh(),
                        null // --> Campo per Elected
                );
            } else {
                // Prezzi uguali --> confronto gli ID
                if(Integer.parseInt(election.getCandidateId()) > Integer.parseInt(plant.getId())){
                    // ID ricevuto maggiore --> inoltro il messaggio
                    client.sendChangRobertsMessage(
                            true,
                            election.getTimestamp(),
                            election.getCandidateId(),
                            election.getPrice(),
                            election.getKwh(),
                            null
                    );
                } else if (Integer.parseInt(election.getCandidateId()) < Integer.parseInt(plant.getId())){
                    // Il mio ID è maggiore --> NON inoltro il messaggio
                } else {
                    // L'ID è uguale --> ho vinto l'elezione → invio messaggio ELECTED
                    client.sendChangRobertsMessage(
                            false,
                            election.getTimestamp(),
                            null,
                            null,
                            election.getKwh(),
                            plant.getId()
                    );
                }
            }

            client.shutdown();
        }

        private void handleElected(ChangRobertsProto.Elected elected) {
            ThermalPowerPlant plant = ThermalPowerPlant.getInstance();

            if(Integer.parseInt(elected.getWinnerId())!=Integer.parseInt(plant.getId())){
                ThermalPowerPlantClient client = new ThermalPowerPlantClient(
                        plant.getNextAddress(), plant.getNextPort()
                );

                client.sendChangRobertsMessage(
                        false,
                        elected.getTimestamp(),
                        null,
                        null,
                        elected.getKwh(),
                        elected.getWinnerId() // --> Campo per Elected
                );
                client.shutdown();
            } else {
                try {
                    plant.setBusy(true);
                    Thread.sleep(120_000); // 180.000 millisecondi = 3 minuto
                    plant.setBusy(false);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                //TODO Bloccare la thermal power plant

            }
        }

    }


}
