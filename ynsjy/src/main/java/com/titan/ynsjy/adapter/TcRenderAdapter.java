package com.titan.ynsjy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.dialog.RenderSetDialog;
import com.titan.ynsjy.entity.MyLayer;

import java.util.ArrayList;
import java.util.List;

public class TcRenderAdapter extends BaseAdapter {
	private List<MyLayer> list = new ArrayList<>();
	private LayoutInflater inflater = null;
	private RenderSetDialog renderSetDialog;
	
	public TcRenderAdapter(Context context,List<MyLayer> list,RenderSetDialog dialog) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.renderSetDialog = dialog;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_expandable_group, null);
			holder.tv_layerName = (TextView) convertView.findViewById(R.id.id_group_txt);
			holder.btn_simple = (Button) convertView.findViewById(R.id.layer_render_simple);
			holder.btn_unique = (Button) convertView.findViewById(R.id.layer_render_unique);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_layerName.setText(list.get(position).getLname());
		holder.btn_simple.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				renderSetDialog.showLayerRender(list.get(position),0);
			}
		});
		holder.btn_unique.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderSetDialog.showLayerRender(list.get(position),1);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_layerName;
		Button btn_simple;
		Button btn_unique;
	}

}
