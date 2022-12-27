package com.review.nw.chap07;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class SelectorTimeClient {
    public static void main(String[] args) {
        Random random = new Random();
        SocketAddress address = new InetSocketAddress("127.0.0.1", 5000);
        try(SocketChannel socketChannel = SocketChannel.open(address)) {
            while(true) {
                ByteBuffer buffer = ByteBuffer.allocate(64);
                int byteRead = socketChannel.read(buffer);
                while(byteRead > 0) {
                    buffer.flip();
                    while(buffer.hasRemaining()) {
                        System.out.print((char) buffer.get());
                    }
                    System.out.println();
                    byteRead = socketChannel.read(buffer);
                }
                Thread.sleep(random.nextInt(1000) + 1000);
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
