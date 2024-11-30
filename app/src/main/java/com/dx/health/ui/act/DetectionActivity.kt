package com.dx.health.ui.act

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.dx.health.R
import com.dx.health.custom.AppActivity
import com.dx.health.databinding.ActDetectionBinding

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class DetectionActivity : AppActivity() {
    private lateinit var binding: ActDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)

        initView()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_detection)
        binding.lifecycleOwner = this

    }

    private fun onBack() {
        val nav = Navigation.findNavController(this, R.id.fragment_detection)
//        nav.popBackStack(R.id.menuFragment, false)
        val back = nav.popBackStack()
        if (!back) {
            finish()
        }
    }
}