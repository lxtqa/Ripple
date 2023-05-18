// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class GetClientListResponse extends Message {
    private String clientListSignature;
    private List<GetClientListResponseItem> items;

    public String getClientListSignature() {
        return clientListSignature;
    }

    public void setClientListSignature(String clientListSignature) {
        this.clientListSignature = clientListSignature;
    }

    public List<GetClientListResponseItem> getItems() {
        return items;
    }

    public void setItems(List<GetClientListResponseItem> items) {
        this.items = items;
    }

    public GetClientListResponse() {
        this.setType(MessageType.GET_CLIENT_LIST_RESPONSE);
        this.setItems(new ArrayList<>());
    }
}
