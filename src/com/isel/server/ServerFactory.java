package com.isel.server;

public class ServerFactory {

    //I am gonna store all the ports here just for aggregate the port definitions.
    private static int TcpPort = 11000;
    private static int UdpPort = 11001;

    public static IServer CreateServer (EServerType serverType) {

        IServer server = null;
        switch (serverType) {
            case TCP:
                server = new TcpServer(TcpPort);
                break;
            case TCPNONBLOCK:
                server = new TcpNonBlockServer(TcpPort);
                break;
            case UDP:
                server = new UdpServer(UdpPort);
                break;
        }

        return server;
    }

}
