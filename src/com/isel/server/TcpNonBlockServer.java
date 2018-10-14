package com.isel.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TcpNonBlockServer implements IServer {

    private int tcpPort;
    private boolean running = true;

    public TcpNonBlockServer(int serverPort){
        this.tcpPort = serverPort;
    }

    @Override
    public void Start() {

        ServerSocket tcpSocket;
        try {
            tcpSocket = new ServerSocket(tcpPort);
            ServerSocketChannel serverChannel = tcpSocket.getChannel();
            //Sets the socket as a non blocking.
            serverChannel.configureBlocking(false);

            System.out.println("Tcp Non blocking server is running on port:" + this.tcpPort);
            //Listen to new connections
            Listener(tcpSocket);
            //Closes the connection
            tcpSocket.close();
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
    }

    private void Listener(ServerSocket tcpSocket) {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            do {
                //Accept any connection. by default its a blocking call until we have a new client
                Socket clientSocket = tcpSocket.accept();
                System.out.println("New client with the IP: " + clientSocket.getInetAddress().getHostAddress());
                //Submit the task to a new thread.
                threadPool.submit(new TaskWorker(clientSocket));

            }while (this.running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }



}
