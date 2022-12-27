package com.review.nw.chap04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionExample {

    public static void main(String[] args) throws Exception{
        HttpURLConnectionExample example = new HttpURLConnectionExample();
        example.sendGet();
    }

    private void sendGet() throws Exception {
        String query = "https://www.google.com/search?q=java+sdk&ie=utf-8&oe=utf-8";
        URL url = new URL(query);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        System.out.println("ResponseCode: " + responseCode);
        if(responseCode == 200) {
            String response = getResponse(connection);
            System.out.println("Response: " + response);
        } else {
            System.out.println("BAD Request Code: " + responseCode);
        }
    }

    private String getResponse(HttpURLConnection connection) {
        String inputLine = "";

        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();

            while( (inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch(IOException e) {
            // 예외처리
        }

        return inputLine;
    }
}
