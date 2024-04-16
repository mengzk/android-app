package com.dxm.robotchat.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dxm.robotchat.R;
import com.dxm.robotchat.base.AppActivity;
import com.dxm.robotchat.base.RecyclerAdapter;
import com.dxm.robotchat.modules.api.ChatApi;
import com.dxm.robotchat.modules.model.body.Model4Body;
import com.dxm.robotchat.modules.model.body.PromptBody;
import com.dxm.robotchat.modules.model.entity.ChatEntity;
import com.dxm.robotchat.modules.model.entity.MessageEntity;
import com.dxm.robotchat.modules.network.RequestCallback;
import com.dxm.robotchat.modules.network.RetrofitServices;
import com.dxm.robotchat.modules.record.AudioRecorder;
import com.dxm.robotchat.ui.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ChatActivity extends AppActivity {

    private AudioRecorder audioRecord;
    private ChatApi chatApi;
    private int mode;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private ArrayList<MessageEntity> chatList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatApi = RetrofitServices.getInstance().getChatApi();
        initView();
        mode = getIntent().getIntExtra("mode", 0);
    }

    private void initView() {
        recyclerView = findViewById(R.id.chat_list);
        Button chatBtn = findViewById(R.id.chat_play);
        EditText inputView = findViewById(R.id.chat_input);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = inputView.getText().toString().trim();
                chatList.add(new MessageEntity(msg));
                inputView.setText("");
                scrollToBottom();
                chatRequest(msg);
            }
        });
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        adapter = new ChatAdapter(this, chatList);
        adapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener<MessageEntity>() {
            @Override
            public void onItemClick(MessageEntity data, int position) {
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatList.size() - 1);
    }

    private void chatRequest(String msg) {
        switch (mode) {
            case 0:
                requestV3(msg);
                break;
            case 1:
                requestV4(msg);
                break;
            default:
                requestV4(msg);
                break;
        }
    }


    private void requestV3(String msg) {

        PromptBody body = new PromptBody(new PromptBody.ListItem("user", msg));

        chatApi.chatModelV3Api("1774346652367437824", body).enqueue(new RequestCallback<ChatEntity>() {
            @Override
            protected void onResult(ChatEntity data) {
                if (data != null) {
                    List<ChatEntity.Choice> choices = data.getChoices();
                    for (int i = 0; i < choices.size(); i++) {
                        chatList.add(new MessageEntity(choices.get(i).getMessage()));
                        scrollToBottom();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
                showToast("知识库ID不对");
            }
        });
    }




    private void requestV4(String msg) {

        Model4Body body = new Model4Body(new Model4Body.ListItem("user", msg));


        chatApi.chatModelV4Api(body).enqueue(new RequestCallback<ChatEntity>() {
            @Override
            protected void onResult(ChatEntity data) {
                if (data != null) {
                    List<ChatEntity.Choice> choices = data.getChoices();
                    for (int i = 0; i < choices.size(); i++) {
                        chatList.add(new MessageEntity(choices.get(i).getMessage()));
                        scrollToBottom();
                        Log.i("======>", "onResult: " + choices.get(i).toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatEntity> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioRecord != null && audioRecord.getStatus() == AudioRecorder.Status.STATUS_START) {
            audioRecord.pauseRecord();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(audioRecord != null) {
            audioRecord.release();
        }
    }
}