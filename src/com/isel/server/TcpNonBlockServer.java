package com.isel.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TcpNonBlockServer implements IServer {

    private int tcpPort;
    private boolean running = true;
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket;


    public TcpNonBlockServer(int serverPort){
        this.tcpPort = serverPort;
    }

    @Override
    public void Start() {

        //ServerSocket tcpSocket;
        try {
            Initialize();
            //tcpSocket = new ServerSocket(tcpPort);
            //ServerSocketChannel serverChannel = tcpSocket.getChannel();
            //Sets the socket as a non blocking.
            //serverChannel.configureBlocking(false);

            System.out.println("Tcp Non blocking server is running on port:" + this.tcpPort);
            //Listen to new connections
            Listener();
            //Closes the connection
            //tcpSocket.close();
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

    private void Listener() {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            do {
                //Accept any connection. by default its a blocking call until we have a new client
                SocketChannel clientSocket = this.serverSocketChannel.accept();

                //Setting to non-blocking socket
                clientSocket.configureBlocking(false);
                Socket socket = clientSocket.socket();
                System.out.println("Connection with " + socket + " has been established.\n");

                this.serverSocketChannel.r
                //socketChannel.register(selector, SelectionKey.OP_READ);
                //selector.selectedKeys().remove(key);

                //System.out.println("New client with the IP: " + clientSocket.getInetAddress().getHostAddress());
                //Submit the task to a new thread.
                //threadPool.submit(new TaskWorker(clientSocket));

            }while (this.running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

    private void Initialize() throws Exception {
        try {
            System.out.println("Server Starting...\n");

            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            serverSocket = serverSocketChannel.socket();
            InetSocketAddress netAddress = new InetSocketAddress(this.tcpPort);
            serverSocket.bind(netAddress);
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }


}
