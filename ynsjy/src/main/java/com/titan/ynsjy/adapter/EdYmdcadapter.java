package com.titan.ynsjy.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.ErDiaoActivity;
import com.titan.ynsjy.dao.Ym;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.dialog.HzbjDialog;
import com.titan.ynsjy.dialog.ShuziDialog;
import com.titan.ynsjy.dialog.XzzyDialog;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.EDUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.util.List;

public class EdYmdcadapter extends BaseAdapter {
	Context mContext;
	List<Ym> list = null;
	int curposition;
	String tv;
	SharedPreferences input_history;

	public EdYmdcadapter() {
		// TODO Auto-generated constructor stub
	}

	public EdYmdcadapter(Context mContext, List<Ym> list, SharedPreferences sp) {
		this.mContext = mContext;
		this.list = list;
		this.input_history = sp;
	}

	public void setData(List<Ym> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		curposition = position;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.ed_ymdclv_item, null);
			holder.ymbh = (TextView) convertView.findViewById(R.id.ymbh);
			holder.szdm = (TextView) convertView.findViewById(R.id.sz);
			holder.xiongjing = (TextView) convertView.findViewById(R.id.xj);
			holder.pjmsg = (TextView) convertView.findViewById(R.id.sg);
			holder.lmzl = (TextView) convertView.findViewById(R.id.lmzl);
			holder.lmlx = (TextView) convertView.findViewById(R.id.lmlx);
			holder.sslc = (TextView) convertView.findViewById(R.id.sslc);
			holder.beizhu = (TextView) convertView.findViewById(R.id.bz);
			holder.btndelete = (Button) convertView
					.findViewById(R.id.ym_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Ym ym = list.get(position);

		holder.ymbh.setText(ym.getYMBH());
		holder.szdm.setText(ym.getSZDM());
		holder.xiongjing.setText(ym.getXIONGJING() + "");
		holder.pjmsg.setText(ym.getPJMSG() + "");
		holder.lmzl.setText(ym.getLMZL());
		holder.lmlx.setText(ym.getLMLX());
		holder.sslc.setText(ym.getSSLC());
		holder.beizhu.setText(ym.getBEIZHU());

		// holder.ymbh.setOnClickListener(new ymOnClickListener());
		holder.szdm.setOnClickListener(new ymOnClickListener());
		holder.xiongjing.setOnClickListener(new ymOnClickListener());
		// holder.pjmsg.setOnClickListener(new ymOnClickListener());
		holder.lmzl.setOnClickListener(new ymOnClickListener());
		holder.lmlx.setOnClickListener(new ymOnClickListener());
		holder.sslc.setOnClickListener(new ymOnClickListener());
		holder.beizhu.setOnClickListener(new ymOnClickListener());
		holder.btndelete.setOnClickListener(new ymOnClickListener());
		return convertView;
	}

	class ViewHolder {
		TextView ymbh;
		TextView szdm;
		TextView xiongjing;
		TextView pjmsg;
		TextView lmzl;
		TextView lmlx;
		TextView sslc;
		TextView beizhu;
		Button btndelete;
	}

	class ymOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String title = "", feildname = "";
			switch (v.getId()) {
				// 删除
				case R.id.ym_delete:
					boolean iddelete = DataBaseHelper.deleteYm(mContext,
							list.get(curposition).getYMBH(),ErDiaoActivity.datapath);
					if (iddelete) {
						ToastUtil.setToast(mContext, "样木删除成功");
						list.remove(getItem(curposition));
						notifyDataSetChanged();
					} else {
						ToastUtil.setToast(mContext, "样木删除失败");
					}

					break;
				// 树种代码
				case R.id.sz:
					title = "树种代码";
					feildname = "SZDM";
					showXzzyDialog((TextView) v, feildname, title);
					// showDialog((TextView) v, 0);
					break;
				// 胸径
				case R.id.xj:
					title = "胸径";
					showShuziDialog((TextView) v, title);
					// showXzzyDialog(title, (TextView) v);
					break;
				// 额备注
				case R.id.bz:
					title = "备注";
					// showShuziDialog((TextView) v,title);
					showHzbjDialog(title, (TextView) v);
					break;
				// 林木质量
				case R.id.lmzl:
					title = "林木质量";
					feildname = "LMZL";
					showXzzyDialog((TextView) v, feildname, title);
					break;
				// 立木类型
				case R.id.lmlx:
					title = "立木类型";
					feildname = "LMLX";
					showXzzyDialog((TextView) v, feildname, title);
					break;
				// 所属林层
				case R.id.sslc:
					title = "所属林层";
					feildname = "SSLC";
					showXzzyDialog((TextView) v, feildname, title);
					break;
				default:
					break;
			}

		}

		/*
		 * public void showDialog(TextView tv, int type) { String title = "",
		 * feildname = ""; switch (type) { case 0: title = "树种代码"; feildname =
		 * "SZDM"; break; case 1: title = "林木质量"; feildname = "LMZL"; break;
		 * case 2: title = "立木类型"; feildname = "LMLX"; break; case 3: title =
		 * "所属林层"; feildname = "SSLC"; break;
		 * 
		 * } if (type == 0) {
		 * 
		 * } else if (type == 1 || type == 2 || type == 3) { List<Row> xzlist =
		 * EDUtil.getEdAttributeList(mContext, feildname, "YMDC_TB"); XzzyDialog
		 * xzdialog = new XzzyDialog(mContext, R.style.Dialog, title, xzlist,
		 * tv, list, curposition, EdYmdcadapter.this); //
		 * xzdialog.setCompletelistener(EdLineAdapter.this); xzdialog.show();
		 * BussUtil.setDialogParams(mContext, xzdialog, 0.5, 0.5); } else {
		 * 
		 * }
		 * 
		 * }
		 */

	}

	public void showXzzyDialog(TextView tv, String feildname, String title) {
		List<Row> xzlist = null;
		if (feildname.equals("YSSZ") || feildname.equals("ZYZCSZ")
				|| feildname.equals("SZDM")) {
			// xzlist = EDUtil.getAttributeList(mContext, "YSSZ", "config.xml");
			xzlist = EDUtil.getEdAttributeList(mContext, "YSSZ", "YMDC_TB");
		} else {
			xzlist = EDUtil.getEdAttributeList(mContext, feildname, "YMDC_TB");
		}
		/*
		 * List<Row> xzlist = EDUtil.getEdAttributeList(mContext, feildname,
		 * "YMDC_TB");
		 */
		XzzyDialog xzdialog = new XzzyDialog(mContext, title, xzlist, tv,
				list.get(curposition), EdYmdcadapter.this);
		xzdialog.show();
		BussUtil.setDialogParams(mContext, xzdialog, 0.5, 0.5);
	}

	public void showHzbjDialog(String title, TextView tv) {
		HzbjDialog wbdialog = new HzbjDialog(mContext, title, tv,
				list.get(curposition), EdYmdcadapter.this);
		wbdialog.show();
		BussUtil.setDialogParams(mContext, wbdialog, 0.5, 0.5);
	}

	public void showShuziDialog(TextView tv, String title) {
		ShuziDialog szdialog = new ShuziDialog(mContext, title, tv,
				list.get(curposition), EdYmdcadapter.this);
		szdialog.show();
		BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);
	}

}
