package com.isel.server;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TcpServer  implements IServer {

    private int tcpPort;
    private boolean running = true;
    private ServerSocket tcpSocket;

    public TcpServer(int serverPort){
        this.tcpPort = serverPort;
    }

    @Override
    public void Start() {

        try {
            this.tcpSocket = new ServerSocket(tcpPort);
            System.out.println("Tcp server is running on port:" + this.tcpPort);
            //Listen to new connections
            Listener();
        }
        catch (Exception excp){
            excp.printStackTrace();
            return;
        }
    }

    @Override
    public void Stop() {
        System.out.println("Tcp server is terminating");

        this.running = false;
        try {
            //Closes the connection
            this.tcpSocket.close();

        }catch (Exception excp){
            //We are closing, no need
            //excp.printStackTrace();
        }
    }

    private void Listener() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            do {
                //Accept any connection. by default its a blocking call until we have a new client
                Socket clientSocket = this.tcpSocket.accept();
                System.out.println("New client with the IP: " + clientSocket.getInetAddress().getHostAddress());
                //Submit the task to a new thread.
                threadPool.submit(new TaskWorker(clientSocket, this));

            }while (this.running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }
}
