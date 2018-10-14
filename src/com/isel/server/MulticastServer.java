package com.isel.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class MulticastServer implements IServer {

    private String multicastIp = "224.0.0.3";
    private int multicastPort;

    private boolean running = true;
    private Map<String, String> registeredUsers;
    private InetAddress multicastNetwork;

    public MulticastServer(int serverPort){
        this.multicastPort = serverPort;
    }


    @Override
    public void Start() {
        MulticastSocket multicastSocket;
        this.registeredUsers = new HashMap<String, String>();
        try {
            //Creates the socket and joins the group.
            multicastSocket = new MulticastSocket (this.multicastPort);
            this.multicastNetwork = InetAddress.getByName(multicastIp);
            multicastSocket.joinGroup(this.multicastNetwork);

            System.out.println("Multicast server is running on: " + this.multicastIp + ":" + this.multicastPort);

            //Listen to new connections
            Listener(multicastSocket);
            //Leaves the group and closes the connection.
            multicastSocket.leaveGroup(multicastNetwork);
            multicastSocket.close();
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

                InetAddress sourceIp = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //in case we use multiple users with the same ip
                String concatedUser = sourceIp.toString() + port;

                //Checks if the user is already register, if not register and moves on.
                if(registeredUsers.containsKey(concatedUser) == false) {
                    String userName = new String( receivePacket.getData()).trim();
                    System.out.println("User " + userName + "connected.Location:" + concatedUser);
                    registeredUsers.putIfAbsent(concatedUser, userName);
                    continue;
                }

                String recvString = new String( receivePacket.getData()).trim();
                String userName = registeredUsers.get(concatedUser);

                String message = userName + ":" + recvString;
                System.out.println("Broacasted:" + message);
                sendUDPMessage(message);

//                byte[] sendData = convertedString.getBytes();
//
//                //Get details from the client.
//
//                DatagramPacket sendPacket =  new DatagramPacket(sendData, sendData.length, sourceIp, port);
//                udpSocket.send(sendPacket);

            }while (convertedString.equals("STOP") == false && convertedString.equals("END") == false && running);
        }
        catch (Exception excp){
            excp.printStackTrace();
        }
    }

    private void SendMessage(String message)
    {
        //here we will send a message to everybody that is register.

    }

    public void sendUDPMessage(String message) throws Exception {
        DatagramSocket socket = new DatagramSocket();

        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, this.multicastNetwork, this.multicastPort);

        socket.send(packet);
        socket.close();
    }


}
