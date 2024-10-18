package com.dxm.aimodel.ui;

import android.content.Intent;
import android.os.Bundle;

import com.dxm.aimodel.R;
import com.dxm.aimodel.base.AppActivity;
import com.dxm.aimodel.ui.activity.MainActivity;

/**
 * Author: Meng
 * Date: 2024/10/14
 * Modify: 2024/10/14
 * Desc:
 */
public class LaunchActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    private void gotoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
