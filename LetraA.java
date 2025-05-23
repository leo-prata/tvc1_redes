package com.mycompany.letraA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class LetraA {

    public static void main(String[] args) throws IOException {
        int port = 4444;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Servidor iniciado na porta " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.err.println("Conexão aceita de: " + clientSocket.getInetAddress().getHostAddress());

            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (Socket s = clientSocket;
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {

            out.println("Conectado ao servidor. Digite 'sair' para desconectar.");
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recebido de " + s.getInetAddress().getHostAddress() + ": " + inputLine);
                out.println("Servidor ecoa: " + inputLine);
                if ("sair".equalsIgnoreCase(inputLine.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
        System.err.println("Cliente desconectado.");
    }
}