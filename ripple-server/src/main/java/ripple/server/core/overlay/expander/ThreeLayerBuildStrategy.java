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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zhen Tang
 */
public class ThreeLayerBuildStrategy implements BuildStrategy {
    @Override
    public void buildOverlay(SpanningTree spanningTree, List<NodeMetadata> availableNodes) {
        TreeNode root = spanningTree.getRoot();
        spanningTree.getNodeList().add(root);
        Set<Integer> taggedCluster = new HashSet<>();
        taggedCluster.add(root.getVertex().getClusterId());

        // First layer: outbound of the root node
        availableNodes.removeIf(n -> n.getId() == root.getVertex().getNodeMetadata().getId());
        for (Vertex vertex : root.getVertex().getNeighbours()) {
            if ((vertex.getClusterId() == root.getVertex().getClusterId()
                    || (vertex.getClusterId() != root.getVertex().getClusterId() && !taggedCluster.contains(vertex.getClusterId())))
                    && availableNodes.stream().anyMatch(n -> n.getId() == vertex.getNodeMetadata().getId())) {
                TreeNode toAdd = new TreeNode(vertex);
                root.getChildren().add(toAdd);
                spanningTree.getNodeList().add(toAdd);
                availableNodes.removeIf(n -> n.getId() == toAdd.getVertex().getNodeMetadata().getId());
                if (vertex.getClusterId() != root.getVertex().getClusterId()) {
                    taggedCluster.add(vertex.getClusterId());
                }
            }
        }
        // Second layer: cross cluster by other nodes
        for (TreeNode child : root.getChildren()) {
            for (Vertex vertex : child.getVertex().getNeighbours()) {
                if ((vertex.getClusterId() != child.getVertex().getClusterId() && !taggedCluster.contains(vertex.getClusterId()))
                        && availableNodes.stream().anyMatch(n -> n.getId() == vertex.getNodeMetadata().getId())) {
                    TreeNode toAdd = new TreeNode(vertex);
                    child.getChildren().add(toAdd);
                    spanningTree.getNodeList().add(toAdd);
                    availableNodes.removeIf(n -> n.getId() == toAdd.getVertex().getNodeMetadata().getId());
                    taggedCluster.add(vertex.getClusterId());
                }
            }
        }
        // Third layer: inside other clusters
        List<TreeNode> nodes = new ArrayList<>(spanningTree.getNodeList());
        for (TreeNode child : nodes) {
            for (Vertex vertex : child.getVertex().getNeighbours()) {
                if (vertex.getClusterId() == child.getVertex().getClusterId()
                        && availableNodes.stream().anyMatch(n -> n.getId() == vertex.getNodeMetadata().getId())) {
                    TreeNode toAdd = new TreeNode(vertex);
                    child.getChildren().add(toAdd);
                    spanningTree.getNodeList().add(toAdd);
                    availableNodes.removeIf(n -> n.getId() == toAdd.getVertex().getNodeMetadata().getId());
                }
            }
        }
    }
}
