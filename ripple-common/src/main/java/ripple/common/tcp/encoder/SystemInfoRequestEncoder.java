package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SystemInfoRequest;

/**
 * @author Zhen Tang
 */
public class SystemInfoRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SystemInfoRequest systemInfoRequest = (SystemInfoRequest) message;
        byteBuf.writeByte(systemInfoRequest.getType().getValue());
        TypeHelper.writeUuid(systemInfoRequest.getUuid(), byteBuf);
    }
}
