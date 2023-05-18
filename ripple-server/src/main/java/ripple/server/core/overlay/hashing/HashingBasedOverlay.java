package ripple.server.core.overlay.hashing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.Hashing;
import ripple.server.core.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class HashingBasedOverlay implements Overlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashingBasedOverlay.class);
    private Hashing hashing;
    private List<NodeMetadata> nodeList;

    public Hashing getHashing() {
        return hashing;
    }

    public void setHashing(Hashing hashing) {
        this.hashing = hashing;
    }

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }

    public HashingBasedOverlay(Hashing hashing) {
        this.setHashing(hashing);
    }

    @Override
    public void buildOverlay(List<NodeMetadata> nodeList) {
        this.setNodeList(nodeList);
    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(AbstractMessage message, NodeMetadata source, NodeMetadata current) {
        LOGGER.info("[HashingBasedOverlay] calculateNodesToSync() called. sourceId = {}, currentId = {}", source.getId(), current.getId());
        if (source.getId() != current.getId()) {
            // Leaf node
            LOGGER.info("[HashingBasedOverlay] Leaf node.");
            return new ArrayList<>();
        } else {
            List<NodeMetadata> ret = new ArrayList<>();
            List<NodeMetadata> candidates = this.getHashing().hashing(message.getApplicationName(), message.getKey(), this.getNodeList());
            for (NodeMetadata nodeMetadata : candidates) {
                if (nodeMetadata.getId() != current.getId()) {
                    ret.add(nodeMetadata);
                    LOGGER.info("[HashingBasedOverlay] Attempting to send to node {} ({}:{})."
                            , nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
                }
            }
            return ret;
        }
    }

    @Override
    public List<NodeMetadata> calculateNodesToCollectAck(AbstractMessage message) {
        return this.getHashing().hashing(message.getApplicationName(), message.getKey(), this.getNodeList());
    }
}
