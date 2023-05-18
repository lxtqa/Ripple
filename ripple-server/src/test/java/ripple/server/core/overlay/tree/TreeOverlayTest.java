// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.overlay.tree;

import org.junit.Assert;
import org.junit.Test;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.NodeMetadata;
import ripple.common.entity.UpdateMessage;

import java.util.ArrayList;
import java.util.Date;
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

    @Test
    public void testCompleteTree() {
        int branch = 3;
        int nodeCount = 10;
        int i = 0;
        List<NodeMetadata> nodeList = new ArrayList<>();
        for (i = 0; i < nodeCount; i++) {
            nodeList.add(new NodeMetadata(i + 1, "test", 0));
        }

        CompleteTree completeTree = new CompleteTree(branch, nodeList);

        TreeNode root = completeTree.getRoot();
        Assert.assertEquals(root, completeTree.getNodeList().get(0));
        Assert.assertEquals(new NodeMetadata(1, "test", 0), root.getNodeMetadata());
        Assert.assertEquals(3, root.getChildren().size());
        Assert.assertEquals(new NodeMetadata(2, "test", 0), root.getChildren().get(0).getNodeMetadata());
        Assert.assertEquals(new NodeMetadata(3, "test", 0), root.getChildren().get(1).getNodeMetadata());
        Assert.assertEquals(new NodeMetadata(4, "test", 0), root.getChildren().get(2).getNodeMetadata());

        TreeNode nodeTwo = completeTree.getNodeList().get(1);
        Assert.assertEquals(new NodeMetadata(2, "test", 0), nodeTwo.getNodeMetadata());
        Assert.assertEquals(3, nodeTwo.getChildren().size());
        Assert.assertEquals(new NodeMetadata(5, "test", 0), nodeTwo.getChildren().get(0).getNodeMetadata());
        Assert.assertEquals(new NodeMetadata(6, "test", 0), nodeTwo.getChildren().get(1).getNodeMetadata());
        Assert.assertEquals(new NodeMetadata(7, "test", 0), nodeTwo.getChildren().get(2).getNodeMetadata());

        TreeNode nodeThree = completeTree.getNodeList().get(2);
        Assert.assertEquals(new NodeMetadata(3, "test", 0), nodeThree.getNodeMetadata());
        Assert.assertEquals(3, nodeThree.getChildren().size());
        Assert.assertEquals(new NodeMetadata(8, "test", 0), nodeThree.getChildren().get(0).getNodeMetadata());
        Assert.assertEquals(new NodeMetadata(9, "test", 0), nodeThree.getChildren().get(1).getNodeMetadata());
        Assert.assertEquals(new NodeMetadata(10, "test", 0), nodeThree.getChildren().get(2).getNodeMetadata());

        for (i = 3; i < nodeCount; i++) {
            // Leaf
            Assert.assertEquals(new NodeMetadata(i + 1, "test", 0), completeTree.getNodeList().get(i).getNodeMetadata());
            Assert.assertEquals(0, completeTree.getNodeList().get(i).getChildren().size());
        }
    }

    @Test
    public void testCalculateNodesToSync() {
        int branch = 3;
        int nodeCount = 10;
        int i = 0;
        List<NodeMetadata> nodeList = new ArrayList<>();
        for (i = 0; i < nodeCount; i++) {
            nodeList.add(new NodeMetadata(i + 1, "test", 0));
        }

        TreeOverlay treeOverlay = new TreeOverlay(branch);
        treeOverlay.buildOverlay(nodeList);

        AbstractMessage testMessage = new UpdateMessage("testApp", "test"
                , "value", new Date(System.currentTimeMillis()), 1);

        NodeMetadata source = new NodeMetadata(1, "test", 0);
        NodeMetadata current = new NodeMetadata(3, "test", 0);
        List<NodeMetadata> nodes = treeOverlay.calculateNodesToSync(testMessage, source, current);

        Assert.assertEquals(3, nodes.size());
        Assert.assertEquals(new NodeMetadata(8, "test", 0), nodes.get(0));
        Assert.assertEquals(new NodeMetadata(9, "test", 0), nodes.get(1));
        Assert.assertEquals(new NodeMetadata(10, "test", 0), nodes.get(2));

        source = new NodeMetadata(3, "test", 0);
        current = new NodeMetadata(1, "test", 0);
        nodes = treeOverlay.calculateNodesToSync(testMessage, source, current);
        Assert.assertEquals(0, nodes.size());
    }
}
