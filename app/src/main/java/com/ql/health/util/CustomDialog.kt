package com.ql.health.util

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.ql.health.R

/**
 * Author: Meng
 * Date: 2025/01/09
 * Modify: 2025/01/09
 * Desc:
 */
object CustomDialog {
    private var popupWindow: PopupWindow? = null

    @SuppressLint("InflateParams")
    fun show(activity: Activity, title: String, message: String, listener: OnListener) {
        if (popupWindow != null) {
            popupWindow?.dismiss()
        }
        val inflater = activity.layoutInflater // LayoutInflater.from(context)

        val view: View = inflater.inflate(R.layout.dialog_custom, null, false)

        // Create the PopupWindow
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val flag = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        val root: View = activity.findViewById(android.R.id.content)
        val focusable = true

        view.findViewById<TextView>(R.id.custom_dialog_title).text = title
        view.findViewById<TextView>(R.id.custom_dialog_msg).text = message
        view.findViewById<View>(R.id.custom_dialog_ok).setOnClickListener {
            listener.onClose(true)
            hide()
        }
        view.findViewById<View>(R.id.custom_dialog_cancel).setOnClickListener {
            listener.onClose(false)
            hide()
        }

        val popup = PopupWindow(view, width, height, focusable)
        popupWindow = popup

        popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup.setOnDismissListener {
            val lp = activity.window.attributes
            lp.alpha = 1.0f
            activity.window.clearFlags(flag)
            activity.window.attributes = lp
        }
        popup.showAtLocation(root, Gravity.CENTER, 0, 0)
        val lp2 = activity.window.attributes
        lp2.alpha = 0.5f
        activity.window.addFlags(flag)
        activity.window.attributes = lp2
    }

    fun hide() {
        if (popupWindow != null) {
            popupWindow?.dismiss()
        }
    }

    interface OnListener {
        fun onClose(ok: Boolean)
    }
}