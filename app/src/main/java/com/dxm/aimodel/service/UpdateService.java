package com.dxm.aimodel.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author: Meng
 * Date: 2023/04/06
 * Desc:
 */
public class UpdateService extends JobService {
    public static int JOB_CODE = 1231;
    private String Tag = "UpdateService";
    private final int JOB_ID = 1000;
    private String apkUrl = "https://bnq-app-store.oss-cn-shanghai.aliyuncs.com/app_package/test/apk/shadowsocks/v1680750650870%26shadowsocks.apk"; // 下载包安装路径
    private String savePath = "";
    private final String apkName = "app_pack.apk";
    private Context mContext;
    private boolean downing = false;
    private final Executor diskIO = Executors.newSingleThreadExecutor();

    public static void start(Context context) {
        // 获取安装包地址
        boolean update = false;
        if(!update) {
            return;
        }

        ComponentName serviceName = new ComponentName(context, UpdateService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(UpdateService.JOB_CODE, serviceName);
        jobBuilder.setPeriodic(1000 * 60 * 15);
        JobInfo myJob = jobBuilder.build();

        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        scheduler.schedule(myJob);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        mContext = getApplicationContext();
        savePath = mContext.getFilesDir() + "/apk";
        downApk();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void downApk() {
        diskIO.execute(new Runnable() {
            @Override
            public void run() {
                handleWork();
            }
        });
    }

    private void handleWork() {
        if (downing) {
            return;
        }
        downing = true;
        Log.i(Tag, "=============> 开始下载");
        FileOutputStream fos;
        InputStream ism;
        try {
            URL url = new URL(apkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(120000);
            conn.connect();
            ism = conn.getInputStream();
            File apkFile = new File(savePath);
            if (!apkFile.exists()) {
                apkFile.mkdirs();
            }
            apkFile = new File(savePath, apkName);

            Log.i(Tag, "=============> APK Path " + savePath);

            fos = new FileOutputStream(apkFile);
            long total = conn.getContentLength();
            int length = 0;
            byte[] buf = new byte[16384];
            int num = 0;
            while ((num = ism.read(buf)) > 0) {
                length += num;
                int progress = (int) (length * 100 / total);
                Log.i(Tag, "=============> " + progress);
                fos.write(buf, 0, num);
            }
            fos.flush();
            fos.close();
            ism.close();
            installApk();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downing = false;
    }

    private void installApk() {
        File apkFile = new File(savePath, apkName);
        if (!apkFile.exists()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean isGranted = getPackageManager().canRequestPackageInstalls();
            if (!isGranted) {
                Log.i(Tag, "=============> 请开启安装Apk权限");
                return;
            }
        }
        Log.i(Tag, "=============> apkFile: " + apkFile.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // 7.0  / com.xxx.xxx.fileprovider为上述manifest中provider所配置相同
                uri = FileProvider.getUriForFile(
                        mContext,
                        mContext.getPackageName() + ".file_provider",
                        apkFile
                );

                intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，该过程没有用户交互
            } else { // 7.0以下
                uri = Uri.fromFile(apkFile);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            Log.i(Tag, "=============> url:" + uri.getPath());
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
            Log.i(Tag, "=============> 安装中...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
