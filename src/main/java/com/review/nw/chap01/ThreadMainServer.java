package com.review.nw.chap01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadMainServer {
    public static void main(String[] args) {
        System.out.println("Thread Server Start");
        try(ServerSocket socket = new ServerSocket(8000)) {
            while(true) {
                System.out.println("Waiting for connecting");
                Socket clientSocket = socket.accept();

                new Thread(new ThreadServer(clientSocket)).start();
            }
        } catch (IOException e) {
            // 예외처리
        }
    }
}
