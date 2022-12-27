package com.review.nw.chap06;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

public class UDPMulticastChannelClient {
    public UDPMulticastChannelClient() {
        try {
            System.setProperty("java.net.preferIPv6Stack", "true");
            NetworkInterface networkInterface = NetworkInterface.getByName("eth0");
            DatagramChannel channel = DatagramChannel.open()
                    .bind(new InetSocketAddress(9003))
                    .setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);

            // IPv6 주소 기반으로 생성되고, 채널의 join 메소드를 사용하여 MembershipKey 인스턴스 생성
            InetAddress group = InetAddress.getByName("FF01:0:0:0:0:0:0:FC");
            MembershipKey key = channel.join(group, networkInterface);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.receive(buffer);

            buffer.flip();
            StringBuilder message = new StringBuilder();
            while(buffer.hasRemaining()) {
                message.append((char)buffer.get());
            }
            System.out.println("Received: " + message.toString());

            key.drop();
        } catch (IOException e) {
            // 예외처리
        }
    }

    public static void main(String[] args) {
        new UDPMulticastChannelClient();
    }
}
