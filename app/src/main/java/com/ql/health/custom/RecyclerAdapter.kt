package com.ql.health.custom

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
abstract class RecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
    context: Context,
    protected var itemList: ArrayList<T>
) :
    RecyclerView.Adapter<VH?>() {

    private var itemClickListener: OnItemClickListener<T>? = null
    protected var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItemClickListener(clickListener: OnItemClickListener<T>?) {
        itemClickListener = clickListener
    }

    protected fun onItemClick(position: Int) {
        if (itemClickListener != null) {
            itemClickListener!!.onItemClick(itemList[position], position)
        }
    }

    interface OnItemClickListener<T> {
        fun onItemClick(data: T, position: Int)
    }
}