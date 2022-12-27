package com.review.nw.chap06;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPServer {
    public UDPServer() {
        System.out.println("UDP Server Started");
        /** DatagramSocket 생성하는 다른 방법
         * DatagramSocket socket = new DatagramSocket(null);
         * socket.bind(new InetSocketAddress(9003);
         */
        try(DatagramSocket socket = new DatagramSocket(9003)) {
            while(true) {
                byte[] receiveMessage = new byte[64]; // 바이트 배열 생성
                DatagramPacket packet = new DatagramPacket(receiveMessage, receiveMessage.length); // DatagramPacket 인스턴스 생성
                socket.receive(packet); // 도착 메세지의 대기를 위한 DatagramSocket 인스턴스 사용
                String message = new String(packet.getData());
                System.out.println("Received Data: " + message);

                // 응답 전송
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                byte[] sendMessage = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, address, port);
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            // 예외 처리
        }
        System.out.println("UDP Server Terminating");
    }

    public static void main(String[] args) {
        new UDPServer();
    }
}
