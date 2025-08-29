package com.ql.health.ui.fragment

import android.os.Bundle
import android.util.Log
import com.ql.health.R
import com.ql.health.custom.VMFragment
import com.ql.health.databinding.NavDetectionBinding

class DetectionFragment : VMFragment<NavDetectionBinding>(R.layout.nav_detection) {

    override fun lazyInit(binding: NavDetectionBinding) {
        initView()
    }

    private fun initView() {
        Log.i("DetectionFragment", "-----> initView <-----")
//        if (Consts.USER_NAME.isEmpty()) {
//            gotoPersonal()
//        } else {
//            gotoPulse()
//        }
    }

    private fun gotoPersonal() {
        val bun = Bundle()
        // 打开新页面, 并销毁当前页面
        navigateTo(R.id.detection_to_personal, bun)
//        findNavController().popBackStack()
    }

    private fun gotoPulse() {
        val bun = Bundle()
        navigateTo(R.id.detection_to_pulse, bun)   // 会销毁当前页面
//        findNavController().popBackStack()
    }

    private fun openPage() {
        val bun = Bundle()
        bun.putString("tag", "-----> test <-----")
//                navController.navigate(R.id.action_detectionFragment_to_testFragment, bun)
    }
}