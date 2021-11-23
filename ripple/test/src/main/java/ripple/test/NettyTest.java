package ripple.test;

import ripple.server.tcp.NettyServer;

public class NettyTest {
    public static void main(String[] args) throws Exception {
        NettyServer nettyServer = new NettyServer(0);
        nettyServer.start();
        System.out.println("Server started on port: " + nettyServer.getPort());
        System.in.read();
        nettyServer.stop();
    }
}
