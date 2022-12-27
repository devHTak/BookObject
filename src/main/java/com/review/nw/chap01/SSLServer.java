package com.review.nw.chap01;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SSLServer {
    public SSLServer() {
        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            ServerSocket serverSocket = factory.createServerSocket(8000);
            System.out.println("SSLServer Started");
            try(Socket clientSocket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Client Socket created");
                String inputLine;
                while((inputLine = br.readLine()) != null) {
                    System.out.println("Client send: " + inputLine);
                    out.write(inputLine);
                }
                System.out.println("SSLServer Terminated");
            } catch (IOException e) {
                // 예외처리
            }
        } catch (IOException e) {
            // 예외처리
        }
    }

    public static void main(String[] args) {
        new SSLServer();
    }
}
