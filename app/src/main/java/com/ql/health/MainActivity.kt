package com.ql.health

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ql.health.config.Consts
import com.ql.health.config.DataConfig
import com.ql.health.custom.AppActivity
import com.ql.health.custom.OnShakeClick
import com.ql.health.model.entity.HomeItem
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.network.Client
import com.ql.health.ui.act.CameraActivity
import com.ql.health.ui.act.CheckActivity
import com.ql.health.ui.act.LoginActivity
import com.ql.health.ui.adapter.HomeAdapter
import com.ql.health.ui.adapter.MainAdapter
import com.ql.health.util.PermissionUtils

class MainActivity : AppActivity() {
    private var itemList = ArrayList<HomeItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.requestPermissions(this, PermissionUtils.DEFAULT, 8918)

        initView()
    }


    override fun onResume() {
        super.onResume()
        checkVersion()
    }

    private fun initView() {

        findViewById<Button>(R.id.now_playing).setOnClickListener(object : OnShakeClick() {
            override fun onTap(v: View?) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            val intent = Intent(this@MainActivity, CheckActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun initRecycler() {
        itemList = DataConfig.getHomeData()
        val recycler = findViewById<RecyclerView>(R.id.main_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = MainAdapter(this, itemList)
    }

    private fun initGrid() {
        itemList = DataConfig.getHomeData()
        val grid = findViewById<GridView>(R.id.main_grid)
        grid.adapter = HomeAdapter(this, itemList)
        grid.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                gotoChat(position)
            }
    }

    private fun gotoChat(mode: Int) {
        val cls = itemList[mode].cls
        var intent = Intent(this, cls)

        if (Consts.USER_ID == "") {
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
    }

    private fun checkVersion() {
        // 获取版本信息
        val info = packageManager.getPackageInfo(packageName, 0)
        val version = info.versionName
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
        Client.main.checkVersion(version, code).enqueue(object : RFCallback<Any>() {
            override fun onResult(res: Any) {

            }
        })
    }

}