package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetRequest;

/**
 * @author Zhen Tang
 */
public class GetRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        GetRequest getRequest = (GetRequest) message;
        byteBuf.writeByte(getRequest.getType().getValue());
        TypeHelper.writeUuid(getRequest.getUuid(), byteBuf);
        TypeHelper.writeString(getRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(getRequest.getKey(), byteBuf);
    }
}