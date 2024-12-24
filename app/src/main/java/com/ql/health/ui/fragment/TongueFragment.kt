package com.ql.health.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.ql.health.R
import com.ql.health.config.Consts
import com.ql.health.custom.AppFragment
import com.ql.health.custom.widget.LoadAnim
import com.ql.health.databinding.FragmentTongueBinding
import com.ql.health.model.body.TongueBody
import com.ql.health.model.entity.TongueEntity
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.event.Bus
import com.ql.health.module.network.Client
import com.ql.health.ui.act.CameraActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class TongueFragment : AppFragment<FragmentTongueBinding>(R.layout.fragment_tongue) {
    private val TAG = "TongueFragment"
    private var curIndex = 0
    private var imgPath1 = ""
    private var imgPath2 = ""
    private var imgPath3 = ""

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i(TAG, "onActivityResult: ${it.resultCode}")
            if (it.resultCode == RESULT_OK && it.data != null) {
                onCameraBack(it.data!!.getStringExtra("path") ?: "")
            }
        }

    override fun lazyInit(binding: FragmentTongueBinding) {

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
    }

    private fun takePicture(index: Int) {
        curIndex = index
        val intent = Intent(activity, CameraActivity::class.java)
        intent.putExtra("mode", 1)
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
                }

                2 -> {
                    imgPath2 = path
                    tongueVersoImg.setImageURI(Uri.fromFile(file))
                }

                3 -> {
                    imgPath3 = path
                    tongueFaceImg.setImageURI(Uri.fromFile(file))
                }
            }
        }
    }

    private fun uploadImage(file: File, index: Int) {

        val requestFile = file.asRequestBody(Client.ImageType)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)

        Client.main.upload(body).enqueue(object : RFCallback<Any>() {
            override fun onResult(res: Any) {}

            override fun onFail(code: Int, e: Throwable) {}
        })
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
                    Bus.send("check-event", Bus.Action("tongue", 0, res))
                }

                override fun onFail(code: Int, e: Throwable) {
                    LoadAnim.dismiss()
                    onToast("诊断失败，请重新提交")
                }
            })
    }

    private fun onToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}