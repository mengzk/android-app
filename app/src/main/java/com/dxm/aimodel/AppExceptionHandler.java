package com.dxm.aimodel;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Meng
 * Date: 2024/03/27
 * Desc:
 */
public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {
    private String TAG = "AppExceptionHandler";

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // MyCrashHandler实例
    private static volatile AppExceptionHandler myCrashHandler;
    private Context context;

    //保证只有一个MyCrashHandler实例
    private AppExceptionHandler() {
    }

    // 获取CrashHandler实例 单例模式 - 双重校验锁
    public static AppExceptionHandler getInstance() {
        if (myCrashHandler == null) {
            synchronized (AppExceptionHandler.class) {
                if (myCrashHandler == null) {
                    myCrashHandler = new AppExceptionHandler();
                }
            }
        }
        return myCrashHandler;
    }

    public void init(Context ctx) {
        context = ctx;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该MyCrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleExample(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理 目的是判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {//Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                Log.d(TAG, "uncaughtException: " + e1.getMessage());
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.d(TAG, "uncaughtException: " + e2.getMessage());
            }
            /** 关闭App 与下面的restartApp重启App保留一个就行 看你需求 **/
            // 如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
//            restartApp();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 将异常信息保存 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleExample(Throwable ex) {
        // 如果已经处理过这个Exception,则让系统处理器进行后续关闭处理
        if (ex == null) {
            return false;
        }
        new Thread(() -> {
            // Toast 显示需要出现在一个线程的消息队列中
            Looper.prepare();
            Toast.makeText(context, "抱歉，程序出现异常!", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();
        saveCrashInfo(ex);
        return true;
    }

    /**
     * 重启应用
     */
    public void restartApp() {
//        Intent intent = new Intent(AppApplication.getContext(), SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(1);

        // 重启应用
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        try {
            ex.printStackTrace(printWriter);
            Throwable exCause = ex.getCause();
            while (exCause != null) {
                exCause.printStackTrace(printWriter);
                exCause = exCause.getCause();
            }
            printWriter.close();

            String path = "";
            // 判断存储是否正常使用
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory().getPath() + "/CrashLog";
            } else {
                path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/CrashLog";
            }
            writeFile(writer, path);
        } catch (Exception exception) {
            printWriter.close();
        }
    }

    private void writeFile(Writer writer, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = "log_" + stampDate() + ".txt";
            FileOutputStream fos = new FileOutputStream(new File(path, fileName));
            try {
                fos.write(writer.toString().getBytes());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos.close();
            Log.i(TAG, "writeFile: " + path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     */
    private String stampDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmdd");
        return sdFormatter.format(nowTime);
    }
}
