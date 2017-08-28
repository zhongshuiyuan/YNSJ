package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.titan.ynsjy.R;

import java.util.List;

public class EdFeatureResultAdapter extends BaseAdapter {

	Context mContext;
	List<GeodatabaseFeature> list = null;
	String fieldname;

	public EdFeatureResultAdapter(Context context,
			List<GeodatabaseFeature> list, String name) {
		this.mContext = context;
		this.list = list;
		this.fieldname = name;
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_txt, null);

			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv1.setVisibility(View.GONE);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv2.setVisibility(View.GONE);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv3.setVisibility(View.GONE);
			holder.tv.setGravity(Gravity.CENTER_HORIZONTAL);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).getId() + "");
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		TextView tv1;
		TextView tv2;
		TextView tv3;
	}

}
