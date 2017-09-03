package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/2/002.
 * 审计适配器
 */

public class AuditAdapter extends BaseAdapter {
    private Context mContext;
    private Map<String,Object> map = new HashMap<>();
    private List<String> list = new ArrayList<>();

    public AuditAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
        //getList();
    }

    public List<String> getList() {
        for (String key : map.keySet()) {
            list.add(key);
        }
        return list;
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
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_choice_item, parent, false);
        }
        final TextView tv_key = ViewHolderUtil.get(convertView,R.id.audit_checkbox);
        String key = list.get(position);
        tv_key.setText(key);
        return convertView;
    }

}
