package com.dxm.aimodel.base;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Author: Meng
 * Date: 2023/04/06
 * Desc:
 */
public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private OnItemClickListener<T> itemClickListener;
    protected ArrayList<T> itemList;
    protected LayoutInflater inflater;

    public RecyclerAdapter(Context context, ArrayList<T> list) {
        inflater = LayoutInflater.from(context);
        itemList = list;
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void setItemClickListener(OnItemClickListener<T> clickListener) {
        itemClickListener = clickListener;
    }

    protected void onItemClick(int position) {
        if(itemClickListener != null) {
            itemClickListener.onItemClick(itemList.get(position), position);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T data, int position);
    }
}