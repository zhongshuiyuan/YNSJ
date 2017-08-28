package com.titan.ynsjy.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Workbook;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ResourcesManager;

/**
 * 
 * @author Administrator
 * 
 */
public class EDStatisticsActivity extends Activity {
	ListView mListView1;
	MyAdapter myAdapter;
	RelativeLayout mHead;
	LinearLayout main;
	Button btn_excel;
	ArrayList<EDAreaSum> data;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ed_tongji);

		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		// mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mListView1 = (ListView) findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		myAdapter = new MyAdapter(this, R.layout.ed_item);
		data = (ArrayList<EDAreaSum>) getIntent().getExtras().getSerializable(
				"data");

		mListView1.setAdapter(myAdapter);
		myAdapter.setData(data);
		btn_excel = (Button) findViewById(R.id.btn_excel);
		btn_excel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (data != null)
					excel(data);
			}
		});
	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	public class MyAdapter extends BaseAdapter {
		// public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();

		int id_row_layout;
		LayoutInflater mInflater;
		ArrayList<EDAreaSum> data;

		public MyAdapter(Context context, int id_row_layout) {
			super();
			this.id_row_layout = id_row_layout;
			mInflater = LayoutInflater.from(context);

		}

		public void setData(ArrayList<EDAreaSum> data) {
			this.data = data;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (data == null)
				return 0;
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentView) {
			ViewHolder holder = null;
			if (convertView == null) {
				// synchronized (StatisticsActivity.this) {
				convertView = mInflater.inflate(R.layout.ed_item, null);
				holder = new ViewHolder();

				MyHScrollView scrollView1 = (MyHScrollView) convertView
						.findViewById(R.id.horizontalScrollView1);

				holder.scrollView = scrollView1;
				holder.txt1 = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.txt2 = (TextView) convertView
						.findViewById(R.id.textView2);
				holder.txt3 = (TextView) convertView
						.findViewById(R.id.textView3);
				holder.txt4 = (TextView) convertView
						.findViewById(R.id.textView4);

				holder.txtm1 = (TextView) convertView
						.findViewById(R.id.textView_m1);
				holder.txtm2 = (TextView) convertView
						.findViewById(R.id.textView_m2);
				holder.txtm3 = (TextView) convertView
						.findViewById(R.id.textView_m3);
				holder.txtm4 = (TextView) convertView
						.findViewById(R.id.textView_m4);
				holder.txtm5 = (TextView) convertView
						.findViewById(R.id.textView_m5);
				holder.txtm6 = (TextView) convertView
						.findViewById(R.id.textView_m6);
				holder.txtm7 = (TextView) convertView
						.findViewById(R.id.textView_m7);
				holder.txtm8 = (TextView) convertView
						.findViewById(R.id.textView_m8);
				holder.txtm9 = (TextView) convertView
						.findViewById(R.id.textView_m9);
				holder.txtm10 = (TextView) convertView
						.findViewById(R.id.textView_m10);
				holder.txtm11 = (TextView) convertView
						.findViewById(R.id.textView_m11);
				holder.txtm12 = (TextView) convertView
						.findViewById(R.id.textView_m12);
				holder.txtm13 = (TextView) convertView
						.findViewById(R.id.textView_m13);
				holder.txtm14 = (TextView) convertView
						.findViewById(R.id.textView_m14);
				holder.txtm15 = (TextView) convertView
						.findViewById(R.id.textView_m15);
				holder.txtm16 = (TextView) convertView
						.findViewById(R.id.textView_m16);
				holder.txtm17 = (TextView) convertView
						.findViewById(R.id.textView_m17);
				holder.txtm18 = (TextView) convertView
						.findViewById(R.id.textView_m18);
				holder.txtm19 = (TextView) convertView
						.findViewById(R.id.textView_m19);
				holder.txtm20 = (TextView) convertView
						.findViewById(R.id.textView_m20);
				holder.txtm21 = (TextView) convertView
						.findViewById(R.id.textView_m21);
				holder.txtm22 = (TextView) convertView
						.findViewById(R.id.textView_m22);
				holder.txtm23 = (TextView) convertView
						.findViewById(R.id.textView_m23);
				holder.txtm24 = (TextView) convertView
						.findViewById(R.id.textView_m24);
				holder.txtm25 = (TextView) convertView
						.findViewById(R.id.textView_m25);

				holder.txt5 = (TextView) convertView
						.findViewById(R.id.textView5);
				holder.txt6 = (TextView) convertView
						.findViewById(R.id.textView6);
				holder.txt7 = (TextView) convertView
						.findViewById(R.id.textView7);
				holder.txt8 = (TextView) convertView
						.findViewById(R.id.textView8);
				holder.txt9 = (TextView) convertView
						.findViewById(R.id.textView9);
				holder.txt10 = (TextView) convertView
						.findViewById(R.id.textView10);
				holder.txt11 = (TextView) convertView
						.findViewById(R.id.textView11);

				MyHScrollView headSrcrollView = (MyHScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);
				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

				convertView.setTag(holder);
				// }
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txt1.setText(data.get(position).tjdw);
			holder.txt2.setText(data.get(position).ldsyq);
			holder.txt3.setText(data.get(position).sllb);
			holder.txt4.setText(String.format("%.2f",
					Double.valueOf(data.get(position).zmj)));

			holder.txtm1.setText(String.format("%.2f",
					Double.valueOf(data.get(position).lyyd_hj)));
			holder.txtm2.setText(String.format("%.2f",
					Double.valueOf(data.get(position).yld_xj)));
			holder.txtm3.setText(String.format("%.2f",
					Double.valueOf(data.get(position).qml_xj)));
			holder.txtm4.setText(String.format("%.2f",
					Double.valueOf(data.get(position).cl)));
			holder.txtm5.setText(String.format("%.2f",
					Double.valueOf(data.get(position).hjl)));
			holder.txtm6.setText(String.format("%.2f",
					Double.valueOf(data.get(position).zl)));
			holder.txtm7.setText(String.format("%.2f",
					Double.valueOf(data.get(position).sld)));
			holder.txtm8.setText(String.format("%.2f",
					Double.valueOf(data.get(position).gmld_xj)));
			holder.txtm9.setText(String.format("%.2f",
					Double.valueOf(data.get(position).gjtbgmld_xj)));
			holder.txtm10.setText(String.format("%.2f",
					Double.valueOf(data.get(position).gmjjl)));
			holder.txtm11.setText(String.format("%.2f",
					Double.valueOf(data.get(position).qtgmld)));
			holder.txtm12.setText(String.format("%.2f",
					Double.valueOf(data.get(position).wclzl_xj)));
			holder.txtm13.setText(String.format("%.2f",
					Double.valueOf(data.get(position).lgzlwcld)));
			holder.txtm14.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fwwcld)));
			holder.txtm15.setText(String.format("%.2f",
					Double.valueOf(data.get(position).mpd)));
			holder.txtm16.setText(String.format("%.2f",
					Double.valueOf(data.get(position).wlmld_xj)));
			holder.txtm17.setText(String.format("%.2f",
					Double.valueOf(data.get(position).cfjd)));
			holder.txtm18.setText(String.format("%.2f",
					Double.valueOf(data.get(position).hsjd)));
			holder.txtm19.setText(String.format("%.2f",
					Double.valueOf(data.get(position).qtwlmld)));
			holder.txtm20.setText(String.format("%.2f",
					Double.valueOf(data.get(position).yldxj)));
			holder.txtm21.setText(String.format("%.2f",
					Double.valueOf(data.get(position).hshd)));
			holder.txtm22.setText(String.format("%.2f",
					Double.valueOf(data.get(position).ssd)));
			holder.txtm23.setText(String.format("%.2f",
					Double.valueOf(data.get(position).sssd)));
			holder.txtm24.setText(String.format("%.2f",
					Double.valueOf(data.get(position).qtlyd)));
			holder.txtm25.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fzscld)));

			holder.txt5.setText(String.format("%.2f",
					Double.valueOf(data.get(position).flyyd_hj)));
			holder.txt6.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fly_yld)));
			holder.txt7.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fly_gmld_xj)));
			holder.txt8.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fly_gjtbgdgmld)));
			holder.txt9.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fly_qtgmld)));
			holder.txt10.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fly_pld)));
			holder.txt11.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fly_qtfld)));

			return convertView;
		}

		class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
			MyHScrollView mScrollViewArg;

			public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
				mScrollViewArg = scrollViewar;
			}

			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				mScrollViewArg.smoothScrollTo(l, t);
			}
		};

		class ViewHolder {
			TextView txt1;
			TextView txt2;
			TextView txt3;
			TextView txt4;

			TextView txtm1;
			TextView txtm2;
			TextView txtm3;
			TextView txtm4;
			TextView txtm5;
			TextView txtm6;
			TextView txtm7;
			TextView txtm8;
			TextView txtm9;
			TextView txtm10;
			TextView txtm11;
			TextView txtm12;
			TextView txtm13;
			TextView txtm14;
			TextView txtm15;
			TextView txtm16;
			TextView txtm17;
			TextView txtm18;
			TextView txtm19;
			TextView txtm20;
			TextView txtm21;
			TextView txtm22;
			TextView txtm23;
			TextView txtm24;
			TextView txtm25;

			TextView txt5;
			TextView txt6;
			TextView txt7;
			TextView txt8;
			TextView txt9;
			TextView txt10;
			TextView txt11;

			HorizontalScrollView scrollView;
		}
	}// end class my

	@SuppressLint("SimpleDateFormat")
	public void excel(ArrayList<EDAreaSum> data) {
		WritableWorkbook book = null;
		try {
			String path = ResourcesManager.getInstance(this).getExcelPath() + "/";
			// 创建文件夹
			File files = new File(path);
			if (!files.exists())
				files.mkdirs();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			FileOutputStream fos = new FileOutputStream(path
					+ df.format(new Date()) + "_二调.xls");

			// 创建excel表格
			book = Workbook.createWorkbook(fos);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("小班统计", 0);
			// 粗体
			WritableFont title_wf = new WritableFont(WritableFont.TIMES, 14,
					WritableFont.BOLD, false);

			WritableCellFormat titlewcf = new WritableCellFormat(title_wf);
			// 加边框
			titlewcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			// 居中显示
			titlewcf.setAlignment(jxl.format.Alignment.CENTRE);
			titlewcf.setVerticalAlignment(VerticalAlignment.CENTRE);

			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			// 以及单元格内容为test

			Label label_titl_01 = new Label(0, 0, "统计单位", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_01);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(0, 0, 0, 3);
			// 将第一列的宽度设为100
			sheet.setColumnView(0, 15);

			Label label_titl_02 = new Label(1, 0, "林地所有权", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_02);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(1, 0, 1, 3);
			sheet.setColumnView(1, 20);

			Label label_titl_03 = new Label(2, 0, "森林类别", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_03);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(2, 0, 2, 3);
			sheet.setColumnView(2, 15);

			Label label_titl_04 = new Label(3, 0, "总面积", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_04);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(3, 0, 3, 3);
			sheet.setColumnView(3, 15);

			Label label_titl_lyyd = new Label(4, 0, "林业用地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_lyyd);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(4, 0, 26, 0);

			Label label_titl_05 = new Label(4, 1, "合计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_05);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(4, 1, 4, 3);
			sheet.setColumnView(4, 15);

			Label label_titl_yld = new Label(5, 1, "有林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_yld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(5, 1, 9, 1);
			sheet.setColumnView(5, 15);

			Label label_titl_06 = new Label(5, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_06);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(5, 2, 5, 3);
			sheet.setColumnView(5, 15);

			Label label_titl_07 = new Label(6, 2, "乔木林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_07);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(6, 2, 8, 2);

			Label label_titl_08 = new Label(6, 3, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_08);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(6, 3, 6, 3);
			sheet.setColumnView(6, 15);

			Label label_titl_09 = new Label(7, 3, "纯林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_09);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(7, 3, 7, 3);
			sheet.setColumnView(7, 15);

			Label label_titl_10 = new Label(8, 3, "混交林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_10);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(8, 3, 8, 3);
			sheet.setColumnView(8, 15);

			Label label_titl_11 = new Label(9, 2, "竹林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_11);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(9, 2, 9, 3);
			sheet.setColumnView(9, 15);

			Label label_titl_12 = new Label(10, 1, "疏林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_12);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(10, 1, 10, 3);
			sheet.setColumnView(8, 15);

			Label label_titl_gmld = new Label(11, 1, "灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_gmld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(11, 1, 14, 1);

			Label label_titl_13 = new Label(11, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_13);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(11, 2, 11, 3);
			sheet.setColumnView(11, 15);

			Label label_titl_gjtbgm = new Label(12, 2, "国家特别灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_gjtbgm);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(12, 2, 13, 2);

			Label label_titl_14 = new Label(12, 3, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_14);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(12, 3, 12, 3);
			sheet.setColumnView(12, 15);

			Label label_titl_15 = new Label(13, 3, "灌木经济林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_15);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(13, 3, 13, 3);
			sheet.setColumnView(13, 20);

			Label label_titl_16 = new Label(14, 2, "其它灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_16);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(14, 2, 14, 3);
			sheet.setColumnView(14, 20);

			Label label_titl_wclzld = new Label(15, 1, "未成林造林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_wclzld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(15, 1, 17, 1);

			Label label_titl_17 = new Label(15, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_17);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(15, 2, 15, 3);
			sheet.setColumnView(15, 15);

			Label label_titl_18 = new Label(16, 2, "人造林未成林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_18);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(16, 2, 16, 3);
			sheet.setColumnView(16, 25);

			Label label_titl_19 = new Label(17, 2, "封育未成林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_19);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(17, 2, 17, 3);
			sheet.setColumnView(17, 20);

			Label label_titl_20 = new Label(18, 1, "苗圃地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_20);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(18, 1, 18, 3);
			sheet.setColumnView(18, 15);

			Label label_titl_wlmld = new Label(19, 1, "无立木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_wlmld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(19, 1, 22, 1);

			Label label_titl_21 = new Label(19, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_21);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(19, 2, 19, 3);
			sheet.setColumnView(19, 15);

			Label label_titl_22 = new Label(20, 2, "采伐迹地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_22);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(20, 2, 20, 3);
			sheet.setColumnView(20, 15);

			Label label_titl_23 = new Label(21, 2, "火烧迹地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_23);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(21, 2, 21, 3);
			sheet.setColumnView(21, 15);

			Label label_titl_24 = new Label(22, 2, "其他无立木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_24);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(22, 2, 22, 3);
			sheet.setColumnView(22, 25);

			Label label_titl_Yld = new Label(23, 1, "宜林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_Yld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(23, 1, 27, 1);

			Label label_titl_25 = new Label(23, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_25);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(23, 2, 23, 3);
			sheet.setColumnView(23, 15);

			Label label_titl_26 = new Label(24, 2, "荒山荒地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_26);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(24, 2, 24, 3);
			sheet.setColumnView(24, 15);

			Label label_titl_27 = new Label(25, 2, "石山地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_27);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(25, 2, 25, 3);
			sheet.setColumnView(25, 15);

			Label label_titl_28 = new Label(26, 2, "砂石山地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_28);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(26, 2, 26, 3);
			sheet.setColumnView(26, 15);

			Label label_titl_29 = new Label(27, 2, "其他宜林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_29);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(27, 2, 27, 3);
			sheet.setColumnView(27, 20);

			Label label_titl_30 = new Label(28, 1, "辅助生产林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_30);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(28, 1, 28, 3);
			sheet.setColumnView(28, 20);

			Label label_titl_flyyd = new Label(29, 0, "非林业用地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_flyyd);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(29, 0, 35, 0);

			Label label_titl_31 = new Label(29, 1, "合计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_31);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(29, 1, 29, 3);
			sheet.setColumnView(29, 15);

			Label label_titl_32 = new Label(30, 1, "有林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_32);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(30, 1, 30, 3);
			sheet.setColumnView(30, 15);

			Label label_titl_fylgmld = new Label(31, 1, "灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_fylgmld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(31, 1, 33, 1);

			Label label_titl_33 = new Label(31, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_33);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(31, 2, 31, 3);
			sheet.setColumnView(31, 15);

			Label label_titl_34 = new Label(32, 2, "国家特别规定灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_34);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(32, 2, 32, 3);
			sheet.setColumnView(32, 35);

			Label label_titl_35 = new Label(33, 2, "其他灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_35);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(33, 2, 33, 3);
			sheet.setColumnView(33, 20);

			Label label_titl_fylqz = new Label(34, 1, "其中", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_fylqz);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(34, 1, 35, 1);

			Label label_titl_36 = new Label(34, 2, "≥25°坡耕地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_36);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(34, 2, 34, 3);
			sheet.setColumnView(34, 15);

			Label label_titl_37 = new Label(35, 2, "其它非林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_37);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(35, 2, 35, 3);
			sheet.setColumnView(35, 15);

			// 填充数据
			// 加边框
			WritableFont data_wf = new WritableFont(WritableFont.TIMES, 11,
					WritableFont.NO_BOLD, false);

			WritableCellFormat datawcf = new WritableCellFormat(data_wf);

			datawcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			// 居中显示
			datawcf.setAlignment(jxl.format.Alignment.CENTRE);
			datawcf.setVerticalAlignment(VerticalAlignment.CENTRE);

			int hang = data.size();
			// 从第四行开始绘制数据，前四行为头部信息
			for (int i = 4; i < hang + 4; i++) {
				// 统计单位，从第五航开始，前四行为头部

				Label label1 = new Label(0, i, data.get(i - 4).tjdw, datawcf);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label1);

				Label label2 = new Label(1, i, data.get(i - 4).ldsyq, datawcf);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label2);

				Label label3 = new Label(2, i, data.get(i - 4).sllb, datawcf);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label3);

				jxl.write.Number number4 = new jxl.write.Number(3, i,
						Double.valueOf(data.get(i - 4).zmj), datawcf);
				sheet.addCell(number4);

				jxl.write.Number numberm1 = new jxl.write.Number(4, i,
						Double.valueOf(data.get(i - 4).lyyd_hj), datawcf);
				sheet.addCell(numberm1);

				jxl.write.Number numberm2 = new jxl.write.Number(5, i,
						Double.valueOf(data.get(i - 4).yld_xj), datawcf);
				sheet.addCell(numberm2);

				jxl.write.Number numberm3 = new jxl.write.Number(6, i,
						Double.valueOf(data.get(i - 4).qml_xj), datawcf);
				sheet.addCell(numberm3);

				jxl.write.Number numberm4 = new jxl.write.Number(7, i,
						Double.valueOf(data.get(i - 4).cl), datawcf);
				sheet.addCell(numberm4);

				jxl.write.Number numberm5 = new jxl.write.Number(8, i,
						Double.valueOf(data.get(i - 4).hjl), datawcf);
				sheet.addCell(numberm5);

				jxl.write.Number numberm6 = new jxl.write.Number(9, i,
						Double.valueOf(data.get(i - 4).zl), datawcf);
				sheet.addCell(numberm6);

				jxl.write.Number numberm7 = new jxl.write.Number(10, i,
						Double.valueOf(data.get(i - 4).sld), datawcf);
				sheet.addCell(numberm7);

				jxl.write.Number numberm8 = new jxl.write.Number(11, i,
						Double.valueOf(data.get(i - 4).gmld_xj), datawcf);
				sheet.addCell(numberm8);

				jxl.write.Number numberm9 = new jxl.write.Number(12, i,
						Double.valueOf(data.get(i - 4).gjtbgmld_xj), datawcf);
				sheet.addCell(numberm9);

				jxl.write.Number numberm10 = new jxl.write.Number(13, i,
						Double.valueOf(data.get(i - 4).gmjjl), datawcf);
				sheet.addCell(numberm10);

				jxl.write.Number numberm11 = new jxl.write.Number(14, i,
						Double.valueOf(data.get(i - 4).qtgmld), datawcf);
				sheet.addCell(numberm11);

				jxl.write.Number numberm12 = new jxl.write.Number(15, i,
						Double.valueOf(data.get(i - 4).wclzl_xj), datawcf);
				sheet.addCell(numberm12);

				jxl.write.Number numberm13 = new jxl.write.Number(16, i,
						Double.valueOf(data.get(i - 4).lgzlwcld), datawcf);
				sheet.addCell(numberm13);

				jxl.write.Number numberm14 = new jxl.write.Number(17, i,
						Double.valueOf(data.get(i - 4).fwwcld), datawcf);
				sheet.addCell(numberm14);

				jxl.write.Number numberm15 = new jxl.write.Number(18, i,
						Double.valueOf(data.get(i - 4).mpd), datawcf);
				sheet.addCell(numberm15);

				jxl.write.Number numberm16 = new jxl.write.Number(19, i,
						Double.valueOf(data.get(i - 4).wlmld_xj), datawcf);
				sheet.addCell(numberm16);

				jxl.write.Number numberm17 = new jxl.write.Number(20, i,
						Double.valueOf(data.get(i - 4).cfjd), datawcf);
				sheet.addCell(numberm17);

				jxl.write.Number numberm18 = new jxl.write.Number(21, i,
						Double.valueOf(data.get(i - 4).hsjd), datawcf);
				sheet.addCell(numberm18);

				jxl.write.Number numberm19 = new jxl.write.Number(22, i,
						Double.valueOf(data.get(i - 4).qtwlmld), datawcf);
				sheet.addCell(numberm19);

				jxl.write.Number numberm20 = new jxl.write.Number(23, i,
						Double.valueOf(data.get(i - 4).yldxj), datawcf);
				sheet.addCell(numberm20);

				jxl.write.Number numberm21 = new jxl.write.Number(24, i,
						Double.valueOf(data.get(i - 4).hshd), datawcf);
				sheet.addCell(numberm21);

				jxl.write.Number numberm22 = new jxl.write.Number(25, i,
						Double.valueOf(data.get(i - 4).ssd), datawcf);
				sheet.addCell(numberm22);

				jxl.write.Number number5 = new jxl.write.Number(26, i,
						Double.valueOf(data.get(i - 4).sssd), datawcf);
				sheet.addCell(number5);

				jxl.write.Number number6 = new jxl.write.Number(27, i,
						Double.valueOf(data.get(i - 4).qtlyd), datawcf);
				sheet.addCell(number6);

				jxl.write.Number number7 = new jxl.write.Number(28, i,
						Double.valueOf(data.get(i - 4).fzscld), datawcf);
				sheet.addCell(number7);

				jxl.write.Number number8 = new jxl.write.Number(29, i,
						Double.valueOf(data.get(i - 4).flyyd_hj), datawcf);
				sheet.addCell(number8);

				jxl.write.Number number9 = new jxl.write.Number(30, i,
						Double.valueOf(data.get(i - 4).fly_yld), datawcf);
				sheet.addCell(number9);

				jxl.write.Number number10 = new jxl.write.Number(31, i,
						Double.valueOf(data.get(i - 4).fly_gmld_xj), datawcf);
				sheet.addCell(number10);

				jxl.write.Number number11 = new jxl.write.Number(32, i,
						Double.valueOf(data.get(i - 4).fly_gjtbgdgmld), datawcf);
				sheet.addCell(number11);

				jxl.write.Number number12 = new jxl.write.Number(33, i,
						Double.valueOf(data.get(i - 4).fly_qtgmld), datawcf);
				sheet.addCell(number12);

				jxl.write.Number number13 = new jxl.write.Number(34, i,
						Double.valueOf(data.get(i - 4).fly_pld), datawcf);
				sheet.addCell(number13);

				jxl.write.Number number14 = new jxl.write.Number(35, i,
						Double.valueOf(data.get(i - 4).fly_qtfld), datawcf);
				sheet.addCell(number14);

				// 将第i行的高度设为200
				sheet.setRowView(i, 460);

				// 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123

			}

			// 写入数据并关闭文件
			book.write();
			book.close();
			Toast.makeText(getBaseContext(), "文件已导出至" + path + "文件夹下",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(getBaseContext(), "操作失败", Toast.LENGTH_SHORT).show();
		}
	}
}
