package com.ql.health.ui.fragment

import android.content.Intent
import android.widget.TextView
import com.ql.health.R
import com.ql.health.custom.VMFragment
import com.ql.health.databinding.FragmentResultBinding
import com.ql.health.ui.act.H5Activity

class ResultFragment: VMFragment<FragmentResultBinding>(R.layout.fragment_result) {

    override fun lazyInit(binding: FragmentResultBinding) {
        activity.findViewById<TextView>(R.id.page_top_title).text = "测量完成，点击查看详细报告"

        binding.lookResult.setOnClickListener{
            gotoReport()
        }
    }

    private fun gotoReport(){

        val pulse = arguments?.getString("pulse")
        val tongue = arguments?.getString("tongue")

        val intent = Intent(activity, H5Activity::class.java)

        intent.putExtra("pulse", pulse)
        intent.putExtra("tongue", tongue)
        activity.startActivity(intent)
    }

}