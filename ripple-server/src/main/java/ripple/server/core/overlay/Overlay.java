package ripple.server.core.overlay;

import ripple.common.entity.AbstractMessage;
import ripple.common.entity.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public interface Overlay {
    void buildOverlay(List<NodeMetadata> nodeList);

    List<NodeMetadata> calculateNodesToSync(AbstractMessage message, NodeMetadata source, NodeMetadata current);

    List<NodeMetadata> calculateNodesToCollectAck(AbstractMessage message);
}
