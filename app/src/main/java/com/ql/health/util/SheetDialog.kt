package com.ql.health.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import com.ql.health.R

/**
 * Author: Meng
 * Date: 2025/01/09
 * Modify: 2025/01/09
 * Desc:
 */
object SheetDialog {
    private var popupWindow: PopupWindow? = null

    @SuppressLint("InflateParams")
    fun show(activity: Activity, list: ArrayList<String>, listener: OnListener) {
        if (popupWindow != null) {
            popupWindow?.dismiss()
        }
        val inflater = LayoutInflater.from(activity)
        val view: View = inflater.inflate(R.layout.dialog_sheet, null, false)

        val listView = view.findViewById<ListView>(R.id.dialog_sheet_list)
        listView.adapter = SheetAdapter(activity, list)
        listView.setOnItemClickListener { _, _, position, _ ->
            listener.onClick(position)
            hide()
        }

        view.setOnClickListener { hide() }

        // Create the PopupWindow
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val flag = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        val root: View = activity.findViewById(android.R.id.content)
        val focusable = true

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


    class SheetAdapter(val context: Context, val list: ArrayList<String>) :
        BaseAdapter() {
        protected var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view = convertView
            val holder: ViewHolder
            if (view == null) {
                view = inflater.inflate(R.layout.apa_sheet, parent, false)
                holder = ViewHolder(view)
                view.setTag(holder)
            } else {
                holder = view.tag as ViewHolder
            }
            holder.nameView.text = list[position]
            return view
        }

        class ViewHolder(view: View?) {
            var nameView: TextView = view!!.findViewById(R.id.apa_sheet_value)
        }
    }

    interface OnListener {
        fun onClick(num: Int)
    }
}