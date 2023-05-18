// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetClientListResponse;
import ripple.common.tcp.message.GetClientListResponseItem;

/**
 * @author Zhen Tang
 */
public class GetClientListResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        GetClientListResponse getClientListResponse = (GetClientListResponse) message;
        byteBuf.writeByte(getClientListResponse.getType().getValue());
        TypeHelper.writeUuid(getClientListResponse.getUuid(), byteBuf);
        TypeHelper.writeString(getClientListResponse.getClientListSignature(), byteBuf);
        int itemsCount = getClientListResponse.getItems().size();
        byteBuf.writeInt(itemsCount);
        int i = 0;
        for (i = 0; i < itemsCount; i++) {
            GetClientListResponseItem getClientListResponseItem = getClientListResponse.getItems().get(i);
            TypeHelper.writeString(getClientListResponseItem.getAddress(), byteBuf);
            byteBuf.writeInt(getClientListResponseItem.getPort());
        }
    }
}