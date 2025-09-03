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
    comments = "Source: changRobertMessage.proto")
public final class ChangRobertsServiceGrpc {

  private ChangRobertsServiceGrpc() {}

  public static final String SERVICE_NAME = "ChangRobertsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage,
      ThermalPowerPlant.grpc.ChangRobertsProto.Ack> getSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendMessage",
      requestType = ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage.class,
      responseType = ThermalPowerPlant.grpc.ChangRobertsProto.Ack.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage,
      ThermalPowerPlant.grpc.ChangRobertsProto.Ack> getSendMessageMethod() {
    io.grpc.MethodDescriptor<ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage, ThermalPowerPlant.grpc.ChangRobertsProto.Ack> getSendMessageMethod;
    if ((getSendMessageMethod = ChangRobertsServiceGrpc.getSendMessageMethod) == null) {
      synchronized (ChangRobertsServiceGrpc.class) {
        if ((getSendMessageMethod = ChangRobertsServiceGrpc.getSendMessageMethod) == null) {
          ChangRobertsServiceGrpc.getSendMessageMethod = getSendMessageMethod =
              io.grpc.MethodDescriptor.<ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage, ThermalPowerPlant.grpc.ChangRobertsProto.Ack>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ThermalPowerPlant.grpc.ChangRobertsProto.Ack.getDefaultInstance()))
              .setSchemaDescriptor(new ChangRobertsServiceMethodDescriptorSupplier("SendMessage"))
              .build();
        }
      }
    }
    return getSendMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChangRobertsServiceStub newStub(io.grpc.Channel channel) {
    return new ChangRobertsServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChangRobertsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ChangRobertsServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChangRobertsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ChangRobertsServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ChangRobertsServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendMessage(ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage request,
        io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.ChangRobertsProto.Ack> responseObserver) {
      asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage,
                ThermalPowerPlant.grpc.ChangRobertsProto.Ack>(
                  this, METHODID_SEND_MESSAGE)))
          .build();
    }
  }

  /**
   */
  public static final class ChangRobertsServiceStub extends io.grpc.stub.AbstractStub<ChangRobertsServiceStub> {
    private ChangRobertsServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChangRobertsServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChangRobertsServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChangRobertsServiceStub(channel, callOptions);
    }

    /**
     */
    public void sendMessage(ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage request,
        io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.ChangRobertsProto.Ack> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ChangRobertsServiceBlockingStub extends io.grpc.stub.AbstractStub<ChangRobertsServiceBlockingStub> {
    private ChangRobertsServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChangRobertsServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChangRobertsServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChangRobertsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ThermalPowerPlant.grpc.ChangRobertsProto.Ack sendMessage(ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ChangRobertsServiceFutureStub extends io.grpc.stub.AbstractStub<ChangRobertsServiceFutureStub> {
    private ChangRobertsServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChangRobertsServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChangRobertsServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChangRobertsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ThermalPowerPlant.grpc.ChangRobertsProto.Ack> sendMessage(
        ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage request) {
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
    private final ChangRobertsServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ChangRobertsServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((ThermalPowerPlant.grpc.ChangRobertsProto.ChangRobertsMessage) request,
              (io.grpc.stub.StreamObserver<ThermalPowerPlant.grpc.ChangRobertsProto.Ack>) responseObserver);
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

  private static abstract class ChangRobertsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChangRobertsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ThermalPowerPlant.grpc.ChangRobertsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ChangRobertsService");
    }
  }

  private static final class ChangRobertsServiceFileDescriptorSupplier
      extends ChangRobertsServiceBaseDescriptorSupplier {
    ChangRobertsServiceFileDescriptorSupplier() {}
  }

  private static final class ChangRobertsServiceMethodDescriptorSupplier
      extends ChangRobertsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ChangRobertsServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ChangRobertsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChangRobertsServiceFileDescriptorSupplier())
              .addMethod(getSendMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
