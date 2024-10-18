package com.dxm.aimodel.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

/**
 * Author: Meng
 * Date: 2023/04/06
 * Desc:
 */
public class MusicService extends Service {

    private static final String TAG = "MusicServiceService";
    private static MusicService instance;


    private ServiceHandler serviceHandler;
    private Looper serviceLooper;
    private int PRIORITY_CODE = 1234567;

    public static MusicService getInstance() {
        if (instance == null) {
            synchronized (MusicService.class) {
                if (instance == null) {
                    instance = new MusicService();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("BluetoothService", PRIORITY_CODE);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        if (intent.hasExtra("bluetooth")) {
            msg.what = 1;
        } else if (intent.hasExtra("scan")) {
            msg.what = 2;
        } else if (intent.hasExtra("contact")) {
            msg.what = 3;
        } else {
            msg.arg1 = startId;
        }
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Handler that receives messages from the thread
    final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
