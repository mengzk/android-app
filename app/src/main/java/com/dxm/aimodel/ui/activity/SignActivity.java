package com.dxm.aimodel.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dxm.aimodel.R;
import com.dxm.aimodel.base.AppActivity;

public class SignActivity extends AppActivity {
    private static final String TAG = "SignActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
    }

    private void initView() {


    }

    // 获取拍照结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode + " " + resultCode);
    }

}