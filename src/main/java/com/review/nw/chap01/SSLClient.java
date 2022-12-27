package com.review.nw.chap01;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SSLClient {

    public SSLClient() {
        Scanner sc = new Scanner(System.in);
        System.out.println("SSL Client Started");
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try(Socket socket = factory.createSocket(InetAddress.getByName("127.0.0.1"), 8000);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            while(true) {
                System.out.print("Input: ");
                String inputLine = sc.nextLine();
                if("quit".equals(inputLine)){
                    break;
                }
                out.write(inputLine);

                System.out.println("Server Sent: " + br.readLine());
            }
        } catch (IOException e) {
            // 예외처리
        }
    }

    public static void main(String[] args) {
        new SSLClient();
    }
}
