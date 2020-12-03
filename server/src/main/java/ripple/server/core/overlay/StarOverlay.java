package ripple.server.core.overlay;

import ripple.server.core.Item;
import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class StarOverlay implements Overlay {
    @Override
    public List<NodeMetadata> calculateNodesToSync(Item toSend, NodeMetadata current, List<NodeMetadata> cluster) {
        // TODO
        return null;
    }
}
