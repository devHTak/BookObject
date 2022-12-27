package com.review.nw.chap06;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPEchoClient {

    public UDPEchoClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("UDP Echo Client Started");
        try {
            SocketAddress address = new InetSocketAddress("127.0.0.1", 9000);
            DatagramChannel channel = DatagramChannel.open();
            channel.connect(address);

            while(true) {
                String message = scanner.nextLine();
                if("quit".equals(message)) {
                    break;
                }
                ByteBuffer buffer = ByteBuffer.allocate(message.length());
                buffer.put(message.getBytes());

                buffer.flip(); // limit을 현재의 position으로 설정하고 position을 0으로 설정
                channel.write(buffer);
                System.out.println("Send: " + message);

                buffer.clear();
                channel.read(buffer);
                buffer.flip();
                StringBuilder receivedMessage = new StringBuilder();
                while(buffer.hasRemaining()) {
                    receivedMessage.append((char)buffer.get());
                }
                System.out.println("Received: " + receivedMessage.toString());
            }
        } catch (IOException e) {
            // 예외 처리
        }
        System.out.println("UDP Echo Client Terminated");
    }

    public static void main(String[] args) {
        new UDPEchoClient();
    }
}
