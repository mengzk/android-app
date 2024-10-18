package com.dxm.aimodel.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dxm.aimodel.R;
import com.dxm.aimodel.base.AppActivity;
import com.dxm.aimodel.base.RecyclerAdapter;
import com.dxm.aimodel.modules.api.ChatApi;
import com.dxm.aimodel.modules.model.body.Model4Body;
import com.dxm.aimodel.modules.model.body.PromptBody;
import com.dxm.aimodel.modules.model.entity.ChatEntity;
import com.dxm.aimodel.modules.model.entity.MessageEntity;
import com.dxm.aimodel.modules.network.ReCallback;
import com.dxm.aimodel.modules.network.RetrofitServices;
import com.dxm.aimodel.modules.record.AudioRecorder;
import com.dxm.aimodel.modules.record.MyRecorder;
import com.dxm.aimodel.modules.record.MySpeechRecognizer;
import com.dxm.aimodel.ui.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ChatActivity extends AppActivity {

    private MyRecorder audioRecord;
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

//        MySpeechRecognizer mySpeechRecognizer = new MySpeechRecognizer(this);
//        mySpeechRecognizer.startListening();

        audioRecord = new MyRecorder();
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
                audioRecord.start();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatList.size() - 1);
    }

    private void chatRequest(String msg) {
        switch (mode) {
            case 0:
                requestV4(msg);
                break;
            case 1:
                requestV3(msg);
                break;
            default:
                requestV4(msg);
                break;
        }
    }


    private void requestV3(String msg) {

        PromptBody body = new PromptBody(new PromptBody.ListItem("user", msg));

        chatApi.chatModelV3Api("1774346652367437824", body).enqueue(new ReCallback<ChatEntity>() {
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

        chatApi.chatModelV4Api(body).enqueue(new ReCallback<ChatEntity>() {
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
//        if (audioRecord != null && audioRecord.getStatus() == AudioRecorder.Status.STATUS_START) {
//            audioRecord.pauseRecord();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(audioRecord != null) {
            audioRecord.stop();
        }
    }
}