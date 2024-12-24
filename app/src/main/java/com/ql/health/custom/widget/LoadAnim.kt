package com.ql.health.custom.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.ql.health.R

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object LoadAnim {
    private var dialog: Dialog? = null
    private var count = 0

    fun show(context: Context, text: String?) {
        var msg = text
        if (dialog != null && dialog!!.isShowing) {
            return
        }
        count = 0
        dialog = Dialog(context)

        val view: View = LayoutInflater.from(context).inflate(R.layout.load_anim, null, false)
        if (msg == null) {
            msg = "报告生成中..."
        }
        view.findViewById<TextView>(R.id.tv_load_anim_text).text = msg
        view.setOnClickListener { v: View? ->
            if (count > 5) {
                dialog!!.dismiss()
            }
            count++
        }
        // 设置dialog倒角为圆角
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(view)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
}