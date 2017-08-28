package com.titan.ynsjy.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.ErDiaoActivity;
import com.titan.ynsjy.adapter.EdYmdcadapter;
import com.titan.ynsjy.adapter.JgdcLineAdapter;
import com.titan.ynsjy.adapter.ListAdapter;
import com.titan.ynsjy.adapter.LxqcYdyzAdapter;
import com.titan.ynsjy.dao.Jgdc;
import com.titan.ynsjy.dao.Ym;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.listviewinedittxt.EdLineAdapter;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.swipemenulistview.SwipeMenu;
import com.titan.ynsjy.swipemenulistview.SwipeMenuCreator;
import com.titan.ynsjy.swipemenulistview.SwipeMenuItem;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.UtilTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**  */
public class HzbjDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private String name;
	private TextView tv;
	private SwipeMenuListView lv;
	private HashMap<String, String> map;
	private String zd;
	private String chushizhi;
	/** ���ڴ洢������ʷ��¼ */
	private SharedPreferences input_histroy;
	private List<String> list;
	/** ��ľ */
	private Ym ym;
	private List<Ym> ymlist;
	private Set<String> items;
	private EditText content;
	private int position;//
	private List<Line> lines;
	private Line line;
	private EdLineAdapter edlineadapter = null;
	private EdYmdcadapter ymdcadapter;
	private JgdcLineAdapter jgAdapter;
	private Jgdc jgdc;
	private LxqcYdyzAdapter ydyzAdapter;

	public interface IComplete {

		void btn_complete(Context mcontext, int position, List<Line> lines,
                          EdLineAdapter edlineadaper);

	}

	public HzbjDialog(Context context, String name, TextView tv,int position, List<Line> lines,
			EdLineAdapter edLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.position = position;
		this.lines = lines;
		this.edlineadapter = edLineAdapter;
	}

	/** ��������������ӵ��� */
	public HzbjDialog(Context context,TextView tv, Line line, LxqcYdyzAdapter ydyzAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = line.getTview();
		this.line = line;
		this.tv = tv;
		this.ydyzAdapter = ydyzAdapter;
	}

	/** ����С�����������ص��� */
	public HzbjDialog(Context context, String name, TextView tv,Line line, EdLineAdapter edLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.line = line;
		this.edlineadapter = edLineAdapter;
	}

	public HzbjDialog(Context context, String name, TextView tv) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
	}

	public HzbjDialog(Context context, String name, TextView tv,HashMap<String, String> map, String zd) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.map = map;
		this.zd = zd;
	}

	/** ��ľ���� */
	public HzbjDialog(Context mContext, String title, TextView tv,Ym ym, EdYmdcadapter edYmdcadapter) {
		super(mContext, R.style.Dialog);
		this.context = mContext;
		this.name = title;
		this.tv = tv;
		this.ym = ym;
		this.ymdcadapter = edYmdcadapter;
	}

	/** �ǹ���� */
	public HzbjDialog(Context mContext, TextView tv, Line line, Jgdc jgdc,
			JgdcLineAdapter jgdcLineAdapter) {
		super(mContext, R.style.Dialog);
		this.context = mContext;
		this.name = line.getTview();
		this.tv = tv;
		this.line = line;
		this.input_histroy = jgdcLineAdapter.input_history;
		this.jgdc = jgdc;
		this.jgAdapter = jgdcLineAdapter;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hzbj);
		setCanceledOnTouchOutside(false);
		// ��ʼֵ
		if (tv != null) {
			chushizhi = tv.getText().toString();
		}
		TextView headtext = (TextView) findViewById(R.id.headtext);
		Button back = (Button) findViewById(R.id.back);
		Button wancheng = (Button) findViewById(R.id.wancheng);
		content = (EditText) findViewById(R.id.content);
		final TextView qingchu = (TextView) findViewById(R.id.qingchu);
		final TextView riqi = (TextView) findViewById(R.id.riqi);
		final TextView shijian = (TextView) findViewById(R.id.shijian);
		final TextView haiba = (TextView) findViewById(R.id.haiba);
		haiba.setOnClickListener(this);
		final TextView jingdu = (TextView) findViewById(R.id.jingdu);
		jingdu.setOnClickListener(this);
		final TextView weidu = (TextView) findViewById(R.id.weidu);
		weidu.setOnClickListener(this);
		lv = (SwipeMenuListView) findViewById(R.id.listView1);

		input_histroy = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		items = input_histroy.getStringSet(name, new HashSet<String>());
		list = new ArrayList<String>();
		if (items.size() > 0) {

			String[] data = (String[]) items.toArray(new String[items
					.size()]);
			for (int i = 0; i < data.length; i++) {
				list.add(data[i].trim().toString());
			}

		}
		final ListAdapter adapter = new ListAdapter(context, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				content.setText(list.get(arg2));
			}
		});

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(context);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		lv.setMenuCreator(creator);

		// step 2. listener item click event
		lv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:
					String str = list.get(position);
					list.remove(str);
					adapter.notifyDataSetChanged();
					items.remove(str);
					updateSet(input_histroy, items, line);
					break;
				}
			}
		});

		headtext.setText(name);
		content.setText(chushizhi);
		content.setSelection(chushizhi.length());
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		qingchu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content.setText("");
			}
		});
		riqi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content.setText(UtilTime.getSystemtime3());
			}
		});
		shijian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content.setText(UtilTime.getSystemtime2());
			}
		});
		wancheng.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!content.getText().toString().trim().equals("")){
					items.add(content.getText().toString());
					Editor editor = input_histroy.edit();
					editor.clear();
					editor.putStringSet(name, items).commit();
				}
				
				tv.setText(content.getText().toString());
				if (map != null) {
					map.put(zd, content.getText().toString());
				}
				// ����С����
				InputMethodManager inputMethodManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						content.getWindowToken(), 0);
				if (edlineadapter != null) {
					btn_complete(context, line);
				} else if (ymdcadapter != null) {
					// ��ľ����
					ed_ymdc_complete(context, ymdcadapter);
				} else if (jgAdapter != null) {
					ed_jgdc_complete(context, jgAdapter);
				}
				dismiss();
			}
		});
	}

	/** �����ǹ�������ݱ��� */
	protected void ed_jgdc_complete(Context context2, JgdcLineAdapter jgAdapter) {
		Line line = lines.get(position);

		String value = content.getText().toString();
		jgdc.setBEIZHU(value);
		line.setText(value);

		boolean isupdate = DataBaseHelper.updateJgdc(context, jgdc,ErDiaoActivity.datapath);
		if (isupdate) {
			ToastUtil.setToast((Activity) context, "���³ɹ�");
			return;
		} else {
			ToastUtil.setToast((Activity) context, "����ʧ��");
		}

		// lines.set(line.getNum(), line);
		jgAdapter.notifyDataSetChanged();
	}

	/** ������ľ�������ݱ��� */
	public void ed_ymdc_complete(Context context, EdYmdcadapter edlineadapter) {
		String curvalue = content.getText().toString();
		// �ж��޸�ֵ��֮ǰֵ�Ƿ����
		if (chushizhi.equals(curvalue)) {
			return;

		}
		switch (tv.getId()) {
		// �ؾ�
		case R.id.xj:
			ym.setXIONGJING(Double.valueOf(curvalue));
			break;

		// ��ע
		case R.id.bz:
			ym.setBEIZHU(curvalue);
			break;

		}
		boolean idupdate = DataBaseHelper.updateYm(context, ym,ErDiaoActivity.datapath);
		if (idupdate) {
			ToastUtil.setToast(context, "��ľ���³ɹ�");
			ymdcadapter.notifyDataSetChanged();
		} else {
			ToastUtil.setToast(context, "��ľ����ʧ��");
		}
	}

	/** ������������ */
	public void btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String curvalue = content.getText().toString();
		// �ж��޸�ֵ��֮ǰֵ�Ƿ����
		if (bfvalue.equals(curvalue)) {
			return;

		} else {
			BaseActivity.selectFeatureAts = edlineadapter.curfeture
					.getAttributes();
			BaseActivity.selectFeatureAts.put(name, curvalue);
			long featureid = edlineadapter.curfeture.getId();
			Graphic updateGraphic = new Graphic(
					edlineadapter.curfeture.getGeometry(),
					edlineadapter.curfeture.getSymbol(),
					BaseActivity.selectFeatureAts);
			try {
				edlineadapter.curfeaturelayer.getFeatureTable().updateFeature(
						featureid, updateGraphic);

				curline.setText(curvalue);
				// curline.setValue(curvalue);
			} catch (Exception e) {
				bfvalue = bfvalue == "null" ? "" : bfvalue;
				curline.setText(bfvalue);
				// curline.setValue(bfvalue);
				BaseActivity.selectFeatureAts.put(name, bfvalue);
				ToastUtil.setToast((Activity) mContext, "����ʧ��");
				return;

			}
			ToastUtil.setToast((Activity) mContext, "���³ɹ�");
		}
		// lines.set(line.getNum(), line);
		edlineadapter.notifyDataSetChanged();

	}

	/** �������� */
	public void btn_complete(Context mContext, int position, List<Line> lines,
			EdLineAdapter edlineadaper) {
		GeodatabaseFeature editgeodatafeature = null;
		FeatureLayer editfeature = null;
		if ("" != null) {
			// editgeodatafeature = edlineadapter.selGeoFeature;
			editfeature = ErDiaoActivity.yddlayer;
		} else {
			editgeodatafeature = BaseActivity.selGeoFeature;
			editfeature = BaseActivity.myLayer.getLayer();
		}
		String name = lines.get(position).getKey();
		String value = content.getText().toString();
		Line line = lines.get(position);
		line.setText(value);
		editgeodatafeature.getAttributes().put(name, value);
		Map<String, Object> att = editgeodatafeature.getAttributes();
		for (Field f : editgeodatafeature.getTable().getFields()) {
			if (f.getName().equals(name)) {
				att.put(f.getName(), value);
			}
		}
		Graphic updateGraphic = new Graphic(editgeodatafeature.getGeometry(),
				editgeodatafeature.getSymbol(), att);
		FeatureTable featureTable = editfeature.getFeatureTable();
		try {
			long featureid = editgeodatafeature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e) {
			ToastUtil.setToast((Activity) mContext, "����ʧ��");
			e.printStackTrace();
			return;
		}
		ToastUtil.setToast((Activity) mContext, "���³ɹ�");

		// lines.set(line.getNum(), line);
		edlineadaper.notifyDataSetChanged();

	}

	/** ɭ����Դ������鱣������ */
	public void lxqc_btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String curvalue = content.getText().toString();
		// �ж��޸�ֵ��֮ǰֵ�Ƿ����
		if (bfvalue.equals(curvalue)) {
			return;

		} else {
			BaseActivity.selectFeatureAts.put(name, curvalue);
			long featureid = BaseActivity.selGeoFeature.getId();
			Graphic updateGraphic = new Graphic(
					BaseActivity.selGeoFeature.getGeometry(),
					BaseActivity.selGeoFeature.getSymbol(),
					BaseActivity.selectFeatureAts);
			try {
				BaseActivity.myLayer.getTable().updateFeature(featureid, updateGraphic);
				curline.setText(curvalue);
			} catch (Exception e) {
				bfvalue = bfvalue == "null" ? "" : bfvalue;
				curline.setText(bfvalue);
				BaseActivity.selectFeatureAts.put(name, bfvalue);
				ToastUtil.setToast((Activity) mContext, "����ʧ��");
				return;

			}
			ToastUtil.setToast((Activity) mContext, "���³ɹ�");
		}

	}

	@Override
	public void onClick(View v) {
		DecimalFormat df = new DecimalFormat("#0.00000");
		switch (v.getId()) {
		// ����
		case R.id.haiba:
			String altitude = df.format(BaseActivity.currentPoint.getZ());
			content.setText(altitude);
			break;
		// ����
		case R.id.jingdu:

			String longitude = df.format(BaseActivity.currentPoint.getX());
			content.setText(longitude);
			break;
		// γ��
		case R.id.weidu:
			String latitude = df.format(BaseActivity.currentPoint.getY());
			content.setText(latitude);
			break;
		default:
			break;
		}
	}

	/** ��edittext�Ĺ���������� */
	public void setEditTextCursorLocation(EditText et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** ������ʷ¼������ */
	public void updateSet(SharedPreferences preferences, Set<String> items,
			Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).commit();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				content.getResources().getDisplayMetrics());
	}

}
