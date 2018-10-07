package com.isel.server;

public class MulticastServer implements IServer {


    private int multicastPort;
    private boolean running = true;

    public MulticastServer(int serverPort){
        this.multicastPort = serverPort;
    }


    @Override
    public void Start() {
        System.out.println("Multicast server is running on port:" + this.multicastPort);

    }

    @Override
    public void Stop() {
        System.out.println("Tcp server is terminating");
        this.running = false;
    }
}
