package ripple.server.core.overlay.tree;

import org.junit.Assert;
import org.junit.Test;
import ripple.server.core.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class TreeOverlayTest {
    @Test
    public void testCalculateNodeListForSource() {
        int branch = 3;
        int nodeCount = 10;
        int locationIndex = 3;

        int i = 0;
        List<NodeMetadata> nodeList = new ArrayList<>();
        for (i = 0; i < nodeCount; i++) {
            nodeList.add(new NodeMetadata(i + 1, "test", 0));
        }
        TreeOverlay treeOverlay = new TreeOverlay(branch);
        NodeMetadata source = new NodeMetadata(locationIndex + 1, "test", 0);
        List<NodeMetadata> ret = treeOverlay.calculateNodeListForSource(source, nodeList);
        Assert.assertEquals(locationIndex + 1, ret.get(0).getId());
        Assert.assertEquals(1, nodeList.get(0).getId());
    }
}
