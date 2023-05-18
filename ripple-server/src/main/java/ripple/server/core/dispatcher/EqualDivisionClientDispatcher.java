// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.dispatcher;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.ClientMetadata;
import ripple.server.core.Node;
import ripple.server.helper.Api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Zhen Tang
 */
public class EqualDivisionClientDispatcher implements ClientDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientDispatcher.class);
    private static final int DEFAULT_PARTS = 3;

    private Node node;
    private int parts;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getParts() {
        return parts;
    }

    public void setParts(int parts) {
        this.parts = parts;
    }

    public EqualDivisionClientDispatcher(Node node, int parts) {
        this.setNode(node);
        this.setParts(parts);
    }

    public EqualDivisionClientDispatcher(Node node) {
        this(node, EqualDivisionClientDispatcher.DEFAULT_PARTS);
    }

    @Override
    public boolean notifyClients(Set<ClientMetadata> clientList, AbstractMessage message) {
        List<ClientMetadata> sourceList = new ArrayList<>(clientList);
        Collections.sort(sourceList);

        List<List<ClientMetadata>> dividedList = this.averageDivide(sourceList, this.getParts());
        for (List<ClientMetadata> list : dividedList) {
            if (list.size() == 0) {
                continue;
            }
            if (list.size() == 1) {
                ClientMetadata metadata = list.get(0);
                LOGGER.info("[EqualDivisionClientDispatcher] Node-{}: Sync {} message ({}) to client {}:{}."
                        , this.getNode().getId(), message.getType(), message.getUuid(), metadata.getAddress(), metadata.getPort());
                Channel channel = this.getNode().getApiServer().findChannel(metadata.getAddress(), metadata.getPort());
                Api.sync(channel, message);
            } else {
                ClientMetadata metadata = list.get(0);
                List<ClientMetadata> remaining = list.subList(1, list.size());
                Collections.sort(remaining);
                String signature = this.getNode().getClientListCache().calculateSignature(remaining);
                if (this.getNode().getClientListCache().get(signature) == null) {
                    this.getNode().getClientListCache().put(signature, remaining);
                }
                LOGGER.info("[EqualDivisionClientDispatcher] Node-{}: Dispatch {} message ({}) to client {}:{}, Signature = {}."
                        , this.getNode().getId(), message.getType(), message.getUuid(), metadata.getAddress(), metadata.getPort(), signature);
                Channel channel = this.getNode().getApiServer().findChannel(metadata.getAddress(), metadata.getPort());
                Api.dispatch(channel, signature, message);
            }
        }
        return true;
    }

    private List<List<ClientMetadata>> averageDivide(List<ClientMetadata> source, int part) {
        List<List<ClientMetadata>> result = new ArrayList<>();
        int remainder = source.size() % part;
        int number = source.size() / part;
        int offset = 0;
        int i = 0;
        for (i = 0; i < part; i++) {
            List<ClientMetadata> subList = null;
            if (remainder > 0) {
                subList = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                subList = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(subList);
        }
        return result;
    }

}
