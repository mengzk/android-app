package com.ql.health.module.common.log

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ql.health.R
import com.ql.health.custom.AppActivity

/**
 * Author: Meng
 * Date: 2023/07/21
 * Modify: 2023/07/21
 * Desc: 日志列表
 */
class LogsActivity: AppActivity() {

    private lateinit var logAdapter: LogAdapter
    private lateinit var recycler: RecyclerView
    private var logList = ArrayList<LogDao>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_logs)

        initView()
    }

    private fun initView() {
//        findViewById<LinearLayout>(R.id.app_bar_back).setOnClickListener { finish() }
//        findViewById<TextView>(R.id.app_bar_title).text = "请求日志"
        recycler = findViewById(R.id.app_logs_recycler)

        logList.addAll(LogStore.getLogs())

        logAdapter = LogAdapter(this, logList)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = logAdapter

        logAdapter.setOnTapListener(object : LogAdapter.OnTapListener {
            override fun onTap(item: LogDao, position: Int) {
                gotoInfo(position)
            }
        })

//        LogStore.setOnLogListener(object : LogStore.OnLogListener{
//            override fun onChange() {
//                logList.addAll(LogStore.getLogs())
//                logAdapter.notifyItemChanged(logList.size-1)
//            }
//        })
    }

    private fun gotoInfo(idx: Int) {
        val intent = Intent(this, LogInfoActivity::class.java)
        intent.putExtra("idx", idx)
        startActivity(intent)
    }
}