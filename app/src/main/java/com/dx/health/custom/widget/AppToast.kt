package com.dx.health.custom.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.dx.health.R


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object AppToast {
    private var toast: Toast? = null
    private var dialog: Dialog? = null

    fun loading(activity: Activity, msg: String?) {
        if (dialog != null && dialog!!.isShowing) {
            return
        }

        // Inflate the custom layout/view
        val inflater = activity.layoutInflater
        val layout: View = inflater.inflate(R.layout.loading_dialog, null)

        val text = layout.findViewById<TextView>(R.id.tv_loading_text)
        text.text = msg

        // Create the dialog
        dialog = Dialog(activity, R.style.CustomDialog)
        dialog!!.setContentView(layout)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun hide() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    fun show(context: Context?, msg: String?) {
        if (toast != null) {
            toast!!.cancel()
        }
        // Inflate the custom layout/view
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast_custom, null)

        // Set the text and image in the custom layout
        val text = layout.findViewById<TextView>(R.id.tv_toast_text)
        text.text = msg

        //        ImageView image = layout.findViewById(R.id.toast_image);
//        image.setImageResource(R.drawable.ic_toast_icon); // Set your desired icon

        // Create and show the custom Toast
        toast = Toast(context)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.setView(layout)
        toast!!.show()
    }
}