package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/6/7.
 * 新建图层目标文件夹选择
 */

public class FileSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<File> folders = new ArrayList<>();

    public FileSelectAdapter(Context context,List<File> folders){
        this.mContext = context;
        this.folders = folders;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        return folders.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_txt, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String cname = folders.get(position).getName();
        holder.tv.setText(cname);
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}
