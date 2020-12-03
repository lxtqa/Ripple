package ripple.server.core.overlay;

import ripple.server.core.Item;
import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class TreeOverlay implements Overlay {
    private int branch;

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public TreeOverlay(int branch) {
        this.setBranch(branch);
    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(Item toSend, NodeMetadata current, List<NodeMetadata> cluster) {
        // TODO
        return null;
    }
}
