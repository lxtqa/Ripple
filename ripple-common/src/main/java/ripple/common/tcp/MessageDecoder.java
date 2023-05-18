// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class MessageDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

    private Map<MessageType, Decoder> decoders;

    private Map<MessageType, Decoder> getDecoders() {
        return decoders;
    }

    private void setDecoders(Map<MessageType, Decoder> decoders) {
        this.decoders = decoders;
    }

    public MessageDecoder() {
        this.setDecoders(new ConcurrentHashMap<>());
    }

    public void registerDecoder(MessageType type, Decoder decoder) {
        this.getDecoders().put(type, decoder);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        MessageType messageType = MessageType.get(byteBuf.readByte());
        Decoder decoder = this.getDecoders().get(messageType);
        if (decoder != null) {
            Message message = decoder.decode(byteBuf, messageType);
            out.add(message);
        } else {
            LOGGER.info("[MessageDecoder] decode(): Cannot find the decoder for the message type: {}.", messageType);
        }
    }
}
