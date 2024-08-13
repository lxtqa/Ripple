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
import ripple.server.core.overlay.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
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
    private ConcurrentHashMap<NodeMetadata, SpanningTreeNode> trees;

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

    public ConcurrentHashMap<NodeMetadata, SpanningTreeNode> getTrees() {
        return trees;
    }

    public void setTrees(ConcurrentHashMap<NodeMetadata, SpanningTreeNode> trees) {
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
            SpanningTreeNode treeNode = new SpanningTreeNode(nodeMetadata);
            this.getTrees().put(nodeMetadata, treeNode);
            List<NodeMetadata> availableNodes = new ArrayList<>(this.getNodeList());
            this.addChildren(treeNode, availableNodes);
        }
    }

    private void addChildren(SpanningTreeNode treeNode, List<NodeMetadata> availableNodes) {
        // Using BFS
        Queue<SpanningTreeNode> nodes = new ArrayBlockingQueue<>(this.getNodeList().size());
        nodes.offer(treeNode);
        while (!nodes.isEmpty()) {
            SpanningTreeNode node = nodes.poll();
            this.doAddChildren(node, availableNodes);
            for (SpanningTreeNode elem : node.getChildren()) {
                nodes.offer(elem);
            }
        }
    }

    private void doAddChildren(SpanningTreeNode treeNode, List<NodeMetadata> availableNodes) {
        availableNodes.removeIf(n -> n.getId() == treeNode.getNodeMetadata().getId());
        Optional<Vertex> optional = this.getVertexList().stream()
                .filter(v -> v.getNodeMetadata().getId() == treeNode.getNodeMetadata().getId()).findFirst();
        if (optional.isPresent()) {
            Vertex vertex = optional.get();
            for (NodeMetadata node : vertex.getNeighbours()) {
                if (availableNodes.stream().anyMatch(n -> n.getId() == node.getId())) {
                    SpanningTreeNode toAdd = new SpanningTreeNode(node);
                    treeNode.getChildren().add(toAdd);
                    availableNodes.removeIf(n -> n.getId() == toAdd.getNodeMetadata().getId());
                }
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
        for (Vertex vertex : vertexList) {
            LOGGER.info("[ExpanderOverlay] node id: {}, cluster id: {}.", vertex.getNodeMetadata().getId(), vertex.getClusterId());
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
        for (NodeMetadata neighbour : one.getNeighbours()) {
            if (neighbour.getId() == other.getNodeMetadata().getId()) {
                found = true;
                break;
            }
        }
        if (!found) {
            one.getNeighbours().add(other.getNodeMetadata());
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
        // TODO
        return null;
    }

    @Override
    public List<NodeMetadata> calculateNodesToCollectAck(AbstractMessage message) {
        return this.getNodeList();
    }
}
