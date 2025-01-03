package com.dxm.aimodel.modules.network;

import android.util.Log;

import com.dxm.aimodel.config.AppConfig;

import java.util.HashMap;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class Config {

    public static String token = "";
    private static HashMap<String, String> hostMap;

    private static void initEnv() {
        hostMap = new HashMap<String, String>();
        // 生产环境 -prod
        hostMap.put("ws-prod", "ws://8.133.188.219:80");
        hostMap.put("api-prod", "http://192.168.253.155:8093");
        hostMap.put("chat-prod", "https://open.bigmodel.cn/api/");
        hostMap.put("health-prod", "https://open.bigmodel.cn/api/");
        hostMap.put("diagnose-prod", "https://open.bigmodel.cn/api/");

        // 测试环境 -test 192.168.0.105
        hostMap.put("ws-test", "ws://8.133.188.219:80?uid=");
        hostMap.put("api-test", "http://192.168.253.155:8093");
        hostMap.put("chat-test", "https://open.bigmodel.cn/api/");
        hostMap.put("health-test", "https://open.bigmodel.cn/api/");
        hostMap.put("diagnose-test", "https://open.bigmodel.cn/api/");

    }

    public static String getTagHost(String host) {
        if (hostMap == null) {
            initEnv();
        }
        String key = String.format("%s-%s", host, AppConfig.app_env.getValue());
        return hostMap.containsKey(key) ? hostMap.get(key) : hostMap.get("-def" + AppConfig.app_env);
    }
}
