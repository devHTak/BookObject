package com.review.nw.chap06;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPEchoServer {

    public UDPEchoServer() {
        System.out.println("UDP Echo Server Started");

        try(DatagramChannel channel = DatagramChannel.open();
            DatagramSocket socket = channel.socket()) {

            SocketAddress address = new InetSocketAddress(9000);
            socket.bind(address);

            ByteBuffer buffer = ByteBuffer.allocateDirect(65507); // allocateDirect는 버퍼 할당에서 네이티브 OS 지원을 사용하려고 시도한다
            while(true) {
                // 메시지 얻기
                SocketAddress client = channel.receive(buffer); // 클라이언트 메시지를 얻기 위한 채널에 적용, 수신될 때까지 블록된다
                buffer.flip(); // 처리를 위한 버퍼를 사용 가능하게 한다. limit을 현재 position으로 설정하고 이후 position 0으로 설정

                // 메시지 출력
                buffer.mark(); // mark 현재 position으로 마킹한다
                StringBuilder message = new StringBuilder();
                while(buffer.hasRemaining()) {
                    message.append((char)buffer.get());
                }
                System.out.println("Received: " + message.toString());
                buffer.reset(); // 마크된 position으로 복원

                // 메시지 반환
                channel.send(buffer, client);
                System.out.println("Send: " + message);
                buffer.clear(); // position을 0으로 설정하고, limit을 capacity로 설정, 마크 삭제
            }
        } catch (IOException e) {
            // 예외 처리
        }
        System.out.println("UDP Echo Server Terminated");
    }

    public static void main(String[] args) {
        new UDPEchoServer();
    }
}
