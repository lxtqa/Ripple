package ripple.agent.core;

import ripple.agent.core.node.AbstractNode;
import ripple.agent.core.node.NodeMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxiao.tz
 */
public class Cluster {
    private UUID clusterUuid;
    private ConcurrentHashMap<UUID, AbstractNode> localNodes;
    private ConcurrentHashMap<UUID, NodeMetadata> allNodes;
    private List<Message> messageList;

    public UUID getClusterUuid() {
        return clusterUuid;
    }

    private void setClusterUuid(UUID clusterUuid) {
        this.clusterUuid = clusterUuid;
    }

    public ConcurrentHashMap<UUID, AbstractNode> getLocalNodes() {
        return localNodes;
    }

    private void setLocalNodes(ConcurrentHashMap<UUID, AbstractNode> localNodes) {
        this.localNodes = localNodes;
    }

    public ConcurrentHashMap<UUID, NodeMetadata> getAllNodes() {
        return allNodes;
    }

    private void setAllNodes(ConcurrentHashMap<UUID, NodeMetadata> allNodes) {
        this.allNodes = allNodes;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    private void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public Cluster(UUID clusterUuid) {
        this.setClusterUuid(clusterUuid);
        this.setLocalNodes(new ConcurrentHashMap<>());
        this.setAllNodes(new ConcurrentHashMap<>());
        this.setMessageList(Collections.synchronizedList(new ArrayList<>()));
    }
}
