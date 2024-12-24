package com.ql.health.config

import com.ql.health.model.entity.HomeItem
import com.ql.health.model.entity.InputItem
import com.ql.health.ui.act.AnswerActivity
import com.ql.health.ui.act.CameraActivity
import com.ql.health.ui.act.ChatActivity
import com.ql.health.ui.act.CheckActivity
import com.ql.health.ui.act.H5Activity
import com.ql.health.ui.act.LoginActivity
import com.ql.health.ui.act.PanelActivity

object DataConfig {

    // 首页数据
    fun getHomeData(): ArrayList<HomeItem> {
        val list = ArrayList<HomeItem>()
        list.add(HomeItem("体征检测", "检测体温，脉搏，呼吸，血压等信息", PanelActivity::class.java))
        list.add(HomeItem("AI脉诊", "让AI帮您号脉，准确又便捷", ChatActivity::class.java))
        list.add(HomeItem("舌面珍", "继承中医手段，望闻问切", CameraActivity::class.java))
        list.add(HomeItem("问诊", "向我说明您的困扰吧", AnswerActivity::class.java))
        list.add(HomeItem("心理检测", "检测您的心理，查看是否有潜在风险", CheckActivity::class.java))
        list.add(HomeItem("测试", "欢迎投递建议", H5Activity::class.java))

        return list
    }

    // 获取输入方式
    fun getInputItem(): ArrayList<InputItem> {
        val list = ArrayList<InputItem>()
        list.add(InputItem("身份证", "检测体温，脉搏，呼吸，血压等信息", LoginActivity::class.java))
        list.add(InputItem("医保卡", "让AI帮您号脉，准确又便捷", LoginActivity::class.java))
        list.add(InputItem("手机号", "继承中医手段，望闻问切", LoginActivity::class.java))

        return list
    }
}