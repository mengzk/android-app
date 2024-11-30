package com.dx.health.ui.fragment

import android.os.Bundle
import com.dx.health.R
import com.dx.health.custom.VMFragment
import com.dx.health.databinding.FragmentDetectionBinding

class PulseFragment: VMFragment<FragmentDetectionBinding>(R.layout.fragment_pulse) {

    override fun onBindView(binding: FragmentDetectionBinding, savedInstanceState: Bundle?) {
        super.onBindView(binding, savedInstanceState)
        this.binding = binding
    }

    override fun lazyInit(binding: FragmentDetectionBinding) {

    }

    fun initView() {
    }
}