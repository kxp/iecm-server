package com.isel.server;

public class ServerFactory {

    //I am gonna store all the ports here just for agregate the port defenitions.
    private static int TcpPort = 11000;
    private static int UdpPort = 11001;
    private static int TcpNonBlockingPort = 11003;

    public static IServer CreateServer (EServerType serverType) {

        IServer server = null;

        switch (serverType) {
            case TCP:
                server = new TcpServer(TcpPort);
                break;
            case TCPNONBLOCK:
                server = new TcpNonBlockServer(TcpNonBlockingPort);
                break;
            case UDP:
                server = new UdpServer(UdpPort);
                break;
        }

        return server;
    }

}
