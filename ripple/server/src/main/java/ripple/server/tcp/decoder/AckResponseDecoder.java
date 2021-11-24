package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.server.tcp.message.AckResponse;

/**
 * @author Zhen Tang
 */
public class AckResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        AckResponse ackResponse = new AckResponse();
        ackResponse.setUuid(TypeHelper.readUuid(byteBuf));
        ackResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return ackResponse;
    }
}
