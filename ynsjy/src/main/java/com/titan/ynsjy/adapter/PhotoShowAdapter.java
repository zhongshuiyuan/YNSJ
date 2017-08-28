package com.titan.ynsjy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ResourcesManager;

import java.util.HashMap;
import java.util.List;

public class PhotoShowAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, Object>> list;
	Context context;
	
	public PhotoShowAdapter(Context context ,List<HashMap<String, Object>> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
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
			convertView = inflater.inflate(R.layout.item_photoshow, null);
			holder.tv=(TextView) convertView.findViewById(R.id.tv);
			holder.iv= (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, Object>map=list.get(position);
		if(map.get(ResourcesManager.BITMAP)!=null){
			holder.iv.setImageBitmap((Bitmap) map.get(ResourcesManager.BITMAP));
		}
		if(map.get(ResourcesManager.NAME)!=null){
			holder.tv.setText((String)map.get(ResourcesManager.NAME));
		}
		return convertView;
	}


	public final class ViewHolder {
		public TextView tv;
		public ImageView iv;
	}

}
