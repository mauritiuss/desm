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
    comments = "Source: exitMessage.proto")
public final class ExitServiceGrpc {

  private ExitServiceGrpc() {}

  public static final String SERVICE_NAME = "ExitService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.ExitProto.ExitMessage,
      ThermalPowerPlant.grpc.ExitProto.ExitAck> getSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendMessage",
      requestType = ThermalPowerPlant.grpc.ExitProto.ExitMessage.class,
      responseType = ThermalPowerPlant.grpc.ExitProto.ExitAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.ExitProto.ExitMessage,
      ThermalPowerPlant.grpc.ExitProto.ExitAck> getSendMessageMethod() {
    io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.ExitProto.ExitMessage, ThermalPowerPlant.grpc.ExitProto.ExitAck> getSendMessageMethod;
    if ((getSendMessageMethod = ExitServiceGrpc.getSendMessageMethod) == null) {
      synchronized (ExitServiceGrpc.class) {
        if ((getSendMessageMethod = ExitServiceGrpc.getSendMessageMethod) == null) {
          ExitServiceGrpc.getSendMessageMethod = getSendMessageMethod =
              io.grpc.MethodDescriptor.<ThermalPowerPlant.grpc.ExitProto.ExitMessage, ThermalPowerPlant.grpc.ExitProto.ExitAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ThermalPowerPlant.grpc.ExitProto.ExitMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ThermalPowerPlant.grpc.ExitProto.ExitAck.getDefaultInstance()))
              .setSchemaDescriptor(new ExitServiceMethodDescriptorSupplier("SendMessage"))
              .build();
        }
      }
    }
    return getSendMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ExitServiceStub newStub(io.grpc.Channel channel) {
    return new ExitServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ExitServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ExitServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ExitServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ExitServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ExitServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendMessage(ThermalPowerPlant.grpc.ExitProto.ExitMessage request,
        io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.ExitProto.ExitAck> responseObserver) {
      asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ThermalPowerPlant.grpc.ExitProto.ExitMessage,
                ThermalPowerPlant.grpc.ExitProto.ExitAck>(
                  this, METHODID_SEND_MESSAGE)))
          .build();
    }
  }

  /**
   */
  public static final class ExitServiceStub extends io.grpc.stub.AbstractStub<ExitServiceStub> {
    private ExitServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExitServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExitServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExitServiceStub(channel, callOptions);
    }

    /**
     */
    public void sendMessage(ThermalPowerPlant.grpc.ExitProto.ExitMessage request,
        io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.ExitProto.ExitAck> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ExitServiceBlockingStub extends io.grpc.stub.AbstractStub<ExitServiceBlockingStub> {
    private ExitServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExitServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExitServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExitServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ThermalPowerPlant.grpc.ExitProto.ExitAck sendMessage(ThermalPowerPlant.grpc.ExitProto.ExitMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ExitServiceFutureStub extends io.grpc.stub.AbstractStub<ExitServiceFutureStub> {
    private ExitServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExitServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExitServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExitServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ThermalPowerPlant.grpc.ExitProto.ExitAck> sendMessage(
        ThermalPowerPlant.grpc.ExitProto.ExitMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_MESSAGE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ExitServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ExitServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((ThermalPowerPlant.grpc.ExitProto.ExitMessage) request,
              (io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.ExitProto.ExitAck>) responseObserver);
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

  private static abstract class ExitServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ExitServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ThermalPowerPlant.grpc.ExitProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ExitService");
    }
  }

  private static final class ExitServiceFileDescriptorSupplier
      extends ExitServiceBaseDescriptorSupplier {
    ExitServiceFileDescriptorSupplier() {}
  }

  private static final class ExitServiceMethodDescriptorSupplier
      extends ExitServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ExitServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ExitServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ExitServiceFileDescriptorSupplier())
              .addMethod(getSendMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
