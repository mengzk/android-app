package com.dxm.aimodel;

import android.app.Application;

import com.dxm.aimodel.config.AppConfig;
import com.dxm.aimodel.utils.DeviceUtils;

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
