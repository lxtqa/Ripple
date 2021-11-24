package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatResponse;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        byteBuf.writeByte(heartbeatResponse.getType().getValue());
        TypeHelper.writeUuid(heartbeatResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(heartbeatResponse.isSuccess(), byteBuf);
    }
}
