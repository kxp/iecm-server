package com.isel;

import com.isel.server.EServerType;
import com.isel.server.IServer;
import com.isel.server.ServerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        EServerType serverType = EServerType.NOTSPECIFIED;

        if (args.length > 1) {
            try {
                serverType = EServerType.valueOf(args[1].toUpperCase());
            } catch (Exception excp) {
                excp.printStackTrace();
                return;
            }
        } else {
            System.out.println("Select the server type by choosing one of the options: tcp, tcpnonblock, udp or multicast.");

            try {
                String line = br.readLine();
                serverType = EServerType.valueOf(line.toUpperCase());
            } catch (Exception excp) {
                excp.printStackTrace();
                return;
            }
        }

        IServer server = ServerFactory.CreateServer(serverType);
        if (server == null) {
            System.out.println("Wrong option. Exiting!");
        }

        server.Start();
    }
}