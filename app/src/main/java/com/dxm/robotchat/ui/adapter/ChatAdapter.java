package com.dxm.robotchat.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dxm.robotchat.R;
import com.dxm.robotchat.base.RecyclerAdapter;
import com.dxm.robotchat.modules.model.entity.MessageEntity;

import java.util.ArrayList;

/**
 * Author: meng
 * Date: 2024-04-05
 * Desc:
 */
public class ChatAdapter extends RecyclerAdapter<MessageEntity, ChatAdapter.HomeViewHolder> {

    public ChatAdapter(Context context, ArrayList<MessageEntity> list) {
        super(context, list);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_chat, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        MessageEntity item = itemList.get(position);
        if(item.getRole().equals("input")) {
            holder.itemIcon2.setVisibility(View.VISIBLE);
            holder.itemName2.setVisibility(View.VISIBLE);
            holder.itemIcon.setVisibility(View.GONE);
            holder.itemName.setVisibility(View.GONE);
            holder.itemName2.setText(item.getContent());
        }else  {
            holder.itemIcon.setVisibility(View.VISIBLE);
            holder.itemName.setVisibility(View.VISIBLE);
            holder.itemIcon2.setVisibility(View.GONE);
            holder.itemName2.setVisibility(View.GONE);
            holder.itemName.setText(item.getContent());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemView;
        public TextView itemName;
        public ImageView itemIcon;
        public TextView itemName2;
        public ImageView itemIcon2;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            itemView = view.findViewById(R.id.chat_item_box);
            itemName = view.findViewById(R.id.chat_item_text);
            itemName2 = view.findViewById(R.id.chat_item_text2);
            itemIcon = view.findViewById(R.id.chat_item_icon);
            itemIcon2 = view.findViewById(R.id.chat_item_icon2);
        }
    }
}
