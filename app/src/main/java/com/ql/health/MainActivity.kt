package com.ql.health

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ql.health.config.Consts
import com.ql.health.custom.AppActivity
import com.ql.health.custom.OnShakeClick
import com.ql.health.model.entity.UpdateEntity
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.libs.jtp.JTPulse
import com.ql.health.module.network.Client
import com.ql.health.module.robot.RobotMsg
import com.ql.health.service.UpdateService
import com.ql.health.ui.act.DetectionActivity
import com.ql.health.ui.act.H5Activity
import com.ql.health.ui.act.LoginActivity
import com.ql.health.ui.act.PhotoActivity
import com.ql.health.util.CustomDialog
import com.ql.health.util.PermissionUtils

class MainActivity : AppActivity() {
    var topText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.requestPermissions(this, PermissionUtils.DEFAULT, 8918)
        initView()
    }

    override fun onResume() {
        super.onResume()
        checkVersion()
    }

    private fun initView() {
        topText = findViewById(R.id.page_top_title)
        findViewById<Button>(R.id.goto_login).setOnClickListener(object : OnShakeClick() {
            override fun onTap(v: View?) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                val intent = Intent(this@MainActivity, DetectionActivity::class.java)
//                val intent = Intent(this@MainActivity, PhotoActivity::class.java)
//                val intent = Intent(this@MainActivity, H5Activity::class.java)
                startActivity(intent)
            }
        })

        RobotMsg.speech("你好，欢迎来到尚瑞健康世界,我是您的健康助手，小瑞! 很高兴为您服务")

        val pulse = JTPulse.getInstance(this)
        pulse.onLoop()
    }

    private fun checkVersion() {
        // 获取版本信息
        val info = packageManager.getPackageInfo(packageName, 0)
        val version = info.versionName
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
        Consts.VERSION_NAME = version
        Consts.VERSION = code
        Consts.setUserAgent()

        Client.main.checkVersion(version, code).enqueue(object : RFCallback<UpdateEntity>() {
            override fun onResult(res: UpdateEntity) {
                if (res.versionSeq > code && res.forcedUpdate == 1) {
                    showVersionDialog(res.downloadUrl)
                }
            }
        })
    }

    // 显示一个简单的对话框
    private fun showVersionDialog(url: String) {
        CustomDialog.show(
            this,
            "发现新版本",
            "是否更新到最新版本",
            object : CustomDialog.OnListener {
                override fun onClose(ok: Boolean) {
                    if (ok) {
                        UpdateService.start(this@MainActivity, url)
                    }
                }
            })
    }

}