package com.isel.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TcpNonBlockServer implements IServer {

    private int tcpPort;
    private boolean running = true;
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket;
    private Selector selector;
    private ExecutorService threadPool;


    public TcpNonBlockServer(int serverPort){
        this.tcpPort = serverPort;
    }

    @Override
    public void Start() {
        try {
            //initializes the server socket
            Initialize();

            //Listen to new connections
            Listener();
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

    @Override
    public void Stop() {
        System.out.println("Tcp Non blocking server is terminating");
        this.running = false;
        try {
            if(this.serverSocket != null && this.serverSocket.isClosed() == false){
                this.serverSocket.close();
            }

            if (this.serverSocketChannel != null) {
                serverSocketChannel.close();
            }

            this.selector.close();
        }catch (Exception excp){
            excp.printStackTrace();
        }
    }

    private void Listener() {
        try {
            do {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {

                    SelectionKey key = iter.next();
                    //Calls register to accept the call
                    if (key.isAcceptable()) {
                        Register();
                    }
                    iter.remove();
                }
            }while (this.running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

    private void Register()
            throws IOException {

        SocketChannel client = this.serverSocketChannel.accept();
        TaskWorker dispatcher = new TaskWorker(client.socket(), this);
        this.threadPool.submit(dispatcher);
    }

    private void Initialize() {
        try {
            this.selector = Selector.open();

            this.serverSocketChannel = ServerSocketChannel.open();
            //Setting to non-blocking socket
            this.serverSocketChannel.configureBlocking(false);

            this.serverSocket = serverSocketChannel.socket();
            InetSocketAddress netAddress = new InetSocketAddress(this.tcpPort);
            this.serverSocket.bind(netAddress);

            //unblock with accept
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //instanciates the threadpool to handle replies in a async way
            this.threadPool = Executors.newCachedThreadPool();
            System.out.println("The Server Starting in:" + netAddress.toString());
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }
}
