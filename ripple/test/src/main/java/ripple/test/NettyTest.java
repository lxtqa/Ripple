package ripple.test;

import ripple.server.tcp.NettyServer;

public class NettyTest {
    public static void main(String[] args) throws Exception {
        NettyServer nettyServer = new NettyServer(9999);
        nettyServer.start();
        System.out.println("Server started on port: " + nettyServer.getPort());
        NettyServer nettyServer1 = new NettyServer(8888);
        nettyServer1.start();
        nettyServer1.connect("127.0.0.1", 9999);
        System.in.read();
        nettyServer.stop();
    }
}
