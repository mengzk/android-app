package com.dxm.aimodel.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.dxm.aimodel.R;
import com.dxm.aimodel.modules.model.entity.HomeItem;

import java.util.ArrayList;

/**
 * Author: meng
 * Date: 2024-04-05
 * Desc:
 */
public class HomeAdapter extends BaseAdapter{
    protected ArrayList<HomeItem> itemList;
    protected LayoutInflater inflater;

    public HomeAdapter(Context context, ArrayList<HomeItem> list) {
        inflater = LayoutInflater.from(context);
        itemList = list;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_home, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeItem item = itemList.get(position);
        holder.itemName.setText(item.name);
        holder.itemDesc.setText(item.desc);
        return convertView;
    }

    public static class ViewHolder {
        public TextView itemName;
        public TextView itemDesc;
        public ViewHolder(View view) {
//            itemView = view.findViewById(R.id.home_grid_item);
            itemName = view.findViewById(R.id.home_grid_name);
            itemDesc = view.findViewById(R.id.home_grid_desc);
        }
    }
}
