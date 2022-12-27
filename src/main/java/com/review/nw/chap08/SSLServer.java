package com.review.nw.chap08;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SSLServer {
    public SSLServer() {
        try {
            System.out.println("SSL Server Started");
            //Security.addProvider(new Provider());
            System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "password"); // 패스워드를 하드코딩한 것은 바람직하지 않다..

            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(5000);

            System.out.println("Waiting for a connection");
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            PrintWriter writer = new PrintWriter(sslSocket.getOutputStream(), true);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            String inputLine;
            while((inputLine = buffer.readLine()) != null) {
                writer.println(inputLine);
                if("quit".equals(inputLine)) {
                    break;
                }
                System.out.println("RECEIVING: " + inputLine);
            }

            System.out.println("SSL Server Termniated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new SSLServer();
    }
}
