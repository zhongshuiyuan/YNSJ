package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.ShouCang;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView;

import java.util.List;

public class ShouCangAdapter extends BaseAdapter {

	Context context;
	List<ShouCang> scdlist;
	SwipeMenuListView lv;
	TextView zwsj;

	public ShouCangAdapter(Context context, List<ShouCang> scdlist,SwipeMenuListView lv, TextView zwsj) {
		this.context = context;
		this.scdlist = scdlist;
		this.lv = lv;
		this.zwsj = zwsj;
	}

	@Override
	public int getCount() {
		return scdlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return scdlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_txt, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv);
			holder.tv1.setGravity(Gravity.LEFT);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}

		//final int pos = position;
		// 必须设置此句，否则不知道点击的是哪个item中的menu
		//holder.tsl.pos = position;
//		holder.menu.findViewById(R.id.menu_delete).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						DbHelperService<ShouCang> service = new DbHelperService<ShouCang>(context, ShouCang.class);
//						ShouCang shouCang = scdlist.get(position);
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("id", shouCang.getId()+"");
//						boolean flag = service.delete(map);
//						//DataBaseHelper.deleteScdData(context,scdlist.get(position).getId() + "");
//						if(flag){
//							ToastUtil.setToast(context, "删除成功");
//							scdlist.remove(scdlist.get(position));
//							notifyDataSetChanged();
//						}
//						if (scdlist.size() == 0) {
//							zwsj.setVisibility(View.VISIBLE);
//							lv.setVisibility(View.GONE);
//						} else {
//							zwsj.setVisibility(View.GONE);
//							lv.setVisibility(View.VISIBLE);
//						}
//					}
//				});
//		holder.menu.findViewById(R.id.menu_hello).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						
//						DbHelperService<ShouCang> service = new DbHelperService<ShouCang>(context, ShouCang.class);
//						ShouCang cang = scdlist.get(position);
//						List<ShouCang> ts = service.getObjects(cang);
//						
//						Toast.makeText(context, "hello" + position, 0).show();
//					}
//				});
		holder.tv1.setText(scdlist.get(position).getMIAOSHU());
		holder.tv2.setText(scdlist.get(position).getTIME());
		holder.tv3.setText(scdlist.get(position).getLON());
		holder.tv4.setText(scdlist.get(position).getLAT());
		return convertView;
	}

	public class HolderView {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
	}

//	public class HolderView {
//		public TSlidLayout tsl;
//		public View content;
//		public View menu;
//	}
}
