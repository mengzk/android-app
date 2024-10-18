package com.dxm.aimodel.utils;

import android.os.Build;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author: Meng
 * Date: 2023/04/19
 * Desc:
 */
public class EventBus {

    private static Executor busThread = null;
    private static HashMap<OnBusListener, String> eventMap = new HashMap<>();

    public static void add(String key, OnBusListener busListener) {
        add(key, "", busListener);
    }

    public static void add(String key, String tag, OnBusListener busListener) {
        eventMap.put(busListener, String.format("%s-%s", key, tag));
    }

    public static void once(String key, OnBusListener busListener) {
        eventMap.put(busListener, key);
    }

    public static void send(String key, Object data) {
        if(busThread == null) {
            busThread = Executors.newSingleThreadExecutor();
        }
        busThread.execute(new Runnable() {
            @Override
            public void run() {
                sendMsg(key, data);
            }
        });

    }

    private static void sendMsg(String key, Object data) {
        boolean has = eventMap.containsValue(key);
        if(has) {
            Set<OnBusListener> listeners = eventMap.keySet();
            for (OnBusListener listener : listeners) {
                if(eventMap.get(listener).equals(key) && listener != null) {
                    listener.onResult(data);
                }
            }
        }
    }

    public static void remove(String key) {
        remove(key, "");
    }

    public static void remove(String key, String tag) {
        String value = String.format("%s-%s", key, tag);
        boolean has = eventMap.containsValue(value);
        if(has) {
            Set<OnBusListener> listeners = eventMap.keySet();
            for (OnBusListener listener : listeners) {
                if(eventMap.get(listener).equals(value)) {
                    eventMap.remove(listener);
                }
            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            eventMap.forEach((_ , name) -> { if (name.equals(key)) { } });
//        }
    }

    public static void remove(OnBusListener busListener) {
        eventMap.remove(busListener);
    }

    public static void clear() {
        eventMap.clear();
    }

    public interface OnBusListener<T> {
        void onResult(T data);
    }
}
