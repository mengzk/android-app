package com.ql.health.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.ql.health.R
import com.ql.health.config.Consts
import com.ql.health.custom.VMFragment
import com.ql.health.custom.widget.LoadAnim
import com.ql.health.databinding.FragmentTongueBinding
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.network.Client
import com.ql.health.module.robot.RobotMsg
import com.ql.health.ui.act.PhotoActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class TongueFragment : VMFragment<FragmentTongueBinding>(R.layout.fragment_tongue) {
    private val TAG = "TongueFragment"
    private var topView: TextView? = null
    private var curIndex = 0
    private var imgPath1 = ""
    private var imgPath2 = ""
    private var imgPath3 = ""

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i(TAG, "onActivityResult: ${it.resultCode}")
            if (it.resultCode == RESULT_OK && it.data != null) {
                Log.i(TAG, "onActivityResult: ${it.data!!.getIntExtra("skip", 0)}")
                val code = it.data!!.getIntExtra("skip", 0)
                if(code == 1) {
//                    sendEvent(Bus.Action("tongue", 0, ""))
                    gotoResult("")
                }else {
                    onCameraBack(it.data!!.getStringExtra("path") ?: "")
                }
            }
        }

    override fun lazyInit(binding: FragmentTongueBinding) {

        topView = activity.findViewById(R.id.page_top_title)
        topView?.text = "请按照提示，完成舌苔检测"
        binding.tongueFrontImg.setOnClickListener {
            takePicture(1)
        }
        binding.tongueVersoImg.setOnClickListener {
            takePicture(2)
        }
        binding.tongueFaceImg.setOnClickListener {
            takePicture(3)
        }
        binding.tongueSubmit.setOnClickListener {
            onCommit()
        }
        binding.tongueReset.setOnClickListener {
            imgPath1 = ""
            imgPath2 = ""
            imgPath3 = ""
            takePicture(1)
        }

        takePicture(1)

        RobotMsg.speech("请按照提示，完成舌苔检测! ")
    }

    private fun takePicture(index: Int) {
        curIndex = index
        val intent = Intent(activity, PhotoActivity::class.java)
        intent.putExtra("mode", index)
        launcher.launch(intent)
    }

    private fun onCameraBack(path: String) {
//        Log.i(TAG, "-----> video path: $path")
        val file = File(path)
        with(binding) {
            when (curIndex) {
                1 -> {
                    imgPath1 = path
                    tongueFrontImg.setImageURI(Uri.fromFile(file))

                    curIndex = 2
                    takePicture(2)
                }

                2 -> {
                    imgPath2 = path
                    tongueVersoImg.setImageURI(Uri.fromFile(file))

                    curIndex = 3
                    takePicture(3)
                }

                3 -> {
                    imgPath3 = path
                    tongueFaceImg.setImageURI(Uri.fromFile(file))

                    // 上传图片
                    onCommit()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun onCommit() {

        if (imgPath1.isEmpty() || imgPath2.isEmpty() || imgPath3.isEmpty()) {
            onToast("请拍摄补全图片")
            return
        }
        LoadAnim.show(activity, "诊断中...")
        // 时间格式化
        val dateStr = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val file1 = File(imgPath1)
        val file1Body = file1.asRequestBody(Client.PNGType)
        val body1: MultipartBody.Part =
            MultipartBody.Part.createFormData("files", "tf_${dateStr}.png", file1Body)

        val file2 = File(imgPath2)
        val file2Body = file2.asRequestBody(Client.PNGType)
        val body2: MultipartBody.Part =
            MultipartBody.Part.createFormData("files", "tb_${dateStr}.png", file2Body)

        val file3 = File(imgPath3)
        val file3Body = file3.asRequestBody(Client.PNGType)
        val body3: MultipartBody.Part =
            MultipartBody.Part.createFormData("files", "ff_${dateStr}.png", file3Body)

        val parts = ArrayList<MultipartBody.Part>()
        parts.add(body1)
        parts.add(body2)
        parts.add(body3)

        val scene = "1".toRequestBody(Client.TextType)
        val userId = Consts.USER_ID.toRequestBody(Client.TextType)

        // 上传数据
        Client.main.faceTongueCheck(parts,scene,userId)
            .enqueue(object : RFCallback<String>() {
                override fun onResult(res: String) {
                    LoadAnim.dismiss()
//                    sendEvent(Bus.Action("tongue", 0, res))
                    gotoResult(res)
                }

                override fun onFail(code: Int, e: Throwable) {
                    LoadAnim.dismiss()
                    val msg = e.message ?: "诊断失败，请重新提交"
                    onToast(msg)
                    topView?.text = msg
                    binding.tongueHint.text = msg
                }
            })
    }

    private fun gotoResult(msg: String) {
        val pulseRes = arguments?.getString("pulse") ?: ""
        val bun = Bundle()
        bun.putString("tongue", msg)
        bun.putString("pulse", pulseRes)
        navigateTo(R.id.tongue_to_result, bun)

    }

    private fun onToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}