package cuit.pymjl;


import cuit.pymjl.transport.NettyClient;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 18:25
 **/
public class ClientMain {
    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient("127.0.0.1", 12345);
        client.start();
        client.sendData();
    }

}
