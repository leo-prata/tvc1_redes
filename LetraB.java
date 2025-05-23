package com.mycompany.LetraB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Example {
    public static int a;
}

public class LetraB {

    public static void main(String[] args) throws Exception {
        int port = 4444;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Servidor iniciado na porta " + port);
        ClientHandler[] clientHandler = new ClientHandler[10];
        Example.a = 0;

        while (true) {
            Socket clientSocket = serverSocket.accept();

            if (clientSocket != null && Example.a < 10) {
                System.err.println("Conexão aceita do cliente: " + clientSocket.getInetAddress().getHostAddress());
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Conectado");
                clientHandler[Example.a] = new ClientHandler(clientSocket);
                new Thread(clientHandler[Example.a]).start();
                Example.a++;
            } else {
                // Limite atingido: envia mensagem e encerra a conexão
                System.err.println("Servidor cheio. Tente novamente mais tarde.");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Servidor cheio. Tente novamente mais tarde.");
                clientSocket.shutdownOutput(); // sinaliza fim da transmissão
                clientSocket.close();
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String s;
            while ((s = in.readLine()) != null) {
                System.out
                        .println("Recebido do cliente (" + clientSocket.getInetAddress().getHostAddress() + "): " + s);
                out.println(s); // envia de volta
            }
        } catch (Exception e) {
            System.err.println("Erro no manipulador do cliente: " + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (clientSocket != null)
                    clientSocket.close();
                System.err.println(
                        "Conexão com o cliente " + clientSocket.getInetAddress().getHostAddress() + " fechada.");
                Example.a--;
            } catch (Exception e) {
                System.err.println("Erro ao fechar recursos do cliente: " + e.getMessage());
            }
        }
    }
}