package com.titan.ynsjy.listviewinedittxt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.titan.ynsjy.edite.activity.LineEditActivity;
import com.titan.ynsjy.entity.MyFeture;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.entity.YzlTree;
import com.titan.ynsjy.supertreeview.NodeResource;
import com.titan.ynsjy.swipemenulistview.SwipeMenu;
import com.titan.ynsjy.swipemenulistview.SwipeMenuCreator;
import com.titan.ynsjy.swipemenulistview.SwipeMenuItem;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView;
import com.titan.ynsjy.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.titan.ynsjy.timepaker.TimePopupWindow;
import com.titan.ynsjy.timepaker.TimePopupWindow.OnTimeSelectListener;
import com.titan.ynsjy.timepaker.TimePopupWindow.Type;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.CursorUtil;
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


/**
 * Created by li on 2017/6/16.
 * 线属性编辑adapter
 */
public class PolylineAdapter extends BaseAdapter {

	private List<Line> lines;
	private LineEditActivity mContext;
	private DecimalFormat format = new DecimalFormat("0.000000");
	DecimalFormat df = new DecimalFormat("0.00");
	/* 标识数据是否修改了 */
	private boolean saveflag = false;
	private HashMap<String, String> hashMap = new HashMap<>();

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String pname;

	private Map<String, Object> attribute;

	/*   */
	private FeatureLayer featureLayer;
	private GeodatabaseFeature selGeoFeature;
	private Set<String> mustColumn = new HashSet<>();
	MyFeture myFeture = null;

	public PolylineAdapter(Context context, List<Line> lines, MyFeture feture) {
		this.mContext = (LineEditActivity) context;
		this.lines = lines;
		this.myFeture = feture;
		this.pname = feture.getPname();
		this.featureLayer = feture.getMyLayer().getLayer();
		this.selGeoFeature = feture.getFeature();
		this.attribute = selGeoFeature.getAttributes();
		SharedPreferences preferences = context.getSharedPreferences(pname
				+ "mustfield", Context.MODE_PRIVATE);
		mustColumn = preferences.getStringSet(pname, new HashSet<String>());
	}

	@Override
	public int getCount() {
		if (lines != null) {
			return lines.size();
		}
		return 0;
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

		if (holder.etLine.getTag() instanceof TextWatcher) {
			holder.etLine
					.removeTextChangedListener((TextWatcher) (holder.etLine
							.getTag()));
		}

		final Line line = lines.get(position);
		String alias = line.getTview();
		holder.tvLine.setText(alias);

		String key = line.getKey();
		boolean flag = false;

		String layername = featureLayer.getFeatureTable().getTableName();
		if (layername.contains("设计")) {
			flag = true;
		} else {
			flag = isNoEditField(key);
		}
		holder.etLine.setText(line.getText());
		holder.etLine.setEnabled(!flag);

		if (flag) {
			holder.tvLine.setBackgroundColor(Color.GRAY);
			holder.etLine.setBackgroundColor(Color.GRAY);
		} else {
			holder.tvLine.setBackgroundColor(Color.WHITE);
			holder.etLine.setBackgroundColor(Color.WHITE);
		}
		boolean ff = line.isNullable();
		if (!ff && !line.getKey().contains("OBJECTID")) {
			holder.tvLine.setBackgroundColor(Color.RED);
			// holder.etLine.setBackgroundColor(Color.RED);
		}
		// if(mustColumn.contains(line.getKey())){
		// holder.tvLine.setBackgroundColor(Color.RED);
		// //holder.etLine.setBackgroundColor(Color.RED);
		// }

		if (line.isFocus()) {
			if (!holder.etLine.isFocused()) {
				holder.etLine.requestFocus();
			}
			// CharSequence text = line.getText();
			// holder.etLine.setSelection(TextUtils.isEmpty(text) ? 0 :
			// text.length());
		} else {
			if (holder.etLine.isFocused()) {
				holder.etLine.clearFocus();
			}
		}

		holder.etLine.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String tview = line.getTview();
				String key = line.getKey();
				CodedValueDomain domain = line.getDomain();
				if (tview.contains("照片") || tview.contains("相片")) {
					showTakePhoto(line);
				} else if (tview.contains("时间") || tview.equals("dcsj")
						|| tview.contains("日期")) {
					hide(holder.etLine);
					initSelectTimePopuwindow(line, holder.etLine);
				} else if (tview.contains("坐标") || tview.contains("经度")
						|| tview.contains("纬度")) {
					hide(holder.etLine);
					showLonLatDialog(line, holder.etLine);
				} else {
					hide(holder.etLine);
					if (domain != null) {
						Map<String, String> values = domain.getCodedValues();

						if(line.getTview().contains("乡")){
							String str = "";
							for(Line ln : lines){
								if(ln.getTview().contains("县")){
									Object obj = attribute.get(ln.getKey());
									str = obj == null ? "" : obj.toString() ;
									break;
								}
							}

							Map<String, String> value = new HashMap<String, String>();
							for(String vv : values.keySet()){
								if(vv.contains(str)){
									value.put(vv,values.get(vv));
								}
							}
							values = value;
						}else if(line.getTview().contains("村")){
							String str = "";
							for(Line ln : lines){
								if(ln.getTview().contains("乡")){
									Object obj = attribute.get(ln.getKey());
									str = obj == null ? "" : obj.toString() ;
									break;
								}
							}
							Map<String, String> value = new HashMap<String, String>();
							for(String vv : values.keySet()){
								if(vv.contains(str)){
									value.put(vv,values.get(vv));
								}
							}
							values = value;
						}

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

		holder.etLine.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					final boolean focus = line.isFocus();
					check(position);
					boolean flag = holder.etLine.isFocused();
					if (!focus && !flag) {
						holder.etLine.requestFocus();
						holder.etLine.onWindowFocusChanged(true);
					}
				}
				return false;
			}
		});

		final TextWatcher watcher = new SimpeTextWather() {

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					line.setText(null);
				} else {
					line.setText(String.valueOf(s));
				}
			}
		};
		holder.etLine.addTextChangedListener(watcher);
		holder.etLine.setTag(watcher);

		return convertView;
	}

	private void check(int position) {
		for (Line l : lines) {
			l.setFocus(false);
		}
		lines.get(position).setFocus(true);
	}

	static class ViewHolder {
		TextView tvLine;
		TextView etLine;
	}

	/** 检测字段是那种类型的字段 */
	private List<Row> isDMField(String fieldname) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, fieldname);
		;
		return list;
	}

	/** 检测字段是否为不能修改的字段 */
	private boolean isNoEditField(String fieldname) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, "notedite");
		if (list == null) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getName();
			if (name.equals(fieldname)) {
				return true;
			}
		}
		return false;
	}

	/** 生成多级菜单 */
	public List<NodeResource> initNodeTree(List<YzlTree> dilei) {

		List<NodeResource> list = new ArrayList<NodeResource>();
		/* 循环添加节点id,代码及名称 */
		/* 第一级节点添加 */
		List<YzlTree> tree = new ArrayList<YzlTree>();
		HashMap<String, Integer> index = new HashMap<String, Integer>();
		int startIndex = 0;
		for (int i = 0; i < dilei.size(); i++) {
			YzlTree dl = dilei.get(i);
			if (dl.getDLLEVEL() != 1)
				continue;
			NodeResource n1 = new NodeResource("" + -1, "" + startIndex,
					dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
			list.add(n1);
			tree.add(dl);
			index.put(dl.getDLDM(), startIndex);
			startIndex++;
		}
		/* 添加第二级 */
		List<YzlTree> tree1 = new ArrayList<YzlTree>();
		HashMap<String, Integer> index1 = new HashMap<String, Integer>();
		for (int ii = 0; ii < tree.size(); ii++) {
			for (int i = 0; i < dilei.size(); i++) {
				YzlTree dl = dilei.get(i);
				if (dl.getDLLEVEL() != 2)
					continue;
				if (dl.getDLPARENTDM().equals(tree.get(ii).getDLDM())) {
					NodeResource n2 = new NodeResource(""
							+ index.get(dl.getDLPARENTDM()), "" + startIndex,
							dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
					list.add(n2);
					tree1.add(dl);
					index1.put(dl.getDLDM(), startIndex);
					startIndex++;
				}
			}
		}
		/* 添加第三级 */
		List<YzlTree> tree2 = new ArrayList<YzlTree>();
		HashMap<String, Integer> index2 = new HashMap<String, Integer>();
		for (int ii = 0; ii < tree1.size(); ii++) {
			for (int i = 0; i < dilei.size(); i++) {
				YzlTree dl = dilei.get(i);
				if (dl.getDLLEVEL() != 3)
					continue;
				if (dl.getDLLEVEL() == 3
						&& dl.getDLPARENTDM().equals(tree1.get(ii).getDLDM())) {
					NodeResource n3 = new NodeResource(""
							+ index1.get(dl.getDLPARENTDM()), "" + startIndex,
							dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
					list.add(n3);
					tree2.add(dl);
					index2.put(dl.getDLDM(), startIndex);
					startIndex++;
				}
			}
		}
		/* 添加第三级 */
		List<YzlTree> tree3 = new ArrayList<YzlTree>();
		HashMap<String, Integer> index3 = new HashMap<String, Integer>();
		for (int ii = 0; ii < tree2.size(); ii++) {
			for (int i = 0; i < dilei.size(); i++) {
				YzlTree dl = dilei.get(i);
				if (dl.getDLLEVEL() != 4)
					continue;
				if (dl.getDLLEVEL() == 4
						&& dl.getDLPARENTDM().equals(tree2.get(ii).getDLDM())) {
					NodeResource n4 = new NodeResource(""
							+ index2.get(dl.getDLPARENTDM()), "" + startIndex,
							dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
					list.add(n4);
					tree3.add(dl);
					index3.put(dl.getDLDM(), startIndex);
					startIndex++;
				}
			}
		}

		return list;
	}

	/** 隐藏软件盘 */
	private void hide(TextView text) {
		InputMethodManager imm = (InputMethodManager) text.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
	}

	/** 时间选择popupwindow */
	public void initSelectTimePopuwindow(final Line line,final TextView editText) {
		TimePopupWindow timePopupWindow = new TimePopupWindow(mContext,Type.ALL);
		timePopupWindow.setCyclic(true);
		// 时间选择后回调
		timePopupWindow.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				String bfText = line.getText();
				String seltime = getTime(date);
				editText.setText(seltime);
				line.setText(seltime);
				updataData(line, bfText, seltime, seltime);
				CursorUtil.setTextViewCursorLocation(editText);
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
				updataData(line, bfText, value, value);
				CursorUtil.setTextViewCursorLocation(editText);
			}
		});

		BussUtil.setDialogParams2(mContext, dialog, 0.55, 0.55);
	}

	/** 代码dialog弹出 */
	private int position = -1;
	private void showTowDialog(final Line line, final List<Row> row,
							   final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog
				.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();

		ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
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
				String txt = lst.get(psition);
				String str = txt.split("-")[1];
				seltext.setText(str);
				CursorUtil.setTextViewCursorLocation(seltext);
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
				} else {
					value = text;
					txt = text;
				}
				String bfText = line.getText();
				if (bfText != null && bfText.equals(txt)) {
					dialog.dismiss();
					return;
				}
				editText.setText(text);
				CursorUtil.setTextViewCursorLocation(editText);
				updataData(line, bfText, txt, value);
				CursorUtil.setTextViewCursorLocation(editText);
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
		final List<String> lst = new ArrayList<>();
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
				String txt = lst.get(psition);
				String str = txt.split("-")[1];
				seltext.setText(str);
				CursorUtil.setTextViewCursorLocation(seltext);
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
				String af = et.toString().trim();
				if (!af.equals(bf)) {
					final List<String> list = new ArrayList<>();
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
							String txt = list.get(psition);
							String str = txt.split("-")[1];
							seltext.setText(str);
							CursorUtil.setTextViewCursorLocation(seltext);
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
				} else {
					value = text;
					txt = text;
				}
				position = -1;
				String bfText = line.getText();
				if (bfText.equals(txt)) {
					dialog.dismiss();
					return;
				}
				line.setText(txt);
				editText.setText(text);
				CursorUtil.setTextViewCursorLocation(editText);
				updataData(line, bfText, txt, value);
				CursorUtil.setTextViewCursorLocation(editText);
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

	/** 拍照 */
	private void showTakePhoto(final Line line) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_takephoto);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
		edit.setText(line.getText());
		CursorUtil.setEditTextLocation(edit);

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
				mContext.lookpictures(mContext,line);
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/** 弹出输入框dialog */
	private void showKeyDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_input);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

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

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
		if (line.getFieldType() == Field.esriFieldTypeDouble) {
			edit.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}
		if (line.getTview().contains("面积")) {
			double area = selGeoFeature.getGeometry().calculateArea2D();
			edit.setText(df.format(Math.abs(area) / 667));
		} else {
			edit.setText(editText.getText().toString());
		}
		CursorUtil.setEditTextLocation(edit);

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
				String strTxt = edit.getText() == null ? null : edit.getText()
						.toString();
				editText.setText(strTxt);
				dialog.dismiss();
				line.setText(strTxt);
				updataData(line, bfText, strTxt, strTxt);
				CursorUtil.setTextViewCursorLocation(editText);
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
				CursorUtil.setTextViewCursorLocation(edit);
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

	public void addMenuItem(SwipeMenu menu) {
		SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
		// set item background
		deleteItem
				.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
		// set item width
		deleteItem.setWidth(dp2px(90));
		// set a icon
		deleteItem.setIcon(R.drawable.ic_delete);
		// add to menu
		menu.addMenuItem(deleteItem);
	}

	/** 保存数据 */
	private void updataData(Line line, String bfText, String txt, String value) {
		Map<String, Object> attributes = featureLayer.getFeature(selGeoFeature.getId()).getAttributes();
		if (bfText.equals(value)) {
			return;
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
				attributes.put(key, date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (line.getFieldType() == Field.esriFieldTypeDouble) {
			double dd = Double.parseDouble(value);
			attributes.put(key, dd);
		} else {
			attributes.put(key, value);
		}

		Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),
				selGeoFeature.getSymbol(), attributes);
		FeatureTable featureTable = featureLayer.getFeatureTable();
		try {
			long featureid = selGeoFeature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e) {
			attribute.put(line.getKey(), bfText);
			line.setText(bfText);
			line.setValue(bfText);
			ToastUtil.setToast((Activity) mContext, "更新失败");
			e.printStackTrace();
			return;
		}
		line.setValue(value);
		line.setText(txt);
		ToastUtil.setToast((Activity) mContext, "更新成功");
		lines.set(line.getNum(), line);
		notifyDataSetChanged();
		attribute = attributes;

	}

	/** 更新历史录入数据 */
	private void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line, String strTxt) {
		if (!items.contains(strTxt.trim()) && !strTxt.equals("")) {
			items.add(strTxt.trim());
			Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(line.getKey(), items).apply();
		}
	}

	/** 更新历史录入数据 */
	private void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).apply();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

}
