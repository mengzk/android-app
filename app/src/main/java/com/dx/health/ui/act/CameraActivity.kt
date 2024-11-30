package com.dx.health.ui.act

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.dx.health.R
import com.dx.health.custom.AppActivity
import com.dx.health.module.common.network.RFCallback
import com.dx.health.module.common.network.ResultData
import com.dx.health.module.network.Client
import com.dx.health.util.TimerCallback
import com.dx.health.util.TimerUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    private var videoCapture: VideoCapture<Recorder>? = null
    private var imageCapture: ImageCapture? = null
    private var recording: Recording? = null
    private val TAG = "CameraActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        startCamera()

        downText = findViewById(R.id.count_down_timer)
        val recordButton = findViewById<Button>(R.id.camera_record)
        recordButton.setOnClickListener { v: View? ->
            if (recording != null) {
                recording!!.stop()
                recording = null
            } else {
                startRecording()
            }
        }

        val captureButton = findViewById<Button>(R.id.camera_capture)
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
                    findViewById<LinearLayout>(R.id.count_down_box).visibility = View.GONE
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
                onDown() // 倒计时
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
        imageCapture = ImageCapture.Builder().build()

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
            File(getExternalFilesDir(null), System.currentTimeMillis().toString() + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture!!.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    uploadFile(photoFile, "image/jpg")
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
                    onToast("Recording started")
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
            val requestFile = file.asRequestBody(media.toMediaTypeOrNull())
            val body: Part = Part.createFormData("file", file.name, requestFile)
            val tag = "SD3123123".toRequestBody("text/plain".toMediaTypeOrNull())

            Client.main.upload3(body, tag).enqueue(object : RFCallback<ResultData<Any>>() {
                override fun onResult(data: ResultData<Any>) {
                    Log.d(TAG, "onResult: $data")
                }

                override fun onFail(code: Int, e: Throwable) {
                    Log.d(TAG, "onFail: $code ${e.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 提示
    private fun onToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
