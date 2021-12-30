package ripple.client.core;

import ripple.common.entity.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public interface NodeSelector {
    NodeMetadata selectNodeToConnect(String applicationName, String key, List<NodeMetadata> nodeList);
}
