package com.review.nw.chap04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class WebClient {

    public WebClient() {
        System.out.println("HTTP Client is Started");
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            Socket socket = new Socket(address, 8080);

            try(OutputStream out = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                sendGet(out);
                System.out.println(getResponse(in));
            }
        } catch (IOException ex) {
            // 예외 처리
        }
    }

    private String getResponse(BufferedReader in) {
        String inputLine = "";
        try {
            StringBuilder response = new StringBuilder();
            while( (inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            return response.toString();
        } catch (IOException ex) {
            // 예외 처리
        }

        return inputLine;
    }

    private void sendGet(OutputStream out) {
        try {
            out.write("GET /default\r\n".getBytes());
            out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
        } catch (IOException e) {
            // 예외 처리
        }
    }

    public static void main(String[] args) {
        new WebClient();
    }
}
