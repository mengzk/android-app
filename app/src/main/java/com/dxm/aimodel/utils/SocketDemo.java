package com.dxm.aimodel.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Meng
 * Date: 2024/10/21
 * Modify: 2024/10/21
 * Desc: Socket通信
 */
public class SocketDemo {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        new Server();
    }


    public static class Server {
        private static final int PORT = 8083;
       private ServerSocket server;
       private final Map<String, Socket> map = new HashMap<>();

        public Server() {
            try {
                // 创建服务器
                server = new ServerSocket(PORT);
                System.out.println("Server started");

                // 监听连接
                while (true) {
                    System.out.println("Waiting for client...");
                    Socket socket = server.accept();
                    System.out.println("Client connected " + socket.getInetAddress());
                    map.put(socket.getInetAddress().toString(), socket);
                    hello(socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendMessage(String ip, String message) {
            Socket socket = map.get(ip);
            if (socket != null) {
                try {
                    socket.getOutputStream().write(message.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void hello(Socket socket) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line = reader.readLine();
            System.out.println("Client: " + line);

            writer.write("Hello, I am Server\n");
            writer.newLine();
            writer.flush();
        }

    }

    public static class Client {
        private static final String HOST = "";
        private static final int PORT = 8083;

        public Client() {
            try {
                Socket socket = new Socket(HOST, PORT);
                System.out.println("Connected to server");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true) {
                    writer.write("Hello, I am Client\n");
                    writer.newLine();
                    writer.flush();

                    String line = reader.readLine();
                    System.out.println("Server: " + line);

                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
