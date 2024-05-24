package com.cibertec;

import java.io.IOException;
import java.net.Socket;

public class Client {
    
    private static final int PORT = 13;
    private static final String HOST = "localhost";

    public Client(){
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("1 >> Connected to server on port " + PORT);
            System.out.println("2 >> Complete");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}