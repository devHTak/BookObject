package com.review.nw.chap06;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UDPMulticastServer {

    public UDPMulticastServer() {
        System.out.println("UDP Multicast Server Started");
        try {
            MulticastSocket socket = new MulticastSocket();
            InetAddress address = InetAddress.getByName("228.5.6.7");
            socket.joinGroup(address);

            byte[] data;
            DatagramPacket packet;

            while(true) {
                Thread.sleep(1000);
                String message = LocalDateTime.now().toString();
                System.out.println("Sending: " + message);
                data = message.getBytes();
                packet = new DatagramPacket(data, message.length(), address, 9877);
                socket.send(packet);
            }
        } catch (IOException | InterruptedException e) {
            // 예외 처리
        }
        System.out.println("UDP Multicast Server Terminated");
    }

    public static void main(String[] args) {
        new UDPMulticastServer();
    }
}
