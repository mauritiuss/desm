package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.grpc.ChangRobertsProto;
import ThermalPowerPlant.grpc.ChangRobertsServiceGrpc;
import ThermalPowerPlant.grpc.ThermalPowerPlantServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ThermalPowerPlant.grpc.PresentationProto.PresentationAck;
import ThermalPowerPlant.grpc.PresentationProto.PresentationMessage;

import javax.annotation.Nullable;


public class ThermalPowerPlantClient {

    private final ManagedChannel channel;
    private final ThermalPowerPlantServiceGrpc.ThermalPowerPlantServiceBlockingStub blockingStub;
    private final ChangRobertsServiceGrpc.ChangRobertsServiceBlockingStub changRobertsBlockingStub;

    /**
     * Costruisce un client gRPC per connettersi a un server.
     * @param host l'indirizzo del server
     * @param port la porta su cui è in ascolto il server
     */
    public ThermalPowerPlantClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // gRPC senza TLS
                .build();
        blockingStub = ThermalPowerPlantServiceGrpc.newBlockingStub(channel);
        changRobertsBlockingStub = ChangRobertsServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Invia un messaggio di presentazione al server, specificando ID, indirizzo e porta.
     * @param id l'identificativo della ThermalPowerPlant
     * @param address l'indirizzo della ThermalPowerPlant --> localhost
     * @param port la porta sulla quale la ThermalPowerPlant è in ascolto
     * @param nextId l'identificativo della successiva ThermalPowerPlant (=La successiva è la prima dell'anello)
     * @param nextAddress l'indirizzo della successiva ThermalPowerPlant (=La successiva è la prima dell'anello)
     * @param nextPort la porta sulla quale la successiva ThermalPowerPlant è in ascolto (=La successiva è la prima dell'anello)
     * @throws io.grpc.StatusRuntimeException se si verifica un errore durante la chiamata RPC
     */
    public void sendPresentation(String id, String address, int port, String nextId, String nextAddress, int nextPort) {
        PresentationMessage request = PresentationMessage.newBuilder()
                .setId(id)
                .setAddress(address)
                .setPort(port)
                .setNextId(nextId)
                .setNextAddress(nextAddress)
                .setNextPort(nextPort)
                .build();

        try {
            PresentationAck response = blockingStub.presentYourself(request);
            //System.out.println(">>>[ThermalPowerPlantClient] Risposta dal server al messaggio Presentation: " + response.getMessage());
        } catch (Exception e) {
            System.err.println(">>>[ThermalPowerPlantClient] Errore durante l'invio del messaggio Presentation gRPC: " + e.getMessage());

        }
    }

    /**
     * Invia un messaggio di tipo Election o Elected al nodo successivo tramite gRPC.
     * @param isElection true se il messaggio è di tipo Election, false se è di tipo Elected
     * @param timestamp identificativo della richiesta
     * @param candidateId id del candidato (usato solo per Election)
     * @param price prezzo offerto (usato solo per Election)
     * @param kwh sono i kwh richiesti dalla RenewableEnergyProvider
     * @param winnerId id del vincitore (usato solo per Elected)
     * @throws io.grpc.StatusRuntimeException se si verifica un errore durante la chiamata RPC
     */
    public void sendChangRobertsMessage(boolean isElection, String timestamp, @Nullable String candidateId, @Nullable Double price, int kwh, @Nullable String winnerId){

        ChangRobertsProto.ChangRobertsMessage message;

        /*
        if(isElection){
            ChangRobertsProto.Election election = ChangRobertsProto.Election.newBuilder()
                    .setTimestamp(timestamp)
                    .setCandidateId(candidateId)
                    .setPrice(price)
                    .setKwh(kwh)
                    .build();

            message = ChangRobertsProto.ChangRobertsMessage.newBuilder()
                    .setElection(election)
                    .build();
        } else {
            ChangRobertsProto.Elected elected = ChangRobertsProto.Elected.newBuilder()
                    .setTimestamp(timestamp)
                    .setWinnerId(winnerId)
                    .setKwh(kwh)
                    .build();

            message = ChangRobertsProto.ChangRobertsMessage.newBuilder()
                    .setElected(elected)
                    .build();
        }
        */

        if(isElection){
            ChangRobertsProto.Election.Builder electionBuilder = ChangRobertsProto.Election.newBuilder();
            //timestamp è obbligatorio -> fallback a stringa vuota
            electionBuilder.setTimestamp(timestamp != null ? timestamp : "");
            //candidateId è obbligatorio -> fallback a stringa vuota
            electionBuilder.setCandidateId(candidateId != null ? candidateId : "");
            //price è opzionale -> set solo se non null
            if (price != null) {
                electionBuilder.setPrice(price);
            }
            //kwh è primitivo, quindi non può essere null
            electionBuilder.setKwh(kwh);

            System.out.println(">>>[ThermalPowerPlantClient] IsElection: "+timestamp+", candidateId: "+candidateId+", price: "+price);
            message = ChangRobertsProto.ChangRobertsMessage.newBuilder()
                    .setElection(electionBuilder.build())
                    .build();

        } else {
            ChangRobertsProto.Elected.Builder electedBuilder = ChangRobertsProto.Elected.newBuilder();
            electedBuilder.setTimestamp(timestamp != null ? timestamp : "");
            electedBuilder.setWinnerId(winnerId != null ? winnerId : "");
            electedBuilder.setKwh(kwh);

            System.out.println(">>>[ThermalPowerPlantClient] IsElected: "+timestamp+", winnerId: "+winnerId+", kwh: "+kwh);
            message = ChangRobertsProto.ChangRobertsMessage.newBuilder()
                    .setElected(electedBuilder.build())
                    .build();
        }

        try {
            ChangRobertsProto.Ack response = changRobertsBlockingStub.sendMessage(message);
            System.out.println(">>>[ThermalPowerPlantClient] Risposta dal server al messaggio ChangRoberts: " + response.getMessage());
        } catch (Exception e) {
            System.err.println(">>>[ThermalPowerPlantClient] Errore durante l'invio del messaggio ChangRoberts gRPC : " + e.getMessage());
        }
    }

    /**
     * Chiude il canale di comunicazione gRPC in modo sicuro.
     */
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }

}
