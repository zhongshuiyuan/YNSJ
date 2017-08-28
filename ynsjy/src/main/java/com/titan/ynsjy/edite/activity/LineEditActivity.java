package com.titan.ynsjy.edite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.listviewinedittxt.PolylineAdapter;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by li on 2017/6/16.
 * 线属性编辑
 */
public class LineEditActivity extends BaseEditActivity {

	private PolylineAdapter mAdapter;
	private Context mContext;
	//private ViewGroup rootView;
	private TextView btnreturn,photograph,seepicture,txtviewxbh,xbarea;
	private DecimalFormat df = new DecimalFormat("0.00");

	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		childView = getLayoutInflater().inflate(R.layout.activity_line_edit,null);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(childView);
		mContext = LineEditActivity.this;


		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		mAdapter = new PolylineAdapter(this, mLines,myFeture);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);
		btnreturn.setOnClickListener(new MyListener());
		photograph = (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new MyListener());
		seepicture = (TextView) findViewById(R.id.ld_see_pic);
		seepicture.setOnClickListener(new MyListener());

		xbarea = (TextView) findViewById(R.id.tv_xbarea);
		double length = selGeoFeature.getGeometry().calculateLength2D();
		String txt = df.format(Math.abs(length))+"   米";
		xbarea.setText(txt);

		Object obj = selGeoFeature.getAttributes().get("WYBH");
		if(obj != null){
			currentxbh = obj.toString();
		}
		//getXbhData(pname);

		txtviewxbh = (TextView) findViewById(R.id.tv_xbh);
		txtviewxbh.setText(currentxbh);
		picPath = getImagePath(path);
		getMustField();

	}

	@Override
	public View getParentView() {
		return childView;
	}

//	/** 绑定数据*/
//	private ArrayList<Line> createLines() {
//		ArrayList<Line> lines = new ArrayList<Line>();
//		String xianD = "",xiangD= "";
//		for (int i = 0; i < LINE_NUM; i++) {
//			Field field = fieldList.get(i);
//			Line line = new Line();
//			line.setNum(i);
//			line.setTview(field.getAlias());
//			line.setfLength(field.getLength());
//			line.setKey(field.getName());
//			CodedValueDomain domain = (CodedValueDomain) field.getDomain();
//			line.setDomain(domain);
//			line.setFieldType(field.getFieldType());
//			boolean ff = field.isNullable();
//			line.setNullable(ff);
//
//			Object obj = selGeoFeature.getAttributeValue(field.getName());
//			if(obj != null){
//				String value = obj.toString();
//				if(line.getFieldType() == Field.esriFieldTypeDate){
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					Date date = new Date(Long.parseLong(obj.toString()));
//					value = sdf.format(date);
//				}
//
//				if(domain != null){
//					Map<String, String> values = domain.getCodedValues();
//					for(String key : values.keySet()){
//						if(key.equals(value)){
//							line.setText(values.get(key));
//							break;
//						}else{
//							line.setText(value);
//						}
//					}
//				}else{
//
//					if(field.getAlias().contains("县") || field.getName().equals("xian") || field.getName().equals("XIAN")){
//						xianD = value;
//						String str = Util.getXXCValue(mContext, value, "", xianMap);
//						line.setText(str);
//					}else if(field.getAlias().contains("乡") || field.getName().equals("xiang") || field.getName().equals("XIANG")){
//						xiangD = value;
//						String str = Util.getXXCValue(mContext, value, xianD, xiangMap);
//						line.setText(str);
//					}else if(field.getAlias().contains("村") || field.getName().equals("cun") || field.getName().equals("CUN")){
//						String str = value;
//						if(xiangD.contains(xianD)){
//							str = Util.getXXCValue(mContext, value, xiangD, cunMap);
//						}else{
//							str = Util.getXXCValue(mContext, value,xianD+xiangD, cunMap);
//						}
//						line.setText(str);
//					}else{
//						List<Row> list = isDMField(field.getName());
//						if(list != null && list.size() > 0){
//							for(Row row : list){
//								if(row.getId().equals(value)){
//									line.setText(row.getName());
//									break;
//								}else{
//									line.setText(value);
//								}
//							}
//						}else{
//							line.setText(value);
//						}
//					}
//				}
//			}else{
//				line.setText("");
//			}
//			lines.add(line);
//		}
//		return lines;
//	}

	class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btnreturn:
					finishThis();
					break;
				case R.id.ld_see_pic:
					/*图片浏览*/
					lookpictures(LineEditActivity.this);
					break;
				default:
					break;
			}
		}
	}

	/** 获取图片保存地址*/
	public String getImagePath(String path){
		File file = new File(path);
		String path1 = file.getParent()+ "/images";
		File file2 = new File(path1);
		boolean flag = file2.exists();
		if(!flag){
			file2.mkdirs();
		}
		if(currentxbh==null || currentxbh.equals("")){
			picPath = path1;
		}else{
			String path2 = file2.getPath()+"/"+currentxbh;
			File file3 = new File(path2);
			if(!file3.exists()){
				file3.mkdirs();
			}
			picPath = file3.getPath();
		}
		return picPath;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			updateZPBH();
			dealPhotoFile(mCurrentPhotoPath);
		}
	}


	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){

		updateJjzp(pctext,line,bfText);
	}

	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(selGeoFeature.getId());
		Map<String, Object> attribute = feature.getAttributes();

		if(pctext == null || (bfText != null && pctext.equals(bfText))){
			return;
		}

		boolean flag = true;
		int length = pctext.length();
		int size = 0;
		for(Field ff : fieldList){
			if(ff.getName().equals(line.getKey())){
				size = ff.getLength();
				break;
			}
		}

		if(length > size){
			ToastUtil.setToast(mContext, "数据长度超过数据库规定长度");
			return;
		}

		attribute.put(line.getKey(), pctext);

		Graphic updateGraphic = new Graphic(feature.getGeometry(),feature.getSymbol(),attribute);
		FeatureTable featureTable = featureLayer.getFeatureTable();
		try
		{
			long featureid = feature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e)
		{
			flag = false;
			e.printStackTrace();
		}
		if(flag){
			line.setText(pctext);
			mAdapter.notifyDataSetChanged();
			ToastUtil.setToast((Activity) mContext, "照片编号更新成功");
		}else{
			ToastUtil.setToast((Activity) mContext, "照片编号更新失败");
		}
	}

	/** 获取小班号数据 */
	public void getXbhData(String pname)
	{
		List<Row> gcmc = BussUtil.getConfigXml(mContext, pname,"wysbzd");// 项目名称
		if(gcmc == null){
			return;
		}
		for(int i = 0;i<gcmc.size();i++){
			String key = gcmc.get(i).getName();
			Object obj = BaseActivity.selectFeatureAts.get(key);
			if(obj != null){
				String str = BaseActivity.selectFeatureAts.get(key).toString();
				currentxbh = currentxbh + str;
			}else{
				continue;
			}
		}
	}

	/**获取必填必填字段*/
	public void getMustField(){
		preferences = getSharedPreferences(pname+"mustfield", MODE_PRIVATE);
		List<Row> list = BussUtil.getConfigXml(mContext, pname, "mustfield");
		if(list == null){
			return;
		}
		if(list != null || list.size() >= 0){
			Set<String> rows = new HashSet<String>();
			for (Row row : list) {
				rows.add(row.getName());
			}
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(pname, rows).apply();
		}
	}


}
