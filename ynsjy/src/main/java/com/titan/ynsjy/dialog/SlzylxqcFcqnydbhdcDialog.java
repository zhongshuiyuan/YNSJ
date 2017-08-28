package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.LxqcUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;

import java.util.HashMap;
import java.util.List;

public class SlzylxqcFcqnydbhdcDialog extends Dialog implements
		View.OnClickListener {
	Context context;
	String ydhselect;
	List<HashMap<String, String>> fcqnydbhLlist;
	TextView dleibhyy;
	TextView ywtsddjsm;
	TextView lzhongbhyy;
	TextView qyuanbhyy;
	TextView ysszhongbhyy;
	TextView lzubhyy;
	TextView zblxingbhyy;
	TextView qqdlei;
	TextView bqdlei;
	TextView qqlzhong;
	TextView bqlzhong;
	TextView qqqyuan;
	TextView bqqyuan;
	TextView qqysszhong;
	TextView bqysszhong;
	TextView qqlzu;
	TextView bqlzu;
	TextView qqzblxing;
	TextView bqzblxing;
	String name="";
	String dbpath = null;
	public SlzylxqcFcqnydbhdcDialog(Context context,List<HashMap<String, String>> fcqnydbhLlist,
			String ydhselect,String path) {
		super(context, R.style.Dialog);
		this.context = context;
		this.fcqnydbhLlist = fcqnydbhLlist;
		this.ydhselect=ydhselect;
		this.name=context.getResources().getString(R.string.ysszname);
		this.dbpath = path;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_fcqlydbhqk_view);
		setCanceledOnTouchOutside(false);
		TextView ydh=(TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		qqdlei = (TextView) findViewById(R.id.qqdlei);
		qqdlei.setOnClickListener(this);
		qqlzhong = (TextView) findViewById(R.id.qqlzhong);
		qqlzhong.setOnClickListener(this);
		qqqyuan = (TextView) findViewById(R.id.qqqyuan);
		qqqyuan.setOnClickListener(this);
		qqysszhong = (TextView) findViewById(R.id.qqysszhong);
		qqysszhong.setOnClickListener(this);
		qqlzu = (TextView) findViewById(R.id.qqlzu);
		qqlzu.setOnClickListener(this);
		qqzblxing = (TextView) findViewById(R.id.qqzblxing);
		qqzblxing.setOnClickListener(this);

		bqdlei = (TextView) findViewById(R.id.bqdlei);
		bqdlei.setOnClickListener(this);
		bqlzhong = (TextView) findViewById(R.id.bqlzhong);
		bqlzhong.setOnClickListener(this);
		bqqyuan = (TextView) findViewById(R.id.bqqyuan);
		bqqyuan.setOnClickListener(this);
		bqysszhong = (TextView) findViewById(R.id.bqysszhong);
		bqysszhong.setOnClickListener(this);
		bqlzu = (TextView) findViewById(R.id.bqlzu);
		bqlzu.setOnClickListener(this);
		bqzblxing = (TextView) findViewById(R.id.bqzblxing);
		bqzblxing.setOnClickListener(this);

		dleibhyy = (TextView) findViewById(R.id.dleibhyy);
		dleibhyy.setOnClickListener(this);
		lzhongbhyy = (TextView) findViewById(R.id.lzhongbhyy);
		lzhongbhyy.setOnClickListener(this);
		qyuanbhyy = (TextView) findViewById(R.id.qyuanbhyy);
		qyuanbhyy.setOnClickListener(this);
		ysszhongbhyy = (TextView) findViewById(R.id.ysszhongbhyy);
		ysszhongbhyy.setOnClickListener(this);
		lzubhyy = (TextView) findViewById(R.id.lzubhyy);
		lzubhyy.setOnClickListener(this);
		zblxingbhyy = (TextView) findViewById(R.id.zblxingbhyy);
		zblxingbhyy.setOnClickListener(this);

		ywtsddjsm = (TextView) findViewById(R.id.ywtsddjsm);
		ywtsddjsm.setOnClickListener(this);


		fcqnydbhLlist = DataBaseHelper.getFcqlydbhqkData(context);
		HashMap<String, String> map = null;
		if (fcqnydbhLlist.size() > 0) {
			map = fcqnydbhLlist.get(0);
		}
		if (map != null) {
			qqdlei.setText(map.get("QQDL").toString());
			qqlzhong.setText(map.get("QQLZ").toString());
			qqqyuan.setText(map.get("QQQY").toString());
			qqysszhong.setText(map.get("QQYSSZ").toString());
			qqlzu.setText(map.get("QQLZU").toString());
			qqzblxing.setText(map.get("QQZBLX").toString());

			bqdlei.setText(map.get("BQDL").toString());
			bqlzhong.setText(map.get("BQLZ").toString());
			bqqyuan.setText(map.get("BQQY").toString());
			bqysszhong.setText(map.get("BQYSSZ").toString());
			bqlzu.setText(map.get("BQLZU").toString());
			bqzblxing.setText(map.get("BQZBLX").toString());

			dleibhyy.setText(map.get("DLBHYY").toString());
			lzhongbhyy.setText(map.get("LZBHYY").toString());
			qyuanbhyy.setText(map.get("QYBHYY").toString());
			ysszhongbhyy.setText(map.get("YSSZBHYY").toString());
			lzubhyy.setText(map.get("LZUBHYY").toString());
			zblxingbhyy.setText(map.get("ZBLXBHYY").toString());

			ywtsddjsm.setText(map.get("YDYWTSDDJSM").toString());
			ydh.setText(ydhselect);
		}
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				String[] zd = context.getResources().getStringArray(
						R.array.lxqcfcqydbhzd);
				hashmap.put("YDH", ydhselect);
				String qqdl = qqdlei.getText().toString().trim();
				hashmap.put("QQDL", qqdl);
				String qqlzhongtx = qqlzhong.getText().toString().trim();
				hashmap.put("QQLZ", qqlzhongtx);
				String qqqyuantx = qqqyuan.getText().toString().trim();
				hashmap.put("QQQY", qqqyuantx);
				String qqysszhongtx = qqysszhong.getText().toString().trim();
				hashmap.put("QQYSSZ", qqysszhongtx);
				String qqlzutx = qqlzu.getText().toString().trim();
				hashmap.put("QQLZU", qqlzutx);
				String qqzblxingtx = qqzblxing.getText().toString().trim();
				hashmap.put("QQZBLX", qqzblxingtx);

				String bqdleitx = bqdlei.getText().toString().trim();
				hashmap.put("BQDL", bqdleitx);
				String bqlzhongtx = bqlzhong.getText().toString().trim();
				hashmap.put("BQLZ", bqlzhongtx);
				String bqqyuantx = bqqyuan.getText().toString().trim();
				hashmap.put("BQQY", bqqyuantx);
				String bqysszhongtx = bqysszhong.getText().toString().trim();
				hashmap.put("BQYSSZ", bqysszhongtx);
				String bqlzutx = bqlzu.getText().toString().trim();
				hashmap.put("BQLZU", bqlzutx);
				String bqzblxingtx = bqzblxing.getText().toString().trim();
				hashmap.put("BQZBLX", bqzblxingtx);

				String dleibhyytx = dleibhyy.getText().toString().trim();
				hashmap.put("DLBHYY", dleibhyytx);
				String lzhongbhyytx = lzhongbhyy.getText().toString().trim();
				hashmap.put("LZBHYY", lzhongbhyytx);
				String qyuanbhyytx = qyuanbhyy.getText().toString().trim();
				hashmap.put("QYBHYY", qyuanbhyytx);
				String ysszhongbhyytx = ysszhongbhyy.getText().toString()
						.trim();
				hashmap.put("YSSZBHYY", ysszhongbhyytx);
				String lzubhyytx = lzubhyy.getText().toString().trim();
				hashmap.put("LZUBHYY", lzubhyytx);
				String zblxingbhyytx = zblxingbhyy.getText().toString().trim();
				hashmap.put("ZBLXBHYY", zblxingbhyytx);
				String ywtsddjsmtx = ywtsddjsm.getText().toString().trim();
				hashmap.put("YDYWTSDDJSM", ywtsddjsmtx);
				DataBaseHelper.deleteFcqnydbhqkAllData(context, ydhselect);
				DataBaseHelper.addFcqnydbhqkData(context, zd, hashmap);
				ToastUtil.setToast(context,context.getResources().getString(R.string.editsuccess));
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		/**ǰ�ڵ���*/ 
		case R.id.qqdlei:
			List<Row> qqdllist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "EDDL");
			XzzyDialog qqdldialog = new XzzyDialog(context,"ǰ�ڵ���", qqdllist, qqdlei);
			BussUtil.setDialogParams(context, qqdldialog, 0.5, 0.5);
			break;
			/**ǰ������*/ 
		case R.id.qqlzhong:
			List<Row> linzhonglist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "LINZHONG");
			XzzyDialog linzhongdialog = new XzzyDialog(context, "ǰ������", linzhonglist, qqlzhong);
			BussUtil.setDialogParams(context, linzhongdialog, 0.5, 0.5);
			break;

			/**ǰ����Դ*/ 
		case R.id.qqqyuan:
			List<Row> qiyuanlist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "QIYUAN");
			XzzyDialog qiyuandialog = new XzzyDialog(context,"ǰ����Դ", qiyuanlist, qqqyuan);
			BussUtil.setDialogParams(context, qiyuandialog, 0.5, 0.5);
			break;
			/**ǰ����������*/ 
		case R.id.qqysszhong:
			List<Row> qqysszlist=LxqcUtil.getAttributeList(context,name,dbpath);
			XzzyDialog qqysszdialog = new XzzyDialog(context,"ǰ����������", qqysszlist, qqysszhong);
			BussUtil.setDialogParams(context, qqysszdialog, 0.5, 0.5);
			break;
			/**ǰ������*/ 
		case R.id.qqlzu:
			List<Row> qqlingzulist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "LINGZU");
			XzzyDialog qqlingzudialog = new XzzyDialog(context,"ǰ������", qqlingzulist, qqlzu);
			BussUtil.setDialogParams(context, qqlingzudialog, 0.5, 0.5);
			break;
			/**ǰ��ֲ������*/ 
		case R.id.qqzblxing:
			List<Row> qqzblxlist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "ZBLX");
			XzzyDialog qqzblxdialog = new XzzyDialog(context,"ǰ��ֲ������", qqzblxlist, qqzblxing);
			BussUtil.setDialogParams(context, qqzblxdialog, 0.5, 0.5);
			break;
			/**���ڵ���*/ 
		case R.id.bqdlei:
			List<Row> bqdllist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "EDDL");
			XzzyDialog bqdldialog = new XzzyDialog(context,"���ڵ���", bqdllist, bqdlei);
			BussUtil.setDialogParams(context, bqdldialog, 0.5, 0.5);
			break;
			/**��������*/ 
		case R.id.bqlzhong:
			List<Row> bqlingzhonglist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "LINZHONG");
			XzzyDialog bqlinzhongdialog = new XzzyDialog(context, "��������", bqlingzhonglist, bqlzhong);
			BussUtil.setDialogParams(context, bqlinzhongdialog, 0.5, 0.5);
			break;
			/**������Դ*/ 
		case R.id.bqqyuan:
			List<Row> bqqiyuanlist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "QIYUAN");
			XzzyDialog bqqiyuandialog = new XzzyDialog(context,"������Դ", bqqiyuanlist, bqqyuan);
			BussUtil.setDialogParams(context, bqqiyuandialog, 0.5, 0.5);
			break;

			/**������������*/ 
		case R.id.bqysszhong:
			List<Row> bqysszlist =LxqcUtil.getAttributeList(context,name,dbpath);
			XzzyDialog bqysszdialog = new XzzyDialog(context,"������������", bqysszlist, bqysszhong);
			BussUtil.setDialogParams(context, bqysszdialog, 0.5, 0.5);
			break;
			/**��������*/ 
		case R.id.bqlzu:
			List<Row> bqlingzulist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "LINGZU");
			XzzyDialog bqlingzudialog = new XzzyDialog(context,"��������", bqlingzulist, bqlzu);
			BussUtil.setDialogParams(context, bqlingzudialog, 0.5, 0.5);
			break;
			/**����ֲ������*/ 
		case R.id.bqzblxing:
			List<Row> bqzblxlist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "ZBLX");
			XzzyDialog bqzblxdialog = new XzzyDialog(context,"����ֲ������", bqzblxlist, bqzblxing);
			BussUtil.setDialogParams(context, bqzblxdialog, 0.5, 0.5);
			break;
			/**����仯ԭ��*/ 
		case R.id.dleibhyy:
			List<Row> wclzlqklist = ResourcesManager.getAssetsAttributeList(
					context, "slzylxqc.xml", "DLBHYY");
			XzzyDialog dialog = new XzzyDialog(context, "����仯ԭ��", wclzlqklist, dleibhyy);
			BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			break;
			/**���ֱ仯ԭ��*/ 
		case R.id.lzhongbhyy:
			HzbjDialog lzbhyydialog=new HzbjDialog(context,  "���ֱ仯ԭ��", lzhongbhyy, null, "");
			BussUtil.setDialogParams(context, lzbhyydialog, 0.5, 0.5);
			break;
			/**��Դ�仯ԭ��*/ 
		case R.id.qyuanbhyy:
			HzbjDialog qybhyydialog=new HzbjDialog(context, "��Դ�仯ԭ��", qyuanbhyy, null, "");
			BussUtil.setDialogParams(context, qybhyydialog, 0.5, 0.5);
			break;
			/**�������ֱ仯ԭ��*/ 
		case R.id.ysszhongbhyy:
			HzbjDialog ysszbhyydialog=new HzbjDialog(context, "�������ֱ仯ԭ��", ysszhongbhyy,null, "");
			BussUtil.setDialogParams(context, ysszbhyydialog, 0.5, 0.5);
			break;
			/**����仯ԭ��*/ 
		case R.id.lzubhyy:
			HzbjDialog linzubhyydialog=new HzbjDialog(context,"����仯ԭ��", lzubhyy,null, "");
			BussUtil.setDialogParams(context, linzubhyydialog, 0.5, 0.5);
			break;
			/**ֲ�����ͱ仯ԭ��*/ 
		case R.id.zblxingbhyy:
			HzbjDialog zblxbhyydialog=new HzbjDialog(context,"ֲ�����ͱ仯ԭ��", zblxingbhyy, null, "");
			BussUtil.setDialogParams(context, zblxbhyydialog, 0.5, 0.5);
			break;
			/**������������Դ���˵��*/ 
		case R.id.ywtsddjsm:
			HzbjDialog ywtsdddialog=new HzbjDialog(context,"������������Դ���˵��", ywtsddjsm, null, "");
			BussUtil.setDialogParams(context, ywtsdddialog, 0.5, 0.5);
			break;

		}
	}

}
