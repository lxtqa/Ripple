package ripple.server.core.overlay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class StarOverlay implements Overlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(StarOverlay.class);

    @Override
    public List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current, List<NodeMetadata> cluster) {
        LOGGER.info("[StarOverlay] calculateNodesToSync() called. sourceId = {}, currentId = {}", source.getId(), current.getId());
        if (source.getId() != current.getId()) {
            // Leaf node
            LOGGER.info("[StarOverlay] Leaf node.");
            return new ArrayList<>();
        } else {
            List<NodeMetadata> ret = new ArrayList<>();
            for (NodeMetadata nodeMetadata : cluster) {
                if (nodeMetadata.getId() != current.getId()) {
                    ret.add(nodeMetadata);
                    LOGGER.info("[StarOverlay] Attempting to send to node {} ({}:{})."
                            , nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
                }
            }
            return ret;
        }
    }
}
