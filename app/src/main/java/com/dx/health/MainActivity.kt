package com.dx.health

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import com.dx.health.custom.AppActivity
import com.dx.health.model.entity.HomeItem
import com.dx.health.module.libs.jtp.JTPulse
import com.dx.health.service.UpdateService
import com.dx.health.ui.act.AnswerActivity
import com.dx.health.ui.act.CameraActivity
import com.dx.health.ui.act.ChatActivity
import com.dx.health.ui.act.DetectionActivity
import com.dx.health.ui.act.H5Activity
import com.dx.health.ui.act.ResultActivity
import com.dx.health.ui.adapter.HomeAdapter
import com.dx.health.util.DialogUtils.showFullScreenPopup
import com.dx.health.util.PermissionUtils.PERMISSION_CAMERAS
import com.dx.health.util.PermissionUtils.requestPermissions


class MainActivity : AppActivity() {
    private val itemList = ArrayList<HomeItem>()
    private lateinit var pulse: JTPulse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(this, PERMISSION_CAMERAS, 8918)

        initView()
    }

    private fun initView() {
        pulse = JTPulse(this)

        itemList.add(HomeItem("中医检测", "基于现有中医大数据模型", 1))
        itemList.add(HomeItem("健康检测", "采用全国健康大数据模型", 2))
        itemList.add(HomeItem("检测规范", "检测中要求与规范说明", 3))
        itemList.add(HomeItem("检测范围", "可以检测项目的明细表", 4))
        itemList.add(HomeItem("联系我们", "欢迎投递建议", 5))

        val grid = findViewById<GridView>(R.id.main_grid)
        grid.adapter = HomeAdapter(this, itemList)

        grid.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                gotoChat(position)
            }

        play()
    }

    private fun gotoChat(mode: Int) {
        var intent: Intent? = null
        when (mode) {
            0 -> intent = Intent(this, ResultActivity::class.java)
            1 -> intent = Intent(this, DetectionActivity::class.java)
            2 -> intent = Intent(this, ChatActivity::class.java)
            3 -> intent = Intent(this, CameraActivity::class.java)
            4 -> intent = Intent(this, AnswerActivity::class.java)
        }
        if (intent != null) {
            startActivity(intent)
        }
    }

    private fun connectPulse() {
        pulse.connect()
    }

    private fun play() {
        val guideButton = findViewById<Button>(R.id.guide_button3)

        // Create scale animations
        val scaleX = ObjectAnimator.ofFloat(guideButton, "scaleX", 1f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(guideButton, "scaleY", 1f, 1.2f, 1f)
        scaleX.setDuration(1000) // 1 second
        scaleY.setDuration(1000) // 1 second

        // Create a ValueAnimator to repeat the animation
        val repeatAnimator = ValueAnimator.ofFloat(0f, 1f)
        repeatAnimator.setDuration(2000) // 2 seconds
        repeatAnimator.repeatCount = ValueAnimator.INFINITE

        repeatAnimator.interpolator = LinearInterpolator()
        repeatAnimator.addUpdateListener { animation: ValueAnimator ->
            val progress = animation.animatedValue as Float
            guideButton.scaleX = 1f + 0.2f * progress
            guideButton.scaleY = 1f + 0.2f * progress
        }

        // Combine animations
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, repeatAnimator)
//        animatorSet.playSequentially(scaleX, scaleY, repeatAnimator)
        animatorSet.start()
    }

    // 获取结果
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("", "onActivityResult: $requestCode $resultCode")
    }
}