package cuit.pymjl;


import cuit.pymjl.transport.NettyServer;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 19:39
 **/
public class ServerMain {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(12345);
        server.run();
    }
}
