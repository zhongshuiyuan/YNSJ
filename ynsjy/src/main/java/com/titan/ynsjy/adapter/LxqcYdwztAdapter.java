package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.dialog.HzbjDialog;
import com.titan.ynsjy.dialog.ShuziYxclDialog;
import com.titan.ynsjy.dialog.SlzylxqcYdwztDialog;
import com.titan.ynsjy.util.BussUtil;

import java.util.HashMap;
import java.util.List;

public class LxqcYdwztAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;

	public LxqcYdwztAdapter(Context context, List<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_ydwzt, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> map = list.get(position);
		if (BussUtil.isEmperty(map.get("MC").toString())) {
			holder.tv1.setText(map.get("MC").toString());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(map.get("BH").toString())) {
			holder.tv2.setText(map.get("BH").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(map.get("FWJ").toString())) {
			holder.tv3.setText(map.get("FWJ").toString());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(map.get("SPJ").toString())) {
			holder.tv4.setText(map.get("SPJ").toString());
		} else {
			holder.tv4.setText("");
		}
		final TextView dwwmc = holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog hzdialog = new HzbjDialog(context,
						"定位物名称", dwwmc,map, "MC");
				BussUtil.setDialogParams(context, hzdialog, 0.5, 0.5);
			}
		});
		final TextView dwwbh = holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"定位物编号", dwwbh, map, "BH", "", "", "", list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView fwj = holder.tv3;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"方位角(°)", fwj, map, "FWJ", "", "", "", list, "0", "1", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView spj = holder.tv4;
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"水平距(米)", spj, map, "SPJ", "", "", "", list, "0", "1", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		holder.tv5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.remove(list.get(position));
				SlzylxqcYdwztDialog.refreshdata();
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
	}

}
