package com.dx.health.ui.act

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dx.health.R
import com.dx.health.custom.AppActivity
import com.dx.health.custom.RecyclerAdapter
import com.dx.health.model.entity.ChatEntity
import com.dx.health.ui.adapter.ChatAdapter


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class ChatActivity : AppActivity() {
    private var mode = 0
    private lateinit var recyclerView: RecyclerView
    private var adapter: ChatAdapter? = null
    private val chatList: ArrayList<ChatEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mode = intent.getIntExtra("mode", 0)

        initView()
    }

    private fun initView() {
        recyclerView = findViewById<RecyclerView>(R.id.chat_list)
        val chatBtn = findViewById<Button>(R.id.chat_play)
        val inputView = findViewById<EditText>(R.id.chat_input)

        chatBtn.setOnClickListener {
            val msg = inputView.text.toString().trim { it <= ' ' }
            chatList.add(ChatEntity(msg))
            inputView.setText("")
            scrollToBottom()
            chatRequest(msg)
        }
        val lm = LinearLayoutManager(this)
        recyclerView.setLayoutManager(lm)
        adapter = ChatAdapter(this, chatList)
        adapter!!.setItemClickListener(object :
            RecyclerAdapter.OnItemClickListener<ChatEntity> {
            override fun onItemClick(data: ChatEntity, position: Int) {
            }
        })
        recyclerView.setAdapter(adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun scrollToBottom() {
        adapter!!.notifyDataSetChanged()
        recyclerView.scrollToPosition(chatList.size - 1)
    }

    private fun chatRequest(msg: String) {

    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}