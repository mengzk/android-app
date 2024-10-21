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
 * Date: 2023/04/27
 * Desc:
 * 文档：https://developer.android.google.cn/guide/components/services?hl=zh-cn
 * Intent intent = new Intent(this, BleLinkService.class);
 * startService(intent);
 */
public class LanLinkService extends Service {

    private final int SERVICE_CODE = 1230;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
//            Thread.sleep(1000);
//            Thread.currentThread().interrupt();
//            stopSelf(msg.arg1);
        }
    }

    /**
     * 首次创建服务时，系统会（在调用 onStartCommand() 或 onBind() 之前）调用此方法来执行一次性设置程序。如果服务已在运行，则不会调用此方法。
     */
    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("BleLinkService", SERVICE_CODE);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    /**
     * 当另一个组件（如 Activity）请求启动服务时，系统会通过调用 startService() 来调用此方法。
     * 执行此方法时，服务即会启动并可在后台无限期运行。
     * 如果您实现此方法，则在服务工作完成后，您需负责通过调用 stopSelf() 或 stopService() 来停止服务。如果您只想提供绑定，则无需实现此方法。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * START_NOT_STICKY
         * 如果系统在 onStartCommand() 返回后终止服务，则除非有待传递的挂起 Intent，否则系统不会重建服务。这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
         * START_STICKY
         * 如果系统在 onStartCommand() 返回后终止服务，则其会重建服务并调用 onStartCommand()，但不会重新传递最后一个 Intent。相反，除非有挂起 Intent 要启动服务，否则系统会调用包含空 Intent 的 onStartCommand()。在此情况下，系统会传递这些 Intent。此常量适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。
         * START_REDELIVER_INTENT
         * 如果系统在 onStartCommand() 返回后终止服务，则其会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()。所有挂起 Intent 均依次传递。此常量适用于主动执行应立即恢复的作业（例如下载文件）的服务。
         */
//        return super.onStartCommand(intent, flags, startId);
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    /**
     * 当另一个组件想要与服务绑定（例如执行 RPC）时，系统会通过调用 bindService() 来调用此方法。
     * 在此方法的实现中，您必须通过返回 IBinder 提供一个接口，以供客户端用来与服务进行通信。请务必实现此方法；但是，如果您并不希望允许绑定，则应返回 null。
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void scan() {


    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 当不再使用服务且准备将其销毁时，系统会调用此方法。服务应通过实现此方法来清理任何资源，如线程、注册的侦听器、接收器等。这是服务接收的最后一个调用。
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
