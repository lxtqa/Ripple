package ripple.server.simulation.core;

import ripple.server.simulation.core.node.AbstractNode;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxiao.tz
 */
@Component
public class Context {
    private ConcurrentHashMap<UUID, AbstractNode> serverNodes;
    private ConcurrentHashMap<UUID, AbstractNode> clientNodes;

    public ConcurrentHashMap<UUID, AbstractNode> getServerNodes() {
        return serverNodes;
    }

    private void setServerNodes(ConcurrentHashMap<UUID, AbstractNode> serverNodes) {
        this.serverNodes = serverNodes;
    }

    public ConcurrentHashMap<UUID, AbstractNode> getClientNodes() {
        return clientNodes;
    }

    private void setClientNodes(ConcurrentHashMap<UUID, AbstractNode> clientNodes) {
        this.clientNodes = clientNodes;
    }

    public Context() {
        this.setServerNodes(new ConcurrentHashMap<>());
        this.setClientNodes(new ConcurrentHashMap<>());
    }
}
