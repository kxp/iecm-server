package com.isel.server;

import java.net.*;
import java.io.*;

public final class TcpServer  implements IServer {

    private int tcpPort;
    private boolean running = true;

    public TcpServer(int serverPort){
        this.tcpPort = serverPort;
    }

    @Override
    public void Start() {

        ServerSocket tcpSocket;
        try {
            tcpSocket = new ServerSocket(tcpPort);
        }
        catch (Exception excp){
            excp.printStackTrace();
            return;
        }
        System.out.println("Tcp server is running on port:" + this.tcpPort);

        Listener(tcpSocket);
    }

    @Override
    public void Stop() {
        System.out.println("Tcp server is terminating");
        this.running = false;
    }

    private void Listener(ServerSocket tcpSocket) {


        try {
            do {
                //Accept any connection. by default its a blocking call until we have a new client
                Socket clientSocket = tcpSocket.accept();
                System.out.println("New client with the IP: " + clientSocket.getInetAddress().getHostAddress());

                //Does the work
                Worker(clientSocket);

            }while (this.running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

    //Does the work of reading the socket and replying it.
    private void Worker(Socket socket) throws Exception {

        String sourceString;
        String convertedString;
        try {
            //Create the input and output buffers.
            //BufferedReader requestStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream responseStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inFromClient = new DataInputStream(socket.getInputStream());

            //reads the text and converts it to upper case.
            //sourceString = requestStream.readLine();
            sourceString = inFromClient.readUTF();
            convertedString = sourceString.toUpperCase();// + 'n';

            System.out.println("Original:" + sourceString + "\nConverted:" + convertedString);
            responseStream.writeChars(convertedString);
        }
        catch (Exception excp){
            //let it be handle by the caller.
            throw excp;
        }

        if (convertedString.equals("EXIT") )
            Stop();
    }


}
