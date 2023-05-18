package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SystemInfoResponse;

/**
 * @author Zhen Tang
 */
public class SystemInfoResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SystemInfoResponse systemInfoResponse = (SystemInfoResponse) message;
        byteBuf.writeByte(systemInfoResponse.getType().getValue());
        TypeHelper.writeUuid(systemInfoResponse.getUuid(), byteBuf);
        byteBuf.writeDouble(systemInfoResponse.getCpuUsage());
    }
}
