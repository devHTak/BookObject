package com.review.nw.chap04;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MyHttpServer {

    public static void main(String[] args) throws Exception{
        System.out.println("MyHTTPServer Started");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/index", new IndexHandler());
        server.start();
    }

    private static class IndexHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println(exchange.getRemoteAddress());
            String response = getResponse();
            exchange.sendResponseHeaders(200, response.length());

            OutputStream out = exchange.getResponseBody();
            out.write(response.toString().getBytes());
            out.close();
        }

        private String getResponse() {
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("<html><h1>WebServer Home Page...</h1><br/>")
                    .append("<b>Welcome to my web server!</b><br/></html>");

            return responseBuilder.toString();
        }
    }
}
