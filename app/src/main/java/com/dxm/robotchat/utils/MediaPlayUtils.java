package com.dxm.robotchat.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Author: Meng
 * Date: 2023/04/23
 * Desc:
 */
public class MediaPlayUtils {
    public static MediaPlayer mediaPlayer;

    public static void play(String url) {
        try{
//            url = "https://sk-sycdn.kuwo.cn/3507004c58765fa903ded9024ed20944/644689af/resource/n3/80/81/768996291.mp3";
            if(mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }else {
                mediaPlayer.reset();
                mediaPlayer.stop();
            }
            mediaPlayer.setDataSource(url);
            mediaPlayer.setVolume(100,100);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static void restart() {
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void pause() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static boolean isPlay() {
        if(mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }
}
