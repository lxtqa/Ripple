package ripple.server.core.overlay;

import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class GossipOverlay implements Overlay {
    private int fanout;

    public int getFanout() {
        return fanout;
    }

    public void setFanout(int fanout) {
        this.fanout = fanout;
    }

    public GossipOverlay(int fanout) {
        this.setFanout(fanout);
    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current, List<NodeMetadata> cluster) {
        // TODO
        return null;
    }
}
