package com.ql.health.ui.act

import android.os.Bundle
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.ql.health.R
import com.ql.health.config.Consts
import com.ql.health.custom.VMNavActivity

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class DetectionActivity : VMNavActivity() {
    private var inited = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_detection)

        findViewById<TextView>(R.id.goto_home).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        if(!inited) {
            initView()
        }
    }

    private fun initView() {
        inited = true
        val nav = Navigation.findNavController(this, R.id.nav_host_detection)
        if (Consts.USER_NAME.isEmpty()) {
            nav.navigate(R.id.detection_to_personal)
        } else {
            nav.navigate(R.id.detection_to_pulse)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_detection)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val nav = findNavController(R.id.nav_host_detection)
        nav.navigateUp()
//        val isTop = nav.popBackStack()
////        Log.i("DetectionActivity", "-----> top: $isTop")
//        if (!isTop) {
//            super.onBackPressed()
//            finish()
//        }
    }
}