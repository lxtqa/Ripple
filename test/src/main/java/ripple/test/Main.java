package ripple.test;

import ripple.server.star.StarNode;

public class Main {
    public static void main(String[] args) {
        StarNode starNode = new StarNode(1);
        starNode.start();
        System.out.println(starNode.getPort());
    }
}
