package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatRequest;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        byteBuf.writeByte(heartbeatRequest.getType().getValue());
        TypeHelper.writeUuid(heartbeatRequest.getUuid(), byteBuf);
    }
}
