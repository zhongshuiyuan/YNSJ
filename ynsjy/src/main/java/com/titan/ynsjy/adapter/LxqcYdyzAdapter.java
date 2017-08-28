package com.titan.ynsjy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.SlzylxqcYdyzActivity;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.swipemenulistview.SwipeMenu;
import com.titan.ynsjy.swipemenulistview.SwipeMenuCreator;
import com.titan.ynsjy.swipemenulistview.SwipeMenuItem;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.titan.ynsjy.timepaker.TimePopupWindow;
import com.titan.ynsjy.timepaker.TimePopupWindow.OnTimeSelectListener;
import com.titan.ynsjy.timepaker.TimePopupWindow.Type;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LxqcYdyzAdapter extends BaseAdapter {

	private List<Line> lines;
	private SlzylxqcYdyzActivity mContext;
	@SuppressLint("UseSparseArrays")
	public HashMap<String, String> hashMap = new HashMap<String, String>();
	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private DecimalFormat format = new DecimalFormat(".000000");
	String xian, xiang, cun;

	DecimalFormat df = new DecimalFormat(".00");

	private String pname;
	private FeatureLayer featureLayer;
	private GeodatabaseFeature selGeoFeature;
	private Map<String, Object> attribute;

	public LxqcYdyzAdapter(List<Line> lines) {
		this.lines = lines;
	}

	public LxqcYdyzAdapter(SlzylxqcYdyzActivity context, List<Line> lines,String name,
						   FeatureLayer featureLayer, GeodatabaseFeature gfFeature) {
		this.mContext = context;
		this.lines = lines;
		this.pname = name;
		this.featureLayer = featureLayer;
		this.selGeoFeature = gfFeature;
		this.attribute = gfFeature.getAttributes();
	}

	@Override
	public int getCount() {
		return lines.size();
	}

	@Override
	public Line getItem(int position) {
		return lines.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
						final ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_listviewinedittxt_line, parent, false);
			holder.etLine = (TextView) convertView.findViewById(R.id.etLine);
			holder.tvLine = (TextView) convertView.findViewById(R.id.tvline);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Line line = lines.get(position);
		holder.tvLine.setText(line.getTview());
		holder.etLine.setText(line.getText());
		holder.etLine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String tview = line.getTview();
				String key = line.getKey();
				CodedValueDomain domain = line.getDomain();
				hide(holder.etLine);
				if (tview.contains("照片") || tview.contains("相片")) {
					showTakePhoto(line);
				} else if (tview.contains("时间") || tview.equals("dcsj")
						|| tview.contains("日期")) {
					initSelectTimePopuwindow(line, holder.etLine);
				} else if (tview.contains("坐标") || tview.contains("经度")
						|| tview.contains("纬度")) {
					showLonLatDialog(line, holder.etLine);
				} else {
					if (domain != null) {
						ToastUtil.setToast(mContext, "domain");
						Map<String, String> values = domain.getCodedValues();
						if (values != null && values.size() > 0) {
							showTowDialog(line, values, holder.etLine, tview,
									position);
							return;
						}
					}
					List<Row> lst = isDMField(key);
					if (lst == null) {
						showKeyDialog(line, holder.etLine);
					} else {
						int size = lst.size();
						if (size > 0) {
							showTowDialog(line, lst, holder.etLine, tview,
									position);
						} else {
							showKeyDialog(line, holder.etLine);
						}
					}

				}
			}
		});

//		holder.etLine.setOnClickListener(new View.OnClickListener() {
//			TextView tv = holder.etLine;
//			@Override
//			public void onClick(View arg0) {
//				// 获取字段别名
//				String title = line.getTview().toString();
//				String name = line.getKey();
//				//判断强制输入类型
//				String qzlx="";
//				// 1:选择类型字段 2:数字类型 3：文字类型 4：日期选择 5:不可修改
////				String type = LxqcUtil.getAttributetype(mContext,"slzylxqc.xml", name);
//				String type = LxqcUtil.getAttributetype(mContext, name,dbpath);
//				if(type!=null&&"5".equals(type)){
////					qzlx=LxqcUtil.getAttributeQztype(mContext, "slzylxqc.xml", name);
//					qzlx=LxqcUtil.getAttributeQztype(mContext,  name,dbpath);
//				}
//				if (type.equals("1")) {
////					List<Row> list = LxqcUtil.getAttributeList(mContext,"slzylxqc.xml",name);
//					List<Row> list = LxqcUtil.getAttributeList(mContext,name,dbpath);
//					if (name.equals("XIANG")) {
//						// 获取县名
//						for (Line line : lines) {
//							if (line.getKey().equals("XIAN")) {
//								xian = line.getText();
//							}
//						}
//						List<Row> xianglist = DataBaseHelper.getXiangList(mContext, xian);
//						list = xianglist;
//					}
//					if (name.equals("CUN")) {
//						// 获取县名
//						for (Line line : lines) {
//							if (line.getKey().equals("XIANG")) {
//								xiang = line.getText();
//							}
//						}
//						List<Row> cunlist = DataBaseHelper.getCunList(mContext,xiang);
//						list = cunlist;
//					}
//
//					XzzyDialog xzdialog = new XzzyDialog(mContext,list, tv, line,LxqcYdyzAdapter.this);
//					BussUtil.setDialogParams(mContext, xzdialog, 0.5, 0.5);
//
//				}else if (type.equals("2")){
//					String numtemp = LxqcUtil.getAttributeXstype(mContext,name,dbpath);
//					if(numtemp.equals("0")){
//						ShuziDialog szdialog = new ShuziDialog(mContext, tv, line,"1","", LxqcYdyzAdapter.this);
//						BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);
//					}else if(numtemp.equals("1")){
//						ShuziDialog szdialog = new ShuziDialog(mContext, tv, line,"0","1", LxqcYdyzAdapter.this);
//						BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);
//					}else if(numtemp.equals("2")){
//						ShuziDialog szdialog = new ShuziDialog(mContext, tv, line,"0","2", LxqcYdyzAdapter.this);
//						BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);
//					}else if(numtemp.equals("-1")){
//						ShuziDialog szdialog = new ShuziDialog(mContext, tv, line,"","", LxqcYdyzAdapter.this);
//						BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);
//					}
//				}else if (type.equals("3")){
//					HzbjDialog wbdialog = new HzbjDialog(mContext, tv,
//							 line, LxqcYdyzAdapter.this);
//					BussUtil.setDialogParams(mContext, wbdialog, 0.5, 0.5);
//
//				}else if (type.equals("5")){
//					if (qzlx != null) {
//						LxqcUtil.showTcAlertDialog(mContext, tv,line, LxqcYdyzAdapter.this,name, qzlx,dbpath);
//					}
//				}
//			}
//		});
		return convertView;
	}

	static class ViewHolder {
		TextView tvLine;
		TextView etLine;
	}

	/** 隐藏软件盘 */
	private void hide(TextView text) {
		InputMethodManager imm = (InputMethodManager) text.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
	}

	/** 时间选择popupwindow */
	public void initSelectTimePopuwindow(final Line line,
										 final TextView editText) {
		TimePopupWindow timePopupWindow = new TimePopupWindow(mContext,
				Type.ALL);
		timePopupWindow.setCyclic(true);
		// 时间选择后回调
		timePopupWindow.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				String bfText = line.getText();
				String seltime = getTime(date);
				editText.setText(seltime);
				line.setText(seltime);
				updataData(line, bfText, seltime);
				setTextViewCursorLocation(editText);
			}
		});
		timePopupWindow.showAtLocation(editText, Gravity.BOTTOM, 0, 0,
				new Date(), false);
	}

	/** 获取当前时间 */
	public String getTime(Date date) {
		return dateFormat.format(date);
	}

	/** 获取当前经纬度 */
	private void showLonLatDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_lonlat_input);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_lonlat);
		aliasTxt.setText(line.getTview());

		TextView txtlon = (TextView) dialog
				.findViewById(R.id.edittxt_input_lon);
		txtlon.setText(format.format(BaseActivity.currentLon) + "");
		TextView txtlat = (TextView) dialog
				.findViewById(R.id.edittxt_input_lat);
		txtlat.setText(format.format(BaseActivity.currentLat) + "");
		final TextView txtx = (TextView) dialog
				.findViewById(R.id.edittxt_input_x);
		txtx.setText(format.format(BaseActivity.currentPoint.getX()) + "");
		final TextView txty = (TextView) dialog
				.findViewById(R.id.edittxt_input_y);
		txty.setText(format.format(BaseActivity.currentPoint.getY()) + "");

		TextView back = (TextView) dialog.findViewById(R.id.editlonlat_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		TextView sure = (TextView) dialog.findViewById(R.id.editlonlat_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				dialog.dismiss();
				String key = line.getTview();
				String value = "";
				if (key.contains("横坐标") || key.contains("经度")) {
					String x = txtx.getText().toString();
					editText.setText(x);
					line.setText(x);
					value = x;
				} else if (key.contains("纵坐标") || key.contains("纬度")) {
					String y = format.format(Double.parseDouble(txty.getText()
							.toString())) + "";
					editText.setText(y);
					line.setText(y);
					value = y;
				}
				updataData(line, bfText, value);
				setTextViewCursorLocation(editText);
			}
		});

		BussUtil.setDialogParams2(mContext, dialog, 0.55, 0.55);
	}

	/** 拍照 */
	public void showTakePhoto(final Line line) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_takephoto);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
		edit.setText(line.getText());
		setEditTextCursorLocation(edit);

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				line.setText(edit.getText().toString());
				mContext.updateZp(edit.getText().toString(), line, bfText);
				dialog.dismiss();
			}
		});

		TextView takephoto = (TextView) dialog.findViewById(R.id.takephoto);
		takephoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 拍照
				mContext.takephoto(line, edit);
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hide(edit);
				dialog.dismiss();
			}
		});

		TextView lookpic = (TextView) dialog.findViewById(R.id.lookphoto);
		lookpic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 图片浏览
				mContext.lookpictures(line);
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/** 代码dialog弹出 */
	private void showTowDialog(final Line line, final Map<String, String> map,
							   final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog
				.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();
		seltext.setText(line.getText());

		final ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
		// lst.add("无");
		for (String key : map.keySet()) {
			String value = map.get(key);
			lst.add(key + "-" + value);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				position = psition;
				String selTxt = lst.get(psition);
				String text = selTxt.split("-")[1];
				seltext.setText(text);
				setTextViewCursorLocation(seltext);
			}
		});

		seltext.addTextChangedListener(new TextWatcher() {
			String bf = "";

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				bf = arg0.toString();
			}

			@Override
			public void afterTextChanged(Editable et) {
				String af = et.toString();
				if (!af.equals(bf)) {
					final List<String> list = new ArrayList<String>();
					for (String str : lst) {
						if (str.contains(af)) {
							list.add(str);
						}
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							mContext, R.layout.myspinner, list);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int psition, long arg3) {
							String selTxt = list.get(psition);
							String text = selTxt.split("-")[1];
							seltext.setText(text);
							setTextViewCursorLocation(seltext);
						}
					});
					notifyDataSetChanged();
				}
			}
		});

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String text = seltext.getText().toString();
				String value = "";
				String txt = "";
				if (position != -1) {
					value = lst.get(position).split("-")[0];
					txt = lst.get(position).split("-")[1];
					if (text.contains(txt)) {
						value = text.replace(txt, value);
					} else {
						value = text;
					}
				} else {
					value = text;
				}
				String bfText = line.getText();
				editText.setText(text);
				setTextViewCursorLocation(editText);
				line.setText(txt);
				updataData(line, bfText, value);
				setTextViewCursorLocation(editText);
				dialog.dismiss();
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		BussUtil.setDialogParams2(mContext, dialog, 0.5, 0.5);
	}

	/** 检测字段是那种类型的字段 */
	private List<Row> isDMField(String fieldname) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, fieldname);
		return list;
	}

	/** 弹出输入框dialog */
	private void showKeyDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_input);
		dialog.setCanceledOnTouchOutside(false);
		final SharedPreferences preferences = mContext.getSharedPreferences(
				line.getKey(), Context.MODE_PRIVATE);
		final Set<String> items = preferences.getStringSet(line.getKey(),
				new HashSet<String>());
		final ArrayList<String> list = new ArrayList<String>();
		if (items.size() > 0) {
			for (String str : items) {
				list.add(str);
			}
		}
		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
		// if(line.getFieldType() == Field.esriFieldTypeDouble){
		// edit.setInputType(InputType.TYPE_CLASS_NUMBER |
		// InputType.TYPE_NUMBER_FLAG_DECIMAL);
		// }
		if (line.getTview().contains("面积")) {
			double area = selGeoFeature.getGeometry().calculateArea2D();
			edit.setText(df.format(Math.abs(area) / 667));
		} else {
			edit.setText(editText.getText().toString());
		}
		setEditTextCursorLocation(edit);

		SwipeMenuListView listView = (SwipeMenuListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.myspinner, list);
		listView.setAdapter(adapter);

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				hide(edit);
				final String strTxt = edit.getText() == null ? null : edit.getText().toString();
				editText.setText(strTxt);
				dialog.dismiss();
				line.setText(strTxt);
				updataData(line, bfText, strTxt);
				setTextViewCursorLocation(editText);
				updateSet(preferences, items, line, strTxt);
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hide(edit);
				dialog.dismiss();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				String selTxt = list.get(psition);
				edit.setText(selTxt);
				setTextViewCursorLocation(edit);
			}
		});

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				addMenuItem(menu);

			}
		};
		// set creator
		listView.setMenuCreator(creator);

		// step 2. listener item click event
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						String str = list.get(position);
						list.remove(str);
						adapter.notifyDataSetChanged();
						items.remove(str);
						updateSet(preferences, items, line);
						break;
				}
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	public void addMenuItem(SwipeMenu menu){
		SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
		// set item background
		deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
		// set item width
		deleteItem.setWidth(dp2px(90));
		// set a icon
		deleteItem.setIcon(R.drawable.ic_delete);
		// add to menu
		menu.addMenuItem(deleteItem);
	}

	/** 保存数据 */
	public void updataData(Line line, String bfText, String value) {
		if (bfText == null) {
			if (line.getText().equals("")) {
				return;
			}
		} else {
			String txt = line.getText();
			if (txt != null && txt.equals(bfText)) {
				return;
			}
		}

		if(value.contains("-")){
			value = value.split("-")[0];
		}

		int length = line.getfLength();
		int size = 0;
		if (value != null) {
			size = value.length();
		}
		if (line.getFieldType() == Field.esriFieldTypeString) {
			if (size > length) {
				ToastUtil.setToast(mContext, "输入值长度大于数据库长度");
				return;
			}
		}
		String key = line.getKey();
		if (line.getFieldType() == Field.esriFieldTypeDate) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(value);
				attribute.put(key, date);
			} catch (ParseException e) {
				ToastUtil.setToast(mContext, "输入数据错误");
				e.printStackTrace();
				return;
			}
		} else if (line.getFieldType() == Field.esriFieldTypeDouble) {
			try {
				double dd = Double.parseDouble(value);
				attribute.put(key, dd);
			} catch (NumberFormatException e) {
				ToastUtil.setToast(mContext, "输入数据错误");
				e.printStackTrace();
				return;
			}
		} else {
			attribute.put(key, value);
		}

		Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),
				selGeoFeature.getSymbol(), attribute);
		FeatureTable featureTable = featureLayer.getFeatureTable();
		try {
			long featureid = selGeoFeature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e) {
			e.printStackTrace();
			attribute.put(line.getKey(), bfText);
			line.setText(bfText);
			line.setValue(bfText);
			ToastUtil.setToast((Activity) mContext, "更新失败");
			return;
		}

		line.setValue(value);
		ToastUtil.setToast((Activity) mContext, "更新成功");
		lines.set(line.getNum(), line);
		notifyDataSetChanged();

		/** 更新小班号 */
		for (Row row : mContext.gcmc) {
			if (row.getName().equals(line.getKey())) {
				mContext.ydhselect = "";
				mContext.getXbhData(pname);
			}
		}

	}

	/** 把TextView的光标放置在最后 */
	public void setTextViewCursorLocation(TextView et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** 把edittext的光标放置在最后 */
	public void setEditTextCursorLocation(EditText et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** 更新历史录入数据 */
	public void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line, String strTxt) {
		if (!items.contains(strTxt.trim())) {
			items.add(strTxt.trim());
			Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(line.getKey(), items).commit();
		}
	}

	/** 更新历史录入数据 */
	public void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).commit();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

	/** 代码dialog弹出 */
	int position = -1;

	private void showTowDialog(final Line line, final List<Row> row,
							   final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog
				.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();

		final ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
		// lst.add("无");
		if (row.size() > 0) {
			for (Row r : row) {
				String id = r.getId();
				String value = r.getName();
				lst.add(id + "-" + value);
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				position = psition;
				String selTxt = lst.get(psition);
				String text = selTxt.split("-")[1];
				seltext.setText(text);
				setTextViewCursorLocation(seltext);
			}
		});

		seltext.addTextChangedListener(new TextWatcher() {
			String bf = "";

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				bf = arg0.toString();
			}

			@Override
			public void afterTextChanged(Editable et) {
				String af = et.toString();
				if (!af.equals(bf)) {
					final List<String> list = new ArrayList<String>();
					for (String str : lst) {
						if (str.contains(af)) {
							list.add(str);
						}
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							mContext, R.layout.myspinner, list);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int psition, long arg3) {
							String selTxt = list.get(psition);
							String text = selTxt.split("-")[1];
							seltext.setText(text);
							setTextViewCursorLocation(seltext);
						}
					});
					notifyDataSetChanged();
				}
			}
		});

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String text = seltext.getText().toString();
				String value = "";
				String txt = "";
				if (position != -1) {
					value = lst.get(position).split("-")[0];
					txt = lst.get(position).split("-")[1];
					if (text.contains(txt)) {
						value = text.replace(txt, value);
					} else {
						value = text;
					}
				} else {
					value = text;
				}
				String bfText = line.getText();
				editText.setText(text);
				setTextViewCursorLocation(editText);
				line.setText(txt);
				updataData(line, bfText, value);
				setTextViewCursorLocation(editText);
				dialog.dismiss();
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		BussUtil.setDialogParams2(mContext, dialog, 0.5, 0.5);
	}




}
