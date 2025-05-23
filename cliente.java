package com.mycompany.cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java Cliente <screenName> <host>");
            System.exit(1);
        }

        String screenName = args[0];
        String host = args[1];
        int port = 4444;

        Socket socket = new Socket(host, port);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String initMessage = in.readLine();
        if (initMessage != null && initMessage.contains("Servidor cheio")) {
            System.err.println("Mensagem do servidor: " + initMessage);
            socket.close();
            return;
        }
        System.err.println("Connected to " + host + " on port " + port);

        String s;
        while ((s = stdin.readLine()) != null) {
            out.println("[" + screenName + "]: " + s);

            String response = in.readLine();
            if (response == null) {
                System.err.println("Server closed connection");
                break;
            }
            System.out.println(response);
        }

        System.err.println("Closing connection to " + host);
        out.close();
        in.close();
        stdin.close();
        socket.close();
    }
}
