package ripple.server.core.overlay;

import ripple.server.core.NodeMetadata;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class ExpanderOverlay implements Overlay {
    private int scale;

    public ExpanderOverlay(int scale) {
        this.setScale(scale);
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
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
