package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SystemInfoResponse;

/**
 * @author Zhen Tang
 */
public class SystemInfoResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SystemInfoResponse systemInfoResponse = new SystemInfoResponse();
        systemInfoResponse.setUuid(TypeHelper.readUuid(byteBuf));
        systemInfoResponse.setCpuUsage(byteBuf.readDouble());
        return systemInfoResponse;
    }
}
