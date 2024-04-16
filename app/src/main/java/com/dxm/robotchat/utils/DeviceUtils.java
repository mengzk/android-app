package com.dxm.robotchat.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Author: Meng
 * Date: 2023/04/15
 * Desc:
 */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    public static String getDeviceId(Context context) {
        String did = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (did == null || did.length() < 1) {
            did = UUID.randomUUID().toString().replace("-", "");
        }
        return did;
    }

    public static String getDId(Context context) {
        StringBuilder sb = new StringBuilder();
        String did = Build.ID;
        sb.append("------> id=");
        sb.append(did);
        String serial = Build.SERIAL;
        sb.append(", serial=");
        sb.append(serial);
        String host = Build.HOST;
        sb.append(", host=");
        sb.append(host);
        String model = Build.MODEL;
        sb.append(", model=");
        sb.append(model);
        String brand = Build.BRAND;
        sb.append(", brand=");
        sb.append(brand);
        String bootloader = Build.BOOTLOADER;
        sb.append(", bootloader=");
        sb.append(bootloader);
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        sb.append(", androidId=");
        sb.append(androidId);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        sb.append(", uuid=");
        sb.append(uuid);
        Log.i(TAG, sb.toString());
        return androidId;
    }

    @SuppressLint("HardwareIds")
    private static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getImei();
        } else {
            return tm.getDeviceId();
        }
    }

    private String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMacAddress(Context context) {
        if (context == null) {
            return "";
        }
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi == null || wi.getMacAddress() == null) {
            return "";
        }
        return wi.getMacAddress().trim();
    }

    public static String getAppVersion(Context context) {
        if (context == null) {
            return "";
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            context.getPackageName();
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int code = pi.versionCode;
        return pi.versionName;
    }

    public static String getResolution(Context context) {
        if (context == null) {
            return "";
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        return widthPixels + "*" + heightPixels;
    }

    public static String getMccmnc(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getNetworkOperator();
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return "Unknown";
        }
        NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return "Unknown";
        }
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null) {
            NetworkInfo.State state = wifiInfo.getState();
            if (state != null && state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                return "WiFi状态";
            }
        }
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            NetworkInfo.State state = networkInfo.getState();
            String subtypeName = networkInfo.getSubtypeName();
            if (state != null && state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                switch (activeNetInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4G";
                    default:
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return "3G";
                        }
                        return "Unknown";
                }
            }
        }
        return "Unknown";
    }
}
