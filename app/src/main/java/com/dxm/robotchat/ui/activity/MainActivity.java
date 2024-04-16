package com.dxm.robotchat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dxm.robotchat.R;
import com.dxm.robotchat.base.AppActivity;
import com.dxm.robotchat.base.RecyclerAdapter;
import com.dxm.robotchat.ui.adapter.HomeAdapter;
import com.dxm.robotchat.utils.PermissionManager;
import com.dxm.robotchat.utils.PermissionUtils;

import java.util.ArrayList;

public class MainActivity extends AppActivity {

    private final ArrayList<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtils.requestPermissions(this, PermissionManager.PERMISSION_AUDIO, 8918);

        initView();
    }

    private void initView() {
        itemList.add("知识库对话");
        itemList.add("普通对话");

        RecyclerView recyclerView = findViewById(R.id.home_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter adapter = new HomeAdapter(this, itemList);
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
    }

}