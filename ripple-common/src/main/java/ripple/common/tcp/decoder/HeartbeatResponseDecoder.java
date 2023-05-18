package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.HeartbeatResponse;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        heartbeatResponse.setUuid(TypeHelper.readUuid(byteBuf));
        heartbeatResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return heartbeatResponse;
    }
}
