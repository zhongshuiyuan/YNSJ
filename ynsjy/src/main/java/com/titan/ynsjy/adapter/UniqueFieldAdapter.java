package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.dialog.ColorDialog;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.BussUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by hanyw on 2017/8/29/029.
 * 唯一值类型适配器
 */

public class UniqueFieldAdapter extends BaseAdapter {
    private Context mContext;
    private Map<String,String> fieldMap;
    private List<String> keyList;
    private LayoutInflater inflater = null;
    private SeekBar seekBar;
    private MyLayer myLayer;
    public UniqueFieldAdapter(Context context, Map<String,String> fieldMap,SeekBar seekBar, MyLayer myLayer){
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.fieldMap = fieldMap;
        this.seekBar = seekBar;
        this.myLayer = myLayer;
        getKeyList();
    }
    private void getKeyList(){
        keyList = new ArrayList<>();
        for (String key : fieldMap.keySet()) {
            keyList.add(key);
        }
    }

    @Override
    public int getCount() {
        return keyList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.unique_field_item, parent,false);
            holder.tv_field = (TextView) convertView.findViewById(R.id.unique_field);
            holder.colorSelect = (TextView) convertView.findViewById(R.id.unique_color);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String field = keyList.get(position);
        holder.tv_field.setText(field);
        holder.colorSelect.setBackgroundColor(getColor(field));
        holder.colorSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDialog colorDialog = new ColorDialog(mContext,R.style.Dialog,2, holder.colorSelect, seekBar, myLayer,field);
                BussUtil.setDialogParam(mContext, colorDialog, 0.35, 0.35, 0.6, 0.6);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_field;
        TextView colorSelect;
    }

    private int getColor(String field){
        int color;
        switch (field){
            case "耕地":
                color=mContext.getResources().getColor(R.color.color_2);
                break;
            case "林地":
                color=mContext.getResources().getColor(R.color.color_4);
                break;
            case "房屋建筑":
                color=mContext.getResources().getColor(R.color.color_6);
                break;
            case "草地":
                color=mContext.getResources().getColor(R.color.color_8);
                break;
            case "水域":
                color=mContext.getResources().getColor(R.color.color_10);
                break;
            default:
                color=mContext.getResources().getColor(R.color.color_12);
                break;
        }
        MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + field + "tianchongse", color).apply();
        return color;
    }
}