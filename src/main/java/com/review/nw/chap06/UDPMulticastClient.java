package com.review.nw.chap06;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPMulticastClient {

    public UDPMulticastClient() {
        System.out.println("UDP Multicast Client Started");
        try {
            MulticastSocket socket = new MulticastSocket(9877);
            InetAddress address = InetAddress.getByName("228.5.6.7");
            socket.joinGroup(address);

            byte[] data = new byte[256];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while(true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Message from: " + message);
            }
        } catch (IOException e) {
            // 예외처리
        }
        System.out.println("UDP Multicast Client Terminated");
    }

    public static void main(String[] args) {
        new UDPMulticastClient();
    }
}
