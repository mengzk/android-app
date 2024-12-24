package com.ql.health.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter

/**
 * Author: Meng
 * Date: 2024/12/03
 * Modify: 2024/12/03
 * Desc:
 */
abstract class AListAdapter<T>(var context: Context?, var list: ArrayList<T>) : BaseAdapter() {
     override fun getCount(): Int {
        return list.size
     }

    override fun getItem(position: Int): T {
        return list[position]
     }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        return bindView(item, convertView, parent)
    }

    abstract fun bindView(item: T, convertView: View?, parent: ViewGroup?): View

    abstract class OnItemClickListener<T>: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            onClick(parent.getItemAtPosition(position) as T, position)
        }
        abstract fun onClick(data: T, position: Int)
    }
}