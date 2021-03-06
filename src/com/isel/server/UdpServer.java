package com.isel.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UdpServer implements IServer {

    private int udpPort;
    private boolean running = true;

    public UdpServer(int serverPort){
        this.udpPort = serverPort;
    }

    @Override
    public void Start() {
        DatagramSocket  udpSocket;
        try {
            udpSocket = new DatagramSocket(this.udpPort);
            System.out.println("UDP server is running on port:" + this.udpPort);
            //Listen to new connections
            Listener(udpSocket);
            //Closes the connection
            udpSocket.close();
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

    private void Listener(DatagramSocket  udpSocket) {

        try {
            String convertedString = "";
            do {
                byte[] receiveData = new byte[512];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocket.receive(receivePacket);

                String recvString = new String( receivePacket.getData());
                convertedString = recvString.trim().toUpperCase();
                System.out.println("Original:" + recvString + "\nConverted:" + convertedString);

                byte[] sendData = convertedString.getBytes();

                //Get details from the client.
                InetAddress sourceIp = receivePacket.getAddress();
                int port = receivePacket.getPort();
                DatagramPacket sendPacket =  new DatagramPacket(sendData, sendData.length, sourceIp, port);
                udpSocket.send(sendPacket);

            }while (convertedString.equals("STOP") == false && convertedString.equals("END") == false && running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

}
