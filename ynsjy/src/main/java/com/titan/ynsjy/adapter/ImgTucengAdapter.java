package com.titan.ynsjy.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geometry.Geometry;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImgTucengAdapter extends BaseAdapter {
	List<File> list = null;
	public List<HashMap<Integer, Boolean>> selectList = new ArrayList<HashMap<Integer, Boolean>>();
	private LayoutInflater inflater = null;
	private HashMap<String, Boolean> isSelected;
	private BaseActivity activity;
	Map<String, ArcGISLocalTiledLayer> imgTileLayerMap;

	public ImgTucengAdapter(BaseActivity activity,List<File> list,
			HashMap<String, Boolean> isSelected,Map<String, ArcGISLocalTiledLayer> imgmap) {
		inflater = LayoutInflater.from(activity);
		this.activity = activity;
		this.list = list;
		this.isSelected = isSelected;
		this.imgTileLayerMap = imgmap;
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
			convertView = inflater.inflate(R.layout.item_img_tuceng, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv1);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_xb);
			holder.imgv = (ImageView) convertView.findViewById(R.id.featurelayer_extent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).getName().split("\\.")[0]);
		holder.cb.setChecked(isSelected.get(list.get(position).getName()));
		holder.imgv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String key = list.get(position).getName();
				ArcGISLocalTiledLayer layer = imgTileLayerMap.get(key);
				if(layer != null){
					final Geometry geometry = layer.getFullExtent();
					activity.ZoomToGeom(geometry);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		CheckBox cb;
		ImageView imgv;
	}

}
