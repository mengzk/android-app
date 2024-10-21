package com.dxm.aimodel.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Author: Meng
 * Date: 2024/10/21
 * Modify: 2024/10/21
 * Desc: 蓝牙近场通信
 */
public class SocketBle {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        new Server();
    }

    public static class Server {
        private static final int PORT = 8083;
        private BluetoothAdapter adapter;
        private BluetoothServerSocket server;
        private final Map<String, BluetoothSocket> map = new HashMap<>();

        @SuppressLint("MissingPermission")
        public Server(Activity activity) {
            try {
                // 初始化适配器
                adapter = BluetoothAdapter.getDefaultAdapter();

                // 检测蓝牙是否可用
                if (adapter == null) {
                    System.out.println("Bluetooth is not available");
                    return;
                }

                // 检测蓝牙是否开启
                if (!adapter.isEnabled()) {
                    System.out.println("Bluetooth is not enabled");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(intent, 1);
                    return;
                }

                // 创建服务器
                server = adapter.listenUsingInsecureRfcommWithServiceRecord("BluetoothServer", UUID.randomUUID());

                // 监听连接
                while (true) {
                    System.out.println("Waiting for client...");
                    BluetoothSocket socket = server.accept();
                    System.out.println("Client connected " + socket.getRemoteDevice());

                    map.put(socket.getRemoteDevice().toString(), socket);
                    hello(socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendMessage(String ip, String message) {
            BluetoothSocket socket = map.get(ip);
            if (socket != null) {
                try {
                    socket.getOutputStream().write(message.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void hello(BluetoothSocket socket) throws IOException {
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
        private BluetoothSocket socket;
        private BluetoothDevice device;
        private BluetoothAdapter adapter;
        private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        @SuppressLint("MissingPermission")
        public Client() {
            try {
                adapter = BluetoothAdapter.getDefaultAdapter();
                if (adapter == null) {
                    System.out.println("Bluetooth is not available");
                    return;
                }
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                for (BluetoothDevice de : devices) {
                    System.out.println("Paired Device: " + de.getName() + " - " + de.getAddress());
                    if (de.getName().equals("BluetoothServer")) {
                        device = de;
                    }
                }
//                device = adapter.getRemoteDevice("");
                if (device == null) {
                    System.out.println("Device not found");
                    return;
                }
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();

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
