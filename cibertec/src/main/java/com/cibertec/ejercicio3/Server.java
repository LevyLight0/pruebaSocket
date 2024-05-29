package com.cibertec.ejercicio3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(13)) {
            System.out.println("Server is listening on port 13");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_imagen", "root", "mysql")) {

            System.out.println("Reading file metadata from client...");
            String fileName = dis.readUTF();
            int fileSize = dis.readInt();
            System.out.println("File name: " + fileName + ", File size: " + fileSize);

            byte[] fileData = new byte[fileSize];
            dis.readFully(fileData);
            System.out.println("File data received.");

            // Guardar el archivo en la base de datos
            String sql = "INSERT INTO imagen (nombre, tamaño, archivo) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, fileName);
                pstmt.setInt(2, fileSize);
                pstmt.setBytes(3, fileData);
                pstmt.executeUpdate();
            }

            System.out.println("File " + fileName + " stored in the database.");
            socket.close();
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
