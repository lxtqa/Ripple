package ripple.server.core.overlay;

import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class ExpanderOverlay implements Overlay {
    private int scale;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public ExpanderOverlay(int scale) {
        this.setScale(scale);
    }

    @Override
    public void buildOverlay(List<NodeMetadata> nodeList) {

    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current, List<NodeMetadata> nodeList) {
        // TODO
        return null;
    }
}
