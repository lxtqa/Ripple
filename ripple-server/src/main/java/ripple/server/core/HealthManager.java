// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.NodeMetadata;
import ripple.server.helper.Api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class HealthManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthManager.class);

    private Node node;
    private Map<NodeMetadata, Boolean> aliveMap;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<NodeMetadata, Boolean> getAliveMap() {
        return aliveMap;
    }

    public void setAliveMap(Map<NodeMetadata, Boolean> aliveMap) {
        this.aliveMap = aliveMap;
    }

    public HealthManager(Node node) {
        this.setNode(node);
        this.setAliveMap(new ConcurrentHashMap<>());
    }

    public void init() {
        for (NodeMetadata metadata : this.getNode().getNodeList()) {
            this.getAliveMap().put(metadata, false);
        }
    }

    public void checkHealth() {
        for (NodeMetadata metadata : this.getNode().getNodeList()) {
            if (this.getAliveMap().get(metadata) == false) {
                Channel channel = this.getNode().getApiServer().findChannel(metadata.getAddress(), metadata.getPort());
                if (channel != null) {
                    Api.heartbeat(channel);
                }
            }
        }
    }

    public boolean isAlive(NodeMetadata metadata) {
        return this.getAliveMap().get(metadata);
    }
}
