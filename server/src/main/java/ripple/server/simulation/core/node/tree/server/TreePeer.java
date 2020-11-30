package ripple.server.simulation.core.node.tree.server;

import ripple.server.simulation.core.node.AbstractPeer;

import java.util.UUID;

/**
 * @author qingzhou.sjq
 */
public class TreePeer extends AbstractPeer{

    /**
     *  N叉树下标
     */
    public int index;

    public TreePeer(String ip, UUID uuid) {
        this.ip = ip;
        this.uuid = uuid;
    }
}
