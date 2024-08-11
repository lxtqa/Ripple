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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class GossipOverlay implements Overlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(GossipOverlay.class);
    private int fanout;
    private List<NodeMetadata> nodeList;

    public GossipOverlay(int fanout) {
        this.setFanout(fanout);
    }

    public int getFanout() {
        return fanout;
    }

    public void setFanout(int fanout) {
        this.fanout = fanout;
    }

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
        LOGGER.info("[GossipOverlay] calculateNodesToSync() called. sourceId = {}, fromId = {}, currentId = {}", source.getId(), from.getId(), current.getId());
        // Only use two-layer of spanning tree, thus it comes a leaf if the last hop is not the message source
        if (from.getId() != source.getId()) {
            // Leaf node
            LOGGER.info("[GossipOverlay] Leaf node.");
            return new ArrayList<>();
        } else {
            List<NodeMetadata> toSelect = new ArrayList<>(this.getNodeList());
            toSelect.removeIf(node -> (node.getId() == source.getId() || node.getId() == current.getId()));
            Collections.shuffle(toSelect);
            List<NodeMetadata> ret = toSelect.size() <= this.getFanout() ? toSelect : toSelect.subList(0, this.getFanout());
            for (NodeMetadata nodeMetadata : ret) {
                LOGGER.info("[GossipOverlay] Attempting to send to node {} ({}:{})."
                        , nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
            }
            return ret;
        }
    }

    @Override
    public List<NodeMetadata> calculateNodesToCollectAck(AbstractMessage message) {
        return this.getNodeList();
    }
}
