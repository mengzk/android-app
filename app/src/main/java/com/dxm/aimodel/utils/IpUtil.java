package com.dxm.aimodel.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Author: Meng
 * Date: 2024/10/18
 * Modify: 2024/10/18
 * Desc:
 */
public class IpUtil {

    /**
     * 获取内网IP地址
     */
    public static byte[] getLocalIP() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                    return inetAddress.getAddress();
                }
            }
        }
        return new byte[4];
    }

    public static String byteToString(byte[] ip) throws UnknownHostException {
        return InetAddress.getByAddress(ip).getHostAddress();
    }

    public static String getIp() {
        try {
            byte[] localIPAddress = getLocalIP();
            return IpUtil.byteToString(localIPAddress);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }
}
