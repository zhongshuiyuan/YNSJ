package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;

import java.util.List;

public class LayerAdapter extends BaseAdapter {
	List<MyLayer> list = null;
	private LayoutInflater inflater = null;
	public LayerAdapter(List<MyLayer> list,Context context) {
		this.list = list;
		inflater = LayoutInflater.from(context);
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
		LayerViewHolder holder = null;
		if(null == convertView){
			holder = new LayerViewHolder();
			convertView = inflater.inflate(R.layout.item_textview, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			convertView.setTag(holder);
		}else{
			holder = (LayerViewHolder) convertView.getTag();
		}
		String gname = list.get(position).getPname();
		String cname = list.get(position).getCname();
		String lname = list.get(position).getLname();
		holder.tv1.setText(gname+":"+cname+":"+lname);
		return convertView;
	}
	
	final class LayerViewHolder {
		TextView tv1;
	}
}
