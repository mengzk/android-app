package com.ql.health.ui.act

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.MutableOptionsBundle
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.ql.health.R
import com.ql.health.custom.AppActivity
import com.ql.health.util.TimerCallback
import com.ql.health.util.TimerUtil
import java.io.File
import java.util.concurrent.ExecutionException

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class CameraActivity : AppActivity() {
    private var downText: TextView? = null
    private var hintText: TextView? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var imageCapture: ImageCapture? = null
    private var recording: Recording? = null
    private val TAG = "CameraActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val mode = intent.getIntExtra("mode", 1) // 1: 拍照 2: 录像

        startCamera()

        downText = findViewById(R.id.count_down_timer)
        hintText = findViewById(R.id.face_detect_hint)
        val recordButton = findViewById<View>(R.id.camera_record)
        recordButton.setOnClickListener { v: View? ->
            if (recording != null) {
                recording!!.stop()
                recording = null
            } else {
                startRecording()
            }
        }

        val captureButton = findViewById<View>(R.id.camera_capture)
        captureButton.setOnClickListener { v: View? ->
            takePicture()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (recording != null) {
            recording!!.stop()
        }
    }

    private fun onDown() {
        val timerUtil = TimerUtil()
        timerUtil.start(object : TimerCallback {
            override fun update(seconds: Long) {
//                Log.i("TimerUtil", "Time left: $seconds")
                downText!!.text = seconds.toString()
                if (seconds <= 0) {
                    downText!!.visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.count_down_box).visibility = View.GONE
                    onRecording()
                }
            }
        })
    }

    private fun onRecording() {
        startRecording()
        val timerUtil = TimerUtil()
        timerUtil.start(object : TimerCallback {
            override fun update(seconds: Long) {
                if (seconds <= 0) {
                    if (recording != null) {
                        recording!!.stop()
                        recording = null
                    }
                }
            }
        })
    }

    // 开启相机
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
//                onDown() // 倒计时
            } catch (e: ExecutionException) {
                Log.e(TAG, "Error starting camera: ", e)
            } catch (e: InterruptedException) {
                Log.e(TAG, "Error starting camera: ", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // 绑定预览
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val recorder = Recorder.Builder().build()
        val previewView = findViewById<PreviewView>(R.id.preview_view)

        videoCapture = VideoCapture.withOutput(recorder)
        imageCapture = ImageCapture.Builder()
//            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setTargetResolution(Size(720, 1080))
            .build()

        // LENS_FACING_FRONT or LENS_FACING_BACK
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture, imageCapture)
        //        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    // 拍照
    private fun takePicture() {
        val photoFile =
            File(getExternalFilesDir(null), System.currentTimeMillis().toString() + ".png")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture!!.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    uploadFile(photoFile, "image/png")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: " + exception.message, exception)
                }
            })
    }

    // 录制视频
    private fun startRecording() {
        val videoFile =
            File(getExternalFilesDir(null), System.currentTimeMillis().toString() + ".mp4")
        val outputOptions = FileOutputOptions.Builder(videoFile).build()

        if (videoCapture == null) {
            Log.e(TAG, "VideoCapture is null")
            return
        }
        recording = videoCapture!!.getOutput().prepareRecording(this, outputOptions)
            .start(ContextCompat.getMainExecutor(this)) { ve ->
                if (ve is VideoRecordEvent.Start) {
//                    onToast("Recording started")
                } else if (ve is Finalize) {
                    if ((ve as Finalize).hasError()) {
                        Log.e(TAG, "Video capture failed: " + ve.error)
                    } else {
//                        onToast("Saved: " + videoFile.absolutePath)
                        uploadFile(videoFile, "video/mp4")
                    }
                }
            }
    }

    // 上传文件
    private fun uploadFile(file: File, media: String) {
        try {
            // 关闭页面，数据传递上一个页面
            intent.putExtra("path", file.absolutePath)
            setResult(RESULT_OK, intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 提示
    private fun onToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
