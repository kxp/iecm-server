package com.isel.server;

public class UdpServer implements IServer {


    private int udpPort;
    private boolean running = true;

    public UdpServer(int serverPort){
        this.udpPort = serverPort;
    }


    @Override
    public void Start() {
        System.out.println("UDP server is running on port:" + this.udpPort);

    }

    @Override
    public void Stop() {
        System.out.println("Tcp server is terminating");
        this.running = false;
    }
}
