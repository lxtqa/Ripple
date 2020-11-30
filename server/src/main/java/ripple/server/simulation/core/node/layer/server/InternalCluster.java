package ripple.server.simulation.core.node.layer.server;

import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.utils.Util;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author qingzhou.sjq
 */
public class InternalCluster {

    private Queue<LayerPeer> peerList = new PriorityQueue<>(new NodeComparator());

    public int id;


    public void addNode(LayerPeer node) {
        peerList.add(node);
    }

    public Queue<LayerPeer> getPeerList() {
        return peerList;
    }

    public LayerPeer getAndRemoveAvailablePeer() {
        return peerList.poll();
    }

    class NodeComparator implements Comparator<LayerPeer> {

        @Override
        public int compare(LayerPeer o1, LayerPeer o2) {
            if (o1.degree > o2.degree) {
                return 1;
            } else if (o1.degree < o2.degree) {
                return -1;
            } else {
                return o1.uuid.compareTo(o2.uuid);
            }
        }
    }
}
