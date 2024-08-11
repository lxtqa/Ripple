// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.overlay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class StarOverlay implements Overlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(StarOverlay.class);
    private List<NodeMetadata> nodeList;

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public void buildOverlay(List<NodeMetadata> nodeList) {
        this.setNodeList(nodeList);
    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(AbstractMessage message, NodeMetadata source, NodeMetadata from, NodeMetadata current) {
        LOGGER.info("[StarOverlay] calculateNodesToSync() called. sourceId = {}, currentId = {}", source.getId(), current.getId());
        if (source.getId() != current.getId()) {
            // Leaf node
            LOGGER.info("[StarOverlay] Leaf node.");
            return new ArrayList<>();
        } else {
            List<NodeMetadata> ret = new ArrayList<>();
            for (NodeMetadata nodeMetadata : this.getNodeList()) {
                if (nodeMetadata.getId() != current.getId()) {
                    ret.add(nodeMetadata);
                    LOGGER.info("[StarOverlay] Attempting to send to node {} ({}:{})."
                            , nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
                }
            }
            return ret;
        }
    }

    @Override
    public List<NodeMetadata> calculateNodesToCollectAck(AbstractMessage message) {
        return this.getNodeList();
    }
}
