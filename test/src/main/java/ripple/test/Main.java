package ripple.test;

import ripple.client.RippleClient;
import ripple.client.entity.Item;
import ripple.server.NodeMetadata;
import ripple.server.protocol.star.StarNode;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StarNode nodeOne = new StarNode(1);
        StarNode nodeTwo = new StarNode(2);
        nodeOne.start();
        System.out.println("Node One: " + nodeOne.getAddress() + ":" + nodeOne.getPort());
        nodeTwo.start();
        System.out.println("Node Two: " + nodeTwo.getAddress() + ":" + nodeTwo.getPort());
        List<NodeMetadata> nodeList = new ArrayList<>();
        nodeList.add(new NodeMetadata(nodeOne.getId(), nodeOne.getAddress(), nodeOne.getPort()));
        nodeList.add(new NodeMetadata(nodeTwo.getId(), nodeTwo.getAddress(), nodeTwo.getPort()));
        nodeOne.setNodeList(nodeList);
        nodeTwo.setNodeList(nodeList);

        RippleClient clientOne = new RippleClient(nodeOne.getAddress(), nodeOne.getPort());
        clientOne.startCallback();
        clientOne.put("test", "test");
        RippleClient clientTwo = new RippleClient(nodeTwo.getAddress(), nodeTwo.getPort());
        clientTwo.startCallback();
        Item item = clientTwo.get("test");
        System.out.println(item.getKey() + " = " + item.getValue());

        clientTwo.put("test", "newTest");
        item = clientOne.get("test");
        System.out.println(item.getKey() + " = " + item.getValue());

        nodeOne.stop();
        nodeTwo.stop();
        clientOne.stopCallback();
        clientTwo.stopCallback();
    }
}
