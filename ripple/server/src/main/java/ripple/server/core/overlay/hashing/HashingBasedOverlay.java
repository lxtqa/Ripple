package ripple.server.core.overlay.hashing;

import ripple.server.core.NodeMetadata;
import ripple.server.core.overlay.Overlay;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class HashingBasedOverlay implements Overlay {
    // TODO: Fix this

    @Override
    public void buildOverlay(List<NodeMetadata> nodeList) {

    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current) {
        return null;
    }
}
