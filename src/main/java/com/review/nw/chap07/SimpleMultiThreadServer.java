package com.review.nw.chap07;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleMultiThreadServer implements Runnable {

    private static ConcurrentHashMap<String, Float> map;
    private Socket clientSocket;
    static {
        map = new ConcurrentHashMap<>();
        map.put("Axle", 238.50f);
        map.put("Gear", 45.55f);
        map.put("Wheel", 86.30f);
        map.put("Rotor", 8.50f);
    }

    public SimpleMultiThreadServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Client Thread Started");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream out = new PrintStream(clientSocket.getOutputStream())) {

            String partName = br.readLine();
            out.println(map.get(partName));
            System.out.println("Request for " + partName + "and returned price is " + map.get(partName));
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client Thread Terminated");
    }

    public static void main(String[] args) {
        System.out.println("Multi-Threaded Server Started");
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                System.out.println("Listeneing for a client connection");
                Socket clientSocket = serverSocket.accept();
                new Thread(new SimpleMultiThreadServer(clientSocket)).start();;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Multi-Threaded Server Terminated");
    }
}
