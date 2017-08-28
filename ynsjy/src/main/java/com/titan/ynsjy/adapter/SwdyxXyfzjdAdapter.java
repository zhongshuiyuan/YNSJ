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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SwdyxXyfzjdAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	List<HashMap<String, String>> list;
	Context context;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SwdyxActivity activity;
	MapView mapView;
	GraphicsLayer graphicsLayer;

	public SwdyxXyfzjdAdapter(Context context, MapView mapView,
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
			convertView = inflater.inflate(R.layout.item_swdyx_xyfzjd, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.tv1 = (TextView) convertView.findViewById(R.id.xyfzjd_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.xyfzjd_tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.xyfzjd_tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.xyfzjd_tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.xyfzjd_tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.xyfzjd_tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.xyfzjd_tv7);
			holder.tv8 = (TextView) convertView.findViewById(R.id.xyfzjd_tv8);
			holder.tv9 = (TextView) convertView.findViewById(R.id.xyfzjd_tv9);
			holder.tv10 = (TextView) convertView.findViewById(R.id.xyfzjd_tv10);
			holder.tv11 = (TextView) convertView.findViewById(R.id.xyfzjd_tv11);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (BussUtil.isEmperty(list.get(position).get("ID").toString())) {
					HashMap<String, String> map = list.get(position);
					map.put(list.get(position).get("ID"), arg1 + "");
					notifyDataSetChanged();
				}
			}
		});
		holder.cb.setChecked(Boolean.parseBoolean(list.get(position).get(
				list.get(position).get("ID"))));

		holder.tv1.setText((position + 1) + "");
		if (BussUtil.isEmperty(list.get(position).get("BASENAME").toString())) {
			holder.tv2.setText(list.get(position).get("BASENAME").toString());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("PROPERTY").toString())) {
			holder.tv3.setText(list.get(position).get("PROPERTY").toString());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("ADDRESS").toString())) {
			holder.tv4.setText(list.get(position).get("ADDRESS").toString());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("MANAGER").toString())) {
			holder.tv5.setText(list.get(position).get("MANAGER").toString());
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("PHONE").toString())) {
			holder.tv6.setText(list.get(position).get("PHONE").toString());
		} else {
			holder.tv6.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("REGDATE").toString())) {
			try {
				String str = list.get(position).get("REGDATE").toString()
						.substring(0, 10);
				Date date = format.parse(str.replace("/", "-"));
				holder.tv7.setText(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			holder.tv7.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("BUSISCOPE").toString())) {
			holder.tv8.setText(list.get(position).get("BUSISCOPE").toString());
		} else {
			holder.tv8.setText("");
		}
		holder.tv9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCheckDailog(list.get(position));
			}
		});
		holder.tv10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDailog(list.get(position));

			}
		});
		holder.tv11.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!BussUtil.isEmperty(list.get(position).get("LONGITUDE"))
						|| !BussUtil.isEmperty(list.get(position).get(
						"LATITUDE"))) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.longorlannull));
					return;
				}
				double lon = Double.parseDouble(list.get(position).get(
						"LONGITUDE"));
				double lat = Double.parseDouble(list.get(position).get(
						"LATITUDE"));
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
		dialog.setContentView(R.layout.swdyx_xyfzjdgl_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView swdyx_xyfzjd_jdmc = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jdmc);
		TextView swdyx_xyfzjd_jdxz = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jdxz);
		TextView swdyx_xyfzjd_fzr = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_fzr);

		TextView swdyx_xyfzjd_lxfs = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_lxfs);

		TextView swdyx_xyfzjd_jddz = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jddz);

		TextView swdyx_xyfzjd_jyfw = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jyfw);

		TextView swdyx_xyfzjd_jyyt = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jyyt);

		TextView swdyx_xyfzjd_xwdd = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_xwdd);

		TextView swdyx_xyfzjd_jbr = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jbr);

		TextView swdyx_xyfzjd_jd = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_jd);

		TextView swdyx_xyfzjd_wd = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_wd);

		TextView swdyx_xyfzjd_bz = (TextView) dialog
				.findViewById(R.id.swdyx_xyfzjd_bz);

		Button back = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_back);
		// 基地名称
		if (BussUtil.isEmperty(map.get("BASENAME").toString())) {
			swdyx_xyfzjd_jdmc.setText(map.get("BASENAME").toString());
		} else {
			swdyx_xyfzjd_jdmc.setText("");
		}
		// 基地性质
		if (BussUtil.isEmperty(map.get("PROPERTY").toString())) {
			int a = Integer.parseInt(map.get("PROPERTY").toString());
			String jdxz = "";
			if (a > 0 && activity.jdxzLlist.size() > a) {
				jdxz = activity.jdxzLlist.get(a - 1).get("DICVAL");
			}
			swdyx_xyfzjd_jdxz.setText(jdxz);
		} else {
			swdyx_xyfzjd_jdxz.setText("");
		}
		// 基地负责人
		if (BussUtil.isEmperty(map.get("MANAGER").toString())) {
			swdyx_xyfzjd_fzr.setText(map.get("MANAGER").toString());
		} else {
			swdyx_xyfzjd_fzr.setText("");
		}
		// 联系方式
		if (BussUtil.isEmperty(map.get("PHONE").toString())) {
			swdyx_xyfzjd_lxfs.setText(map.get("PHONE").toString());
		} else {
			swdyx_xyfzjd_lxfs.setText("");
		}
		// 基地地址
		if (BussUtil.isEmperty(map.get("ADDRESS").toString())) {
			swdyx_xyfzjd_jddz.setText(map.get("ADDRESS").toString());
		} else {
			swdyx_xyfzjd_jddz.setText("");
		}
		// 经营范围
		if (BussUtil.isEmperty(map.get("BUSISCOPE").toString())) {
			swdyx_xyfzjd_jyfw.setText(map.get("BUSISCOPE").toString());
		} else {
			swdyx_xyfzjd_jyfw.setText("");
		}
		// 经营用途
		if (BussUtil.isEmperty(map.get("BUSIUSE").toString())) {
			swdyx_xyfzjd_jyyt.setText(map.get("BUSIUSE").toString());
		} else {
			swdyx_xyfzjd_jyyt.setText("");
		}
		// 销往地点
		if (BussUtil.isEmperty(map.get("SELLPLACE").toString())) {
			swdyx_xyfzjd_xwdd.setText(map.get("SELLPLACE").toString());
		} else {
			swdyx_xyfzjd_xwdd.setText("");
		}
		// 经办人
		if (BussUtil.isEmperty(map.get("TRANSACTOR").toString())) {
			swdyx_xyfzjd_jbr.setText(map.get("TRANSACTOR").toString());
		} else {
			swdyx_xyfzjd_jbr.setText("");
		}
		// 经度
		if (BussUtil.isEmperty(map.get("LONGITUDE").toString())) {
			swdyx_xyfzjd_jd.setText(map.get("LONGITUDE").toString());
		} else {
			swdyx_xyfzjd_jd.setText("");
		}
		// 纬度
		if (BussUtil.isEmperty(map.get("LATITUDE").toString())) {
			swdyx_xyfzjd_wd.setText(map.get("LATITUDE").toString());
		} else {
			swdyx_xyfzjd_wd.setText("");
		}
		// 备注
		if (BussUtil.isEmperty(map.get("REMARK").toString())) {
			swdyx_xyfzjd_bz.setText(map.get("REMARK").toString());
		} else {
			swdyx_xyfzjd_bz.setText("");
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
		dialog.setContentView(R.layout.swdyx_xyfzjdgl_edit);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		final EditText swdyx_xyfzjd_jdmc = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_jdmc);
		if (BussUtil.isEmperty(map.get("BASENAME").toString())) {
			swdyx_xyfzjd_jdmc.setText(map.get("BASENAME").toString());
		} else {
			swdyx_xyfzjd_jdmc.setText("");
		}

		final Spinner swdyx_xyfzjd_jdxz = (Spinner) dialog
				.findViewById(R.id.swdyx_xyfzjd_jdxz);
		List<String>list=new ArrayList<String>();
		list.add("--请选择基地性质--");
		for(int i=0;i<activity.jdxzLlist.size();i++){
			list.add(activity.jdxzLlist.get(i).get("DICVAL"));
		}
		ArrayAdapter jdxzadapter =new ArrayAdapter(context, R.layout.myspinner, list);
		swdyx_xyfzjd_jdxz.setAdapter(jdxzadapter);
		if (BussUtil.isEmperty(map.get("PROPERTY").toString())) {
			int a = Integer.parseInt(map.get("PROPERTY").toString());
			if (a >= 0 && list.size() > a) {
				swdyx_xyfzjd_jdxz.setSelection(a);
			}
		}
		final EditText swdyx_xyfzjd_fzr = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_fzr);
		if (BussUtil.isEmperty(map.get("MANAGER").toString())) {
			swdyx_xyfzjd_fzr.setText(map.get("MANAGER").toString());
		} else {
			swdyx_xyfzjd_fzr.setText("");
		}

		final EditText swdyx_xyfzjd_lxfs = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_lxfs);
		if (BussUtil.isEmperty(map.get("PHONE").toString())) {
			swdyx_xyfzjd_lxfs.setText(map.get("PHONE").toString());
		} else {
			swdyx_xyfzjd_lxfs.setText("");
		}

		final EditText swdyx_xyfzjd_jddz = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_jddz);
		if (BussUtil.isEmperty(map.get("ADDRESS").toString())) {
			swdyx_xyfzjd_jddz.setText(map.get("ADDRESS").toString());
		} else {
			swdyx_xyfzjd_jddz.setText("");
		}

		final EditText swdyx_xyfzjd_jyfw = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_jyfw);
		if (BussUtil.isEmperty(map.get("BUSISCOPE").toString())) {
			swdyx_xyfzjd_jyfw.setText(map.get("BUSISCOPE").toString());
		} else {
			swdyx_xyfzjd_jyfw.setText("");
		}

		final EditText swdyx_xyfzjd_jyyt = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_jyyt);
		if (BussUtil.isEmperty(map.get("BUSIUSE").toString())) {
			swdyx_xyfzjd_jyyt.setText(map.get("BUSIUSE").toString());
		} else {
			swdyx_xyfzjd_jyyt.setText("");
		}

		final EditText swdyx_xyfzjd_xwdd = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_xwdd);
		if (BussUtil.isEmperty(map.get("SELLPLACE").toString())) {
			swdyx_xyfzjd_xwdd.setText(map.get("SELLPLACE").toString());
		} else {
			swdyx_xyfzjd_xwdd.setText("");
		}

		final EditText swdyx_xyfzjd_jbr = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_jbr);
		if (BussUtil.isEmperty(map.get("TRANSACTOR").toString())) {
			swdyx_xyfzjd_jbr.setText(map.get("TRANSACTOR").toString());
		} else {
			swdyx_xyfzjd_jbr.setText("");
		}

		final EditText swdyx_xyfzjd_jd = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_jd);
		if (BussUtil.isEmperty(map.get("LONGITUDE").toString())) {
			swdyx_xyfzjd_jd.setText(map.get("LONGITUDE").toString());
		} else {
			swdyx_xyfzjd_jd.setText("");
		}

		final EditText swdyx_xyfzjd_wd = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_wd);
		if (BussUtil.isEmperty(map.get("LATITUDE").toString())) {
			swdyx_xyfzjd_wd.setText(map.get("LATITUDE").toString());
		} else {
			swdyx_xyfzjd_wd.setText("");
		}
		final EditText swdyx_xyfzjd_bz = (EditText) dialog
				.findViewById(R.id.swdyx_xyfzjd_bz);
		if (BussUtil.isEmperty(map.get("REMARK").toString())) {
			swdyx_xyfzjd_bz.setText(map.get("REMARK").toString());
		} else {
			swdyx_xyfzjd_bz.setText("");
		}

		Button save = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Webservice webservice = new Webservice(context);
				String id = "";
				if (BussUtil.isEmperty(map.get("ID").toString())) {
					id = map.get("ID").toString();
				}
				String jdmc = swdyx_xyfzjd_jdmc.getText().toString().trim();
				if (!BussUtil.isEmperty(jdmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdmcnotnull));
					return;
				}
				if (swdyx_xyfzjd_jdxz.getSelectedItemPosition()<=0) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdxznotnull));
					return;
				}
				String jyfw = swdyx_xyfzjd_jyfw.getText().toString().trim();
				if (!BussUtil.isEmperty(jyfw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdfwnotnull));
					return;
				}
				String fzr = swdyx_xyfzjd_fzr.getText().toString().trim();
				String lxdh = swdyx_xyfzjd_lxfs.getText().toString().trim();
				String dz = swdyx_xyfzjd_jddz.getText().toString().trim();

				String jyyt = swdyx_xyfzjd_jyyt.getText().toString().trim();

				String xwdd = swdyx_xyfzjd_xwdd.getText().toString().trim();

				String jbr = swdyx_xyfzjd_jbr.getText().toString().trim();

				String jd = swdyx_xyfzjd_jd.getText().toString().trim();
				String wd = swdyx_xyfzjd_wd.getText().toString().trim();
				String bz = swdyx_xyfzjd_bz.getText().toString().trim();
				String jdxz = swdyx_xyfzjd_jdxz.getSelectedItemPosition()+"";
				String result = webservice.updateSwdyxXyfzjdData(id, jdmc,
						jdxz, fzr, lxdh, dz, jyfw, jyyt, xwdd, jbr, jd, wd, bz);
				if ("true".equals(result)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editsuccess));
					map.put("BASENAME", jdmc);
					map.put("PROPERTY", jdxz);
					map.put("MANAGER", fzr);
					map.put("PHONE", lxdh);
					map.put("ADDRESS", dz);
					map.put("BUSISCOPE", jyfw);
					map.put("BUSIUSE", jyyt);
					map.put("SELLPLACE", xwdd);
					map.put("TRANSACTOR", jbr);
					map.put("LONGITUDE", jd);
					map.put("LATITUDE", wd);
					map.put("REMARK", bz);
					notifyDataSetChanged();
					dialog.dismiss();
				} else {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editfailed));
				}
			}
		});

		Button cancle = (Button) dialog
				.findViewById(R.id.swdyx_xyfzjd_cancle);
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
		public TextView tv9;
		public TextView tv10;
		public TextView tv11;
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
