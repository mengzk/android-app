package com.dxm.aimodel.modules.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Meng
 * Date: 2024/08/20
 * Modify: 2024/08/20
 * Desc:
 */
public class MySpeechRecognizer implements RecognitionListener {
    private static final String TAG = "MySpeechRecognizer";
    private SpeechRecognizer sRecognizer;
    private Context context;

    public MySpeechRecognizer(Context context) {
        sRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        sRecognizer.setRecognitionListener(this);
        this.context = context;
    }

    public boolean isAvailable() {
        @SuppressLint("QueryPermissionsNeeded") final List<ResolveInfo> list = this.context.getPackageManager().queryIntentServices(
                new Intent(RecognitionService.SERVICE_INTERFACE), 0);
        boolean available = !list.isEmpty();
        if(available) {
            Intent recognitionIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);

//            recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            sRecognizer.startListening(recognitionIntent);
        }
        Log.i(TAG, "available: " + available);
        return available;
    }

    public void stopListening() {
        sRecognizer.stopListening();
    }

    public void startListening() {
        isAvailable();
    }

    public void cancel() {
        sRecognizer.cancel();
    }

    public void destroy() {
        sRecognizer.destroy();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.i(TAG, "onReadyForSpeech: " + bundle.toString());
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.i(TAG, "onRmsChanged: " + v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.i(TAG, "onBufferReceived: " + bytes.length);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int i) {
        Log.i(TAG, "onError: " + i);
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> list = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (list != null && !list.isEmpty()) {
            String result = list.get(0);
            Log.i(TAG, "onResults: " + result);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

//    @Override
//    public void onSegmentResults(@NonNull Bundle segmentResults) {
//        RecognitionListener.super.onSegmentResults(segmentResults);
//    }
//
//    @Override
//    public void onEndOfSegmentedSession() {
//        RecognitionListener.super.onEndOfSegmentedSession();
//    }
//
//    @Override
//    public void onLanguageDetection(@NonNull Bundle results) {
//        RecognitionListener.super.onLanguageDetection(results);
//    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
