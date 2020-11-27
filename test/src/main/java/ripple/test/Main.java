package ripple.test;

import ripple.client.RippleClient;
import ripple.client.core.Item;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;
import ripple.server.core.NodeType;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RippleServer nodeOne = new RippleServer(NodeType.STAR, 1, "D:\\server-1.txt");
        RippleServer nodeTwo = new RippleServer(NodeType.STAR, 2, "D:\\server-2.txt");
        nodeOne.start();
        System.out.println("Node One: " + nodeOne.getAddress() + ":" + nodeOne.getPort());
        nodeTwo.start();
        System.out.println("Node Two: " + nodeTwo.getAddress() + ":" + nodeTwo.getPort());
        List<NodeMetadata> nodeList = new ArrayList<>();
        nodeList.add(new NodeMetadata(nodeOne.getId(), nodeOne.getAddress(), nodeOne.getPort()));
        nodeList.add(new NodeMetadata(nodeTwo.getId(), nodeTwo.getAddress(), nodeTwo.getPort()));
        nodeOne.setNodeList(nodeList);
        nodeTwo.setNodeList(nodeList);

        RippleClient clientOne = new RippleClient(nodeOne.getAddress(), nodeOne.getPort(), "D:\\client-1.txt");
        clientOne.start();
        clientOne.subscribe("testApp", "test");
        RippleClient clientTwo = new RippleClient(nodeTwo.getAddress(), nodeTwo.getPort(), "D:\\client-2.txt");
        clientTwo.start();
        clientTwo.subscribe("testApp", "test");

        clientOne.put("testApp", "test", "test");
        System.out.println("Setting testApp.test = test by Client 1.");
        Item item = clientTwo.get("testApp", "test");
        System.out.println("[Client 2] " + item.getApplicationName() + "." + item.getKey() + " = " + item.getValue());

        clientTwo.put("testApp", "test", "newTest");
        System.out.println("Setting testApp.test = newTest by Client 2.");
        item = clientOne.get("testApp", "test");
        System.out.println("[Client 1] " + item.getApplicationName() + "." + item.getKey() + " = " + item.getValue());

        clientTwo.put("testApp", "test", "newTest1");
        System.out.println("Setting testApp.test = newTest1 by Client 1.");
        item = clientOne.get("testApp", "test");
        System.out.println("[Client 1] " + item.getApplicationName() + "." + item.getKey() + " = " + item.getValue());
        item = clientTwo.get("testApp", "test");
        System.out.println("[Client 2] " + item.getApplicationName() + "." + item.getKey() + " = " + item.getValue());

        clientOne.unsubscribe("testApp", "test");
        clientTwo.unsubscribe("testApp", "test");
        clientOne.stop();
        clientTwo.stop();
        nodeOne.stop();
        nodeTwo.stop();
    }
}
