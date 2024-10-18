package com.dxm.aimodel.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dxm.aimodel.R;
import java.util.ArrayList;

/**
 * Author: meng
 * Date: 2024-04-05
 * Desc:
 */
public class ModelAdapter extends BaseAdapter{
    protected ArrayList<String> itemList;
    protected LayoutInflater inflater;

    public ModelAdapter(Context context, ArrayList<String> list) {
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
            convertView = inflater.inflate(R.layout.adapter_model, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemName.setText(itemList.get(position));
        return convertView;
    }

    public static class ViewHolder {
        public ConstraintLayout itemView;
        public TextView itemName;
        public ViewHolder(View view) {
            itemView = view.findViewById(R.id.model_item_box);
            itemName = view.findViewById(R.id.model_item_name);
        }
    }
}
