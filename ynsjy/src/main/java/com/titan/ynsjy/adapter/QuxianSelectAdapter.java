package com.titan.ynsjy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.titan.ynsjy.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuxianSelectAdapter extends BaseAdapter{

	List<Map<String, Object>> list = null;
	private LayoutInflater inflater = null;
    private HashMap<String,Boolean> isSelected;
    
	@SuppressLint("UseSparseArrays")
	public QuxianSelectAdapter(Context context,List<Map<String, Object>> list,HashMap<String,Boolean> isSelected) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.isSelected = isSelected;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_quxianselect, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.item_xb = (CheckBox)convertView.findViewById(R.id.item_xb);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv1.setText(list.get(position).get("name").toString());
		holder.item_xb.setChecked(isSelected.get(list.get(position).get("name").toString()));
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv1;
		CheckBox item_xb;
	}
}
