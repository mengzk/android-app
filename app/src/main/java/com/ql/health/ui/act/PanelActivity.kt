package com.ql.health.ui.act

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ql.health.R
import com.ql.health.custom.AppActivity
import com.ql.health.custom.RecyclerAdapter
import com.ql.health.databinding.ActivityPanelBinding
import com.ql.health.model.entity.PanelItem
import com.ql.health.ui.adapter.PanelAdapter

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:Check
 */
class PanelActivity : AppActivity() {
    private val TAG = "CheckActivity"
    private lateinit var binding: ActivityPanelBinding

    private var adapter: PanelAdapter? = null
    private val itemList: ArrayList<PanelItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_panel)
        binding.lifecycleOwner = this
        initView()
    }

    private fun initView() {
        val lm = LinearLayoutManager(this)
        binding.panelRecycler.setLayoutManager(lm)
        adapter = PanelAdapter(this, itemList)
        adapter!!.setItemClickListener(object :
            RecyclerAdapter.OnItemClickListener<PanelItem> {
            override fun onItemClick(data: PanelItem, position: Int) {
            }
        })
        binding.panelRecycler.setAdapter(adapter)
    }

    // 获取拍照结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult: $requestCode $resultCode")
    }
}