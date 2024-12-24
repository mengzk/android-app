package com.ql.health.ui.act

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ql.health.R
import com.ql.health.custom.AppActivity
import com.ql.health.databinding.ActivityCheckBinding
import com.ql.health.module.event.Bus
import com.ql.health.ui.fragment.PulseFragment
import com.ql.health.ui.fragment.TongueFragment

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:Check
 */
class CheckActivity : AppActivity() {
    private lateinit var binding: ActivityCheckBinding
    private var curFragment: Fragment? = null

    private var pulseReportId = "ctdej3ejq8hlgmdttj90"
    private var tongueReportId = "3f73d428-b9d2-11ef-a67a-00163e2e82b2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_check)
        binding.lifecycleOwner = this
        initView()

        gotoResultH5()
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.remove("check-event")
    }

    private fun initView() {
        curFragment = PulseFragment()
//        curFragment = TongueFragment()
        supportFragmentManager.commit {
            add(R.id.check_container, curFragment!!)
            setReorderingAllowed(true)
        }
        Bus.add("check-event", object : Bus.BusListener {
            override fun onEvent(data: Bus.Action) {
                // 检测结果
                val id = data.data as String
                when(data.tag) {
                    "pulse" -> gotoTongue(id)
                    "tongue" -> gotoResult(id)
                }
            }
        })
    }
    // 开始检测 -舌苔
    private fun gotoTongue(resId: String) {
        pulseReportId = resId
        val fragment = TongueFragment()
        supportFragmentManager
            .beginTransaction()
            .hide(curFragment!!)
            .add(R.id.check_container, fragment)
            .commit()
    }

    private fun gotoResult(resId: String) {
        tongueReportId = resId
    }

    private fun gotoResultH5() {
        val intent = Intent(this, H5Activity::class.java)
        intent.putExtra("url", "result")
        intent.putExtra("title", "检测结果")
        intent.putExtra("tongueId", tongueReportId)
        intent.putExtra("pulseId", pulseReportId)
        startActivity(intent)
    }
}