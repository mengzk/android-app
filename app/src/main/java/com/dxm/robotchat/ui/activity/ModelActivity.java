package com.dxm.robotchat.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dxm.robotchat.R;
import com.dxm.robotchat.base.AppActivity;
import com.dxm.robotchat.base.RecyclerAdapter;
import com.dxm.robotchat.ui.adapter.ModelAdapter;
import com.dxm.robotchat.utils.PermissionManager;
import com.dxm.robotchat.utils.PermissionUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ModelActivity extends AppActivity {

    private final ArrayList<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        PermissionUtils.requestPermissions(this, PermissionManager.PERMISSION_AUDIO, 8918);

        initView();
    }

    private void initView() {
        itemList.add("测试对话");
        itemList.add("职业对话");

        RecyclerView recyclerView = findViewById(R.id.model_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ModelAdapter adapter = new ModelAdapter(this, itemList);
        adapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(String data, int position) {
                Log.i("", data);
                gotoChat(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void gotoChat(int mode) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);

        // 打开系统拍照
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri imageFileUri = getOutFileUri(1);
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
//        startActivityForResult(intent, 102);


//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//        Uri uri = Uri.fromFile(file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(intent, 1);
    }

    //得到输出文件的URI
    private Uri getOutFileUri(int fileType) {
        return Uri.fromFile(getOutFile(fileType));
    }
    //生成输出文件
    private File getOutFile(int fileType) {

        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_REMOVED.equals(storageState)){
            Toast.makeText(getApplicationContext(), "oh,no, SD卡不存在", Toast.LENGTH_SHORT).show();
            return null;
        }

        File mediaStorageDir = new File (Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                ,"MyPictures");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.i("MyPictures", "创建图片存储路径目录失败");
                Log.i("MyPictures", "mediaStorageDir : " + mediaStorageDir.getPath());
                return null;
            }
        }

        File file = new File(getFilePath(mediaStorageDir,fileType));

        return file;
    }

    //生成输出文件路径
    private String getFilePath(File mediaStorageDir, int fileType){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String filePath = mediaStorageDir.getPath() + File.separator;
        filePath += ("IMG_" + timeStamp + ".jpg");
        return filePath;
    }

    // 获取拍照结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "onActivityResult: " + requestCode + " " + resultCode);
    }

}