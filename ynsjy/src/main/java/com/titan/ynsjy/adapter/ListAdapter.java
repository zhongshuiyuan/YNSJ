package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.titan.ynsjy.R;

import java.util.List;

/**
 * Created by whs on 2017/9/27
 * ListAdapter
 */

public class ListAdapter extends BaseAdapter {
    private List<GeodatabaseFeatureTable> list;
    private Context mContext;

    public ListAdapter(Context context, List<GeodatabaseFeatureTable> list) {
        this.list = list;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_featuretable, null);
            holder.ctv = (CheckedTextView) convertView.findViewById(R.id.ctv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ctv.setText(list.get(position).getTableName());
        //holder.ctv.s
        return convertView;

    }

    private class ViewHolder {
        CheckedTextView ctv;
    }
}
