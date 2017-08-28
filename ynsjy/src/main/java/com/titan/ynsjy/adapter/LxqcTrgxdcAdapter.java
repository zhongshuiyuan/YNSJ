package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.dialog.HzbjDialog;
import com.titan.ynsjy.dialog.ShuziDialog;
import com.titan.ynsjy.dialog.XzzyDialog;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ResourcesManager;

import java.util.HashMap;
import java.util.List;

public class LxqcTrgxdcAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;

	public LxqcTrgxdcAdapter(Context context ,List<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_trgxqkdc, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String>map=list.get(position);

		if (BussUtil.isEmperty(map.get("SZ").toString())) {
			holder.tv1.setText(map.get("SZ").toString());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(map.get("GAOZS1").toString())) {
			holder.tv2.setText(map.get("GAOZS1").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(map.get("GAOZS2").toString())) {
			holder.tv3.setText(map.get("GAOZS2").toString());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(map.get("GAOZS3").toString())) {
			holder.tv4.setText(map.get("GAOZS3").toString());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(map.get("JKZK").toString())) {
			holder.tv5.setText(map.get("JKZK").toString());
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(map.get("PHQK").toString())) {
			holder.tv6.setText(map.get("PHQK").toString());
		} else {
			holder.tv6.setText("");
		}
		final TextView shuhzhong=holder.tv1;
		//树种
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> listtemp =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog dialog = new XzzyDialog(context, "树种", listtemp, shuhzhong,map,"SZ");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		final TextView zhushu1=holder.tv2;
		//株数(高<30cm)
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String str=context.getResources().getString(R.string.zhushu1);
				ShuziDialog dialog=new ShuziDialog(context,  str, zhushu1, map, "GAOZS1", list, null, "1", "", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		final TextView zhushu2=holder.tv3;
		//株数(30=<高<50cm)
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String str=context.getResources().getString(R.string.zhushu2);
				ShuziDialog dialog=new ShuziDialog(context,str, zhushu2, map, "GAOZS2", list, null, "1", "", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		final TextView zhushu3=holder.tv4;
		//株数(30=<高<50cm)
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String str=context.getResources().getString(R.string.zhushu3);
				ShuziDialog dialog=new ShuziDialog(context,  str, zhushu3, map, "GAOZS3",  list, null, "1", "", "");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		final TextView jkzk=holder.tv5;
		//健康状况
		holder.tv5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "SLJKD");
				XzzyDialog dialog = new XzzyDialog(context,"健康状况", listtemp, jkzk,map,"JKZK");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		final TextView phqk=holder.tv6;
		//破坏情况
		holder.tv6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog dialog=new HzbjDialog(context, "破坏情况", phqk, map, "PHQK");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		//删除
		holder.tv7.setOnClickListener(new View.OnClickListener() {

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
		public TextView tv7;
	}

}
