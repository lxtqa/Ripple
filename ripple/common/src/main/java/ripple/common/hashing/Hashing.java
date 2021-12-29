package ripple.common.hashing;

import ripple.common.entity.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public interface Hashing {
    // Calculate the node list to store the key-value pair
    List<NodeMetadata> hashing(String key, List<NodeMetadata> nodeList);
}
