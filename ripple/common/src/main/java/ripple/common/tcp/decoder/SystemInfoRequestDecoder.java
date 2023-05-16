package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SystemInfoRequest;

/**
 * @author Zhen Tang
 */
public class SystemInfoRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SystemInfoRequest systemInfoRequest = new SystemInfoRequest();
        systemInfoRequest.setUuid(TypeHelper.readUuid(byteBuf));
        return systemInfoRequest;
    }
}