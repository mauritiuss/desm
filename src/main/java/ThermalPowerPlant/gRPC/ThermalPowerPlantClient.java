package ThermalPowerPlant.gRPC;

import ThermalPowerPlant.grpc.ThermalPowerPlantServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ThermalPowerPlant.grpc.PresentationProto.PresentationAck;
import ThermalPowerPlant.grpc.PresentationProto.PresentationMessage;


public class ThermalPowerPlantClient {

    private final ManagedChannel channel;
    private final ThermalPowerPlantServiceGrpc.ThermalPowerPlantServiceBlockingStub blockingStub;

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
            System.out.println("Risposta dal server: " + response.getMessage());
        } catch (Exception e) {
            System.err.println("Errore durante l'invio del messaggio gRPC: " + e.getMessage());
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
