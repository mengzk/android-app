package com.dxm.aimodel.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.dxm.aimodel.R;
import com.dxm.aimodel.base.AppActivity;
import com.dxm.aimodel.base.Loading;
import com.dxm.aimodel.modules.model.entity.HomeItem;
import com.dxm.aimodel.service.UpdateService;
import com.dxm.aimodel.ui.adapter.HomeAdapter;
import com.dxm.aimodel.utils.DialogUtils;
import com.dxm.aimodel.utils.PermissionManager;
import com.dxm.aimodel.utils.PermissionUtils;
import java.util.ArrayList;

public class MainActivity extends AppActivity {

    private final ArrayList<HomeItem> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtils.requestPermissions(this, PermissionManager.PERMISSION_AUDIO, 8918);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        UpdateService.start(this);
    }

    private void initView() {
//        startActivity(new Intent(this, H5Activity.class));
        itemList.add(new HomeItem("中医检测", "基于现有中医大数据模型", 1));
        itemList.add(new HomeItem("健康检测", "采用全国健康大数据模型", 2));
        itemList.add(new HomeItem("检测规范", "检测中要求与规范说明", 3));
        itemList.add(new HomeItem("检测范围", "可以检测项目的明细表", 4));
        itemList.add(new HomeItem("联系我们", "欢迎投递建议", 5));

        GridView grid = findViewById(R.id.main_grid);
        grid.setAdapter(new HomeAdapter(this, itemList));

        grid.setOnItemClickListener((parent, view, position, id) -> {
            gotoChat(position);
        });
    }

    private void gotoChat(int mode) {
        Intent intent = null;
        switch (mode) {
            case 0:
            case 1:
                intent = new Intent(this, ChatActivity.class);
                intent.putExtra("mode", mode);
                break;
            case 2:
                DialogUtils.inputDialog(this);
                break;
            case 3:
                intent = new Intent(this, CameraActivity.class);
                break;
            case 4:
                intent = new Intent(this, H5Activity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    // 获取拍照结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "onActivityResult: " + requestCode + " " + resultCode);
    }
}