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

public class SwdyxBbgAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SwdyxActivity activity;
	MapView mapView;
	GraphicsLayer graphicsLayer;

	public SwdyxBbgAdapter(Context context, MapView mapView,
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
			convertView = inflater.inflate(R.layout.item_swdyx_bbg, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
			holder.tv8 = (TextView) convertView.findViewById(R.id.tv8);
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
		if (BussUtil.isEmperty(list.get(position).get("BBG").toString())) {
			holder.tv2.setText(list.get(position).get("BBG").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("FZR").toString())) {
			holder.tv3.setText(list.get(position).get("FZR").toString());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("ADDRESS").toString())) {
			holder.tv4.setText(list.get(position).get("ADDRESS").toString());
		} else {
			holder.tv4.setText("");
		}

		if (BussUtil.isEmperty(list.get(position).get("PHONE").toString())) {
			holder.tv5.setText(list.get(position).get("PHONE").toString());
		} else {
			holder.tv5.setText("");
		}
		holder.tv6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCheckDailog(list.get(position));
			}
		});
		holder.tv7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDailog(list.get(position));

			}
		});
		holder.tv8.setOnClickListener(new OnClickListener() {
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
		dialog.setContentView(R.layout.swdyx_bbg_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView swdyx_bbg_bbgmc = (TextView) dialog
				.findViewById(R.id.swdyx_bbg_bbgmc);
		TextView swdyx_bbg_fzr = (TextView) dialog
				.findViewById(R.id.swdyx_bbg_fzr);
		TextView swdyx_bbg_jd = (TextView) dialog
				.findViewById(R.id.swdyx_bbg_jd);

		TextView swdyx_bbg_wd = (TextView) dialog
				.findViewById(R.id.swdyx_bbg_wd);

		TextView swdyx_bbg_lxfs = (TextView) dialog
				.findViewById(R.id.swdyx_bbg_lxfs);

		TextView swdyx_bbg_dz = (TextView) dialog
				.findViewById(R.id.swdyx_bbg_dz);

		Button back = (Button) dialog.findViewById(R.id.swdyx_bbg_back);
		//标本馆名称
		if (BussUtil.isEmperty(map.get("BBG").toString())) {
			swdyx_bbg_bbgmc.setText(map.get("BBG").toString());
		} else {
			swdyx_bbg_bbgmc.setText("");
		}
		// 联系方式
		if (BussUtil.isEmperty(map.get("FZR").toString())) {
			swdyx_bbg_fzr.setText(map.get("FZR").toString());
		} else {
			swdyx_bbg_fzr.setText("");
		}
		// 经度
		if (BussUtil.isEmperty(map.get("X").toString())) {
			swdyx_bbg_jd.setText(map.get("X").toString());
		} else {
			swdyx_bbg_jd.setText("");
		}
		// 纬度
		if (BussUtil.isEmperty(map.get("Y").toString())) {
			swdyx_bbg_wd.setText(map.get("Y").toString());
		} else {
			swdyx_bbg_wd.setText("");
		}
		// 联系方式
		if (BussUtil.isEmperty(map.get("PHONE").toString())) {
			swdyx_bbg_lxfs.setText(map.get("PHONE").toString());
		} else {
			swdyx_bbg_lxfs.setText("");
		}
		// 地址
		if (BussUtil.isEmperty(map.get("ADDRESS").toString())) {
			swdyx_bbg_dz.setText(map.get("ADDRESS").toString());
		} else {
			swdyx_bbg_dz.setText("");
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
		dialog.setContentView(R.layout.swdyx_bbg_edit);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		TextView head=(TextView) dialog.findViewById(R.id.swdyx_bbg_head);
		head.setText(context.getResources().getString(R.string.bbgedit));

		final EditText swdyx_bbg_bbgmc = (EditText) dialog
				.findViewById(R.id.swdyx_bbg_bbgmc);
		if (BussUtil.isEmperty(map.get("BBG").toString())) {
			swdyx_bbg_bbgmc.setText(map.get("BBG").toString());
		} else {
			swdyx_bbg_bbgmc.setText("");
		}

		final EditText swdyx_bbg_fzr = (EditText) dialog
				.findViewById(R.id.swdyx_bbg_fzr);
		if (BussUtil.isEmperty(map.get("FZR").toString())) {
			swdyx_bbg_fzr.setText(map.get("FZR").toString());
		} else {
			swdyx_bbg_fzr.setText("");
		}

		final EditText swdyx_bbg_jd = (EditText) dialog
				.findViewById(R.id.swdyx_bbg_jd);
		if (BussUtil.isEmperty(map.get("X").toString())) {
			swdyx_bbg_jd.setText(map.get("X").toString());
		} else {
			swdyx_bbg_jd.setText("");
		}

		final EditText swdyx_bbg_wd = (EditText) dialog
				.findViewById(R.id.swdyx_bbg_wd);
		if (BussUtil.isEmperty(map.get("Y").toString())) {
			swdyx_bbg_wd.setText(map.get("Y").toString());
		} else {
			swdyx_bbg_wd.setText("");
		}
		final EditText swdyx_bbg_lxfs = (EditText) dialog
				.findViewById(R.id.swdyx_bbg_lxfs);
		if (BussUtil.isEmperty(map.get("PHONE").toString())) {
			swdyx_bbg_lxfs.setText(map.get("PHONE").toString());
		} else {
			swdyx_bbg_lxfs.setText("");
		}

		final EditText swdyx_bbg_dz = (EditText) dialog
				.findViewById(R.id.swdyx_bbg_dz);
		if (BussUtil.isEmperty(map.get("ADDRESS").toString())) {
			swdyx_bbg_dz.setText(map.get("ADDRESS").toString());
		} else {
			swdyx_bbg_dz.setText("");
		}
		Button save = (Button) dialog.findViewById(R.id.swdyx_bbg_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Webservice webservice = new Webservice(context);
				String id = "";
				if (BussUtil.isEmperty(map.get("OBJECTID").toString())) {
					id = map.get("OBJECTID").toString();
				}
				String mc = swdyx_bbg_bbgmc.getText().toString().trim();
				if (!BussUtil.isEmperty(mc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bbgmcnotnull));
					return;
				}
				String fzr = swdyx_bbg_fzr.getText().toString().trim();
				if (!BussUtil.isEmperty(fzr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bbgfzrnotnull));
					return;
				}
				String jd = swdyx_bbg_jd.getText().toString().trim();
				if (!BussUtil.isEmperty(jd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdnotnull));
					return;
				}
				String wd = swdyx_bbg_wd.getText().toString().trim();
				if (!BussUtil.isEmperty(wd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.wdnotnull));
					return;
				}
				String dz = swdyx_bbg_dz.getText().toString().trim();
				if (!BussUtil.isEmperty(dz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bbgdznotnull));
					return;
				}
				String lxfs=swdyx_bbg_lxfs.getText().toString();
				String result = webservice.updateSwdyxBbgData(id, mc, fzr, jd, wd, lxfs, dz);
				if ("true".equals(result)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editsuccess));
					map.put("BBG", mc);
					map.put("FZR", fzr);
					map.put("X", jd);
					map.put("Y", wd);
					map.put("PHONE", lxfs);
					map.put("ADDRESS", dz);
					notifyDataSetChanged();
					dialog.dismiss();
				} else {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editfailed));
				}
			}
		});

		Button cancle = (Button) dialog
				.findViewById(R.id.swdyx_bbg_cancle);
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
		public TextView tv7;
		public TextView tv8;
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
