package ripple.common.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {
    private HandlerFactory handlerFactory;

    private HandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    private void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public ServerMessageHandler() {
        this.setHandlerFactory(new HandlerFactory());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) {
        try {
            System.out.println("handle request");

            Message request = null;
            if (message.getType() == MessageType.ACK_REQUEST) {
                AckRequest ackRequest = new AckRequest();
                ackRequest.setUuid(message.getUuid());
                ackRequest.setType(message.getType());
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(message.getPayload()));
                int uuidSize = dataInputStream.readInt();
                byte[] uuidBytes = new byte[uuidSize];
                dataInputStream.readFully(uuidBytes);
                ackRequest.setMessageUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));
                ackRequest.setSourceId(dataInputStream.readInt());
                ackRequest.setNodeId(dataInputStream.readInt());
                request = ackRequest;
            }

            Handler handler = this.getHandlerFactory().findHandler(request);
            Message response = handler.handle(request);

            Message result = null;
            if (response.getType() == MessageType.ACK_RESPONSE) {
                AckResponse ackResponse = (AckResponse) response;
                result = new Message();
                result.setUuid(ackResponse.getUuid());
                result.setType(ackResponse.getType());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeBoolean(ackResponse.isSuccess());
                result.setPayload(byteArrayOutputStream.toByteArray());
            }

            ctx.writeAndFlush(result);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.getHandlerFactory().registerHandler(new AckRequestHandler());
        this.getHandlerFactory().registerHandler(new AckResponseHandler());
    }
}
