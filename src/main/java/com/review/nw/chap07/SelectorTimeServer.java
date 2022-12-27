package com.review.nw.chap07;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public class SelectorTimeServer {
    private static Selector selector;

    public static void main(String[] args) {
        System.out.println("Time Server Started");
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(5000));

            selector = Selector.open(); // 셀렉터 생성 및 스레드 실행
            while(true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("Socket Channel Accepted - " + socketChannel);
                if(socketChannel != null) {
                    // 스레드는 select 메소드에 의해 블록될 수 있다 - wakeup 메소드를 사용하면 select 메소드가 즉시 반환되므로 register 메소드가 차단을 해제할 수 있다.
                    socketChannel.configureBlocking(false); // 해당 메소드 호출 필요
                    selector.wakeup(); // 셀렉터는 시작
                    socketChannel.register(selector, SelectionKey.OP_WRITE, null); // 채널 셀렉터에 등록

                    new Thread(new SelectorHandler()).start();
                }
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Time Server Terminated");
    }

    private static class SelectorHandler implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    System.out.println("About to select ...");
                    int readyChannels = selector.select();
                    if(readyChannels == 0) {
                        continue;
                    }

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                       if(selectionKey.isAcceptable()) {
                           // 연결 가능
                       } else if(selectionKey.isConnectable()) {
                           // 연결 성립
                       } else if(selectionKey.isReadable()) {
                           // 읽을 준비가 된 채널
                       } else if(selectionKey.isWritable()) {
                           sendMessage(selectionKey);
                       }
                       Thread.sleep(1000);
                       iterator.remove();
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMessage(SelectionKey key) throws IOException {
            String message = "Date: " + LocalDateTime.now().toString();
            ByteBuffer buffer = ByteBuffer.allocate(64);
            buffer.put(message.getBytes());
            buffer.flip();
            SocketChannel socketChannel = null;
            while(buffer.hasRemaining()) {
                socketChannel = (SocketChannel) key.channel();
                socketChannel.write(buffer);
            }
            System.out.println("SENT Message: " + message);
        }
    }
}
