package com.dxm.robotchat.modules.record;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Author: Meng
 * Date: 2024/08/20
 * Modify: 2024/08/20
 * Desc: 录音
 */
public class MyRecorder {
    private static final String TAG = "MyRecorder";
    private MediaRecorder recorder;
    private MediaPlayer player;
    private boolean isRecording = false;
    private String savePath;

    public void start() {
        if (recorder == null) {
            recorder = new MediaRecorder();
        }
        isRecording = true;
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioChannels(1);
        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(192000);

        savePath = Environment.getExternalStorageDirectory().getPath();
        // 时间格式化
        savePath += "/record.mp3";
//        File file = new File(savePath);
//        if(!file.exists()) {
//            file.mkdirs();
//        }

        recorder.setOutputFile(savePath);
        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            isRecording = false;
        }
    }

    public void stop() {
        if (recorder != null && isRecording) {
            recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
        }
    }

    public void pause() {
        if (recorder != null && isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                recorder.pause();
            }
            isRecording = false;
        }
    }

    public void resume() {
        if (recorder != null && !isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                recorder.resume();
            }
            isRecording = true;
        }
    }

    public void reset() {
        if (recorder != null) {
            recorder.reset();
            isRecording = false;
        }
    }

    public void play() {
        player = new MediaPlayer();
        try {
            player.setDataSource(savePath);
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        if (player != null) {
            try {
                player.stop();
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

}
