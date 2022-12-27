package com.review.nw.chap01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServer implements Runnable {

    private static Socket clientSocket;

    public ThreadServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Connected to client using [" + Thread.currentThread() + "]");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                System.out.println("Client request [" + Thread.currentThread() + "]:" + inputLine);
                out.write(inputLine);
            }
        } catch (IOException e) {
            // 예외처리
        }
    }
}
