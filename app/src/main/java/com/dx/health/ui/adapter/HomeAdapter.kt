package com.dx.health.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dx.health.R
import com.dx.health.model.entity.HomeItem


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class HomeAdapter(context: Context?, list: ArrayList<HomeItem>) :
    BaseAdapter() {
    protected var itemList: ArrayList<HomeItem> = list
    protected var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_home, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.setTag(holder)
        } else {
            holder = convertView.tag as ViewHolder
        }
        val item: HomeItem = itemList[position]
        holder.itemName.setText(item.name)
        holder.itemDesc.setText(item.desc)
        return convertView
    }

    class ViewHolder(view: View?) {
        //            itemView = view.findViewById(R.id.home_grid_item);
        var itemName: TextView = view!!.findViewById<TextView>(R.id.home_grid_name)
        var itemDesc: TextView = view!!.findViewById<TextView>(R.id.home_grid_desc)
    }
}