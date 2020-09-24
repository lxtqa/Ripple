package ripple.agent.core.node;

import ripple.agent.core.Cluster;
import ripple.agent.core.Message;
import ripple.agent.core.node.star.StarNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxiao.tz
 */
@Component
public class NodeManager {
    private static final Logger logger = LoggerFactory.getLogger(NodeManager.class);

    private ConcurrentHashMap<UUID, Cluster> clusters;

    public ConcurrentHashMap<UUID, Cluster> getClusters() {
        return clusters;
    }

    private void setClusters(ConcurrentHashMap<UUID, Cluster> clusters) {
        this.clusters = clusters;
    }

    public NodeManager() {
        this.setClusters(new ConcurrentHashMap<>());
    }

    private AbstractNode createNode(UUID clusterUuid, String type, Cluster cluster) {
        if (NodeType.STAR.equals(type)) {
            return new StarNode(clusterUuid, cluster);
        }

        return new StarNode(clusterUuid, cluster);
    }

    public List<AbstractNode> createCluster(UUID clusterUuid, String nodeType, int nodeCount) {
        this.getClusters().put(clusterUuid, new Cluster(clusterUuid));
        Cluster cluster = this.getClusters().get(clusterUuid);
        int i;
        for (i = 0; i < nodeCount; i++) {
            AbstractNode node = this.createNode(clusterUuid, nodeType, cluster);
            cluster.getLocalNodes().put(node.getMetadata().getUuid(), node);
            cluster.getAllNodes().put(node.getMetadata().getUuid(), node.getMetadata());
        }
        return Collections.list(cluster.getLocalNodes().elements());
    }

    public boolean updateCluster(UUID clusterUuid, List<NodeMetadata> nodeList) {
        Cluster cluster = this.getClusters().get(clusterUuid);
        for (NodeMetadata nodeMetadata : nodeList) {
            cluster.getAllNodes().put(nodeMetadata.getUuid(), nodeMetadata);
        }
        return true;
    }

    public boolean removeCluster(UUID clusterUuid) {
        Cluster cluster = this.getClusters().get(clusterUuid);
        for (AbstractNode localNode : cluster.getLocalNodes().values()) {
            localNode.stop();
        }
        cluster.getLocalNodes().clear();
        cluster.getAllNodes().clear();
        cluster.getMessageList().clear();
        this.getClusters().remove(clusterUuid);
        System.gc();
        return false;
    }

    public List<Message> getMessages(UUID clusterUuid) {
        return this.getClusters().get(clusterUuid).getMessageList();
    }
}
