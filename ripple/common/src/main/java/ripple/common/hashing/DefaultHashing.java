package ripple.common.hashing;

import ripple.common.entity.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class DefaultHashing implements Hashing {
    // Returns the node list

    @Override
    public List<NodeMetadata> hashing(String key, List<NodeMetadata> nodeList) {
        return new ArrayList<>(nodeList);
    }
}
