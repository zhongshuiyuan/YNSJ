package com.titan.ynsjy.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
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

import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.YHSWActivity;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.service.Webservice;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.util.HashMap;
import java.util.List;

public class YhswScxcbpcAdapter extends BaseAdapter {

	Context context;
	List<HashMap<String, String>> list;
	private LayoutInflater inflater = null;
	YHSWActivity activity;

	public YhswScxcbpcAdapter(Context context,List<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (YHSWActivity) context;
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
			convertView = inflater.inflate(R.layout.item_yhsw_scxcbpc, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
			holder.tv8 = (TextView) convertView.findViewById(R.id.tv8);
			holder.tv9 = (TextView) convertView.findViewById(R.id.tv9);
			holder.tv10 = (TextView) convertView.findViewById(R.id.tv10);
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

		if (BussUtil.isEmperty(list.get(position).get("YDBH"))) {
			holder.tv1.setText(list.get(position).get("YDBH"));
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("DCR"))) {
			holder.tv2.setText(list.get(position).get("DCR"));
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("DCDW"))) {
			holder.tv3.setText(list.get(position).get("DCDW"));
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("JZMC"))) {
			holder.tv4.setText(list.get(position).get("JZMC"));
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBR"))) {
			holder.tv5.setText(list.get(position).get("SBR"));
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBSJ"))) {
			String sbsj = list.get(position).get("SBSJ").replace("/", "-");
			holder.tv6.setText(sbsj);
		} else {
			holder.tv6.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SCZT"))) {
			if ("0".equals(list.get(position).get("SCZT"))) {
				holder.tv7.setText(context.getResources().getString(
						R.string.havenotupload));
				holder.tv10.setVisibility(View.VISIBLE);
				holder.tv9.setVisibility(View.VISIBLE);
			} else if ("1".equals(list.get(position).get("SCZT"))) {
				holder.tv7.setText(context.getResources().getString(
						R.string.haveupload));
				holder.tv10.setVisibility(View.GONE);
				holder.tv9.setVisibility(View.GONE);
			} else {
				holder.tv7.setText("");
				holder.tv10.setVisibility(View.VISIBLE);
				holder.tv9.setVisibility(View.VISIBLE);
			}
		} else {
			holder.tv7.setText("");
			holder.tv10.setVisibility(View.VISIBLE);
			holder.tv9.setVisibility(View.VISIBLE);
		}
		holder.tv8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCkeckDialog(list.get(position));
			}
		});
		holder.tv9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDialog(list.get(position));
			}
		});
		holder.tv10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShangBao(list.get(position));
			}
		});
		return convertView;
	}

	/**松材线虫病普查 上报 */
	protected void ShangBao(HashMap<String, String> map) {
		Webservice web = new Webservice(context);
		String ydbh = map.get("YDBH");
		String dcr = map.get("DCR");
		String dcsj = map.get("DCSJ");
		String dcdw = map.get("DCDW");
		String ksjd = map.get("KSJD");
		String kswd = map.get("KSWD");
		String xbh = map.get("XBH");
		String xbmj = map.get("XBMJ");
		String whcd = map.get("WHCD");
		String ksmj = map.get("KSMJ");
		String sz = map.get("SZ");
		String kssl = map.get("KSSL");
		String jzmc = map.get("JZMC");
		String pjg = map.get("PJG");
		String pjxj = map.get("PJXJ");
		String qyr = map.get("QYR");
		String qybw = map.get("QYBW");
		String qysl = map.get("QYSL");
		String jjsl = map.get("JJSL");
		String xdm = map.get("XDM");
		String jdr = map.get("JDR");
		String jdrq = map.get("JDRQ");
		String jdjg = map.get("JDJG");
		String mclbf = map.get("MCLBF");
		String city = map.get("CITY");
		String county = map.get("COUNTY");
		String town = map.get("TOWN");
		String village = map.get("VILLAGE");
		String bz = map.get("BZ");
		String sbr = map.get("SBR");
		String sbsj = map.get("SBSJ");

		String result = web.addYhswSxcbpcData(ydbh, dcr, dcsj, dcdw, ksjd, kswd, xbh, xbmj, whcd, ksmj, sz, kssl, jzmc, pjg, pjxj, qyr, qybw, qysl, jjsl, xdm, jdr, jdrq, jdjg, mclbf, city, county, town, village, bz, "1", sbr, sbsj);
		String[] splits = result.split(",");
		if (splits.length > 0) {
			if ("True".equals(splits[0])) {
				DataBaseHelper.deleteYhswScxcbpcData(context, "db.sqlite",
						map.get("ID"));
				map.put("SCZT", "1");
				notifyDataSetChanged();
				ToastUtil.setToast(context,
						context.getResources()
								.getString(R.string.uploadsuccess));
			} else {
				ToastUtil
						.setToast(
								context,
								context.getResources().getString(
										R.string.uploadfailed));
			}
		} else {
			ToastUtil.setToast(context,
					context.getResources().getString(R.string.uploadfailed));
		}
	}

	/**松材线虫病普查 编辑 */
	protected void showEditDialog(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.yhsw_scxcbpc_add);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView yhsw_ycddc_head = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_head);
		yhsw_ycddc_head.setText(context.getResources().getString(R.string.scxcbpcedit));

		View status = dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		final TextView yhsw_scxcbpc_ydbh = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_ydbh);
		yhsw_scxcbpc_ydbh.setText(map.get("YDBH").toString());

		final EditText yhsw_scxcbpc_dcr = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_dcr);
		yhsw_scxcbpc_dcr.setText(map.get("DCR").toString());

		final TextView yhsw_scxcbpc_dcrq = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_dcrq);
		yhsw_scxcbpc_dcrq.setText(map.get("DCSJ").toString());

		final EditText yhsw_scxcbpc_dcdw = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_dcdw);
		yhsw_scxcbpc_dcdw.setText(map.get("DCDW").toString());

		final EditText yhsw_scxcbpc_jd = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_jd);
		yhsw_scxcbpc_jd.setText(map.get("KSJD").toString());

		final EditText yhsw_scxcbpc_wd = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_wd);
		yhsw_scxcbpc_wd.setText(map.get("KSWD").toString());

		final EditText yhsw_scxcbpc_xbh = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_xbh);
		yhsw_scxcbpc_xbh.setText(map.get("XBH").toString());

		final EditText yhsw_scxcbpc_xbmj = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_xbmj);
		yhsw_scxcbpc_xbmj.setText(map.get("XBMJ").toString());

		final Spinner yhsw_scxcbpc_whcd = (Spinner) dialog
				.findViewById(R.id.yhsw_scxcbpc_whcd);
		ArrayAdapter whcdapter = new ArrayAdapter(context, R.layout.myspinner,
				context.getResources().getStringArray(R.array.mcwhcd));
		yhsw_scxcbpc_whcd.setAdapter(whcdapter);
		if (BussUtil.isEmperty(map.get("WHCD"))) {
			int a = Integer.parseInt(map.get("WHCD"));
			yhsw_scxcbpc_whcd.setSelection(a);
		}

		final EditText yhsw_scxcbpc_ksssmj = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_ksssmj);
		yhsw_scxcbpc_ksssmj.setText(map.get("KSMJ").toString());

		final EditText yhsw_scxcbpc_sz = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_sz);
		yhsw_scxcbpc_sz.setText(map.get("SZ").toString());

		final EditText yhsw_scxcbpc_kssssl = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_kssssl);
		yhsw_scxcbpc_kssssl.setText(map.get("KSSL").toString());

		final EditText yhsw_scxcbpc_jzmc = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_jzmc);
		yhsw_scxcbpc_jzmc.setText(map.get("JZMC").toString());

		final EditText yhsw_scxcbpc_pjg = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_pjg);
		yhsw_scxcbpc_pjg.setText(map.get("PJG").toString());

		final EditText yhsw_scxcbpc_pjxj = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_pjxj);
		yhsw_scxcbpc_pjxj.setText(map.get("PJXJ").toString());

		final EditText yhsw_scxcbpc_qyr = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_qyr);
		yhsw_scxcbpc_qyr.setText(map.get("QYR").toString());

		final EditText yhsw_scxcbpc_qybw = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_qybw);
		yhsw_scxcbpc_qybw.setText(map.get("QYBW").toString());

		final EditText yhsw_scxcbpc_qysl = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_qysl);
		yhsw_scxcbpc_qysl.setText(map.get("QYSL").toString());

		final EditText yhsw_scxcbpc_jjsl = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_jjsl);
		yhsw_scxcbpc_jjsl.setText(map.get("JJSL").toString());

		final EditText yhsw_scxcbpc_xdm = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_xdm);
		yhsw_scxcbpc_xdm.setText(map.get("XDM").toString());

		final EditText yhsw_scxcbpc_jdr = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_jdr);
		yhsw_scxcbpc_jdr.setText(map.get("JDR").toString());

		final Button yhsw_scxcbpc_jdrq = (Button) dialog
				.findViewById(R.id.yhsw_scxcbpc_jdrq);
		yhsw_scxcbpc_jdrq.setText(map.get("JDRQ").toString());
		yhsw_scxcbpc_jdrq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_scxcbpc_jdrq, false);
			}
		});

		final EditText yhsw_scxcbpc_jdjg = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_jdjg);
		yhsw_scxcbpc_jdjg.setText(map.get("JDJG").toString());

		final EditText yhsw_scxcbpc_sflb = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_sflb);
		yhsw_scxcbpc_sflb.setText(map.get("MCLBF").toString());

		final EditText yhsw_scxcbpc_city = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_city);
		yhsw_scxcbpc_city.setText(map.get("CITY").toString());

		final EditText yhsw_scxcbpc_county = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_county);
		yhsw_scxcbpc_county.setText(map.get("COUNTY").toString());

		final EditText yhsw_scxcbpc_town = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_town);
		yhsw_scxcbpc_town.setText(map.get("TOWN").toString());

		final EditText yhsw_scxcbpc_village = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_village);
		yhsw_scxcbpc_village.setText(map.get("VILLAGE").toString());

		final EditText yhsw_scxcbpc_sbr = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_sbr);
		yhsw_scxcbpc_sbr.setText(map.get("SBR").toString());

		final Button yhsw_scxcbpc_sbsj = (Button) dialog
				.findViewById(R.id.yhsw_scxcbpc_sbsj);
		yhsw_scxcbpc_sbsj.setText(map.get("SBSJ").toString());
		yhsw_scxcbpc_sbsj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_scxcbpc_sbsj, false);
			}
		});

		final TextView yhsw_scxcbpc_sbzt = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_sbzt);
		if ("1".equals(map.get("SCZT").toString())) {
			yhsw_scxcbpc_sbzt.setText(context.getResources().getString(
					R.string.haveupload));
		} else if ("0".equals(map.get("SCZT").toString())) {
			yhsw_scxcbpc_sbzt.setText(context.getResources().getString(
					R.string.havenotupload));
		} else {
			yhsw_scxcbpc_sbzt.setText("");
		}

		final EditText yhsw_scxcbpc_bz = (EditText) dialog
				.findViewById(R.id.yhsw_scxcbpc_bz);
		yhsw_scxcbpc_bz.setText(map.get("BZ").toString());

		Button upload = (Button) dialog.findViewById(R.id.yhsw_scxcbpc_upload);
		upload.setText(context.getResources().getString(R.string.bc));
		Button bdsave = (Button) dialog.findViewById(R.id.yhsw_scxcbpc_bdsave);
		bdsave.setVisibility(View.GONE);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_scxcbpc_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcsj = yhsw_scxcbpc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcr = yhsw_scxcbpc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}

				String dcdw = yhsw_scxcbpc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String jd = yhsw_scxcbpc_jd.getText().toString().trim();
				if (TextUtils.isEmpty(jd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksjdnotnull));
					return;
				}
				String wd = yhsw_scxcbpc_wd.getText().toString().trim();
				if (TextUtils.isEmpty(wd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.kswdnotnull));
					return;
				}
				String xbh = yhsw_scxcbpc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_scxcbpc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String whcd = yhsw_scxcbpc_whcd.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whcd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whcdnotnull));
					return;
				}
				String ksssmj = yhsw_scxcbpc_ksssmj.getText().toString().trim();
				if (TextUtils.isEmpty(ksssmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksssmjnotnull));
					return;
				}
				String sz = yhsw_scxcbpc_sz.getText().toString().trim();
				if (TextUtils.isEmpty(sz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sznotnull));
					return;
				}
				String kssssl = yhsw_scxcbpc_kssssl.getText().toString().trim();
				if (TextUtils.isEmpty(kssssl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksssslnotnull));
					return;
				}
				String jzmc = yhsw_scxcbpc_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}

				String qyr = yhsw_scxcbpc_qyr.getText().toString().trim();
				if (TextUtils.isEmpty(qyr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.qyrnotnull));
					return;
				}
				String qysl = yhsw_scxcbpc_qysl.getText().toString().trim();
				if (TextUtils.isEmpty(qysl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.qyslnotnull));
					return;
				}
				String jjsl = yhsw_scxcbpc_jjsl.getText().toString().trim();
				if (TextUtils.isEmpty(jjsl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jjslnotnull));
					return;
				}
				String xdm = yhsw_scxcbpc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String jdr = yhsw_scxcbpc_jdr.getText().toString().trim();
				if (TextUtils.isEmpty(jdr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdrnotnull));
					return;
				}
				String jdrq = yhsw_scxcbpc_jdrq.getText().toString().trim();
				if (TextUtils.isEmpty(jdrq)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdrqnotnull));
					return;
				}

				String jdjg = yhsw_scxcbpc_jdjg.getText().toString().trim();
				if (TextUtils.isEmpty(jdjg)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdjgnotnull));
					return;
				}
				String sflb = yhsw_scxcbpc_sflb.getText().toString().trim();
				if (TextUtils.isEmpty(sflb)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.mcywlbnotnull));
					return;
				}
				String city = yhsw_scxcbpc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_scxcbpc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_scxcbpc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_scxcbpc_village.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String sbr = yhsw_scxcbpc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_scxcbpc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}

				String pjg = yhsw_scxcbpc_pjg.getText().toString().trim();
				String pjxj = yhsw_scxcbpc_pjxj.getText().toString().trim();
				String qybw = yhsw_scxcbpc_qybw.getText().toString().trim();
				String bz = yhsw_scxcbpc_bz.getText().toString().trim();
				DataBaseHelper.updateYhswScxcbpcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, jd, wd, xbh, xbmj, whcd, ksssmj, sz, kssssl, jzmc, pjg, pjxj, qyr, qybw, qysl, jjsl, xdm, jdr, jdrq, jdjg, sflb, city, county, town, village, bz, "0", sbr, sbsj);
				map.put("YDBH", ydbh);
				map.put("DCR", dcr);
				map.put("DCSJ", dcsj);
				map.put("DCDW", dcdw);
				map.put("KSJD", jd);
				map.put("KSWD", wd);
				map.put("XBH", xbh);
				map.put("XBMJ", xbmj);
				map.put("KSMJ", ksssmj);
				map.put("SZ", sz);
				map.put("KSSL", kssssl);
				map.put("JZMC", jzmc);
				map.put("PJG", pjg);
				map.put("PJXJ", pjxj);
				map.put("QYR", qyr);
				map.put("QYBW", qybw);
				map.put("QYSL", qysl);
				map.put("JJSL", jjsl);
				map.put("XDM", xdm);
				map.put("JDR", jdr);
				map.put("JDRQ", jdrq);
				map.put("JDJG", jdjg);
				map.put("MCLBF", sflb);
				map.put("CITY", city);
				map.put("COUNTY", county);
				map.put("TOWN", town);
				map.put("VILLAGE", village);
				map.put("BZ", bz);
				map.put("SCZT", "0");
				map.put("SBR", sbr);
				map.put("SBSJ", sbsj);
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.editsuccess));
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		Button cancle = (Button) dialog.findViewById(R.id.yhsw_scxcbpc_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
	}

	/**松材线虫病普查 查看 */
	protected void showCkeckDialog(HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_scxcbpc_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView ydbh = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_ydbh);
		ydbh .setText(map.get("YDBH").toString());

		TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_dcr);
		dcr.setText(map.get("DCR").toString());

		TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_dcrq);
		dcsj.setText(map.get("DCSJ").replace("/", "-"));

		TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_dcdw);
		dcdw.setText(map.get("DCDW").toString());

		TextView jd = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_jd);
		jd.setText(map.get("KSJD").toString());

		TextView wd = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_wd);
		wd.setText(map.get("KSWD").toString());

		TextView xbh = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_xbh);
		xbh.setText(map.get("XBH").toString());

		TextView xbmj = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_xbmj);
		xbmj.setText(map.get("XBMJ").toString());

		TextView whcd = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_whcd);
		if (BussUtil.isEmperty(map.get("WHCD").toString())) {
			int a = Integer.parseInt(map.get("WHCD").toString());
			String[] array = context.getResources().getStringArray(
					R.array.mcwhcd);
			whcd.setText(array[a]);
		}

		TextView kssmmj = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_ksssmj);
		kssmmj.setText(map.get("KSMJ").toString());

		TextView sz = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_sz);
		sz.setText(map.get("SZ").toString());

		TextView kssssl = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_kssssl);
		kssssl.setText(map.get("KSSL").toString());

		TextView jzmc = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_jzmc);
		jzmc.setText(map.get("JZMC").toString());

		TextView pjg = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_pjg);
		pjg.setText(map.get("PJG").toString());

		TextView pjxj = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_pjxj);
		pjxj.setText(map.get("PJXJ").toString());

		TextView qyr = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_qyr);
		qyr.setText(map.get("QYR").toString());

		TextView qybw = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_qybw);
		qybw.setText(map.get("QYBW").toString());

		TextView qysl = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_qysl);
		qysl.setText(map.get("QYSL").toString());

		TextView jjsl = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_jjsl);
		jjsl.setText(map.get("JJSL").toString());

		TextView xdm = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_xdm);
		xdm.setText(map.get("XDM").toString());

		TextView jdr = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_jdr);
		jdr.setText(map.get("JDR").toString());

		TextView jdrq = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_jdrq);
		jdrq.setText(map.get("JDRQ").toString());

		TextView jdjg = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_jdjg);
		jdjg.setText(map.get("JDJG").toString());

		TextView sflb = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_sflb);
		sflb.setText(map.get("MCLBF").toString());

		TextView city = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_city);
		city.setText(map.get("CITY").toString());

		TextView county = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_county);
		county.setText(map.get("COUNTY").toString());

		TextView town = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_town);
		town.setText(map.get("TOWN").toString());

		TextView village = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_village);
		village.setText(map.get("VILLAGE").toString());

		TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_sbr);
		sbr.setText(map.get("SBR").toString());

		TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_sbsj);
		sbsj.setText(map.get("SBSJ").replace("/", "-"));


		View status = dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		TextView sbzt = (TextView) dialog
				.findViewById(R.id.yhsw_scxcbpc_sbzt);
		if (BussUtil.isEmperty(map.get("SCZT").toString())) {
			if ("1".equals(map.get("SCZT").toString())) {
				sbzt.setText(context.getResources().getString(
						R.string.haveupload));
			} else if ("0".equals(map.get("SCZT").toString())) {
				sbzt.setText(context.getResources().getString(
						R.string.havenotupload));
			}
		} else {
			sbzt.setText("");
		}
		TextView bz = (TextView) dialog.findViewById(R.id.yhsw_scxcbpc_bz);
		bz.setText(map.get("BZ").toString());

		Button back = (Button) dialog.findViewById(R.id.yhsw_scxcbpc_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
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
	}

}
