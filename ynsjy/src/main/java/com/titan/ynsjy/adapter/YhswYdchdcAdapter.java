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

public class YhswYdchdcAdapter extends BaseAdapter {

	Context context;
	List<HashMap<String, String>> list;
	List<HashMap<String, String>> xclist;
	HashMap<String, String> xcmap;
	private LayoutInflater inflater = null;
	YHSWActivity activity;

	public YhswYdchdcAdapter(Context context,
							 List<HashMap<String, String>> list,
							 List<HashMap<String, String>> xclist) {
		this.context = context;
		this.list = list;
		this.xclist = xclist;
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
			convertView = inflater.inflate(R.layout.item_yhsw_ydchdc, null);
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
			holder.tv11 = (TextView) convertView.findViewById(R.id.tv11);
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
		if (BussUtil.isEmperty(list.get(position).get("HCMC"))) {
			holder.tv4.setText(list.get(position).get("HCMC"));
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBR"))) {
			holder.tv5.setText(list.get(position).get("SBR"));
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBSJ"))) {
			holder.tv6.setText(list.get(position).get("SBSJ"));
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
				ShangBao(xcmap,list.get(position));
			}
		});
		holder.tv11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showYdchxcDialog(xcmap, list.get(position));
			}
		});
		return convertView;
	}

	/* 样地虫害详查 查看 */
	protected void showYdchxcDialog(HashMap<String, String> xcmap,
									HashMap<String, String> map) {
		// 判断是否存在对应的详情表
		for (int i = 0; i < xclist.size(); i++) {
			if (BussUtil.isEmperty(map.get("YDBH"))
					&& BussUtil.isEmperty(xclist.get(i).get("YDBH"))) {
				if (map.get("YDBH").equals(xclist.get(i).get("YDBH"))) {
					xcmap = xclist.get(i);
					break;
				}
			}
		}
		if (xcmap != null) {
			final Dialog dialog = new Dialog(context, R.style.Dialog);
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			dialog.setContentView(R.layout.yhsw_ydchxcb_check);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			TextView yhsw_ydchxcb_ydbh = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_ydbh);
			TextView yhsw_ydchxcb_ysbh = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_ysbh);
			TextView yhsw_ydchxcb_luan = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_luan);
			TextView yhsw_ydchxcb_youchong = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_youchong);
			TextView yhsw_ydchxcb_yong = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_yong);
			TextView yhsw_ydchxcb_cchong = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_cchong);
			TextView yhsw_ydchxcb_cdao = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_cdao);
			TextView yhsw_ydchxcb_shugao = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_shugao);
			TextView yhsw_ydchxcb_xjing = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_xjing);
			TextView yhsw_ydchxcb_gfu = (TextView) dialog
					.findViewById(R.id.yhsw_ydchxcb_gfu);
			yhsw_ydchxcb_ydbh.setText(xcmap.get("YDBH"));
			yhsw_ydchxcb_ysbh.setText(xcmap.get("YSBH"));
			yhsw_ydchxcb_luan.setText(xcmap.get("LUAN"));
			yhsw_ydchxcb_youchong.setText(xcmap.get("YC"));
			yhsw_ydchxcb_yong.setText(xcmap.get("YONG"));
			yhsw_ydchxcb_cchong.setText(xcmap.get("CC"));
			yhsw_ydchxcb_cdao.setText(xcmap.get("CD"));
			yhsw_ydchxcb_shugao.setText(xcmap.get("SG"));
			yhsw_ydchxcb_xjing.setText(xcmap.get("XJ"));
			yhsw_ydchxcb_gfu.setText(xcmap.get("GF"));
			Button back = (Button) dialog.findViewById(R.id.yhsw_ydchxcb_back);
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();

				}
			});
			BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
		} else {
			ToastUtil.setToast(context,
					context.getResources().getString(R.string.xcxxbcz));
		}

	}

	/* 上报 */
	protected void ShangBao(HashMap<String, String> xcmap,HashMap<String, String> map) {
		Webservice web = new Webservice(context);
		String ydbh = map.get("YDBH");
		String dcr = map.get("DCR");
		String dcsj = map.get("DCTIME");
		String dcdw = map.get("DCDW");
		String ydxz = map.get("YDXZ");
		String ydc = map.get("YDC");
		String ydk = map.get("YDK");
		String ydmj = map.get("YDMJ");
		String dclx = map.get("DCLX");
		String yddbmj = map.get("YDDBMJ");
		String zhmj = map.get("ZHMJ");
		String hb = map.get("HB");
		String ycjjd = map.get("YCJJD");
		String ycjwd = map.get("YCJWD");
		String xbh = map.get("XBH");
		String xbmj = map.get("XBMJ");
		String city = map.get("CITY");
		String county = map.get("COUNTY");
		String town = map.get("TOWN");
		String village = map.get("VILLAGE");
		String xdm = map.get("XDM");
		String jzmc = map.get("JZMC");
		String chmc = map.get("HCMC");
		String ct = map.get("CT");
		String whbw = map.get("WHBW");
		String whcd = map.get("WHCD");
		String ly = map.get("SOURCE");
		String cbtj = map.get("CBTJ");
		String cl = map.get("CAGE");
		String czl = map.get("CZL");
		String ckmd = map.get("CKMD");
		String sl = map.get("SAGE");
		String ybd = map.get("YBD");
		String jkzs = map.get("JKZS");
		String swzs = map.get("SWZS");
		String ykh = map.get("YKH");
		String yksd = map.get("YKSD");
		String pw = map.get("PW");
		String px = map.get("PX");
		String gslx = map.get("GSLX");
		String mpmc = map.get("MPMC");
		String mpzmj = map.get("MPZMJ");
		String cml = map.get("CML");
		String zymmpz = map.get("ZYMMPZ");
		String bz = map.get("BZ");
		String sbr = map.get("SBR");
		String sbsj = map.get("SBSJ");
		String result = web.addYhswYdchdcData(ydbh, dcr, dcsj, dcdw, ydxz, ydc,
				ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj,
				city, county, town, village, xdm, jzmc, chmc, ct, whbw, whcd,
				ly, cbtj, cl, czl, ckmd, sl, ybd, jkzs, swzs, ykh, yksd, pw,
				px, gslx, mpmc, mpzmj, cml, zymmpz, bz, "1", sbr, sbsj);
		String[] splits = result.split(",");
		if (splits.length > 0) {
			if ("True".equals(splits[0])) {
				DataBaseHelper.deleteYhswYdchdcData(context, "db.sqlite",
						map.get("ID"));
				map.put("SCZT", "1");
				notifyDataSetChanged();
				ToastUtil.setToast(context,
						context.getResources()
								.getString(R.string.uploadsuccess));
				// 判断是否存在对应的详情表
				for (int i = 0; i < xclist.size(); i++) {
					if (BussUtil.isEmperty(map.get("YDBH"))&& BussUtil.isEmperty(xclist.get(i).get("YDBH"))) {
						if (map.get("YDBH").equals(xclist.get(i).get("YDBH"))) {
							xcmap = xclist.get(i);
							break;
						}
					}
				}
				if (xcmap != null) {
					String xcresult = web.addYhswYdchxcData(xcmap.get("YDBH"),
							xcmap.get("YSBH"), xcmap.get("LUAN"),
							xcmap.get("YC"), xcmap.get("YONG"),
							xcmap.get("CC"), xcmap.get("CD"), xcmap.get("SG"),
							xcmap.get("XJ"), xcmap.get("GF"));
					String[] xcsplits = xcresult.split(",");
					if (xcsplits.length > 0) {
						if ("True".equals(xcsplits[0])) {
							ToastUtil.setToast(context, context.getResources()
									.getString(R.string.xcsbsuccess));
							DataBaseHelper.deleteYhswYdchxcData(context,
									"db.sqlite", xcmap.get("ID"));
						} else {
							ToastUtil.setToast(context, context.getResources()
									.getString(R.string.xcsbfailed));
						}
					} else {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.xcsbfailed));
					}
				}
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

	/* 编辑 */
	protected void showEditDialog(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.yhsw_ydchdc_add);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView yhsw_tcd_head = (TextView) dialog
				.findViewById(R.id.yhsw_tcd_head);
		yhsw_tcd_head.setText(context.getString(R.string.editydchdc));
		final TextView yhsw_ydchdc_ydbh = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_ydbh);
		if (BussUtil.isEmperty(map.get("YDBH").toString())) {
			yhsw_ydchdc_ydbh.setText(map.get("YDBH").toString());
		} else {
			yhsw_ydchdc_ydbh.setText("");
		}
		final EditText yhsw_ydchdc_dcr = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_dcr);
		if (BussUtil.isEmperty(map.get("DCR").toString())) {
			yhsw_ydchdc_dcr.setText(map.get("DCR").toString());
		} else {
			yhsw_ydchdc_dcr.setText("");
		}
		final TextView yhsw_ydchdc_dcrq = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_dcrq);
		if (BussUtil.isEmperty(map.get("DCTIME").toString())) {
			yhsw_ydchdc_dcrq.setText(map.get("DCTIME").toString());
		} else {
			yhsw_ydchdc_dcrq.setText("");
		}
		final EditText yhsw_ydchdc_dcdw = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_dcdw);
		if (BussUtil.isEmperty(map.get("DCDW").toString())) {
			yhsw_ydchdc_dcdw.setText(map.get("DCDW").toString());
		} else {
			yhsw_ydchdc_dcdw.setText("");
		}
		final EditText yhsw_ydchdc_ydc = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ydc);
		if (BussUtil.isEmperty(map.get("YDC").toString())) {
			yhsw_ydchdc_ydc.setText(map.get("YDC").toString());
		} else {
			yhsw_ydchdc_ydc.setText("");
		}
		final EditText yhsw_ydchdc_ydk = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ydk);
		if (BussUtil.isEmperty(map.get("YDK").toString())) {
			yhsw_ydchdc_ydk.setText(map.get("YDK").toString());
		} else {
			yhsw_ydchdc_ydk.setText("");
		}
		final Spinner yhsw_ydchdc_ydxz = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_ydxz);
		final EditText yhsw_ydchdc_ydmj = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ydmj);
		if (BussUtil.isEmperty(map.get("YDMJ").toString())) {
			yhsw_ydchdc_ydmj.setText(map.get("YDMJ").toString());
		} else {
			yhsw_ydchdc_ydmj.setText("");
		}
		final Spinner yhsw_ydchdc_dclx = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_dclx);
		final EditText yhsw_ydchdc_yddbmj = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_yddbmj);
		if (BussUtil.isEmperty(map.get("YDDBMJ").toString())) {
			yhsw_ydchdc_yddbmj.setText(map.get("YDDBMJ").toString());
		} else {
			yhsw_ydchdc_yddbmj.setText("");
		}
		final EditText yhsw_ydchdc_zhmj = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_zhmj);
		if (BussUtil.isEmperty(map.get("ZHMJ").toString())) {
			yhsw_ydchdc_zhmj.setText(map.get("ZHMJ").toString());
		} else {
			yhsw_ydchdc_zhmj.setText("");
		}
		final EditText yhsw_ydchdc_hb = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_hb);
		if (BussUtil.isEmperty(map.get("HB").toString())) {
			yhsw_ydchdc_hb.setText(map.get("HB").toString());
		} else {
			yhsw_ydchdc_hb.setText("");
		}
		final EditText yhsw_ydchdc_ycjjd = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ycjjd);
		if (BussUtil.isEmperty(map.get("YCJJD").toString())) {
			yhsw_ydchdc_ycjjd.setText(map.get("YCJJD").toString());
		} else {
			yhsw_ydchdc_ycjjd.setText("");
		}
		final EditText yhsw_ydchdc_ycjwd = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ycjwd);
		if (BussUtil.isEmperty(map.get("YCJWD").toString())) {
			yhsw_ydchdc_ycjwd.setText(map.get("YCJWD").toString());
		} else {
			yhsw_ydchdc_ycjwd.setText("");
		}
		final EditText yhsw_ydchdc_xbh = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_xbh);
		if (BussUtil.isEmperty(map.get("XBH").toString())) {
			yhsw_ydchdc_xbh.setText(map.get("XBH").toString());
		} else {
			yhsw_ydchdc_xbh.setText("");
		}
		final EditText yhsw_ydchdc_xbmj = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_xbmj);
		if (BussUtil.isEmperty(map.get("XBMJ").toString())) {
			yhsw_ydchdc_xbmj.setText(map.get("XBMJ").toString());
		} else {
			yhsw_ydchdc_xbmj.setText("");
		}
		final EditText yhsw_ydchdc_city = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_city);
		if (BussUtil.isEmperty(map.get("CITY").toString())) {
			yhsw_ydchdc_city.setText(map.get("CITY").toString());
		} else {
			yhsw_ydchdc_city.setText("");
		}
		final EditText yhsw_ydchdc_county = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_county);
		if (BussUtil.isEmperty(map.get("COUNTY").toString())) {
			yhsw_ydchdc_county.setText(map.get("COUNTY").toString());
		} else {
			yhsw_ydchdc_county.setText("");
		}
		final EditText yhsw_ydchdc_town = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_town);
		if (BussUtil.isEmperty(map.get("TOWN").toString())) {
			yhsw_ydchdc_town.setText(map.get("TOWN").toString());
		} else {
			yhsw_ydchdc_town.setText("");
		}
		final EditText yhsw_ydchdc_village = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_village);
		if (BussUtil.isEmperty(map.get("VILLAGE").toString())) {
			yhsw_ydchdc_village.setText(map.get("VILLAGE").toString());
		} else {
			yhsw_ydchdc_village.setText("");
		}
		final EditText yhsw_ydchdc_xdm = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_xdm);
		if (BussUtil.isEmperty(map.get("XDM").toString())) {
			yhsw_ydchdc_xdm.setText(map.get("XDM").toString());
		} else {
			yhsw_ydchdc_xdm.setText("");
		}
		final EditText yhsw_ydchdc_jzmc = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_jzmc);
		if (BussUtil.isEmperty(map.get("JZMC").toString())) {
			yhsw_ydchdc_jzmc.setText(map.get("JZMC").toString());
		} else {
			yhsw_ydchdc_jzmc.setText("");
		}
		final EditText yhsw_ydchdc_chmc = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_chmc);
		if (BussUtil.isEmperty(map.get("HCMC").toString())) {
			yhsw_ydchdc_chmc.setText(map.get("HCMC").toString());
		} else {
			yhsw_ydchdc_chmc.setText("");
		}
		final Spinner yhsw_ydchdc_ct = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_ct);
		final Spinner yhsw_ydchdc_whbw = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_whbw);
		final Spinner yhsw_ydchdc_whcd = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_whcd);
		final Spinner yhsw_ydchdc_ly = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_ly);
		final Spinner yhsw_ydchdc_cbtj = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_cbtj);
		final EditText yhsw_ydchdc_cage = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_cage);
		if (BussUtil.isEmperty(map.get("CAGE").toString())) {
			yhsw_ydchdc_cage.setText(map.get("CAGE").toString());
		} else {
			yhsw_ydchdc_cage.setText("");
		}
		final EditText yhsw_ydchdc_czlv = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_czlv);
		if (BussUtil.isEmperty(map.get("CZL").toString())) {
			yhsw_ydchdc_czlv.setText(map.get("CZL").toString());
		} else {
			yhsw_ydchdc_czlv.setText("");
		}
		final EditText yhsw_ydchdc_ckmd = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ckmd);
		if (BussUtil.isEmperty(map.get("CKMD").toString())) {
			yhsw_ydchdc_ckmd.setText(map.get("CKMD").toString());
		} else {
			yhsw_ydchdc_ckmd.setText("");
		}
		final EditText yhsw_ydchdc_shuage = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_shuage);
		if (BussUtil.isEmperty(map.get("SAGE").toString())) {
			yhsw_ydchdc_shuage.setText(map.get("SAGE").toString());
		} else {
			yhsw_ydchdc_shuage.setText("");
		}
		final EditText yhsw_ydchdc_yubidu = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_yubidu);
		if (BussUtil.isEmperty(map.get("YBD").toString())) {
			yhsw_ydchdc_yubidu.setText(map.get("YBD").toString());
		} else {
			yhsw_ydchdc_yubidu.setText("");
		}
		final EditText yhsw_ydchdc_jkzs = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_jkzs);
		if (BussUtil.isEmperty(map.get("JKZS").toString())) {
			yhsw_ydchdc_jkzs.setText(map.get("JKZS").toString());
		} else {
			yhsw_ydchdc_jkzs.setText("");
		}
		final EditText yhsw_ydchdc_swzs = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_swzs);
		if (BussUtil.isEmperty(map.get("SWZS").toString())) {
			yhsw_ydchdc_swzs.setText(map.get("SWZS").toString());
		} else {
			yhsw_ydchdc_swzs.setText("");
		}
		final EditText yhsw_ydchdc_ykh = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_ykh);
		if (BussUtil.isEmperty(map.get("YKH").toString())) {
			yhsw_ydchdc_ykh.setText(map.get("YKH").toString());
		} else {
			yhsw_ydchdc_ykh.setText("");
		}
		final EditText yhsw_ydchdc_yksd = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_yksd);
		if (BussUtil.isEmperty(map.get("YKSD").toString())) {
			yhsw_ydchdc_yksd.setText(map.get("YKSD").toString());
		} else {
			yhsw_ydchdc_yksd.setText("");
		}
		final Spinner yhsw_ydchdc_powei = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_powei);
		final EditText yhsw_ydchdc_poxiang = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_poxiang);
		if (BussUtil.isEmperty(map.get("PX").toString())) {
			yhsw_ydchdc_poxiang.setText(map.get("PX").toString());
		} else {
			yhsw_ydchdc_poxiang.setText("");
		}
		final Spinner yhsw_ydchdc_gslx = (Spinner) dialog
				.findViewById(R.id.yhsw_ydchdc_gslx);
		final EditText yhsw_ydchdc_mpmc = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_mpmc);
		if (BussUtil.isEmperty(map.get("MPMC").toString())) {
			yhsw_ydchdc_mpmc.setText(map.get("MPMC").toString());
		} else {
			yhsw_ydchdc_mpmc.setText("");
		}
		final EditText yhsw_ydchdc_mpzmj = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_mpzmj);
		if (BussUtil.isEmperty(map.get("MPZMJ").toString())) {
			yhsw_ydchdc_mpzmj.setText(map.get("MPZMJ").toString());
		} else {
			yhsw_ydchdc_mpzmj.setText("");
		}
		final EditText yhsw_ydchdc_chanml = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_chanml);
		if (BussUtil.isEmperty(map.get("CML").toString())) {
			yhsw_ydchdc_chanml.setText(map.get("CML").toString());
		} else {
			yhsw_ydchdc_chanml.setText("");
		}
		final EditText yhsw_ydchdc_zymmpz = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_zymmpz);
		if (BussUtil.isEmperty(map.get("ZYMMPZ").toString())) {
			yhsw_ydchdc_zymmpz.setText(map.get("ZYMMPZ").toString());
		} else {
			yhsw_ydchdc_zymmpz.setText("");
		}
		final EditText yhsw_ydchdc_bz = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_bz);
		if (BussUtil.isEmperty(map.get("BZ").toString())) {
			yhsw_ydchdc_bz.setText(map.get("BZ").toString());
		} else {
			yhsw_ydchdc_bz.setText("");
		}
		final EditText yhsw_ydchdc_sbr = (EditText) dialog
				.findViewById(R.id.yhsw_ydchdc_sbr);
		if (BussUtil.isEmperty(map.get("SBR").toString())) {
			yhsw_ydchdc_sbr.setText(map.get("SBR").toString());
		} else {
			yhsw_ydchdc_sbr.setText("");
		}
		final TextView yhsw_ydchdc_sczt = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_uploadstatus);
		if (BussUtil.isEmperty(map.get("SCZT").toString())) {
			if ("1".equals(map.get("SCZT").toString())) {
				yhsw_ydchdc_sczt.setText(context.getResources().getString(
						R.string.haveupload));
			} else if ("0".equals(map.get("SCZT").toString())) {
				yhsw_ydchdc_sczt.setText(context.getResources().getString(
						R.string.havenotupload));
			} else {
				yhsw_ydchdc_sczt.setText("");
			}
		} else {
			yhsw_ydchdc_sczt.setText("");
		}
		final Button yhsw_ydchdc_sbsj = (Button) dialog
				.findViewById(R.id.yhsw_ydchdc_sbsj);
		if (BussUtil.isEmperty(map.get("SBSJ").toString())) {
			yhsw_ydchdc_sbsj.setText(map.get("SBSJ").toString());
		} else {
			yhsw_ydchdc_sbsj.setText("");
		}
		yhsw_ydchdc_sbsj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_ydchdc_sbsj, false);
			}
		});
		ArrayAdapter gslxadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.gslx));
		yhsw_ydchdc_gslx.setAdapter(gslxadapter);
		if (BussUtil.isEmperty(map.get("GSLX").toString())) {
			int a = Integer.parseInt(map.get("GSLX").toString());
			yhsw_ydchdc_gslx.setSelection(a);
		}

		ArrayAdapter poweiadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.pw));
		yhsw_ydchdc_powei.setAdapter(poweiadapter);
		if (BussUtil.isEmperty(map.get("PW").toString())) {
			int a = Integer.parseInt(map.get("PW").toString());
			yhsw_ydchdc_powei.setSelection(a);
		}

		ArrayAdapter cbtjadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.cbtj));
		yhsw_ydchdc_cbtj.setAdapter(cbtjadapter);
		if (BussUtil.isEmperty(map.get("CBTJ").toString())) {
			int a = Integer.parseInt(map.get("CBTJ").toString());
			yhsw_ydchdc_cbtj.setSelection(a);
		}

		ArrayAdapter lyadapter = new ArrayAdapter(context, R.layout.myspinner,
				context.getResources().getStringArray(R.array.ly));
		yhsw_ydchdc_ly.setAdapter(lyadapter);
		if (BussUtil.isEmperty(map.get("SOURCE").toString())) {
			int a = Integer.parseInt(map.get("SOURCE").toString());
			yhsw_ydchdc_ly.setSelection(a);
		}

		ArrayAdapter whcdadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.mcwhcd));
		yhsw_ydchdc_whcd.setAdapter(whcdadapter);
		if (BussUtil.isEmperty(map.get("WHCD").toString())) {
			int a = Integer.parseInt(map.get("WHCD").toString());
			yhsw_ydchdc_whcd.setSelection(a);
		}

		ArrayAdapter whbwadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.whbw));
		yhsw_ydchdc_whbw.setAdapter(whbwadapter);
		if (BussUtil.isEmperty(map.get("WHBW").toString())) {
			int a = Integer.parseInt(map.get("WHBW").toString());
			yhsw_ydchdc_whbw.setSelection(a);
		}

		ArrayAdapter ctadapter = new ArrayAdapter(context, R.layout.myspinner,
				context.getResources().getStringArray(R.array.ct));
		yhsw_ydchdc_ct.setAdapter(ctadapter);
		if (BussUtil.isEmperty(map.get("CT").toString())) {
			int a = Integer.parseInt(map.get("CT").toString());
			yhsw_ydchdc_ct.setSelection(a);
		}

		ArrayAdapter dclxadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.dclx));
		yhsw_ydchdc_dclx.setAdapter(dclxadapter);
		if (BussUtil.isEmperty(map.get("DCLX").toString())) {
			int a = Integer.parseInt(map.get("DCLX").toString());
			yhsw_ydchdc_dclx.setSelection(a);
		}

		ArrayAdapter ydxzadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.ydxz));
		yhsw_ydchdc_ydxz.setAdapter(ydxzadapter);
		if (BussUtil.isEmperty(map.get("YDXZ").toString())) {
			int a = Integer.parseInt(map.get("YDXZ").toString());
			yhsw_ydchdc_ydxz.setSelection(a);
		}

		Button upload = (Button) dialog
				.findViewById(R.id.yhsw_tcd_uploadstatus);
		upload.setText(context.getResources().getString(R.string.bc));
		Button bdsave = (Button) dialog.findViewById(R.id.yhsw_tcd_bdsave);
		bdsave.setVisibility(View.GONE);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(map.get("YDBH").toString())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcr = yhsw_ydchdc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_ydchdc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_ydchdc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String ydxz = yhsw_ydchdc_ydxz.getSelectedItemPosition()+"";
				String ydc = yhsw_ydchdc_ydc.getText().toString().trim();
				String ydk = yhsw_ydchdc_ydk.getText().toString().trim();
				String ydmj = yhsw_ydchdc_ydmj.getText().toString().trim();
				if (TextUtils.isEmpty(ydmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String dclx = yhsw_ydchdc_dclx.getSelectedItemPosition()+"";
				String yddbmj = yhsw_ydchdc_yddbmj.getText().toString().trim();
				if (TextUtils.isEmpty(yddbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String zhmj = yhsw_ydchdc_zhmj.getText().toString().trim();
				if (TextUtils.isEmpty(zhmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String hb = yhsw_ydchdc_hb.getText().toString().trim();
				String ycjjd = yhsw_ydchdc_ycjjd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjwd = yhsw_ydchdc_ycjwd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String xbh = yhsw_ydchdc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_ydchdc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String city = yhsw_ydchdc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_ydchdc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_ydchdc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_ydchdc_village.getText().toString()
						.trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String xdm = yhsw_ydchdc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String jzmc = yhsw_ydchdc_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String chmc = yhsw_ydchdc_chmc.getText().toString().trim();
				if (TextUtils.isEmpty(chmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.chmcnotnull));
					return;
				}
				String ct = yhsw_ydchdc_ct.getSelectedItemPosition()+"";
				String whbw = yhsw_ydchdc_whbw.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whbw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whbwnotnull));
					return;
				}
				String whcd = yhsw_ydchdc_whcd.getSelectedItemPosition()+"";
				String ly = yhsw_ydchdc_ly.getSelectedItemPosition()+"";
				String cbtj = yhsw_ydchdc_cbtj.getSelectedItemPosition()+"";
				String cl = yhsw_ydchdc_cage.getText().toString().trim();
				String czl = yhsw_ydchdc_czlv.getText().toString().trim();
				if (TextUtils.isEmpty(czl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.czlnotnull));
					return;
				}
				String ckmd = yhsw_ydchdc_ckmd.getText().toString().trim();
				if (TextUtils.isEmpty(ckmd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ckmdnotnull));
					return;
				}
				String sbr = yhsw_ydchdc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_ydchdc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String sl = yhsw_ydchdc_shuage.getText().toString().trim();
				String ybd = yhsw_ydchdc_yubidu.getText().toString().trim();
				String jkzs = yhsw_ydchdc_jkzs.getText().toString().trim();
				String swzs = yhsw_ydchdc_swzs.getText().toString().trim();
				String ykh = yhsw_ydchdc_ykh.getText().toString().trim();
				String yksd = yhsw_ydchdc_yksd.getText().toString().trim();
				String pw = yhsw_ydchdc_powei.getSelectedItemPosition()+"";
				String px = yhsw_ydchdc_poxiang.getText().toString().trim();
				String gslx = yhsw_ydchdc_gslx.getSelectedItemPosition()+"";
				String mpmc = yhsw_ydchdc_mpmc.getText().toString().trim();
				String mpzmj = yhsw_ydchdc_mpzmj.getText().toString().trim();
				String cml = yhsw_ydchdc_chanml.getText().toString().trim();
				String zymmpz = yhsw_ydchdc_zymmpz.getText().toString().trim();
				String bz = yhsw_ydchdc_bz.getText().toString().trim();
				DataBaseHelper.updateYhswYdchdcData(context, "db.sqlite", map
								.get("YDBH").toString(), dcr, dcsj, dcdw, ydxz, ydc,
						ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh,
						xbmj, city, county, town, village, xdm, jzmc, chmc, ct,
						whbw, whcd, ly, cbtj, cl, czl, ckmd, sl, ybd, jkzs,
						swzs, ykh, yksd, pw, px, gslx, mpmc, mpzmj, cml,
						zymmpz, bz, sbr, sbsj);
				map.put("DCR", dcr);
				map.put("DCTIME", dcsj);
				map.put("DCDW", dcdw);
				map.put("YDXZ", ydxz);
				map.put("YDC", ydc);
				map.put("YDK", ydk);
				map.put("YDMJ", ydmj);
				map.put("DCLX", dclx);
				map.put("YDDBMJ", yddbmj);
				map.put("ZHMJ", zhmj);
				map.put("HB", hb);
				map.put("YCJJD", ycjjd);
				map.put("YCJWD", ycjwd);
				map.put("XBH", xbh);
				map.put("XBMJ", xbmj);
				map.put("CITY", city);
				map.put("COUNTY", county);
				map.put("TOWN", town);
				map.put("VILLAGE", village);
				map.put("XDM", xdm);
				map.put("JZMC", jzmc);
				map.put("HCMC", chmc);
				map.put("CT", ct);
				map.put("WHBW", whbw);
				map.put("WHCD", whcd);
				map.put("SOURCE", ly);
				map.put("CBTJ", cbtj);
				map.put("CAGE", cl);
				map.put("CZL", czl);
				map.put("CKMD", ckmd);
				map.put("SAGE", sl);
				map.put("YBD", ybd);
				map.put("JKZS", jkzs);
				map.put("SWZS", swzs);
				map.put("YKH", ykh);
				map.put("YKSD", yksd);
				map.put("PW", pw);
				map.put("PX", px);
				map.put("GSLX", gslx);
				map.put("MPMC", mpmc);
				map.put("MPZMJ", mpzmj);
				map.put("CML", cml);
				map.put("ZYMMPZ", zymmpz);
				map.put("BZ", bz);
				map.put("SBR", sbr);
				map.put("SBSJ", sbsj);
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.editsuccess));
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		Button cancle = (Button) dialog.findViewById(R.id.yhsw_tcd_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
	}

	/* 查看 */
	protected void showCkeckDialog(HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_ydchdc_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView dcdid = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ydbh);
		if (BussUtil.isEmperty(map.get("YDBH").toString())) {
			dcdid.setText(map.get("YDBH").toString());
		} else {
			dcdid.setText("");
		}
		TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_dcr);
		if (BussUtil.isEmperty(map.get("DCR").toString())) {
			dcr.setText(map.get("DCR").toString());
		} else {
			dcr.setText("");
		}
		TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_dcrq);
		if (BussUtil.isEmperty(map.get("DCTIME").toString())) {
			dcsj.setText(map.get("DCTIME").toString());
		} else {
			dcsj.setText("");
		}
		TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_dcdw);
		if (BussUtil.isEmperty(map.get("DCDW").toString())) {
			dcdw.setText(map.get("DCDW").toString());
		} else {
			dcdw.setText("");
		}
		TextView ydc = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ydc);
		if (BussUtil.isEmperty(map.get("YDC").toString())) {
			ydc.setText(map.get("YDC").toString());
		} else {
			ydc.setText("");
		}
		TextView ydk = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ydk);
		if (BussUtil.isEmperty(map.get("YDK").toString())) {
			ydk.setText(map.get("YDK").toString());
		} else {
			ydk.setText("");
		}
		TextView ydxz = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ydxz);
		if(BussUtil.isEmperty(map.get("YDXZ"))){
			int a=Integer.parseInt(map.get("YDXZ"));
			String[]array=context.getResources().getStringArray(R.array.ydxz);
			ydxz.setText(array[a]);
		}
		TextView ydmj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ydmj);
		if (BussUtil.isEmperty(map.get("YDMJ").toString())) {
			ydmj.setText(map.get("YDMJ").toString());
		} else {
			ydmj.setText("");
		}
		TextView dclx = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_dclx);
		if(BussUtil.isEmperty(map.get("DCLX"))){
			int a=Integer.parseInt(map.get("DCLX"));
			String[]array=context.getResources().getStringArray(R.array.dclx);
			dclx.setText(array[a]);
		}
		TextView yddbmj = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_yddbmj);
		if (BussUtil.isEmperty(map.get("YDDBMJ").toString())) {
			yddbmj.setText(map.get("YDDBMJ").toString());
		} else {
			yddbmj.setText("");
		}
		TextView zhmj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_zhmj);
		if (BussUtil.isEmperty(map.get("ZHMJ").toString())) {
			zhmj.setText(map.get("ZHMJ").toString());
		} else {
			zhmj.setText("");
		}
		TextView hb = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_hb);
		if (BussUtil.isEmperty(map.get("HB").toString())) {
			hb.setText(map.get("HB").toString());
		} else {
			hb.setText("");
		}
		TextView ycjjd = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ycjjd);
		if (BussUtil.isEmperty(map.get("YCJJD").toString())) {
			ycjjd.setText(map.get("YCJJD").toString());
		} else {
			ycjjd.setText("");
		}
		TextView ycjwd = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ycjwd);
		if (BussUtil.isEmperty(map.get("YCJWD").toString())) {
			ycjwd.setText(map.get("YCJWD").toString());
		} else {
			ycjwd.setText("");
		}
		TextView xbh = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_xbh);
		if (BussUtil.isEmperty(map.get("XBH").toString())) {
			xbh.setText(map.get("XBH").toString());
		} else {
			xbh.setText("");
		}
		TextView xbmj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_xbmj);
		if (BussUtil.isEmperty(map.get("XBMJ").toString())) {
			xbmj.setText(map.get("XBMJ").toString());
		} else {
			xbmj.setText("");
		}
		TextView city = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_city);
		if (BussUtil.isEmperty(map.get("CITY").toString())) {
			city.setText(map.get("CITY").toString());
		} else {
			city.setText("");
		}
		TextView county = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_county);
		if (BussUtil.isEmperty(map.get("COUNTY").toString())) {
			county.setText(map.get("COUNTY").toString());
		} else {
			county.setText("");
		}
		TextView town = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_town);
		if (BussUtil.isEmperty(map.get("TOWN").toString())) {
			town.setText(map.get("TOWN").toString());
		} else {
			town.setText("");
		}
		TextView village = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_village);
		if (BussUtil.isEmperty(map.get("VILLAGE").toString())) {
			village.setText(map.get("VILLAGE").toString());
		} else {
			village.setText("");
		}
		TextView xdm = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_xdm);
		if (BussUtil.isEmperty(map.get("XDM").toString())) {
			xdm.setText(map.get("XDM").toString());
		} else {
			xdm.setText("");
		}
		TextView jzmc = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_jzmc);
		if (BussUtil.isEmperty(map.get("JZMC").toString())) {
			jzmc.setText(map.get("JZMC").toString());
		} else {
			jzmc.setText("");
		}
		TextView chmc = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_chmc);
		if (BussUtil.isEmperty(map.get("HCMC").toString())) {
			chmc.setText(map.get("HCMC").toString());
		} else {
			chmc.setText("");
		}
		TextView ct = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ct);
		if(BussUtil.isEmperty(map.get("CT"))){
			int a=Integer.parseInt(map.get("CT"));
			String[]array=context.getResources().getStringArray(R.array.ct);
			ct.setText(array[a]);
		}
		TextView whbw = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_whbw);
		if(BussUtil.isEmperty(map.get("WHBW"))){
			int a=Integer.parseInt(map.get("WHBW"));
			String[]array=context.getResources().getStringArray(R.array.whbw);
			whbw.setText(array[a]);
		}
		TextView whcd = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_whcd);
		if(BussUtil.isEmperty(map.get("WHCD"))){
			int a=Integer.parseInt(map.get("WHCD"));
			String[]array=context.getResources().getStringArray(R.array.mcwhcd);
			whcd.setText(array[a]);
		}
		TextView ly = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ly);
		if(BussUtil.isEmperty(map.get("SOURCE"))){
			int a=Integer.parseInt(map.get("SOURCE"));
			String[]array=context.getResources().getStringArray(R.array.ly);
			ly.setText(array[a]);
		}
		TextView cbtj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_cbtj);
		if(BussUtil.isEmperty(map.get("CBTJ"))){
			int a=Integer.parseInt(map.get("CBTJ"));
			String[]array=context.getResources().getStringArray(R.array.cbtj);
			cbtj.setText(array[a]);
		}
		TextView cl = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_cage);
		if (BussUtil.isEmperty(map.get("CAGE").toString())) {
			cl.setText(map.get("CAGE").toString());
		} else {
			cl.setText("");
		}
		TextView czl = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_czlv);
		if (BussUtil.isEmperty(map.get("CZL").toString())) {
			czl.setText(map.get("CZL").toString());
		} else {
			czl.setText("");
		}
		TextView ckmd = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ckmd);
		if (BussUtil.isEmperty(map.get("CKMD").toString())) {
			ckmd.setText(map.get("CKMD").toString());
		} else {
			ckmd.setText("");
		}
		TextView sl = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_shuage);
		if (BussUtil.isEmperty(map.get("SAGE").toString())) {
			sl.setText(map.get("SAGE").toString());
		} else {
			sl.setText("");
		}
		TextView ybd = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_yubidu);
		if (BussUtil.isEmperty(map.get("YBD").toString())) {
			ybd.setText(map.get("YBD").toString());
		} else {
			ybd.setText("");
		}
		TextView jkzs = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_jkzs);
		if (BussUtil.isEmperty(map.get("JKZS").toString())) {
			jkzs.setText(map.get("JKZS").toString());
		} else {
			jkzs.setText("");
		}
		TextView swzs = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_swzs);
		if (BussUtil.isEmperty(map.get("SWZS").toString())) {
			swzs.setText(map.get("SWZS").toString());
		} else {
			swzs.setText("");
		}
		TextView ykh = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_ykh);
		if (BussUtil.isEmperty(map.get("YKH").toString())) {
			ykh.setText(map.get("YKH").toString());
		} else {
			ykh.setText("");
		}
		TextView yksd = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_yksd);
		if (BussUtil.isEmperty(map.get("YKSD").toString())) {
			yksd.setText(map.get("YKSD").toString());
		} else {
			yksd.setText("");
		}
		TextView pw = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_powei);
		if(BussUtil.isEmperty(map.get("PW"))){
			int a=Integer.parseInt(map.get("PW"));
			String[]array=context.getResources().getStringArray(R.array.pw);
			pw.setText(array[a]);
		}
		TextView px = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_poxiang);
		if (BussUtil.isEmperty(map.get("PX").toString())) {
			px.setText(map.get("PX").toString());
		} else {
			px.setText("");
		}
		TextView gslx = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_gslx);
		if(BussUtil.isEmperty(map.get("GSLX"))){
			int a=Integer.parseInt(map.get("GSLX"));
			String[]array=context.getResources().getStringArray(R.array.gslx);
			gslx.setText(array[a]);
		}
		TextView mpmc = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_mpmc);
		if (BussUtil.isEmperty(map.get("MPMC").toString())) {
			mpmc.setText(map.get("MPMC").toString());
		} else {
			mpmc.setText("");
		}
		TextView mpzmj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_mpzmj);
		if (BussUtil.isEmperty(map.get("MPZMJ").toString())) {
			mpzmj.setText(map.get("MPZMJ").toString());
		} else {
			mpzmj.setText("");
		}
		TextView cml = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_chanml);
		if (BussUtil.isEmperty(map.get("CML").toString())) {
			cml.setText(map.get("CML").toString());
		} else {
			cml.setText("");
		}
		TextView zymmpz = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_zymmpz);
		if (BussUtil.isEmperty(map.get("ZYMMPZ").toString())) {
			zymmpz.setText(map.get("ZYMMPZ").toString());
		} else {
			zymmpz.setText("");
		}
		TextView bz = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_bz);
		if (BussUtil.isEmperty(map.get("BZ").toString())) {
			bz.setText(map.get("BZ").toString());
		} else {
			bz.setText("");
		}
		TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_sbr);
		if (BussUtil.isEmperty(map.get("SBR").toString())) {
			sbr.setText(map.get("SBR").toString());
		} else {
			sbr.setText("");
		}
		TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_sbsj);
		if (BussUtil.isEmperty(map.get("SBSJ").toString())) {
			sbsj.setText(map.get("SBSJ").toString());
		} else {
			sbsj.setText("");
		}
		TextView uploadstatus = (TextView) dialog
				.findViewById(R.id.yhsw_ydchdc_uploadstatus);
		if (BussUtil.isEmperty(map.get("SCZT").toString())) {
			if ("1".equals(map.get("SCZT").toString())) {
				uploadstatus.setText(context.getResources().getString(
						R.string.haveupload));
			} else if ("0".equals(map.get("SCZT").toString())) {
				uploadstatus.setText(context.getResources().getString(
						R.string.havenotupload));
			}
		} else {
			uploadstatus.setText("");
		}
		Button back = (Button) dialog.findViewById(R.id.yhsw_ydchdc_back);
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
		public TextView tv11;
		public TextView tv12;
	}

}
