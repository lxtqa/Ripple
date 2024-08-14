// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.overlay.expander;

import ripple.common.entity.NodeMetadata;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Zhen Tang
 */
public class BfsBuildStrategy implements BuildStrategy {
    @Override
    public void buildOverlay(SpanningTree spanningTree, List<NodeMetadata> availableNodes) {
        Queue<TreeNode> nodes = new ArrayBlockingQueue<>(availableNodes.size());
        nodes.offer(spanningTree.getRoot());
        spanningTree.getNodeList().add(spanningTree.getRoot());
        while (!nodes.isEmpty()) {
            TreeNode node = nodes.poll();
            this.doAddChildren(spanningTree, node, availableNodes);
            for (TreeNode elem : node.getChildren()) {
                nodes.offer(elem);
            }
        }
    }

    private void doAddChildren(SpanningTree spanningTree, TreeNode treeNode, List<NodeMetadata> availableNodes) {
        availableNodes.removeIf(n -> n.getId() == treeNode.getVertex().getNodeMetadata().getId());
        for (Vertex vertex : treeNode.getVertex().getNeighbours()) {
            if (availableNodes.stream().anyMatch(n -> n.getId() == vertex.getNodeMetadata().getId())) {
                TreeNode toAdd = new TreeNode(vertex);
                treeNode.getChildren().add(toAdd);
                spanningTree.getNodeList().add(toAdd);
                availableNodes.removeIf(n -> n.getId() == toAdd.getVertex().getNodeMetadata().getId());
            }
        }
    }
}
