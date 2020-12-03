package ripple.server.core.overlay;

import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public interface Overlay {
    void buildOverlay(List<NodeMetadata> nodeList);

    List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current, List<NodeMetadata> nodeList);
}
