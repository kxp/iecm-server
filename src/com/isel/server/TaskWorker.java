package com.isel.server;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TaskWorker implements Runnable{

    private Socket socket;

    public TaskWorker (Socket socket) {
        this.socket = socket;
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
            }while(convertedString.equals("STOP") && convertedString.equals("END"));

            //clears the resources
            out.close();
            inFromClient.close();
            this.socket.close();
        }
        catch (Exception excp){
            //Lets print the issue.Since we have one thread per used it can be dealt with
            excp.printStackTrace();
        }
    }

}
