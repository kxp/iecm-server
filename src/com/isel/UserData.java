package com.isel;

import java.net.InetAddress;

public class UserData {

    private InetAddress clientAddress;
    private int clientPort ;
    private String userName;

    public UserData(String name, InetAddress address, int port){
        this.userName = name;
        this.clientAddress = address;
        this.clientPort = port;
    }


    public void SendMessage(String mesage) {
    }

}
