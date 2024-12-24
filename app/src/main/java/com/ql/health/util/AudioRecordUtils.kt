package com.ql.health.util

import android.media.MediaRecorder
import android.text.format.DateFormat
import android.util.Log
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Locale


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object AudioRecordUtils {
    private var mMediaRecorder: MediaRecorder? = null
    private var filePath = ""
    private const val audioDir = ""
    fun start() {
// 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) mMediaRecorder = MediaRecorder()
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC) // 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            val fileName: String =
                DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)).toString() + ".m4a"

            filePath = audioDir + fileName
            /* ③准备 */
            mMediaRecorder!!.setOutputFile(filePath)
            mMediaRecorder!!.prepare()
            /* ④开始 */
            mMediaRecorder!!.start()
        } catch (e: IllegalStateException) {
            Log.i("call startAmr(File mRecAudioFile) failed!", e.message!!)
        } catch (e: IOException) {
            Log.i("call startAmr(File mRecAudioFile) failed!", e.message!!)
        }
    }

    fun stop() {
        try {
            mMediaRecorder!!.stop()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            filePath = ""
        } catch (e: RuntimeException) {
            Log.e("", e.toString())
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null

            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
            filePath = ""
        }
    }

    fun countTime() {
        val timeCount = 0
    }

    fun FormatMiss(miss: Int): String {
        val hh = if ((miss / 3600) > 9) (miss / 3600).toString() + "" else "0" + miss / 3600
        val mm =
            if ((miss % 3600) / 60 > 9) ((miss % 3600) / 60).toString() + "" else "0" + (miss % 3600) / 60
        val ss =
            if ((miss % 3600) % 60 > 9) ((miss % 3600) % 60).toString() + "" else "0" + (miss % 3600) % 60
        return "$hh:$mm:$ss"
    }

    fun play() {
    }

    fun reset() {
    }
}
