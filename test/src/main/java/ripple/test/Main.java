package ripple.test;

import ripple.server.NodeMetadata;
import ripple.server.protocol.star.StarNode;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StarNode nodeOne = new StarNode(1);
        StarNode nodeTwo = new StarNode(2);
        nodeOne.start();
        System.out.println(nodeOne.getAddress() + ":" + nodeOne.getPort());
        nodeTwo.start();
        System.out.println(nodeTwo.getAddress() + ":" + nodeTwo.getPort());
        List<NodeMetadata> nodeList = new ArrayList<>();
        nodeList.add(new NodeMetadata(nodeOne.getId(), nodeOne.getAddress(), nodeOne.getPort()));
        nodeList.add(new NodeMetadata(nodeTwo.getId(), nodeTwo.getAddress(), nodeTwo.getPort()));
        nodeOne.setNodeList(nodeList);
        nodeTwo.setNodeList(nodeList);
    }
}
