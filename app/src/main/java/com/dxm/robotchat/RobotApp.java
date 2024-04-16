package com.dxm.robotchat;

import android.app.Application;

import com.dxm.robotchat.config.AppConfig;
import com.dxm.robotchat.utils.DeviceUtils;

/**
 * Author: Meng
 * Date: 2024/03/28
 * Modify: 2024/03/28
 * Desc:
 */
public class RobotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initCode();
    }

    private void initCode() {
        String androidId = DeviceUtils.getDeviceId(this);
        AppConfig.device_id = androidId.toUpperCase();

        AppExceptionHandler.getInstance().init(this);
    }

}
