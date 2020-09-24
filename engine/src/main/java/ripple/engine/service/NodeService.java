package ripple.engine.service;

import ripple.engine.entity.Agent;
import ripple.engine.entity.Cluster;
import ripple.engine.entity.Context;
import ripple.engine.entity.Message;
import ripple.engine.entity.Node;
import ripple.engine.helper.AgentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxiao.tz
 */
@Component
public class NodeService {
    private RegistryService registryService;
    private Context context;
    private AgentHelper agentHelper;

    private RegistryService getRegistryService() {
        return registryService;
    }

    @Autowired
    private void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    private Context getContext() {
        return context;
    }

    @Autowired
    private void setContext(Context context) {
        this.context = context;
    }

    private AgentHelper getAgentHelper() {
        return agentHelper;
    }

    @Autowired
    private void setAgentHelper(AgentHelper agentHelper) {
        this.agentHelper = agentHelper;
    }

    public List<Node> createCluster(UUID clusterUuid, String nodeType, int nodeCount) {
        Cluster cluster = new Cluster(clusterUuid);
        this.getContext().getClusters().put(clusterUuid, cluster);
        List<Agent> agentList = this.getRegistryService().getAgentList();
        int averageNodesPerAgent = (int) Math.ceil((nodeCount + 0.0) / agentList.size());
        int i;
        int remaining = nodeCount;
        for (i = 0; i < agentList.size(); i++) {
            if (remaining == 0) {
                break;
            }
            int toAllocate = remaining < averageNodesPerAgent ? remaining : averageNodesPerAgent;
            remaining -= toAllocate;
            Agent agent = agentList.get(i);
            List<Node> nodesOnAgent = this.getAgentHelper().createCluster(agent, clusterUuid, nodeType, toAllocate);
            for (Node node : nodesOnAgent) {
                cluster.getNodes().put(node.getUuid(), node);
            }
        }
        List<Node> nodeList = Collections.list(cluster.getNodes().elements());
        for (Agent agent : agentList) {
            this.getAgentHelper().updateCluster(agent, clusterUuid, nodeList);
        }
        return nodeList;
    }

    public boolean removeCluster(UUID clusterUuid) {
        Cluster cluster = this.getContext().getClusters().get(clusterUuid);
        ConcurrentHashMap<UUID, Agent> agentMap = new ConcurrentHashMap<>(16);
        for (Node node : cluster.getNodes().values()) {
            if (!agentMap.containsKey(node.getAgentUuid())) {
                agentMap.put(node.getAgentUuid(), this.getContext().getAgents().get(node.getAgentUuid()));
            }
        }
        for (Agent agent : agentMap.values()) {
            this.getAgentHelper().removeCluster(agent, clusterUuid);
        }
        cluster.getNodes().clear();
        this.getContext().getClusters().remove(clusterUuid);
        System.gc();
        return true;
    }

    public List<Message> gatherMessages(UUID clusterUuid) {
        Cluster cluster = this.getContext().getClusters().get(clusterUuid);
        ConcurrentHashMap<UUID, Agent> agentMap = new ConcurrentHashMap<>(16);
        for (Node node : cluster.getNodes().values()) {
            if (!agentMap.containsKey(node.getAgentUuid())) {
                agentMap.put(node.getAgentUuid(), this.getContext().getAgents().get(node.getAgentUuid()));
            }
        }
        List<Message> list = new ArrayList<>();
        for (Agent agent : agentMap.values()) {
            List<Message> agentMessageList = this.getAgentHelper().gatherMessage(agent, clusterUuid);
            list.addAll(agentMessageList);
        }
        return list;
    }
}
