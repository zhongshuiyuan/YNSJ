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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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


public class LdqdStatisticActivity extends Activity {

	ListView mListView1;
	MyAdapter myAdapter;
	RelativeLayout mHead;
	LinearLayout main;
	Button btn_excel;
	ArrayList<LdqdAreaSum> data;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ld_wfsyldajqd);

		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		// mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mListView1 = (ListView) findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		myAdapter = new MyAdapter(this, R.layout.ld_wfsyldajqd);
		data = (ArrayList<LdqdAreaSum>) getIntent().getExtras().getSerializable("data");

		mListView1.setAdapter(myAdapter);
		myAdapter.setData((ArrayList<LdqdAreaSum>) getIntent().getExtras().getSerializable("data"));
		btn_excel = (Button) findViewById(R.id.btn_excel);
		btn_excel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
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
		ArrayList<LdqdAreaSum> data;

		public MyAdapter(Context context, int id_row_layout) {
			super();
			this.id_row_layout = id_row_layout;
			mInflater = LayoutInflater.from(context);

		}

		public void setData(ArrayList<LdqdAreaSum> data) {
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
				convertView = mInflater.inflate(id_row_layout, null);
				holder = new ViewHolder();

				MyHScrollView scrollView1 = (MyHScrollView) convertView
						.findViewById(R.id.horizontalScrollView1);

				holder.scrollView = scrollView1;

				holder.txtm1 = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.txtm2 = (TextView) convertView
						.findViewById(R.id.textView2);
				holder.txtm3 = (TextView) convertView
						.findViewById(R.id.textView3);
				holder.txtm4 = (TextView) convertView
						.findViewById(R.id.textView4);

				holder.txtm5 = (TextView) convertView
						.findViewById(R.id.textView5);
				holder.txtm6 = (TextView) convertView
						.findViewById(R.id.textView6);
				holder.txtm7 = (TextView) convertView
						.findViewById(R.id.textView7);
				holder.txtm8 = (TextView) convertView
						.findViewById(R.id.textView8);
				holder.txtm9 = (TextView) convertView
						.findViewById(R.id.textView9);
				holder.txtm10 = (TextView) convertView
						.findViewById(R.id.textView10);
				holder.txtm11 = (TextView) convertView
						.findViewById(R.id.textView11);

				holder.txtm12 = (TextView) convertView
						.findViewById(R.id.textView12);
				holder.txtm13 = (TextView) convertView
						.findViewById(R.id.textView13);

				holder.txtm14 = (TextView) convertView
						.findViewById(R.id.textView14);

				holder.txtm15 = (TextView) convertView
						.findViewById(R.id.textView15);
				holder.txtm16 = (TextView) convertView
						.findViewById(R.id.textView16);
				holder.txtm17 = (TextView) convertView
						.findViewById(R.id.textView17);
				holder.txtm18 = (TextView) convertView
						.findViewById(R.id.textView18);
				holder.txtm19 = (TextView) convertView
						.findViewById(R.id.textView19);
				holder.txtm20 = (TextView) convertView
						.findViewById(R.id.textView20);
				holder.txtm21 = (TextView) convertView
						.findViewById(R.id.textView21);
				holder.txtm22 = (TextView) convertView
						.findViewById(R.id.textView22);

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
			LdqdAreaSum ldqdas=data.get(position);
			/**案件编号*/
			holder.txtm1.setText(ldqdas.anjbh);
			/**案件名称*/
			holder.txtm2.setText(ldqdas.anjmc);
			/**项目类别*/
			holder.txtm3.setText(ldqdas.xmlb);
			/**违法使用林地责任单位（责任人）名称（姓名） */
			holder.txtm4.setText(ldqdas.zrrmc);
			/**责任单位法定代表人姓名*/
			holder.txtm5.setText(ldqdas.dbrmc);
			/**涉及本次核实的疑似图斑编号 */
			holder.txtm6.setText(ldqdas.ystbbh);
			/**涉及林地落界小班号 */
			holder.txtm7.setText(ldqdas.ljxbh);
			/**开工日期 */
			holder.txtm8.setText(ldqdas.kgrq);
			/**违法使用林地面积（公顷）*/
			holder.txtm9.setText(ldqdas.wfsyldmj+"");
			/**违法使用林地地类*/
			holder.txtm10.setText(ldqdas.lddl);
			/**违法使用林地的森林类别 */
			holder.txtm11.setText(ldqdas.sllb);
			/**违法使用林地类型 */
			holder.txtm12.setText(ldqdas.ldlx);
			/**违法使用林地的行为类型 */
			holder.txtm13.setText(ldqdas.ldxwlx);
			/**违法使用林地行为的查处情况*/
			holder.txtm14.setText(ldqdas.ccqk);
			/**违法使用林地行为的依法处理情况及查处依据*/
			holder.txtm15.setText(ldqdas.clqk);
			/**案件性质*/
			holder.txtm16.setText(ldqdas.anjxz);
			/**有无无证采伐 */
			holder.txtm17.setText(ldqdas.ywwzcf);
			/**无证采伐面积(公顷） */
			holder.txtm18.setText(ldqdas.wzcfmj+"");
			/**无证采伐蓄积（m3） */
			holder.txtm19.setText(ldqdas.wzcfxj+"");
			/**无证采伐林木的查处情况*/
			holder.txtm20.setText(ldqdas.wzccqk);
			/**督办级别 */
			holder.txtm21.setText(ldqdas.dbjb);
			/**备注 */
			holder.txtm22.setText(ldqdas.bz);

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
			HorizontalScrollView scrollView;
		}
	}

	/**
	 * 导出excel
	 * @param data
	 */

	@SuppressLint("SimpleDateFormat")
	public void excel(ArrayList<LdqdAreaSum> data) {
		WritableWorkbook book = null;
		try {
			// 路径
			// String path =
			// Environment.getExternalStorageDirectory().getAbsolutePath() +
			// "/excel/";
			// String path =
			// ResourcesManager.getInstance(this).getExcelPath()+"/";
			String path = ResourcesManager.getInstance(this).getExcelPath() + "/";
			// 创建文件夹
			File files = new File(path);
			if (!files.exists())
				files.mkdirs();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			FileOutputStream fos = new FileOutputStream(path
					+ df.format(new Date()) + "附表：六个严禁专项行动方案系列表格-修改稿.xls");

			// 创建excel表格
			book = Workbook.createWorkbook(fos);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("违法使用林地案件清单", 0);
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

			Label label_titl_01 = new Label(0, 0, "案件编号", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_01);
			// 将第一列的宽度设为100
			sheet.setColumnView(0, 15);

			Label label_titl_02 = new Label(1, 0, "案件名称", titlewcf);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label_titl_02);
			sheet.setColumnView(1, 15);

			Label label_titl_03 = new Label(2, 0, "项目类别", titlewcf);
			sheet.addCell(label_titl_03);
			sheet.setColumnView(2, 15);

			Label label_titl_04 = new Label(3, 0, "违法使用林地责任单位（责任人）名称（姓名）", titlewcf);
			sheet.addCell(label_titl_04);
			sheet.setColumnView(3, 15);

			Label label_titl_05 = new Label(4, 0, "责任单位法定代表人姓名", titlewcf);
			sheet.addCell(label_titl_05);
			sheet.setColumnView(4, 15);

			Label label_titl_06 = new Label(5, 0, "涉及本次核实的疑似图斑编号", titlewcf);
			sheet.addCell(label_titl_06);
			sheet.setColumnView(5, 15);

			Label label_titl_07 = new Label(6, 0, "涉及林地落界小班号", titlewcf);
			sheet.addCell(label_titl_07);
			sheet.setColumnView(6, 15);

			Label label_titl_08 = new Label(7, 0, "开工日期", titlewcf);
			sheet.addCell(label_titl_08);
			sheet.setColumnView(7, 15);

			Label label_titl_09 = new Label(8, 0, "违法使用林地面积（公顷）", titlewcf);
			sheet.addCell(label_titl_09);
			sheet.setColumnView(8, 15);

			Label label_titl_10 = new Label(9, 0, "违法使用林地地类", titlewcf);
			sheet.addCell(label_titl_10);
			sheet.setColumnView(9, 15);

			Label label_titl_11 = new Label(10, 0, "违法使用林地的森林类别", titlewcf);
			sheet.addCell(label_titl_11);
			sheet.setColumnView(10, 15);

			Label label_titl_12 = new Label(11, 0, "违法使用林地类型", titlewcf);
			sheet.addCell(label_titl_12);
			sheet.setColumnView(11, 15);

			Label label_titl_13 = new Label(12, 0, "违法使用林地的行为类型", titlewcf);
			sheet.addCell(label_titl_13);
			sheet.setColumnView(12, 15);

			Label label_titl_14 = new Label(13, 0, "违法使用林地行为的查处情况", titlewcf);
			sheet.addCell(label_titl_14);
			sheet.setColumnView(13, 15);

			Label label_titl_15 = new Label(14, 0, "违法使用林地行为的依法处理情况及查处依据", titlewcf);
			sheet.addCell(label_titl_15);
			sheet.setColumnView(14, 15);

			Label label_titl_16 = new Label(15, 0, "案件性质", titlewcf);
			sheet.addCell(label_titl_16);
			sheet.setColumnView(15, 15);

			Label label_titl_17 = new Label(16, 0, "有无无证采伐", titlewcf);
			sheet.addCell(label_titl_17);
			sheet.setColumnView(16, 15);

			Label label_titl_18 = new Label(17, 0, "无证采伐面积(公顷）", titlewcf);
			sheet.addCell(label_titl_18);
			sheet.setColumnView(17, 15);

			Label label_titl_19 = new Label(18, 0, "无证采伐蓄积（m3）", titlewcf);
			sheet.addCell(label_titl_19);
			sheet.setColumnView(18, 15);

			Label label_titl_20 = new Label(19, 0, "无证采伐林木的查处情况", titlewcf);
			sheet.addCell(label_titl_20);
			sheet.setColumnView(19, 15);

			Label label_titl_21 = new Label(20, 0, "督办级别", titlewcf);
			sheet.addCell(label_titl_21);
			sheet.setColumnView(20, 15);

			Label label_titl_22 = new Label(21, 0, "备注", titlewcf);
			sheet.addCell(label_titl_22);
			sheet.setColumnView(21, 15);


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
			for (int i = 1; i < hang + 1; i++) {
				// 统计单位，从第五航开始，前四行为头部

				Label label1 = new Label(0, i, data.get(i - 1).anjbh, datawcf);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label1);

				Label label2 = new Label(1, i, data.get(i - 1).anjmc, datawcf);
				sheet.addCell(label2);

				Label label3 = new Label(2, i, data.get(i - 1).xmlb, datawcf);
				sheet.addCell(label3);
				
				Label label4 = new Label(3, i, data.get(i - 1).zrrmc, datawcf);
				sheet.addCell(label4);
				
				Label label5 = new Label(4, i, data.get(i - 1).dbrmc, datawcf);
				sheet.addCell(label5);
				
				Label label6 = new Label(5, i, data.get(i - 1).ystbbh, datawcf);
				sheet.addCell(label6);
				
				Label label7 = new Label(6, i, data.get(i - 1).ljxbh, datawcf);
				sheet.addCell(label7);
				
				Label label8 = new Label(7, i, data.get(i - 1).kgrq, datawcf);
				sheet.addCell(label8);
				
				Label label9 = new Label(8, i, data.get(i - 1).wfsyldmj+"", datawcf);
				sheet.addCell(label9);
				
				Label label10 = new Label(9, i, data.get(i - 1).lddl, datawcf);
				sheet.addCell(label10);
				
				Label label11 = new Label(10, i, data.get(i - 1).sllb, datawcf);
				sheet.addCell(label11);

				Label label12 = new Label(11, i, data.get(i - 1).ldlx, datawcf);
				sheet.addCell(label12);
				
				Label label13 = new Label(12, i, data.get(i - 1).ldxwlx, datawcf);
				sheet.addCell(label13);
				
				Label label14 = new Label(13, i, data.get(i - 1).ccqk, datawcf);
				sheet.addCell(label14);
				
				Label label15 = new Label(14, i, data.get(i - 1).clqk, datawcf);
				sheet.addCell(label15);
				
				Label label16 = new Label(15, i, data.get(i - 1).anjxz, datawcf);
				sheet.addCell(label16);
				
				Label label17 = new Label(16, i, data.get(i - 1).ywwzcf, datawcf);
				sheet.addCell(label17);

				Label label18 = new Label(17, i, data.get(i - 1).wzcfmj+"", datawcf);
				sheet.addCell(label18);
				
				Label label19 = new Label(18, i, data.get(i - 1).wzcfxj+"", datawcf);
				sheet.addCell(label19);
				
				Label label20 = new Label(19, i, data.get(i - 1).wzccqk, datawcf);
				sheet.addCell(label20);
				
				Label label21 = new Label(20, i, data.get(i - 1).dbjb, datawcf);
				sheet.addCell(label21);
				
				Label label22 = new Label(21, i, data.get(i - 1).bz, datawcf);
				sheet.addCell(label22);
				
				// 将第i行的高度设为200
				sheet.setRowView(i, 460);
				// 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
			}
			// 写入数据并关闭文件
			book.write();
			book.close();
			Toast.makeText(getBaseContext(), "文件已导出至" + path + "文件夹下",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "操作失败", Toast.LENGTH_SHORT).show();
		}
	}

}
