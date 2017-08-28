package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.Row;

import java.util.List;

public class XzzyAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	Context context;
	List<Row> list;
	public XzzyAdapter(Context context,List<Row> list ) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_xzzy, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Row row=list.get(position);
		holder.tv1.setText(row.getId()+"-"+row.getName());
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv1;
	}

}
