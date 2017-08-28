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
import com.titan.ynsjy.dialog.XzzyDialog;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ResourcesManager;

import java.util.HashMap;
import java.util.List;

public class LxqcSlzhdcAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;
	HashMap<String, String>map;

	public LxqcSlzhdcAdapter(Context context ,List<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_slzhqkdc, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		map=list.get(position);
		if (BussUtil.isEmperty(list.get(position).get("XH").toString())) {
			holder.tv1.setText(list.get(position).get("XH").toString());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("ZHLX").toString())) {
			holder.tv2.setText(list.get(position).get("ZHLX").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("WHBW").toString())) {
			holder.tv3.setText(list.get(position).get("WHBW").toString());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SHYMZS").toString())) {
			holder.tv4.setText(list.get(position).get("SHYMZS").toString());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SHDJ").toString())) {
			holder.tv5.setText(list.get(position).get("SHDJ").toString());
		} else {
			holder.tv5.setText("");
		}
		//序号
		final TextView xhtv=holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog dialog=new ShuziDialog(context,"序号", xhtv, map, "XH",list, null, "1", "", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//灾害类型
		final TextView zhlxtv=holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "ZHLX");
				XzzyDialog dialog = new XzzyDialog(context,"灾害类型", listtemp, zhlxtv,map,"ZHLX");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//危害部位
		final TextView whbwtv=holder.tv3;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog dialog=new HzbjDialog(context,"危害部位", whbwtv,map, "WHBW");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//受害样木株数(%）
		final TextView shymzstv=holder.tv4;
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog dialog=new ShuziDialog(context, "受害样木株数(%)", shymzstv, map, "SHYMZS",  list, null, "1", "", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//受害等级
		final TextView shdjtv=holder.tv5;
		holder.tv5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "ZHDJ");
				XzzyDialog dialog = new XzzyDialog(context,"受害等级", listtemp, shdjtv,map,"SHDJ");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//删除
		holder.tv6.setOnClickListener(new View.OnClickListener() {

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
		public TextView tv5;
		public TextView tv6;
	}

}
