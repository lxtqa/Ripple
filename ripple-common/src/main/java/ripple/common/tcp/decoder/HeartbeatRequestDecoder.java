package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.HeartbeatRequest;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
        heartbeatRequest.setUuid(TypeHelper.readUuid(byteBuf));
        return heartbeatRequest;
    }
}
