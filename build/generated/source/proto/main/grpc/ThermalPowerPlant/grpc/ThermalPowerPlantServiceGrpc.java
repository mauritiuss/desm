package ThermalPowerPlant.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: presentationMessage.proto")
public final class ThermalPowerPlantServiceGrpc {

  private ThermalPowerPlantServiceGrpc() {}

  public static final String SERVICE_NAME = "ThermalPowerPlantService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.PresentationProto.PresentationMessage,
      ThermalPowerPlant.grpc.PresentationProto.PresentationAck> getPresentYourselfMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PresentYourself",
      requestType = ThermalPowerPlant.grpc.PresentationProto.PresentationMessage.class,
      responseType = ThermalPowerPlant.grpc.PresentationProto.PresentationAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.PresentationProto.PresentationMessage,
      ThermalPowerPlant.grpc.PresentationProto.PresentationAck> getPresentYourselfMethod() {
    io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.PresentationProto.PresentationMessage, ThermalPowerPlant.grpc.PresentationProto.PresentationAck> getPresentYourselfMethod;
    if ((getPresentYourselfMethod = ThermalPowerPlantServiceGrpc.getPresentYourselfMethod) == null) {
      synchronized (ThermalPowerPlantServiceGrpc.class) {
        if ((getPresentYourselfMethod = ThermalPowerPlantServiceGrpc.getPresentYourselfMethod) == null) {
          ThermalPowerPlantServiceGrpc.getPresentYourselfMethod = getPresentYourselfMethod =
              io.grpc.MethodDescriptor.<ThermalPowerPlant.grpc.PresentationProto.PresentationMessage, ThermalPowerPlant.grpc.PresentationProto.PresentationAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PresentYourself"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ThermalPowerPlant.grpc.PresentationProto.PresentationMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ThermalPowerPlant.grpc.PresentationProto.PresentationAck.getDefaultInstance()))
              .setSchemaDescriptor(new ThermalPowerPlantServiceMethodDescriptorSupplier("PresentYourself"))
              .build();
        }
      }
    }
    return getPresentYourselfMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ThermalPowerPlantServiceStub newStub(io.grpc.Channel channel) {
    return new ThermalPowerPlantServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ThermalPowerPlantServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ThermalPowerPlantServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ThermalPowerPlantServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ThermalPowerPlantServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ThermalPowerPlantServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void presentYourself(ThermalPowerPlant.grpc.PresentationProto.PresentationMessage request,
        io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.PresentationProto.PresentationAck> responseObserver) {
      asyncUnimplementedUnaryCall(getPresentYourselfMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPresentYourselfMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ThermalPowerPlant.grpc.PresentationProto.PresentationMessage,
                ThermalPowerPlant.grpc.PresentationProto.PresentationAck>(
                  this, METHODID_PRESENT_YOURSELF)))
          .build();
    }
  }

  /**
   */
  public static final class ThermalPowerPlantServiceStub extends io.grpc.stub.AbstractStub<ThermalPowerPlantServiceStub> {
    private ThermalPowerPlantServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ThermalPowerPlantServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ThermalPowerPlantServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ThermalPowerPlantServiceStub(channel, callOptions);
    }

    /**
     */
    public void presentYourself(ThermalPowerPlant.grpc.PresentationProto.PresentationMessage request,
        io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.PresentationProto.PresentationAck> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPresentYourselfMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ThermalPowerPlantServiceBlockingStub extends io.grpc.stub.AbstractStub<ThermalPowerPlantServiceBlockingStub> {
    private ThermalPowerPlantServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ThermalPowerPlantServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ThermalPowerPlantServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ThermalPowerPlantServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ThermalPowerPlant.grpc.PresentationProto.PresentationAck presentYourself(ThermalPowerPlant.grpc.PresentationProto.PresentationMessage request) {
      return blockingUnaryCall(
          getChannel(), getPresentYourselfMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ThermalPowerPlantServiceFutureStub extends io.grpc.stub.AbstractStub<ThermalPowerPlantServiceFutureStub> {
    private ThermalPowerPlantServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ThermalPowerPlantServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ThermalPowerPlantServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ThermalPowerPlantServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ThermalPowerPlant.grpc.PresentationProto.PresentationAck> presentYourself(
        ThermalPowerPlant.grpc.PresentationProto.PresentationMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getPresentYourselfMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PRESENT_YOURSELF = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ThermalPowerPlantServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ThermalPowerPlantServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PRESENT_YOURSELF:
          serviceImpl.presentYourself((ThermalPowerPlant.grpc.PresentationProto.PresentationMessage) request,
              (io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.PresentationProto.PresentationAck>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ThermalPowerPlantServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ThermalPowerPlantServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ThermalPowerPlant.grpc.PresentationProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ThermalPowerPlantService");
    }
  }

  private static final class ThermalPowerPlantServiceFileDescriptorSupplier
      extends ThermalPowerPlantServiceBaseDescriptorSupplier {
    ThermalPowerPlantServiceFileDescriptorSupplier() {}
  }

  private static final class ThermalPowerPlantServiceMethodDescriptorSupplier
      extends ThermalPowerPlantServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ThermalPowerPlantServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ThermalPowerPlantServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ThermalPowerPlantServiceFileDescriptorSupplier())
              .addMethod(getPresentYourselfMethod())
              .build();
        }
      }
    }
    return result;
  }
}
