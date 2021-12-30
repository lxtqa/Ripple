package ripple.client.core;

import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.Hashing;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class HashingBasedSelector implements NodeSelector {
    private Hashing hashing;

    public Hashing getHashing() {
        return hashing;
    }

    private void setHashing(Hashing hashing) {
        this.hashing = hashing;
    }

    public HashingBasedSelector(Hashing hashing) {
        this.setHashing(hashing);
    }

    //  Randomly select
    @Override
    public NodeMetadata selectNodeToConnect(String applicationName, String key, List<NodeMetadata> nodeList) {
        List<NodeMetadata> candidates = this.getHashing().hashing(applicationName, key, nodeList);
        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }
}
