package ripple.engine.entity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxiao.tz
 */
public class Cluster {
    private UUID uuid;
    private ConcurrentHashMap<UUID, Node> nodes;

    public UUID getUuid() {
        return uuid;
    }

    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ConcurrentHashMap<UUID, Node> getNodes() {
        return nodes;
    }

    private void setNodes(ConcurrentHashMap<UUID, Node> nodes) {
        this.nodes = nodes;
    }

    public Cluster(UUID uuid) {
        this.setUuid(uuid);
        this.setNodes(new ConcurrentHashMap<>());
    }
}
