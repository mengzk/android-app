package com.dxm.aimodel.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Meng
 * Date: 2024/10/21
 * Modify: 2024/10/21
 * Desc: 基于Wifi的Direct通信 -P2P
 */
public class SocketWifi {

    public static void main(String[] args) {
        System.out.println("Hello World!");
//        new Server();
    }


    public static class Server {
        private static final int PORT = 8083;
        private WifiP2pManager manager;
        private WifiP2pManager.Channel channel;

        private BroadcastReceiver receiver;

        private ServerSocket server;
        private final Map<String, Socket> map = new HashMap<>();

        @SuppressLint("MissingPermission")
        public Server(Activity activity) {
            try {
                // 初始化 WifiP2pManager
                manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
                if (manager == null) {
                    System.out.println("WifiP2pManager is not available");
                    return;
                }
                // 初始化 Channel
                channel = manager.initialize(activity, activity.getMainLooper(), null);

                // 注册广播
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                            // Respond to new connection or disconnections
                            NetworkInfo netInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                            if (netInfo.isConnected()) {
                                // 连接服务器 socket
                                try {
                                    start();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                            // Respond to new peer devices 设备列表改变
                            manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                                @Override
                                public void onPeersAvailable(WifiP2pDeviceList deviceList) {
                                    for (WifiP2pDevice device : deviceList.getDeviceList()) {
                                        System.out.println("Device: " + device.deviceAddress);
                                        // 连接设备
                                        if (device.deviceName.equals("Client")) {
                                            WifiP2pConfig config = new WifiP2pConfig();
                                            config.deviceAddress = device.deviceAddress;
                                            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                                                @Override
                                                public void onSuccess() {
                                                    // WiFi Direct connection established
                                                    System.out.println("Connected to " + device.deviceAddress);
                                                }

                                                @Override
                                                public void onFailure(int reason) {
                                                    // WiFi Direct connection failed
                                                    System.out.println("Failed to connect to " + device.deviceAddress);
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        } else if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                            // wifi p2p 状态改变
                            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                                // Wifi Direct is enabled 启用
                            } else {
                                // Wi-Fi Direct is not enabled 停用
                            }
                        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                            // Respond to this device's wifi state changing
                            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                            System.out.println("Device: " + device.deviceName);
                        }
                    }
                };

                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
                activity.registerReceiver(receiver, filter);

                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        // Discovery initiated
                        System.out.println("Discovery initiated");
                    }

                    @Override
                    public void onFailure(int reason) {
                        // Discovery failed
                        System.out.println("Discovery failed");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void start() throws IOException {
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
        private WifiP2pManager manager;
        private WifiP2pManager.Channel channel;
        private WifiP2pDevice device;

        private BroadcastReceiver receiver;

        private ServerSocket server;
        private Socket socket;

        private static final int PORT = 8083;

        @SuppressLint("MissingPermission")
        public Client(Context context) {
            manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
            if (manager == null) {
                System.out.println("WifiP2pManager is not available");
                return;
            }
            channel = manager.initialize(context, context.getMainLooper(), null);

            // 注册广播
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                        // Respond to new connection or disconnections
                        NetworkInfo netInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                        if (netInfo.isConnected()) {
                            // 连接服务器 socket
                            start();
                        }
                    } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                        // Respond to new peer devices 设备列表改变
                        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                            @Override
                            public void onPeersAvailable(WifiP2pDeviceList deviceList) {
                                for (WifiP2pDevice device : deviceList.getDeviceList()) {
                                    System.out.println("Device: " + device.deviceAddress);
                                    // 连接设备
                                    if (device.deviceName.equals("Client")) {
                                        WifiP2pConfig config = new WifiP2pConfig();
                                        config.deviceAddress = device.deviceAddress;
                                        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                                            @Override
                                            public void onSuccess() {
                                                // WiFi Direct connection established
                                                System.out.println("Connected to " + device.deviceAddress);
                                            }

                                            @Override
                                            public void onFailure(int reason) {
                                                // WiFi Direct connection failed
                                                System.out.println("Failed to connect to " + device.deviceAddress);
                                            }
                                        });
                                    }
                                }
                            }
                        });

                    } else if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                        // wifi p2p 状态改变
                        int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                            // Wifi Direct is enabled 启用
                        } else {
                            // Wi-Fi Direct is not enabled 停用
                        }
                    } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                        // Respond to this device's wifi state changing
                        WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                        System.out.println("Device: " + device.deviceName);
                    }
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
            context.registerReceiver(receiver, filter);

            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Discovery initiated
                    System.out.println("Discovery initiated");
                }

                @Override
                public void onFailure(int reason) {
                    // Discovery failed
                    System.out.println("Discovery failed");
                }
            });
        }

        private void start() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(device.deviceAddress, PORT), 5000);
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
