package ripple.server.core.overlay;

import ripple.server.core.Item;
import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public interface Overlay {
    List<NodeMetadata> calculateNodesToSync(Item toSend, NodeMetadata current, List<NodeMetadata> cluster);
}
