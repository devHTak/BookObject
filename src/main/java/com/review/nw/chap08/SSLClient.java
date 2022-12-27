package com.review.nw.chap08;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.Security;
import java.util.Scanner;

public class SSLClient {

    public static void main(String[] args) {
        try {
            System.out.println("SSL Client Started");
            // Security.addProvider(new Provider());
            System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "password"); // 패스워드를 하드코딩한 것은 바람직하지 않다..

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 5000);
            System.out.println("Connection is established");

            PrintWriter writer = new PrintWriter(sslSocket.getOutputStream(), true);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            while(true) {
                System.out.print("INPUT: ");
                String input = scanner.nextLine();
                writer.println(input);
                System.out.println("SENT: " + buffer.readLine());
                if("quit".equals(input)) {
                    break;
                }
            }
            writer.close();
            buffer.close();
            sslSocket.close();
            System.out.println("SSL Client Terminated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
