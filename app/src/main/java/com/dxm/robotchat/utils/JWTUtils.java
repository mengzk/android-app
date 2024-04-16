package com.dxm.robotchat.utils;


import android.util.Log;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.dxm.robotchat.config.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Meng
 * Date: 2024/04/01
 * Modify: 2024/04/01
 * Desc:
 */
public class JWTUtils {
    private static final String TAG = "JWTUtils";

    public static String getJWTToken(String key) {
        long expSeconds = 12 * 3600000;

        long currentTime = System.currentTimeMillis();
        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", key);
        payload.put("exp", currentTime + expSeconds);
        payload.put("timestamp", currentTime);

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("sign_type", "SIGN");

        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256(AppConfig.secretKey);
            token = JWT.create()
//                    .withIssuer("auth0")
                    .withHeader(headers)
                    .withPayload(payload)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            Log.i(TAG, exception.getMessage());
        }
        return token;
    }
}
