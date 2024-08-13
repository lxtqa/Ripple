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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.NodeMetadata;
import ripple.server.core.overlay.Overlay;
import ripple.server.core.overlay.tree.CompleteTree;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class ExpanderOverlay implements Overlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpanderOverlay.class);
    private int scale;
    private List<NodeMetadata> nodeList;
    private int clusterCount;
    private List<Vertex> vertexList;
    private ConcurrentHashMap<NodeMetadata, SpanningTree> trees;

    public ExpanderOverlay(int scale) {
        this.setScale(scale);
        this.setTrees(new ConcurrentHashMap<>());
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }

    public int getClusterCount() {
        return clusterCount;
    }

    public void setClusterCount(int clusterCount) {
        this.clusterCount = clusterCount;
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    public ConcurrentHashMap<NodeMetadata, SpanningTree> getTrees() {
        return trees;
    }

    public void setTrees(ConcurrentHashMap<NodeMetadata, SpanningTree> trees) {
        this.trees = trees;
    }

    @Override
    public void buildOverlay(List<NodeMetadata> nodeList) {
        this.setNodeList(nodeList);
        this.assignCluster();
        this.addFullConnectionsInsideCluster();
        this.assignEdges();
        this.buildSpanningTree();
    }

    private void buildSpanningTree() {
        for (NodeMetadata nodeMetadata : this.getNodeList()) {
            Optional<Vertex> optional = this.getVertexList().stream()
                    .filter(v -> v.getNodeMetadata().getId() == nodeMetadata.getId()).findFirst();
            if (optional.isPresent()) {
                TreeNode treeNode = new TreeNode(optional.get());
                SpanningTree spanningTree = new SpanningTree(treeNode);
                this.getTrees().put(nodeMetadata, spanningTree);
                List<NodeMetadata> availableNodes = new ArrayList<>(this.getNodeList());
                // this.bfsBuildOverlay(spanningTree, availableNodes);
                this.threeLayerBuildOverlay(spanningTree, availableNodes);
            }
        }
    }

    private void threeLayerBuildOverlay(SpanningTree spanningTree, List<NodeMetadata> availableNodes) {
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

    private void bfsBuildOverlay(SpanningTree spanningTree, List<NodeMetadata> availableNodes) {
        // Using BFS
        Queue<TreeNode> nodes = new ArrayBlockingQueue<>(this.getNodeList().size());
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

    private void assignCluster() {
        int clusterCount = (int) Math.ceil((double) this.getNodeList().size() / this.getScale());
        LOGGER.info("[ExpanderOverlay] Count of clusters: {}", clusterCount);
        List<Vertex> vertexList = new ArrayList<>();
        int i = 0;
        for (i = 0; i < clusterCount; i++) {
            int j = 0;
            for (j = 0; j < this.getScale(); j++) {
                int index = i * this.getScale() + j;
                if (index >= this.getNodeList().size()) {
                    break;
                }
                Vertex vertex = new Vertex(i + 1, this.getNodeList().get(index));
                vertexList.add(vertex);
            }
        }
        this.setClusterCount(clusterCount);
        this.setVertexList(vertexList);
    }

    private void addNeighbour(Vertex one, Vertex other) {
        this.doAddNeighbourIfNotExist(one, other);
        this.doAddNeighbourIfNotExist(other, one);
    }

    private void doAddNeighbourIfNotExist(Vertex one, Vertex other) {
        boolean found = false;
        for (Vertex neighbour : one.getNeighbours()) {
            if (neighbour.getNodeMetadata().getId() == other.getNodeMetadata().getId()) {
                found = true;
                break;
            }
        }
        if (!found) {
            one.getNeighbours().add(other);
        }
    }

    private void addFullConnectionsInsideCluster() {
        for (Vertex one : this.getVertexList()) {
            for (Vertex other : this.getVertexList()) {
                if (one.getClusterId() == other.getClusterId()
                        && one.getNodeMetadata().getId() != other.getNodeMetadata().getId()) {
                    addNeighbour(one, other);
                }
            }
        }
    }

    private void assignEdges() {
        // Add edges between cluster
        int i = 0;
        int j = 0;
        for (i = 0; i < this.getClusterCount(); i++) {
            for (j = i + 1; j < this.getClusterCount(); j++) {
                // Select vertex with minimal id and degree
                Vertex one = this.selectVertexInCluster(i + 1);
                Vertex other = this.selectVertexInCluster(j + 1);
                this.addNeighbour(one, other);
                LOGGER.info("[ExpanderOverlay] Edge between cluster {} and {} is (Node {} <--> Node {})."
                        , i + 1, j + 1, one.getNodeMetadata().getId(), other.getNodeMetadata().getId());
            }
        }
    }

    private Vertex selectVertexInCluster(int clusterId) {
        Vertex selected = null;
        int minId = Integer.MAX_VALUE;
        int minDegree = Integer.MAX_VALUE;
        int k = 0;
        for (Vertex vertex : this.getVertexList()) {
            if (vertex.getClusterId() == clusterId) {
                if ((vertex.getNeighbours().size() < minDegree)
                        || (vertex.getNeighbours().size() == minDegree && vertex.getNodeMetadata().getId() < minId)) {
                    selected = vertex;
                    minId = selected.getNodeMetadata().getId();
                    minDegree = selected.getNeighbours().size();
                }
            }
        }
        return selected;
    }

    @Override
    public List<NodeMetadata> calculateNodesToSync(AbstractMessage message, NodeMetadata source, NodeMetadata from, NodeMetadata current) {
        LOGGER.info("[ExpanderOverlay] calculateNodesToSync() called. sourceId = {}, currentId = {}", source.getId(), current.getId());
        SpanningTree tree = this.getTrees().get(source);
        for (TreeNode treeNode : tree.getNodeList()) {
            if (treeNode.getVertex().getNodeMetadata().equals(current)) {
                return this.generateNodeList(treeNode.getChildren());
            }
        }
        LOGGER.warn("[ExpanderOverlay] I cannot find children list for node {}.", current.getId());
        return new ArrayList<>();
    }

    public List<NodeMetadata> generateNodeList(List<TreeNode> treeNodeList) {
        List<NodeMetadata> ret = new ArrayList<>();
        int i = 0;
        for (i = 0; i < treeNodeList.size(); i++) {
            NodeMetadata nodeMetadata = treeNodeList.get(i).getVertex().getNodeMetadata();
            LOGGER.info("[ExpanderOverlay] Attempting to send to node {} ({}:{})."
                    , nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
            ret.add(nodeMetadata);
        }
        return ret;
    }

    @Override
    public List<NodeMetadata> calculateNodesToCollectAck(AbstractMessage message) {
        return this.getNodeList();
    }
}
