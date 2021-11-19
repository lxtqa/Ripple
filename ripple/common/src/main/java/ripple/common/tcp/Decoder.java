package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

public interface Decoder {

    Message decode(ByteBuf byteBuf, MessageType messageType);
}
