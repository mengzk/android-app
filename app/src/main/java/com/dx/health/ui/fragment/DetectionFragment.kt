package com.dx.health.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.dx.health.R
import com.dx.health.custom.VMFragment
import com.dx.health.databinding.FragmentDetectionBinding

class DetectionFragment: VMFragment<FragmentDetectionBinding>(R.layout.fragment_detection) {
    private var detection = 0;


    override fun onBindView(binding: FragmentDetectionBinding, savedInstanceState: Bundle?) {
        super.onBindView(binding, savedInstanceState)
        this.binding = binding
    }

    override fun lazyInit(binding: FragmentDetectionBinding) {
        val title = activity.intent.getStringExtra("title")
        detection = activity.intent.getIntExtra("menu", 0)
        initView(binding)
    }

    private fun initView(binding: FragmentDetectionBinding) {

//        val lm = GridLayoutManager(activity, 3)
//        val adapter = MenuAdapter(activity, itemList, menuColor)
//        adapter.setItemClickListener(object: OnItemClickListener<MenuDao> {
//            override fun itemClick(dao: MenuDao, position: Int) {
//                startHandle(dao)
//            }
//        })
//        binding.menuRecycler.layoutManager = lm
//        binding.menuRecycler.adapter = adapter
    }

    private fun openPage() {
        val bun = Bundle()
        bun.putInt("detection", detection)
        bun.putString("tag", "test")
        navigateTo(R.id.detection_to_pulseFragment, bun)
    }
}