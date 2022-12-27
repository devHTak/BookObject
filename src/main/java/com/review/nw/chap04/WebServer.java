package com.review.nw.chap04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

public class WebServer {

    public WebServer() {
        System.out.println("Web Server Started");
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                System.out.println("Waiting for client");
                Socket socket = serverSocket.accept();
                System.out.println("Connection made");
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException ex) {
            // 예외처리
        }
    }

    public static void main(String[] args) {
        new WebServer();
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Client Handler Started for " + this.socket);
            handleRequest(this.socket);
            System.out.println("Client Handler Terminated for " + this.socket);
        }

        private void handleRequest(Socket socket) {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String headerLine = in.readLine();
                StringTokenizer tokenizer = new StringTokenizer(headerLine);
                String httpMethod = tokenizer.nextToken();

                if("GET".equals(httpMethod)) {
                    System.out.println("GET Method processed");
                    String httpQueryString = tokenizer.nextToken();
                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("<html><h1>WebServer Home Page...</h1><br/>")
                            .append("<b>Welcome to my web server!</b><br/></html>");

                    sendResponse(socket, 200, responseBuilder.toString());
                } else {
                    System.out.println("The HTTP Method is not recognized");
                    sendResponse(socket, 405, "Mehtod is not allowed");
                }
            } catch (IOException e) {
                // 예외처리
            }
        }

        private void sendResponse(Socket socket, int statusCode, String responseString) {
            String statusLine;
            String serverHeader = "Server: WebServer\r\n";
            String contentTypeHeader = "Content-Type: text/html\r\n";

            try(DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                if(statusCode == 200) {
                    statusLine = "HTTP/1.0 200 OK\r\n";
                    String contentLengthHeader = "Content-Length: " + responseString.length() +"\r\n";

                    out.writeBytes(statusLine);
                    out.writeBytes(serverHeader);
                    out.writeBytes(contentTypeHeader);
                    out.writeBytes(contentLengthHeader);
                    out.writeBytes("\r\n");
                    out.writeBytes(responseString);
                } else if(statusCode == 405) {
                    statusLine = "HTTP/1.0 405 Method Not Allowed\r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                } else {
                    statusLine = "HTTP/1.0 404 Not Found\r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                }
                out.close();
            } catch (IOException e) {
                // 예외처리
            }
        }
    }
}
