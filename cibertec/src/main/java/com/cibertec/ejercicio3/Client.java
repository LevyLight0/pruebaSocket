package com.cibertec.ejercicio3;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 13;

    public Client() {
        System.out.println("1 >> [ini] Client constructor");
        try {
            System.out.println("2 >> connecting to server...");
            Socket socket = new Socket(HOST, PORT);
            System.out.println("3 >> connected to server...");

            // Archivo a enviar
            File file = new File("C:\\client\\robot.png");
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return;
            }

            // Flujo de datos de entrada y salida
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Envío del nombre del archivo y tamaño
            dos.writeUTF(file.getName());
            dos.writeInt((int) file.length());

            // Envío del archivo
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.close();
            socket.close();

            System.out.println("Archivo enviado: " + file.getAbsolutePath());
            System.out.println("4 >> final for client...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
