package com.ql.health.ui.act

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.ql.health.util.TextUtils
import com.ql.health.util.TimerCallback
import com.ql.health.util.TimerUtil
import java.util.Calendar

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:Check
 */
class LoginActivity : AppActivity() {
    private val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    val timerUtil = TimerUtil(60000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        binding.loginEditCommit.setOnClickListener {
            onLogin()
        }
        binding.loginEditAge.setOnClickListener {
            showDatePickerDialog()
        }
        binding.loginGetCode.setOnClickListener {
            getPhoneCode()
        }
    }

    private fun onLogin() {
        val phone = binding.loginEditPhone.text.toString()
        val code = binding.loginEditCode.text.toString()
        val name = binding.loginEditName.text.toString().trim()
        val date = binding.loginEditDate.text.toString()
        val sex = if (binding.loginSexMan.isEnabled) 1 else 2
        val height = binding.loginEditHeight.text.toString().toInt()
        val weight = binding.loginEditKg.text.toString().toInt()

        if (!TextUtils.isPhone(phone)) {
            onToast("请填写正确的手机号")
            return
        }
        if (code.isEmpty() || date.isEmpty()) {
            onToast("请填写完整信息")
            return
        }

        Client.main.loginAccount(LoginBody(phone, code, name, date, sex, height, weight))
            .enqueue(object : RFCallback<UserEntity>() {
                override fun onResult(res: UserEntity) {
                    Consts.USER_ID = res.id
                    Consts.USER_TOKEN = res.token
                    Consts.USER_NAME = res.realName ?: res.loginName
                    Consts.USER_PHONE = res.phone
                    Consts.USER_JSON = Gson().toJson(res)
                    finish()
                    onToast("登录成功")
                    gotoCheck()
                }

                override fun onFail(code: Int, e: Throwable) {
                    Log.i(TAG, "onFail: $code ${e.message}")
                    onToast("登录失败")
                }
            })
    }

    private fun getPhoneCode() {
        binding.loginGetCode.isEnabled = false
        val phone = binding.loginEditPhone.text.toString()
        if (!TextUtils.isPhone(phone)) {
            onToast("请填写正确的手机号")
            return
        }
        timerUtil.start(object : TimerCallback {
            override fun update(seconds: Long) {
                if (seconds <= 0) {
                    // 结束连接 -开始检测舌苔
                    binding.loginGetCode.isEnabled = true
                    binding.loginGetCode.text = "获取验证码"
                } else {
                    binding.loginGetCode.text = "重新获取(${seconds}s)"
                }
            }
        })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.CustomDatePickerDialog,
            { _, year3, month3, day3 ->
                val dateStr = String.format("%04d-%02d-%02d", year3, month3 + 1, day3);
                binding.loginEditDate.setText(dateStr)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun gotoCheck() {
        val intent = Intent(this, CheckActivity::class.java)
        startActivity(intent)
    }

    private fun onToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}