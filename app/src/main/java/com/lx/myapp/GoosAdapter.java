package com.lx.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by 李莘 on 2018/4/10.
 */

public class GoosAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<Goods> mData;

    public GoosAdapter() {}

    public GoosAdapter(LinkedList<Goods> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void add(Goods data) {
        if (mData == null) {
            mData = new LinkedList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_item,parent,false);
            holder = new ViewHolder();
            holder.id = convertView.findViewById(R.id.goods_id);
            holder.name = convertView.findViewById(R.id.goods_name);
            holder.price = convertView.findViewById(R.id.goods_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.id.setText(mData.get(position).getId());
        holder.name.setText(mData.get(position).getName());
        holder.price.setText(mData.get(position).getPrice());
        return convertView;
    }

    private class ViewHolder{
        TextView id;
        TextView name;
        TextView price;
    }
}
