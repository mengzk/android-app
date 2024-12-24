package com.ql.health.module.common.log

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ql.health.R
import com.ql.health.custom.RecyclerAdapter

/**
 * Author: Meng
 * Date: 2023/07/21
 * Modify: 2023/07/21
 * Desc:
 */
class LogAdapter(val context: Context, private val list: ArrayList<LogDao>): RecyclerAdapter<LogDao, LogAdapter.LogHolder>(context, list) {

    private val inflater2: LayoutInflater = LayoutInflater.from(context)
    private var tapListener: OnTapListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val view = inflater2.inflate(R.layout.app_ada_log, parent, false)
        return LogHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LogHolder, i: Int) {
        val item = list[i]
        holder.statusView.text = "${item.code}"
        val color = if(item.code != 0 && item.code != 200) "#F2453A" else "#00B42A"
        holder.statusView.setTextColor(Color.parseColor(color))
        holder.durationView.text = "${item.time}ms"
        holder.methodView.text = item.method
        holder.urlView.text = item.url
        holder.resView.text = item.result

        holder.layout.setOnClickListener {
            if(tapListener != null) {
                tapListener?.onTap(list[i], i)
            }
        }
    }

    fun setOnTapListener(listener: OnTapListener) {
        tapListener = listener
    }

    open class LogHolder(val view: View): ViewHolder(view) {
         val layout: LinearLayout = view.findViewById(R.id.app_item_log_layout)
         val statusView: TextView = view.findViewById(R.id.app_item_log_status) // F2453A/00B42A
         val methodView: TextView = view.findViewById(R.id.app_item_log_method)
         val durationView: TextView = view.findViewById(R.id.app_item_log_duration)
         val urlView: TextView = view.findViewById(R.id.app_item_log_url)
         val resView: TextView = view.findViewById(R.id.app_item_log_result)
    }

    interface OnTapListener {
        fun onTap(item: LogDao, position: Int)
    }
}