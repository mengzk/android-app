package com.dxm.aimodel.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.dxm.aimodel.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private VideoCapture<Recorder> videoCapture;
    private ImageCapture imageCapture;
    private Recording recording;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        startCamera();

        Button recordButton = findViewById(R.id.camera_record);
        recordButton.setOnClickListener(v -> {
            if (recording != null) {
                recording.stop();
                recording = null;
            } else {
                startRecording();
            }
        });

        Button captureButton = findViewById(R.id.camera_capture);
        captureButton.setOnClickListener(v -> {
            takePicture();
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error starting camera: ", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        Recorder recorder = new Recorder.Builder().build();
        PreviewView previewView = findViewById(R.id.preview_view);

        videoCapture = VideoCapture.withOutput(recorder);
        imageCapture = new ImageCapture.Builder().build();

        // LENS_FACING_FRONT or LENS_FACING_BACK
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture, imageCapture);
//        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void takePicture() {
        File photoFile = new File(getExternalFilesDir(null), System.currentTimeMillis()+".jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(CameraActivity.this, "Photo saved: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Photo capture failed: " + exception.getMessage(), exception);
            }
        });
    }

    private void startRecording() {
        File videoFile = new File(getExternalFilesDir(null), System.currentTimeMillis()+".mp4");
        FileOutputOptions outputOptions = new FileOutputOptions.Builder(videoFile).build();

        recording = videoCapture.getOutput().prepareRecording(this, outputOptions)
                .start(ContextCompat.getMainExecutor(this), videoRecordEvent -> {
                    if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                        Toast.makeText(CameraActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
                    } else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                        if (((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                            Log.e(TAG, "Video capture failed: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError());
                        } else {
                            Toast.makeText(CameraActivity.this, "Video saved: " + videoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}