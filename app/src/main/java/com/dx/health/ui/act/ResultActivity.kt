package com.dx.health.ui.act

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dx.health.R
import com.dx.health.custom.AppActivity
import com.dx.health.databinding.ActResultBinding
import com.dx.health.ui.fragment.ResultFragment
import com.dx.health.ui.fragment.ZYIFragment


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

    private fun switchFragment(fragment: Fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (curFragment != null && curFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                supportFragmentManager
                    .beginTransaction()
                    .hide(curFragment!!)
                    .add(R.id.result_fragment, fragment)
                    .commit()
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                supportFragmentManager
                    .beginTransaction()
                    .hide(curFragment!!)
                    .show(fragment)
                    .commit()
            }
            curFragment = fragment
        }
    }


}