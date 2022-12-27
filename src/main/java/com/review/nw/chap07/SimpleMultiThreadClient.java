package com.review.nw.chap07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SimpleMultiThreadClient {

    public SimpleMultiThreadClient() {
        System.out.println("Client started");
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream ps = new PrintStream(socket.getOutputStream());

            System.out.print("Part Name: ");
            String partName = scanner.nextLine();
            ps.println(partName);;
            System.out.println(partName + " request sent. Response: " + br.readLine());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client Terminated");
    }

    public static void main(String[] args) {
        new SimpleMultiThreadClient();
    }
}
