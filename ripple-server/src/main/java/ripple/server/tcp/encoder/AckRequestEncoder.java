// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.server.tcp.message.AckRequest;

/**
 * @author Zhen Tang
 */
public class AckRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        AckRequest ackRequest = (AckRequest) message;
        byteBuf.writeByte(ackRequest.getType().getValue());
        TypeHelper.writeUuid(ackRequest.getUuid(), byteBuf);
        TypeHelper.writeUuid(ackRequest.getMessageUuid(), byteBuf);
        byteBuf.writeInt(ackRequest.getSourceId());
        byteBuf.writeInt(ackRequest.getNodeId());
    }
}
