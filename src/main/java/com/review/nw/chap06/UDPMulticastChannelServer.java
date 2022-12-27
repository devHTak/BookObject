package com.review.nw.chap06;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class UDPMulticastChannelServer {

    public UDPMulticastChannelServer() {
        try {
            System.setProperty("java.net.preferIPv6Stack", "true"); // IPv6 를 사용하게 지정
            DatagramChannel channel = DatagramChannel.open();
            NetworkInterface networkInterface = NetworkInterface.getByName("eth0"); // eth0 네트워크 인터페이스 생성
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface); // 채널과 그룹을 식별하는 데 사용된 네트워크 인터페이스를 연결
            InetSocketAddress group = new InetSocketAddress("FF01:0:0:0:0:0:0:FC", 9003);

            String message = "Message";
            ByteBuffer buffer = ByteBuffer.allocate(message.length()); // 바이트 버퍼를 문자열 크기만큼 생성
            buffer.put(message.getBytes());

            while(true) {
                // 버퍼는 그룹 멤버에게 전송
                channel.send(buffer, group);
                System.out.println("Sent the multicast message: " + message);
                buffer.clear();

                buffer.mark(); // 현재 position으로 marking 처리
                StringBuilder receiveMessage = new StringBuilder();
                while(buffer.hasRemaining()) {
                    receiveMessage.append((char)buffer.get());
                }
                System.out.print("Sent: " + receiveMessage.toString());
                buffer.reset(); // mark된 상태로 position 이동
            }
        } catch (IOException e) {
            // 예외처리
        }
    }

    public static void main(String[] args) {
        new UDPMulticastChannelServer();
    }
}
