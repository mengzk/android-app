package com.ql.health.module.robot

import android.util.Log
import com.ainirobot.lib.shadowopk.RobotMessengerManager.triggerCommand
import com.alibaba.fastjson.JSON
import com.ql.health.module.robot.MRobotMessenger.RobotCallback
import org.json.JSONObject

/**
 * Author: Meng
 * Date: 2024/12/22
 * Desc:
 * https://doc.orionstar.com/blog/knowledge-base
 */

object RobotMsg {
    fun exercise(action: String?, mode: Int, num: Int): String {
        try {
            val json: JSONObject = JSONObject()
            json.put(
                "command",
                action
            ) // headUp:抬头;headDown:低头 ;headLeft:左转头 ;headRight:右转头 ;bodyForward:前进 ;bodyBack:后退 ;bodyLeft:左转 ;bodyRight:右转
            json.put("text", "body forward")
            /**
             * 头部参数：
             * hMode：左右转动的模式，绝对运动：absolute，相对运动：relative
             * vMode：上下运动的模式，绝对运动：absolute，相对运动：relative
             * hAngle：左右转动角度，范围：-120 ~ 120
             * vAngle：上下运动的角度，范围：0 ~ 90
             *
             * 底盘参数：
             * lineSpeed：线速度，取值范围：-1.2 ～ 1.2（正数为前进，负数为后退）
             * angularSpeed：角速度，取值范围：-2.2 ～ 2.2（正数为左转，负数为右转）
             */
            val params: JSONObject = JSONObject()
            params.put(if (mode == 1) "hMode" else "vMode", "relative")
            params.put(if (mode == 1) "hAngle" else "vAngle", num)
            json.put(
                "params",
                params
            ) //object 示例：{"hMode":"absolute","vMode":"relative","hAngle":10,"vAngle":10,"lineSpeed":0.5,"angularSpeed":1}
            return json.toString()
            //            RobotMessengerManager.INSTANCE.triggerCommand(json.toString());
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun exercise2(action: String?, mode: Int, num: Int): String {
        try {
            val json: JSONObject = JSONObject()
            json.put(
                "command",
                action
            ) // headUp:抬头;headDown:低头 ;headLeft:左转头 ;headRight:右转头 ;bodyForward:前进 ;bodyBack:后退 ;bodyLeft:左转 ;bodyRight:右转
            json.put("text", "body forward")
            /**
             * 底盘参数：
             * lineSpeed：线速度，取值范围：-1.2 ～ 1.2（正数为前进，负数为后退）
             * angularSpeed：角速度，取值范围：-2.2 ～ 2.2（正数为左转，负数为右转）
             */
            val params: JSONObject = JSONObject()
            params.put(if (mode == 1) "hAngle" else "vAngle", "relative")
            params.put(if (mode == 1) "lineSpeed" else "angularSpeed", num)
            json.put(
                "params",
                params
            ) //object 示例：{"hMode":"absolute","vMode":"relative","hAngle":10,"vAngle":10,"lineSpeed":0.5,"angularSpeed":1}
            return json.toString()
            //            RobotMessengerManager.INSTANCE.triggerCommand(json.toString());
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun vision() {
        try {
            val json: JSONObject = JSONObject()
            json.put("command", "startPersonAppear") // 操作指令： 根据条件找人
            json.put("personId", -1) // 根据id查找，用于检测特定id的人脸，默认-1
            json.put("personName", "") // 用于检测特定名字的人脸，默认为””
            json.put("maxDistance", 3) // 检测的最大距离，默认3米，超过该距离的人脸数据自动忽略
            json.put("maxFaceAngleX", 60) // 检测的最大人脸角度，默认60度
            json.put("isNeedInCompleteFace", false) // 是否包含不完成人脸检测，默认false
            json.put("incompleteFaceCacheTimeout", 3000) // 不完成人脸检测缓存时间，默认3000毫秒
            json.put("isNeedBody", false) // 是否检测人体，默认false
            json.put("isNeedRecognize", true) // 是否需要识别详细信息，默认true
            json.put("recognizeTimeout", 2000) // 人脸识别超时时间，默认2000毫秒
            json.put("appearTimeout", 7000) // 检测超时时间，默认7000毫秒
            json.put("text", "Enable personnel detection")
            json.put("params", "")
            triggerCommand(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigate() {
        try {
            val map: MutableMap<String, Any> = HashMap()
            /** 操作指令
             * map:获取地图数据;currentPosition:获取机器人当前坐标信息;gotoMapSite:运动到指定站点;naviAction:导航运动
             * stopMapSite:运动到指定站点
             */
            map["command"] = "map"
            map["text"] = "get the map" // 地图信息说明
            val params: JSONObject = JSONObject()
            params.put("coordinate_deviation", 0.5) //目的地范围，在目的地范围内均认为正常到达，默认0.5米
            params.put("moving_timeout_time", 20000) //机器人未移动超时时间，默认20000毫秒，超过该时间未移动，此次导航失败
            params.put("max_avoid_count", 5) //堵死状态检测次数，机器人在被堵死后会每隔一段时间检测一次，超过该次数后还是被堵死，则认为导航失败，默认5次
            params.put("avoid_interval_time", 1000) //堵死状态检测间隔时间，默认1000毫秒
            params.put("auto_reset_estimate", true) //导航过程中如果发生定位丢失，是否尝试自动重定位，默认true
            params.put("param_reset_estimate_count", 5) //重定位尝试次数，默认5次
            params.put("get_distance_interval_time", 1000) //距离信息上报间隔时间，默认1000毫秒
            params.put("param_linear_speed", 0.3) //导航线速度，可不填写，范围0.1-1.2 m/s
            params.put("param_angular_speed", 1) //导航角速度，可不填写，范围0.2-1.8 rad/s（1rad=180°/π，约 57.3°）
            params.put(
                "param_is_adjust_angle",
                true
            ) //到达目标点时机器的朝向是按照前进方向还是设置位置点时的方向，true 前进方向，false 设置位置点时的方向，默认false
            params.put(
                "param_is_need_avoid_notify_immediately",
                false
            ) //被障碍物堵死立即上报状态，默认false （1.34.0版本引入）
            params.put("param_destination_range", 0) //当目标点不可到达时，停在目标点附近的距离范围，默认0米（1.34.0版本引入)
            map["params"] = params
            triggerCommand(JSON.toJSONString(map))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun recharge() {
        try {
            val json: JSONObject = JSONObject()
            json.put("command", "startCharge") // 操作指令 startCharge: 开始充电;stopCharge: 停止充电
            json.put("text", "start charge")
            json.put("params", "")
            triggerCommand(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun speech(text: String?): String {
        try {
            val json: JSONObject = JSONObject()
            json.put(
                "command",
                "speechPlay"
            ) // 操作指令 speechPlay:开始播报;speechStop:停止播报;speechQuery:查询播报
            json.put("text", text)
            json.put("params", "")
            return json.toString()
            //            RobotMessengerManager.INSTANCE.triggerCommand(json.toString());
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    // 追踪人脸
    fun trackFace() {
        try {
            val json: JSONObject = JSONObject()
            json.put("command", "trackFace")
            json.put("personId", -1)
            json.put("maxDistance", 3)
            json.put("maxFaceAngleX", 60)
            json.put("isNeedInCompleteFace", false)
            json.put("disappearTimeout", 7000)
            json.put("isMultiPersonNotTrack", false)
            json.put("multiPersonNotTrackDistance", 2)
            json.put("isAllowMoveBody", true)
            json.put("text", "track face")
            triggerCommand(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 停止追踪人脸
    fun stopTrackFace() {
        try {
            val json: JSONObject = JSONObject()
            json.put("command", "stopTrackFace")
            json.put("personId", -1)
            json.put("maxDistance", 3)
            json.put("maxFaceAngleX", 60)
            json.put("isNeedInCompleteFace", false)
            json.put("disappearTimeout", 7000)
            json.put("isMultiPersonNotTrack", false)
            json.put("multiPersonNotTrackDistance", 2)
            json.put("isAllowMoveBody", true)
            json.put("text", "track face")
            triggerCommand(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun triggerToOpk() {
        try {
            val json: JSONObject = JSONObject()
            json.put("command", "triggerToOpk") // 操作指令：跳转指令
            json.put("text", "clicked then trigger to opk")
            json.put(
                "jumpNum",
                36362227
            ) //指令对应关系 [36362227：home页面；36362228：大眼睛页面；36362229：问路引领页面；36362228：天气页面]
            json.put("params", "")
            triggerCommand(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRobotSn() {
        try {
            val json: JSONObject = JSONObject()
            json.put("command", "getRobotSn") // 操作指令：获取SN
            json.put("text", "get the robot sn")
            triggerCommand(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendCommand(msg: String?) {
        try {
            triggerCommand(msg!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setRobotCallback() {
        try {
            MRobotMessenger.getInstance().setRobotCallback(object : RobotCallback {
                override fun onResult(result: String?) {
                    try {
                        Log.i("SHADOW_OPK", "收取callback内容: $result")
                        //                        JSONObject jsonObj = new JSONObject(result);
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}