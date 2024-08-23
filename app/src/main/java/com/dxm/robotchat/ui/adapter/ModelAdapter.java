package com.dxm.robotchat.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dxm.robotchat.R;
import com.dxm.robotchat.base.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Author: meng
 * Date: 2024-04-05
 * Desc:
 */
public class ModelAdapter extends RecyclerAdapter<String, ModelAdapter.HomeViewHolder> {


    public ModelAdapter(Context context, ArrayList<String> list) {
        super(context, list);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_model, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.itemName.setText(item);
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
        public HomeViewHolder(@NonNull View view) {
            super(view);
            itemView = view.findViewById(R.id.model_item_box);
            itemName = view.findViewById(R.id.model_item_name);
        }
    }
}
