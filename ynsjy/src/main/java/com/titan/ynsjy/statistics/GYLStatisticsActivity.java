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
 * С��ͳ�ƽ���
 * 
 * @author Administrator
 * 
 */
public class GYLStatisticsActivity extends Activity {
	ListView mListView1;
	MyAdapter myAdapter;
	RelativeLayout mHead;
	LinearLayout main;
	Button btn_excel;
	ArrayList<GYLAreaSum> data;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gyl_tongji);

		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		// mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mListView1 = (ListView) findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		myAdapter = new MyAdapter(this, R.layout.gyl_item);
		data = (ArrayList<GYLAreaSum>) getIntent().getExtras().getSerializable(
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
		ArrayList<GYLAreaSum> data;

		public MyAdapter(Context context, int id_row_layout) {
			super();
			this.id_row_layout = id_row_layout;
			mInflater = LayoutInflater.from(context);

		}

		public void setData(ArrayList<GYLAreaSum> data) {
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
				convertView = mInflater.inflate(R.layout.gyl_item, null);
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
				holder.txt12 = (TextView) convertView
						.findViewById(R.id.textView12);
				holder.txt13 = (TextView) convertView
						.findViewById(R.id.textView13);
				holder.txt14 = (TextView) convertView
						.findViewById(R.id.textView14);
				holder.txt15 = (TextView) convertView
						.findViewById(R.id.textView15);

				MyHScrollView headSrcrollView = (MyHScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);
				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));

				convertView.setTag(holder);
				// }
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txt1.setText(data.get(position).tjdw);
			holder.txt2.setText(data.get(position).qs);
			holder.txt3.setText(String.format("%.2f",
					Double.valueOf(data.get(position).lyyd_hj)));
			holder.txt4.setText(String.format("%.2f",
					Double.valueOf(data.get(position).yld_xj)));
			holder.txt5.setText(String.format("%.2f",
					Double.valueOf(data.get(position).qml)));
			holder.txt6.setText(String.format("%.2f",
					Double.valueOf(data.get(position).zl)));
			holder.txt7.setText(String.format("%.2f",
					Double.valueOf(data.get(position).sld)));
			holder.txt8.setText(String.format("%.2f",
					Double.valueOf(data.get(position).gmld_xj)));
			holder.txt9.setText(String.format("%.2f",
					Double.valueOf(data.get(position).gjtbgmld_xj)));
			holder.txt10.setText(String.format("%.2f",
					Double.valueOf(data.get(position).qtgmld)));
			holder.txt11.setText(String.format("%.2f",
					Double.valueOf(data.get(position).wclzl_xj)));
			holder.txt12.setText(String.format("%.2f",
					Double.valueOf(data.get(position).mpd)));
			holder.txt13.setText(String.format("%.2f",
					Double.valueOf(data.get(position).wlmld_xj)));
			holder.txt14.setText(String.format("%.2f",
					Double.valueOf(data.get(position).yild)));
			holder.txt15.setText(String.format("%.2f",
					Double.valueOf(data.get(position).fzscld)));
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
			TextView txt5;
			TextView txt6;
			TextView txt7;
			TextView txt8;
			TextView txt9;
			TextView txt10;
			TextView txt11;
			TextView txt12;
			TextView txt13;
			TextView txt14;
			TextView txt15;

			HorizontalScrollView scrollView;
		}
	}// end class my

	/**
	 * ����excel���
	 * 
	 * @param data
	 */
	@SuppressLint("SimpleDateFormat")
	public void excel(ArrayList<GYLAreaSum> data) {
		WritableWorkbook book = null;
		try {
			String path = ResourcesManager.getInstance(this).getExcelPath()
					+ "/";
			// 创建文件夹
			File files = new File(path);
			if (!files.exists())
				files.mkdirs();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			FileOutputStream fos = new FileOutputStream(path
					+ df.format(new Date()) + "_公益林.xls");
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

			Label label_titl_02 = new Label(1, 0, "权属", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_02);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(1, 0, 1, 3);
			sheet.setColumnView(1, 15);

			Label label_titl_lyyd = new Label(2, 0, "林业用地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_lyyd);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(2, 0, 14, 0);
			sheet.setColumnView(2, 40);

			Label label_titl_05 = new Label(2, 1, "合计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_05);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(2, 1, 2, 3);
			sheet.setColumnView(2, 15);

			Label label_titl_yld = new Label(3, 1, "有林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_yld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(3, 1, 5, 1);
			sheet.setColumnView(3, 15);

			Label label_titl_06 = new Label(3, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_06);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(3, 2, 3, 3);
			sheet.setColumnView(3, 15);

			Label label_titl_07 = new Label(4, 2, "乔木林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_07);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(4, 2, 4, 3);
			sheet.setColumnView(4, 15);

			Label label_titl_08 = new Label(5, 2, "竹林", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_08);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(5, 2, 5, 3);
			sheet.setColumnView(5, 15);

			Label label_titl_09 = new Label(6, 1, "疏林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_09);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(6, 1, 6, 3);
			sheet.setColumnView(6, 15);

			Label label_titl_gmld = new Label(7, 1, "灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_gmld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(7, 1, 9, 1);
			// sheet.setColumnView(10, 35);

			Label label_titl_10 = new Label(7, 2, "小计", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_10);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(7, 2, 7, 3);
			sheet.setColumnView(7, 15);

			Label label_titl_gjtbgm = new Label(8, 2, "国家特别灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_gjtbgm);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(8, 2, 8, 3);
			sheet.setColumnView(8, 35);

			Label label_titl_13 = new Label(9, 2, "其它灌木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_13);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(9, 2, 9, 3);
			sheet.setColumnView(9, 20);

			Label label_titl_wclzld = new Label(10, 1, "未成林造林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_wclzld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(10, 1, 10, 3);
			sheet.setColumnView(10, 20);

			Label label_titl_17 = new Label(11, 1, "苗圃地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_17);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(11, 1, 11, 3);
			sheet.setColumnView(11, 15);

			Label label_titl_wlmld = new Label(12, 1, "无立木林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_wlmld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(12, 1, 12, 3);
			sheet.setColumnView(12, 15);

			Label label_titl_Yld = new Label(13, 1, "宜林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_Yld);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(13, 1, 13, 3);
			sheet.setColumnView(13, 15);

			Label label_titl_26 = new Label(14, 1, "辅助生产林地", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_26);
			// 合并单元格第一个参数：要合并的单元格最左上角的列号，
			// 第二个参数：要合并的单元格最左上角的行号，
			// 第三个参数：要合并的单元格最右角的列号，
			// 第四个参数：要合并的单元格最右下角的行号，
			sheet.mergeCells(14, 1, 14, 3);
			sheet.setColumnView(14, 20);

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

				Label label2 = new Label(1, i, data.get(i - 4).qs, datawcf);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label2);

				jxl.write.Number numberm1 = new jxl.write.Number(2, i,
						Double.valueOf(data.get(i - 4).lyyd_hj), datawcf);
				sheet.addCell(numberm1);

				jxl.write.Number numberm2 = new jxl.write.Number(3, i,
						Double.valueOf(data.get(i - 4).yld_xj), datawcf);
				sheet.addCell(numberm2);

				jxl.write.Number numberm3 = new jxl.write.Number(4, i,
						Double.valueOf(data.get(i - 4).qml), datawcf);
				sheet.addCell(numberm3);

				jxl.write.Number numberm4 = new jxl.write.Number(5, i,
						Double.valueOf(data.get(i - 4).zl), datawcf);
				sheet.addCell(numberm4);

				jxl.write.Number numberm5 = new jxl.write.Number(6, i,
						Double.valueOf(data.get(i - 4).sld), datawcf);
				sheet.addCell(numberm5);

				jxl.write.Number numberm6 = new jxl.write.Number(7, i,
						Double.valueOf(data.get(i - 4).gmld_xj), datawcf);
				sheet.addCell(numberm6);

				jxl.write.Number numberm7 = new jxl.write.Number(8, i,
						Double.valueOf(data.get(i - 4).gjtbgmld_xj), datawcf);
				sheet.addCell(numberm7);

				jxl.write.Number numberm8 = new jxl.write.Number(9, i,
						Double.valueOf(data.get(i - 4).qtgmld), datawcf);
				sheet.addCell(numberm8);

				jxl.write.Number numberm9 = new jxl.write.Number(10, i,
						Double.valueOf(data.get(i - 4).wclzl_xj), datawcf);
				sheet.addCell(numberm9);

				jxl.write.Number numberm10 = new jxl.write.Number(11, i,
						Double.valueOf(data.get(i - 4).mpd), datawcf);
				sheet.addCell(numberm10);

				jxl.write.Number numberm11 = new jxl.write.Number(12, i,
						Double.valueOf(data.get(i - 4).wlmld_xj), datawcf);
				sheet.addCell(numberm11);

				jxl.write.Number numberm12 = new jxl.write.Number(13, i,
						Double.valueOf(data.get(i - 4).yild), datawcf);
				sheet.addCell(numberm12);

				jxl.write.Number numberm13 = new jxl.write.Number(14, i,
						Double.valueOf(data.get(i - 4).fzscld), datawcf);
				sheet.addCell(numberm13);

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
