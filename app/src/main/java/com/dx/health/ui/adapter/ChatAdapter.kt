package com.dx.health.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dx.health.R
import com.dx.health.custom.RecyclerAdapter
import com.dx.health.model.entity.ChatEntity


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class ChatAdapter(context: Context, list: ArrayList<ChatEntity>) :
    RecyclerAdapter<ChatEntity, ChatAdapter.HomeViewHolder>(context, list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view: View = inflater.inflate(R.layout.adapter_chat, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item: ChatEntity = itemList[position]
        if (item.getRole().equals("input")) {
            holder.itemIcon2.visibility = View.VISIBLE
            holder.itemName2.visibility = View.VISIBLE
            holder.itemIcon.visibility = View.GONE
            holder.itemName.visibility = View.GONE
            holder.itemName2.setText(item.getContent())
        } else {
            holder.itemIcon.visibility = View.VISIBLE
            holder.itemName.visibility = View.VISIBLE
            holder.itemIcon2.visibility = View.GONE
            holder.itemName2.visibility = View.GONE
            holder.itemName.setText(item.getContent())
        }

        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView
        var itemIcon: ImageView
        var itemName2: TextView
        var itemIcon2: ImageView

        init {
            var itemView = view.findViewById<LinearLayout>(R.id.chat_item_box)
            itemName = view.findViewById<TextView>(R.id.chat_item_text)
            itemName2 = view.findViewById<TextView>(R.id.chat_item_text2)
            itemIcon = view.findViewById<ImageView>(R.id.chat_item_icon)
            itemIcon2 = view.findViewById<ImageView>(R.id.chat_item_icon2)
        }
    }
}
