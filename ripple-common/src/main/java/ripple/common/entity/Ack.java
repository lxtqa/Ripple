// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.entity;

import java.util.Set;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Ack {
    private UUID messageUuid;
    private Set<Integer> nodeList;
    private Set<Integer> ackNodes;

    public UUID getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public Set<Integer> getNodeList() {
        return nodeList;
    }

    public void setNodeList(Set<Integer> nodeList) {
        this.nodeList = nodeList;
    }

    public Set<Integer> getAckNodes() {
        return ackNodes;
    }

    public void setAckNodes(Set<Integer> ackNodes) {
        this.ackNodes = ackNodes;
    }
}
