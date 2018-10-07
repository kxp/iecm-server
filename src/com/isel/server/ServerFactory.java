package com.isel.server;

public class ServerFactory {
    public static IServer CreateServer (EServerType serverType) {

        IServer server = null;

        switch (serverType) {
            case NOTSPECIFIED:
                break;
            case TCP:
                break;
            case UDP:
                break;
            case MULTICAST:
                break;
        }

        return server;
    }

}
