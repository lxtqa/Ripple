package ripple.server.core.overlay;

import ripple.common.entity.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class GossipOverlay implements Overlay {
    private int fanout;

    public GossipOverlay(int fanout) {
        this.setFanout(fanout);
    }

    public int getFanout() {
        return fanout;
    }

    public void setFanout(int fanout) {
        this.fanout = fanout;
    }

    @Override
    public void buildOverlay(List<NodeMetadata> nodeList) {

    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current) {
        // TODO
        return null;
    }
}
