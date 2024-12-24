package com.ql.health.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ql.health.R
import com.ql.health.custom.RecyclerAdapter
import com.ql.health.model.entity.PanelItem


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class PanelAdapter(context: Context, list: ArrayList<PanelItem>) :
    RecyclerAdapter<PanelItem, PanelAdapter.HomeViewHolder>(context, list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view: View = inflater.inflate(R.layout.adapter_panel, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item: PanelItem = itemList[position]

        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            var itemView = view.findViewById<ConstraintLayout>(R.id.panel_grid_item)
        }
    }
}
