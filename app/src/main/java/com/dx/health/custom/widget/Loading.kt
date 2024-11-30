package com.dx.health.custom.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.dx.health.R


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object Loading {
    private var dialog: Dialog? = null
    private var count = 0

    fun show(context: Context?, msg: String?) {
        var msg = msg
        if (dialog != null && dialog!!.isShowing) {
            return
        }
        count = 0
        dialog = Dialog(context!!)
        val view: View = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null, false)
        if (msg == null) {
            msg = "加载中..."
        }
        (view.findViewById<View>(R.id.tv_loading_text) as TextView).text =
            msg
        view.setOnClickListener { v: View? ->
            if (count > 5) {
                dialog!!.dismiss()
            }
            count++
        }
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