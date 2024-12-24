package com.ql.health.module.common.log

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import com.ql.health.R
import com.ql.health.custom.AppActivity

/**
 * Author: Meng
 * Date: 2023/07/21
 * Modify: 2023/07/21
 * Desc: 日志详情
 */
class LogInfoActivity: AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_log_info)

        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val idx = intent.getIntExtra("idx", 0)
        val log = LogStore.getLog(idx)
        if(log != null) {
            val statusView = findViewById<TextView>(R.id.app_log_info_status)
            val urlView = findViewById<TextView>(R.id.app_log_info_url)
            val paramView = findViewById<TextView>(R.id.app_log_info_params)
            val headerView = findViewById<TextView>(R.id.app_log_info_header)
            val resultView = findViewById<TextView>(R.id.app_log_info_result)

            val color = if(log.code != 0 && log.code != 200) "#F2453A" else "#00B42A"
            statusView.setTextColor(Color.parseColor(color))

            statusView.text = "${log.code} | ${log.method} | ${log.time}ms"
            urlView.text = log.url
            paramView.text = "Params: \n${log.params}"
            headerView.text = "Header: \n${log.headers}"
            resultView.text = "Response: \n${log.result}"
        }
    }
}