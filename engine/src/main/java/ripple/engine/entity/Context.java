package ripple.engine.entity;

/**
 * @author fuxiao.tz
 */

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Context {
    private ConcurrentHashMap<UUID, Agent> agents;
    private ConcurrentHashMap<UUID, Cluster> clusters;

    public ConcurrentHashMap<UUID, Agent> getAgents() {
        return agents;
    }

    private void setAgents(ConcurrentHashMap<UUID, Agent> agents) {
        this.agents = agents;
    }

    public ConcurrentHashMap<UUID, Cluster> getClusters() {
        return clusters;
    }

    private void setClusters(ConcurrentHashMap<UUID, Cluster> clusters) {
        this.clusters = clusters;
    }

    public Context() {
        this.setAgents(new ConcurrentHashMap<>());
        this.setClusters(new ConcurrentHashMap<>());
    }
}
