package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.dialog.HzbjDialog;
import com.titan.ynsjy.dialog.ShuziDialog;
import com.titan.ynsjy.util.BussUtil;

import java.util.HashMap;
import java.util.List;

public class LxqcXmdcAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;

	public LxqcXmdcAdapter(Context context ,List<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_xmdc, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (BussUtil.isEmperty(list.get(position).get("SZMC").toString())) {
			holder.tv1.setText(list.get(position).get("SZMC").toString());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("GD").toString())) {
			holder.tv2.setText(list.get(position).get("GD").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("XJ").toString())) {
			holder.tv3.setText(list.get(position).get("XJ").toString());
		} else {
			holder.tv3.setText("");
		}
		//树种名称
		final TextView sztv=holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog dialog=new HzbjDialog(context,"名称(树种)", sztv, list.get(position), "SZMC");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//高度
		final TextView gdtv=holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog dialog=new ShuziDialog(context, "高度(米)", gdtv, list.get(position), "GD", list, null, "0", "1", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//胸径
		final TextView xjtv=holder.tv3;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog dialog=new ShuziDialog(context, "胸径(米)", xjtv, list.get(position), "XJ",  list, null, "0", "1", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//删除
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.remove(list.get(position));
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
	}

}
