package com.dxm.robotchat.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

/**
 * Author: Meng
 * Date: 2023/06/17
 * Desc:
 */
public class USBUtils {

    private UsbDeviceAttachedListener usbDeviceAttachedListener;
    private USBReceiver mUsbReceiver;
    private int scanGunVendorId = 7851;

    public HashMap<String, UsbDevice> getUSBDeviceList(Context context){
        UsbManager usbManager = (UsbManager) context.getSystemService(AppCompatActivity.USB_SERVICE);
        return usbManager.getDeviceList();
    }

    public boolean hasHidKeyBoard(Context context){
        HashMap<String, UsbDevice> deviceList = getUSBDeviceList(context);
        boolean hasGun = false;
        for (UsbDevice device:deviceList.values()) {
            if(device.getDeviceId() == scanGunVendorId) {
                hasGun = true;
            }
        }
        return hasGun;
    }

    public void setUsbDeviceAttachedListener(UsbDeviceAttachedListener listener){
        this.usbDeviceAttachedListener = listener;
    }

    public void registerReceiver( Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mUsbReceiver = new USBReceiver();
        context.registerReceiver(mUsbReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        if(mUsbReceiver != null){
            context.unregisterReceiver(mUsbReceiver);
        }
    }

    private class USBReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            switch (intent.getAction()) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    usbDeviceAttachedListener.onResult(usbDevice, true);
                    break;
            }
        }
    }

    public interface UsbDeviceAttachedListener {
        void onResult(UsbDevice usbDevice, boolean has);
    }
}
