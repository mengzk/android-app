package com.dxm.aimodel.config;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public enum AppEnv {
    PROD("prod"), TEST("test"), DEV("dev");
    private String value;

    AppEnv(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }
}
