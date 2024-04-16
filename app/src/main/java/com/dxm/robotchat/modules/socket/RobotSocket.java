package com.dxm.robotchat.modules.socket;

import com.dxm.robotchat.config.AppConfig;
import com.dxm.robotchat.modules.network.Config;
import com.dxm.robotchat.modules.socket.entity.SendDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class RobotSocket extends WebSocketListener {

    private static RobotSocket client;

    private String TAG = "RobotSocket";
    private String wsUrl = Config.getTagHost("ws");
    private WebSocket webSocket;
    private OnMessageListener onMessageListener;

    private RobotSocket() {
        run();
    }

    public static synchronized RobotSocket getClient() {
        if(client == null) {
            client = new RobotSocket();
        }
        return client;
    }

    public void setMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public void run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(wsUrl + AppConfig.device_id)
                .build();
        client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
        onSend(new SendDto(AppConfig.device_id, "open"));
//        webSocket.send(ByteString.decodeHex("deadbeef"));
//        webSocket.close(1000, "Goodbye, World!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("MESSAGE: " + text);
        if (this.onMessageListener != null) {
            this.onMessageListener.onMessage(text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("MESSAGE2: 2");
//        System.out.println(bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(10, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public void onSend(SendDto dto) {
        if (webSocket != null) {
            try {
                Gson gson = new GsonBuilder()
                        .disableHtmlEscaping()
//                        .setPrettyPrinting()
                        .create();
                String msg = gson.toJson(dto);
                System.out.println(msg);
                webSocket.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//                JSONObject json = new JSONObject();
//                json.put("event", "robot");
//                json.put("data", "unit-#2&^@%*&!_+adsa=asda");
//
//    public static void main(String... args) {
//        new RobotSocket().run();
//    }

    public interface OnMessageListener {
        void onMessage(String text);
    }
}