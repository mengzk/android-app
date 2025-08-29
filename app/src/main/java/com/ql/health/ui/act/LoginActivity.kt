package com.ql.health.ui.act

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.ql.health.R
import com.ql.health.config.Consts
import com.ql.health.custom.AppActivity
import com.ql.health.databinding.ActivityLoginBinding
import com.ql.health.model.body.LoginBody
import com.ql.health.model.entity.UserEntity
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.network.Client
import com.ql.health.module.robot.RobotMsg
import com.ql.health.util.TextUtils
import com.ql.health.util.TimerCallback
import com.ql.health.util.TimerUtil

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:Check
 */
class LoginActivity : AppActivity() {
    private val timerUtil = TimerUtil(60000)
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
//        setContentView(binding.root)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerUtil.stop()
    }

    private fun initView() {
        binding.loginCommit.setOnClickListener {
            onLogin()
        }
        binding.loginGetCode.setOnClickListener {
            getPhoneCode()
        }
        findViewById<TextView>(R.id.page_top_title).text = "请您输入手机号与验证码!进行登陆"

        RobotMsg.speech("你好，请您输入手机号与验证码!进行登陆! ")
    }

    private fun onLogin() {
        var phone = binding.loginEditPhone.text.toString()
        val code = binding.loginEditCode.text.toString()

        if (phone.isEmpty()) {
            phone = "17621739576"
        }

        if (!TextUtils.isPhone(phone)) {
            onToast("请填写正确的手机号")
            return
        }
        if (code.isEmpty()) {
            onToast("请输入验证码")
            return
        }

        Client.main.loginAccount(LoginBody(phone, code))
            .enqueue(object : RFCallback<UserEntity>() {
                override fun onResult(res: UserEntity) {
                    Consts.USER_ID = res.id
                    Consts.USER_TOKEN = "Bearer ${res.token}"
                    Consts.USER_NAME = res.realName ?: ""
                    Consts.USER_PHONE = res.phone
                    res.sn = Consts.DEVICE_SN
                    Consts.USER_JSON = Gson().toJson(res)
                    onToast("登录成功")
                    gotoCheck()
                }

                override fun onFail(code: Int, e: Throwable) {
                    Log.i("Login TAG", "onFail: $code ${e.message}")
                    onToast("登录失败")
                }
            })

    }

    private fun getPhoneCode() {
        val phone = binding.loginEditPhone.text.toString()
        if (!TextUtils.isPhone(phone)) {
            onToast("请填写正确的手机号")
            return
        }
        binding.loginGetCode.isEnabled = false
        binding.loginGetCode.setTextColor(resources.getColor(R.color.gray))

        timerUtil.start(object : TimerCallback {
            override fun update(seconds: Long) {
                if (seconds <= 0) {
                    // 结束连接 -开始检测舌苔
                    binding.loginGetCode.isEnabled = true
                    binding.loginGetCode.setTextColor(resources.getColor(R.color.white))
                    binding.loginGetCode.text = "获取验证码"
                } else {
                    binding.loginGetCode.text = "重新发送(${seconds}s)"
                }
            }
        })
    }

    private fun onToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun gotoCheck() {
        val intent = Intent(this, DetectionActivity::class.java)
        startActivity(intent)
        finish()
    }
}