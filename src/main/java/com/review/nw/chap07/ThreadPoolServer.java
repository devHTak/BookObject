package com.review.nw.chap07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class ThreadPoolServer {

    public static void main(String[] args) {
        System.out.println("Thread Pool Server Started");
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true) {
                System.out.println("Listening for a client connection");
                Socket clientSocket = serverSocket.accept();

                executor.execute(new WorkerThread(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Future<Float> future1 = executor.submit(() -> 1.0f);
        Future<Float> future2 = executor.submit(() -> 2.0f);
        try {
            Float price1 = future1.get();
            Float price2 = future2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        System.out.println("Thread Pool Server Terminated");
    }

    public static class WorkerThread implements Runnable {
        private Socket clientSocket;
        public WorkerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
            System.out.println("Worker Thread Started");
            try(BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintStream out = new PrintStream(clientSocket.getOutputStream())) {

                String partName = br.readLine();
                float price = new WorkerCallable(partName).call();
                out.println(price);
                System.out.println("Request for " + partName + ", price: " + price);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Worker Thread Started");
        }
    }

    public static class WorkerCallable implements Callable<Float> {
        private static ConcurrentHashMap<String, Float> map;
        private String partName;
        static {
            map = new ConcurrentHashMap<>();
            map.put("Axle", 238.50f);
            map.put("Gear", 45.55f);
            map.put("Wheel", 86.30f);
            map.put("Rotor", 8.50f);
        }
        public WorkerCallable(String partName) {
            this.partName = partName;
        }
        @Override
        public Float call() throws Exception {
            return map.get(partName);
        }
    }
}
