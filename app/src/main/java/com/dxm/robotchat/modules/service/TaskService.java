package com.dxm.robotchat.modules.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author: Meng
 * Date: 2023/04/06
 * Desc:
 * https://developer.android.google.cn/guide/components/services?hl=zh-cn
 */
public class TaskService extends Service {
    private Executor taskThread;

    /**
     * 首次创建服务时调用（在调用 onStartCommand() 或onBind() 之前）。
     * 如果服务已在运行，则不会调用该方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
        taskThread = Executors.newSingleThreadExecutor();
    }

    /**
     *
     intent ：启动时，启动组件传递过来的Intent，如Activity可利用Intent封装所需要的参数并传递给Service

     flags：表示启动请求时是否有额外数据，可选值有 0，START_FLAG_REDELIVERY，START_FLAG_RETRY，0代表没有，它们具体含义如下：
     - START_FLAG_REDELIVERY:
     这个值代表了onStartCommand方法的返回值为 START_REDELIVER_INTENT，而且在上一次服务被杀死前会去调用stopSelf方法停止服务。
     其中START_REDELIVER_INTENT意味着当Service因内存不足而被系统kill后，则会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()，此时Intent时有值的。
     - START_FLAG_RETRY:
     该flag代表当onStartCommand调用后一直没有返回值时，会尝试重新去调用onStartCommand()。

     startId ： 指明当前服务的唯一ID，与stopSelfResult (int startId)配合使用，stopSelfResult 可以更安全地根据ID停止服务。
     实际上onStartCommand的返回值int类型才是最最值得注意的，它有三种可选值， START_STICKY，START_NOT_STICKY，START_REDELIVER_INTENT，它们具体含义如下：

     - START_STICKY
     当Service因内存不足而被系统kill后，一段时间后内存再次空闲时，系统将会尝试重新创建此Service，一旦创建成功后将回调onStartCommand方法，
     但其中的Intent将是null，除非有挂起的Intent，如pendingintent，这个状态下比较适用于不执行命令、但无限期运行并等待作业的媒体播放器或类似服务。
     - START_NOT_STICKY
     当Service因内存不足而被系统kill后，即使系统内存再次空闲时，系统也不会尝试重新创建此Service。
     除非程序中再次调用startService启动此Service，这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
     - START_REDELIVER_INTENT
     当Service因内存不足而被系统kill后，则会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()，任何挂起 Intent均依次传递。
     与START_STICKY不同的是，其中的传递的Intent将是非空，是最后一次调用startService中的intent。这个值适用于主动执行应该立即恢复的作业（例如下载文件）的服务。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
}
