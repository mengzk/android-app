package com.dxm.aimodel.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * LogTools
 *
 * @author Orion
 */
public class LogTools {

    private static final String TAG = LogTools.class.getName();

    private static List<OnLogListener> mAllListener = new ArrayList<>();

    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    private static StringBuilder mBuilder = new StringBuilder();

    public static void addLogListener(OnLogListener listener) {
        if (!mAllListener.contains(listener)) {
            mAllListener.add(listener);
        }
    }

    public static void info(String data) {
        Log.i(TAG, data);
        mBuilder.append(data + "\r\n");
        notifyListener(mBuilder.toString());

    }

    private static void notifyListener(final String data) {
        if (mAllListener.size() > 0) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (OnLogListener listener : mAllListener) {
                        listener.onLog(data);
                    }
                }
            });
        }
    }

    public static void clearHistory() {
        mBuilder.delete(0, mBuilder.length());
    }

    public static String getHistoryText(){
        return mBuilder.toString();
    }

    public interface OnLogListener {
        void onLog(String data);
    }
}
