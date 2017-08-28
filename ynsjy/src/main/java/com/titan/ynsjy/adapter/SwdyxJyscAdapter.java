package com.titan.ynsjy.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.SwdyxActivity;
import com.titan.ynsjy.service.Webservice;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.PadUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class SwdyxJyscAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SwdyxActivity activity;
	MapView mapView;
	GraphicsLayer graphicsLayer;

	public SwdyxJyscAdapter(Context context, MapView mapView,
							GraphicsLayer graphicsLayer, List<HashMap<String, String>> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mapView = mapView;
		this.graphicsLayer = graphicsLayer;
		this.activity=(SwdyxActivity) context;
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
			convertView = inflater.inflate(R.layout.item_swdyx_jysc, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
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

		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (BussUtil.isEmperty(list.get(position).get("OBJECTID").toString())) {
					HashMap<String, String> map = list.get(position);
					map.put(list.get(position).get("OBJECTID"), arg1 + "");
					notifyDataSetChanged();
				}
			}
		});
		holder.cb.setChecked(Boolean.parseBoolean(list.get(position).get(
				list.get(position).get("OBJECTID"))));

		holder.tv1.setText((position + 1) + "");
		if (BussUtil.isEmperty(list.get(position).get("NAME").toString())) {
			holder.tv2.setText(list.get(position).get("NAME").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("ADDRESS").toString())) {
			holder.tv3.setText(list.get(position).get("ADDRESS").toString());
		} else {
			holder.tv3.setText("");
		}
		holder.tv4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCheckDailog(list.get(position));
			}
		});
		holder.tv5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDailog(list.get(position));

			}
		});
		holder.tv6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!BussUtil.isEmperty(list.get(position).get("X"))
						|| !BussUtil.isEmperty(list.get(position).get(
						"Y"))) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.longorlannull));
					return;
				}
				double lon = Double.parseDouble(list.get(position).get(
						"X"));
				double lat = Double.parseDouble(list.get(position).get(
						"Y"));
				Point point = new Point(lon, lat);
				Graphic graphic = new Graphic(point, new SimpleMarkerSymbol(
						Color.RED, 10,
						com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE));
				graphicsLayer.addGraphic(graphic);
				mapView.setExtent(point);
				ToastUtil.setToast(
						context,
						context.getResources().getString(
								R.string.locationsuccess));
			}
		});

		return convertView;
	}

	/* 查看 */
	protected void showCheckDailog(HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.swdyx_jysc_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView swdyx_jysc_mc = (TextView) dialog
				.findViewById(R.id.swdyx_jysc_mc);
		TextView swdyx_jysc_jd = (TextView) dialog
				.findViewById(R.id.swdyx_jysc_jd);

		TextView swdyx_jysc_wd = (TextView) dialog
				.findViewById(R.id.swdyx_jysc_wd);

		TextView swdyx_jysc_dz = (TextView) dialog
				.findViewById(R.id.swdyx_jysc_dz);

		Button back = (Button) dialog.findViewById(R.id.swdyx_jysc_back);
		//交易市场名称
		if (BussUtil.isEmperty(map.get("NAME").toString())) {
			swdyx_jysc_mc.setText(map.get("NAME").toString());
		} else {
			swdyx_jysc_mc.setText("");
		}
		// 经度
		if (BussUtil.isEmperty(map.get("X").toString())) {
			swdyx_jysc_jd.setText(map.get("X").toString());
		} else {
			swdyx_jysc_jd.setText("");
		}
		// 纬度
		if (BussUtil.isEmperty(map.get("Y").toString())) {
			swdyx_jysc_wd.setText(map.get("Y").toString());
		} else {
			swdyx_jysc_wd.setText("");
		}
		// 地址
		if (BussUtil.isEmperty(map.get("ADDRESS").toString())) {
			swdyx_jysc_dz.setText(map.get("ADDRESS").toString());
		} else {
			swdyx_jysc_dz.setText("");
		}

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();

			}
		});
		setDialogParams(context, dialog, 0.8, 0.90);
	}

	/* 编辑 */
	protected void showEditDailog(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.swdyx_jysc_edit);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		TextView head=(TextView) dialog.findViewById(R.id.swdyx_jysc_head);
		head.setText(context.getResources().getString(R.string.jyscedit));

		final EditText swdyx_jysc_mc = (EditText) dialog
				.findViewById(R.id.swdyx_jysc_mc);
		if (BussUtil.isEmperty(map.get("NAME").toString())) {
			swdyx_jysc_mc.setText(map.get("NAME").toString());
		} else {
			swdyx_jysc_mc.setText("");
		}

		final EditText swdyx_jysc_jd = (EditText) dialog
				.findViewById(R.id.swdyx_jysc_jd);
		if (BussUtil.isEmperty(map.get("X").toString())) {
			swdyx_jysc_jd.setText(map.get("X").toString());
		} else {
			swdyx_jysc_jd.setText("");
		}

		final EditText swdyx_jysc_wd = (EditText) dialog
				.findViewById(R.id.swdyx_jysc_wd);
		if (BussUtil.isEmperty(map.get("Y").toString())) {
			swdyx_jysc_wd.setText(map.get("Y").toString());
		} else {
			swdyx_jysc_wd.setText("");
		}
		final EditText swdyx_jysc_dz = (EditText) dialog
				.findViewById(R.id.swdyx_jysc_dz);
		if (BussUtil.isEmperty(map.get("ADDRESS").toString())) {
			swdyx_jysc_dz.setText(map.get("ADDRESS").toString());
		} else {
			swdyx_jysc_dz.setText("");
		}
		Button save = (Button) dialog.findViewById(R.id.swdyx_jysc_save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Webservice webservice = new Webservice(context);
				String id = "";
				if (BussUtil.isEmperty(map.get("OBJECTID").toString())) {
					id = map.get("OBJECTID").toString();
				}
				String mc = swdyx_jysc_mc.getText().toString().trim();
				if (!BussUtil.isEmperty(mc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jyscnamenotnull));
					return;
				}
				String jd = swdyx_jysc_jd.getText().toString().trim();
				if (!BussUtil.isEmperty(jd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdnotnull));
					return;
				}
				String wd = swdyx_jysc_wd.getText().toString().trim();
				if (!BussUtil.isEmperty(wd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.wdnotnull));
					return;
				}
				String dz = swdyx_jysc_dz.getText().toString().trim();
				if (!BussUtil.isEmperty(dz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jyscdznotnull));
					return;
				}
				String result = webservice.updateSwdyxJyscData(id, jd, wd, dz, mc);
				if ("true".equals(result)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editsuccess));
					map.put("X", jd);
					map.put("Y", wd);
					map.put("ADDRESS", dz);
					map.put("NAME", mc);
					notifyDataSetChanged();
					dialog.dismiss();
				} else {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editfailed));
				}
			}
		});

		Button cancle = (Button) dialog
				.findViewById(R.id.swdyx_jysc_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		setDialogParams(context, dialog, 0.8, 0.90);
	}

	public final class ViewHolder {
		public CheckBox cb;
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public TextView tv6;
	}

	public void showInfoDailog() {
		Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_update_mobileinfo);
		dialog.show();
		setDialogParams(context, dialog, 0.5, 0.6);
	}

	/* dialog 宽度和高度设置 */
	public void setDialogParams(Context mContext, Dialog dialog, double pwidth,
								double mwidth) {
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (PadUtil.isPad(mContext)) {
			params.width = (int) (MyApplication.screen.getWidthPixels() * pwidth);
		} else {
			params.width = (int) (MyApplication.screen.getWidthPixels() * mwidth);
		}
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}

}
