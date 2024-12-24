package com.ql.health.ui.act

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ql.health.R
import com.ql.health.custom.AppActivity
import com.ql.health.databinding.ActResultBinding
import com.ql.health.ui.fragment.ResultFragment
import com.ql.health.ui.fragment.ZYIFragment


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class ResultActivity : AppActivity() {
    private lateinit var binding: ActResultBinding
    private var curFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.act_result)

        initView()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_result)
        binding.lifecycleOwner = this
        curFragment = ResultFragment()
        supportFragmentManager.commit {
            add(R.id.result_fragment, curFragment!!)
            setReorderingAllowed(true)
//            addToBackStack(null)
        }
    }

}