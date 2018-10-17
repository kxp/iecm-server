package com.isel.server;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TaskWorker implements Runnable{

    private Socket socket;
    //it makes it simpler to stop the server
    private IServer sourceServer;

    public TaskWorker (Socket socket, IServer server) {
        this.socket = socket;
        this.sourceServer = server;
    }

    @Override
    public void run() {
        try {
            worker();
        }
        catch ( Exception excp){
            excp.printStackTrace();
        }
    }

    //Does the work of reading the socket and replying it.
    private void worker() throws Exception {

        String sourceString;
        String convertedString;
        try {
            //Create the input and output buffers.
            DataInputStream inFromClient = new DataInputStream(this.socket.getInputStream());
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            do {
                //reads the text and converts it to upper case.
                sourceString = inFromClient.readUTF();
                convertedString = sourceString.toUpperCase();

                System.out.println("Original:" + sourceString + "\nConverted:" + convertedString);
                out.println(convertedString);
                out.flush();
            }while(convertedString.equals("STOP") == false && convertedString.equals("END") == false );

            //clears the resources
            out.close();
            inFromClient.close();
            this.socket.close();

            //Closes the server
            if("STOP".equals(convertedString) == true){
                this.sourceServer.Stop();
            }

        }
        catch (Exception excp){
            //Lets print the issue.Since we have one thread per used it can be dealt with
            excp.printStackTrace();
        }
    }

}
