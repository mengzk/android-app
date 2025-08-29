package com.ql.health.ui.act

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.Point
import android.graphics.Rect
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.MeteringRectangle
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ql.health.R
import com.ql.health.custom.AppActivity
import com.ql.health.custom.widget.FocusView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class PhotoActivity : AppActivity() {
    private var zoomLevel = 1f
    private val maxZoom = 5.0f
    private val REQUEST_CAMERA_PERMISSION = 1
    private var cameraDevice: CameraDevice? = null
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var imageReader: ImageReader
    private lateinit var focusView: FocusView

    private val ORIENTATION: SparseIntArray = SparseIntArray()

    init {
        ORIENTATION.append(Surface.ROTATION_0, 90)
        ORIENTATION.append(Surface.ROTATION_90, 0)
        ORIENTATION.append(Surface.ROTATION_180, 270)
        ORIENTATION.append(Surface.ROTATION_270, 180)
//        ORIENTATION.append(Surface.ROTATION_0, 0)
//        ORIENTATION.append(Surface.ROTATION_90, 90)
//        ORIENTATION.append(Surface.ROTATION_180, 180)
//        ORIENTATION.append(Surface.ROTATION_270, 270)
    }

    private var FRONT = false

    var cameraHandler: Handler? = null
    val cameraThread = HandlerThread("CameraThread")

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        cameraThread.start()
        cameraHandler = Handler(cameraThread.looper)

        val mode = intent.getIntExtra("mode", 1)

        val markView = findViewById<ImageView>(R.id.face_mask)

        if(mode < 3) {
            zoomLevel = 3.5f
            markView.setImageResource(R.mipmap.tongue_mask)
        }else {
            zoomLevel = 1.5f
            markView.setImageResource(R.mipmap.face_mask)
        }

        requestCameraPermission()

        findViewById<View>(R.id.photo_capture).setOnClickListener {
            takePicture()
        }
        findViewById<View>(R.id.skip_btn).setOnClickListener {
            intent.putExtra("path", imgPath)
            intent.putExtra("skip", 1)
            setResult(RESULT_OK, intent)
            finish()
        }
//        findViewById<ImageView>(R.id.switch_btn).setOnClickListener {
//            FRONT = !FRONT
//            cameraDevice?.close()
//            startCamera()
//        }
        val hintView = findViewById<TextView>(R.id.top_hint)
        hintView.text = when (mode) {
            1 -> "现在开始舌诊,请您伸出舌头将面部置于框内,并点击拍照按钮."
            2 -> "请把舌尖顶住上颚,暴露出舌下部位,并点击拍照按钮."
            else -> "接下来开始面诊,请将面部图像置于图框内,并点击拍照按钮."
        }

        focusView = findViewById(R.id.focus_view)

        val frameView = findViewById<FrameLayout>(R.id.preview_container)
        frameView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusRect = calculateFocusArea(event.x.toInt(), event.y.toInt())
                focusView.setFocusArea(focusRect)
                if(captureRequestBuilder != null) {
                    val focusArea = createFocusArea(Point(event.x.toInt(), event.y.toInt()), 200)
                    applyFocusArea(captureRequestBuilder, focusArea)
                }
                true
            } else {
                false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (cameraDevice != null) {
            cameraDevice?.close()
        }
    }

    @SuppressLint("ServiceCast")
    private fun startCamera() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (cameraManager.cameraIdList.isEmpty()) {
                Toast.makeText(this, "未发现可以摄像头", Toast.LENGTH_SHORT).show()
                return
            }
            var cameraId = ""
            cameraManager.cameraIdList.forEach {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                if (FRONT) {
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                        cameraId = it
                    }
                } else {
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                        cameraId = it
                    }
                }
            }
            if (cameraId.isEmpty()) {
                cameraId = cameraManager.cameraIdList[0]
            }
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val largest =
                map!!.getOutputSizes(ImageFormat.JPEG).maxByOrNull { it.height * it.width }!!
            // ImageReader.newInstance(largest.width, largest.height, ImageFormat.JPEG, 1)

            // Calculate dimensions for 3:4 aspect ratio
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val screenWidth = metrics.widthPixels
            val screenHeight = (screenWidth * 4) / 3

            imageReader =
                ImageReader.newInstance(screenWidth, screenHeight, ImageFormat.JPEG, 1)
            imageReader.setOnImageAvailableListener({ reader ->
                imgPath = getFileName()
                cameraHandler?.post(ImageSaver(reader.acquireNextImage(), imgPath))
            }, null)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createCameraPreviewSession(screenWidth, screenHeight)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun createCameraPreviewSession(width: Int, height: Int) {
        val maskView = findViewById<ImageView>(R.id.face_mask)
        val textureView = findViewById<TextureView>(R.id.texture_preview)
//        Log.d("Camera", "------> width: $width, height: $height, maskHeight: ${maskView.height}")
//        textureView.minimumWidth = width
//        val layoutParams = textureView.layoutParams
//        layoutParams.width = width
//        layoutParams.height = height
//        textureView.layoutParams = layoutParams
//        maskView.layoutParams = layoutParams

        val texture = textureView.surfaceTexture
//        texture?.setDefaultBufferSize(imageReader.width, imageReader.height)
        texture?.setDefaultBufferSize(width, height)
        val surface = Surface(texture)

        if (cameraDevice == null) return

        captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)

        if(zoomLevel < 2f) {
            // Enable auto-focus
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
        }
        // Example focus area at the center of the screen
//        val focusArea = createFocusArea(Point(width / 2, height / 2), 200)
//        applyFocusArea(captureRequestBuilder, focusArea)

        cameraDevice!!.createCaptureSession(
            listOf(surface, imageReader.surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    captureSession = session
                    captureRequestBuilder.set(
                        CaptureRequest.CONTROL_MODE,
                        CameraMetadata.CONTROL_MODE_AUTO
                    )
                    captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    // Handle configuration failure
                }
            },
            null
        )

        val focusRect = calculateFocusArea(width / 2, (height / 1.3).toInt())
        focusView.setFocusArea(focusRect)

        applyZoom(captureRequestBuilder, zoomLevel)
    }

    private fun takePicture() {
        if (cameraDevice == null) return

        val captureBuilder =
            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureBuilder.addTarget(imageReader.surface)
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

        // Example focus area at the center of the screen
//        val focusArea = createFocusArea(Point(imageReader.width / 2, imageReader.height / 2), 200)
//        applyFocusArea(captureBuilder, focusArea)
        if(zoomLevel < 2f) {
            // Enable auto-focus
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
        }

        applyZoom(captureBuilder, zoomLevel)

        val rotation = windowManager.defaultDisplay.rotation
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION[rotation])

        captureSession.capture(
            captureBuilder.build(),
            object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
                    // 延时时5s
                    val countDownTimer = object : CountDownTimer(5, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {
                            intent.putExtra("path", imgPath)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }.start()
                }
            },
            null
        )
    }

    // 对焦框
    private fun createFocusArea(point: Point, size: Int): MeteringRectangle {
        try {
            val left = point.x - size / 2
            val top = point.y - size / 2
            val right = point.x + size / 2
            val bottom = point.y + size / 2
            return MeteringRectangle(left, top, right - left, bottom - top, MeteringRectangle.METERING_WEIGHT_MAX)
        }catch (e: Exception) {
            e.printStackTrace()
            return MeteringRectangle(0, 0, 0, 0, MeteringRectangle.METERING_WEIGHT_MAX)
        }
    }

    // 对焦框
    private fun applyFocusArea(captureRequestBuilder: CaptureRequest.Builder, focusArea: MeteringRectangle) {
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, arrayOf(focusArea))
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO)
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START)
    }

    private fun calculateFocusArea(x: Int, y: Int): Rect {
        val size = 200
        val left = x - size / 2
        val top = y - size / 2
        val right = x + size / 2
        val bottom = y + size / 2
        return Rect(left, top, right, bottom)
    }

    // 电子变焦
    private fun applyZoom(captureRequestBuilder: CaptureRequest.Builder, zoomLevel: Float) {
        val zoomRect = calculateZoomRect(zoomLevel)
        captureRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoomRect)
    }

    // 电子变焦
    private fun calculateZoomRect(zoomLevel: Float): Rect {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraDevice!!.id)
        val sensorSize = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)!!
        val centerX = sensorSize.width() / 2
        val centerY = sensorSize.height() / 2
        val deltaX = (0.5f * sensorSize.width() / zoomLevel).toInt()
        val deltaY = (0.5f * sensorSize.height() / zoomLevel).toInt()
        return Rect(centerX - deltaX, centerY - deltaY, centerX + deltaX, centerY + deltaY)
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Show an explanation to the user
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                startCamera()
            } else {
                // Permission denied
            }
        }
    }


    class ImageSaver(private val mImage: Image, private val imgPath: String) : Runnable {

        @SuppressLint("SimpleDateFormat")
        override fun run() {
            val buffer = mImage.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer[data]

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(imgPath)
                fos.write(data, 0, data.size)

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        private var imgPath = ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFileName(): String {
        val path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/"
        val mImageFile = File(path)
        if (!mImageFile.exists()) {
            mImageFile.mkdir()
        }
        val num = (0..100).random() + 100
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = path + "P" + timeStamp + num + ".jpg"
        return fileName
    }
}