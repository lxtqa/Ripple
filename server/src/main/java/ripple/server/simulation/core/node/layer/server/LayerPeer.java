package ripple.server.simulation.core.node.layer.server;

import ripple.server.simulation.core.node.AbstractPeer;

import java.util.UUID;

/**
 * @author qingzhou.sjq
 */
public class LayerPeer extends AbstractPeer{

    /**
     * degree是指外部连接边的个数，目前只在小集群列表里维护
     */
    public int degree = 0;

    public int clusterId;

    public LayerPeer(String ip, UUID uuid) {
        this.ip = ip;
        this.uuid = uuid;
    }
}
