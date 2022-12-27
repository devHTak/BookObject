package com.review.nw.chap06;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {

    public UDPClient() {
        System.out.println("UDP Client Started");
        Scanner scanner = new Scanner(System.in);

        try(DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 9003;
            byte[] sendMessage;

            while(true) {
                System.out.print("Enter a message: ");
                String message = scanner.nextLine();
                if("quit".equals(message)) {
                    break;
                }

                // 서버로 데이터 전달
                sendMessage = message.getBytes();
                DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, port);
                socket.send(packet);

                // 서버에게 데이터 받음
                byte[] receiveMessage = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveMessage, receiveMessage.length);
                socket.receive(receivePacket);
                message = new String(receivePacket.getData());
                System.out.println("Received Data: " + message);
            }
            socket.close();
        } catch (IOException e) {
            // 예외처리
        }
        System.out.println("UDP Client Terminated");
    }

    public static void main(String[] args) {
        new UDPClient();
    }
}
