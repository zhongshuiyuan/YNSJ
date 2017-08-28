package com.titan.ynsjy.edite.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer.SelectionMode;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.EdFeatureResultAdapter;
import com.titan.ynsjy.entity.MyFeture;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.listviewinedittxt.PontAdapter;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by li on 2017/6/16.
 * 点属性编辑
 */
public class PointEditActivity extends BaseEditActivity {

	private PontAdapter mAdapter;
	/** 小班唯一识别字段 */
	public List<Row> gcmc = null;

	private TextView btnreturn, seepicture, txtviewxbh;

	/**与样地点关联的小班唯一号*/
	public String xbwybh="";

	private TextView xbfeatureView;
	private List<GeodatabaseFeature> xblist = new ArrayList<>();
	private Map<GeodatabaseFeature, MyLayer> xbMap = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        childView = getLayoutInflater().inflate(R.layout.activity_point_edit,null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(childView);
		mContext = PointEditActivity.this;

		if(parentStr.equals("XbEdit")){
			xbwybh = (String) getIntent().getSerializableExtra("pWybh");
		}

		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		mAdapter = new PontAdapter(PointEditActivity.this, mLines, myFeture);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);
		btnreturn.setOnClickListener(new MyListener());

		seepicture = (TextView) findViewById(R.id.ld_see_pic);
		seepicture.setOnClickListener(new MyListener());

		txtviewxbh = (TextView) findViewById(R.id.tv_yddh);

		xbfeatureView = (TextView) findViewById(R.id.xbfeature);
		xbfeatureView.setOnClickListener(new MyListener());

		//getXbhData(pname);

		Object obj = selGeoFeature.getAttributes().get("WYBH");
		if(obj != null){
			currentxbh = obj.toString();
			txtviewxbh.setText(currentxbh);
		}

		getXdLayer();

		picPath = getImagePath(path);

	}

    @Override
    public View getParentView() {
        return childView;
    }

	class MyListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btnreturn:
					finishThis();
					break;
				case R.id.ld_see_pic:
					/* 图片浏览 */
					lookpictures(PointEditActivity.this);
					break;
				case R.id.xbfeature:
					toXbFeatureData();
					break;
				default:
					break;
			}
		}
	}

	/** 获取图片保存地址 */
	public String getImagePath(String path) {
		File file = new File(path);
		String path1 = file.getParent()+ "/images";
		File file2 = new File(path1);
		boolean flag = file2.exists();
		if(!flag){
			file2.mkdirs();
		}
		if(currentxbh.equals("") || currentxbh==null ){
			if(xbwybh != null || xbwybh.equals("")){
				String path2 = file2.getPath()+"/"+xbwybh;
				File file3 = new File(path2);
				if(!file3.exists()){
					file3.mkdirs();
				}
				picPath = file3.getPath();
			}else{
				picPath = path1;
			}
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

	/**activity 回调*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			updateZPBH();
			dealPhotoFile(mCurrentPhotoPath);
		}
	}
	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){
		this.pcLine = line;
		updateJjzp(pctext,line,bfText);
	}
	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(selGeoFeature.getId());
		Map<String, Object> attribute = feature.getAttributes();

		if(pctext == null || (bfText != null && pctext.equals(bfText))){
			return;
		}

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
			ToastUtil.setToast((Activity) mContext, "照片编号更新失败,请重新更新");
			e.printStackTrace();
			return;
		}

		line.setText(pctext);
		mAdapter.notifyDataSetChanged();
		ToastUtil.setToast(mContext, "照片编号更新成功");
	}

	/** 获取小班号数据 */
	public void getXbhData(String pname) {
		gcmc = BussUtil.getConfigXml(mContext, pname, "wysbzd");// 项目名称
		if (gcmc == null) {
			return;
		}
		for (int i = 0; i < gcmc.size(); i++) {
			String key = gcmc.get(i).getName();
			Object obj = selGeoFeature.getAttributes().get(key);
			if (obj != null) {
				String str = selGeoFeature.getAttributes().get(key).toString();
				currentxbh = currentxbh + str;
			} else {
				continue;
			}
		}
		txtviewxbh.setText(currentxbh);

		picPath = getImagePath(path);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		finishThis();
		return;
	}

	/** 获取样地所在位置的小班数据 */
	public void getGeometryInfo(Geometry geometry,final MyLayer layer) {
		QueryParameters queryParams = new QueryParameters();
		queryParams.setOutFields(new String[] { "*" });
		queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
		queryParams.setGeometry(geometry);
		queryParams.setReturnGeometry(true);
		queryParams.setOutSpatialReference(SpatialReference.create(2343));
		layer.getLayer().selectFeatures(queryParams, SelectionMode.NEW,
				new CallbackListener<FeatureResult>() {

					@Override
					public void onError(Throwable arg0) {
						ToastUtil.setToast(mContext, "查询出错");
					}

					@Override
					public void onCallback(FeatureResult result) {
						if(result.featureCount() > 0){
							Iterator<Object> iterator = result.iterator();
							while (iterator.hasNext()) {
								GeodatabaseFeature feature = (GeodatabaseFeature) iterator.next();
								xblist.add(feature);
								xbMap.put(feature, layer);
							}

							if(xblist.size() == 1){
								xbwybh = xblist.get(0).getAttributes().get("WYBH").toString();
							}
						}
					}
				});
	}

	/**获取样地所在的图层及样地数据*/
	public void getXdLayer(){
		for(MyLayer myLayer : BaseActivity.layerNameList){
			Type type = myLayer.getTable().getGeometryType();
			String name = myLayer.getCname();
			if(type.equals(Type.POLYGON) && cname.equals(name)){
				getGeometryInfo(selGeoFeature.getGeometry(),myLayer);
			}
		}
	}


	/**跳转到样地所在的小班属性页*/
	public void toXbFeatureData(){

		if(parentStr.equals("XbEdit")){
			finish();
			return;
		}

		int size = xblist.size();
		if(size > 1){
			showXiaobData();
		}else if(size == 1){
			GeodatabaseFeature feature = xblist.get(0);
			toXiaobanInfo(feature,xbMap.get(feature));
		}
	}

	/**当样地个数大于1个时显示样地列表*/
	public void showXiaobData(){
		Dialog dialog = new Dialog(mContext,R.style.Dialog);
		dialog.setContentView(R.layout.dialog_yd_list);
		ListView yd_lstview = (ListView) dialog.findViewById(R.id.yd_data_listview);

		EdFeatureResultAdapter adapter = new EdFeatureResultAdapter(mContext, xblist, "小班列表");
		yd_lstview.setAdapter(adapter);

		yd_lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adv, View v, int position,
									long id) {
				GeodatabaseFeature feature = xblist.get(position);
				toXiaobanInfo(feature,xbMap.get(feature));
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/**跳转到样地属性页*/
	public void toXiaobanInfo(GeodatabaseFeature feature,MyLayer layer){
		Intent intent = new Intent(mContext, XbEditActivity.class);
		MyFeture feture = new MyFeture(pname, path, cname, feature,layer);
		Bundle bundle = new Bundle();
		bundle.putSerializable("myfeture", feture);
		bundle.putSerializable("parent", "PointEdit");
		bundle.putSerializable("id",fid +"");
		intent.putExtras(bundle);
		startActivity(intent);
	}


	//    /** 绑定数据 */
//	public ArrayList<Line> createLines() {
//		ArrayList<Line> lines = new ArrayList<>();
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
//			boolean fl = field.isNullable();
//			line.setNullable(fl);
//
//			Object obj = selGeoFeature.getAttributes().get(field.getName());
//			if (obj != null) {
//				String value = obj.toString();
//
//				if(line.getFieldType() == Field.esriFieldTypeDate){
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					Date date = new Date(Long.parseLong(obj.toString()));
//					value = sdf.format(date);
//				}
//
//				if (domain != null) {
//					Map<String, String> values = domain.getCodedValues();
//					for (String key : values.keySet()) {
//						if (key.equals(value)) {
//							line.setText(values.get(key));
//							break;
//						} else {
//							line.setText(value);
//						}
//					}
//				} else {
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
//						if (list != null && list.size() > 0) {
//							for (Row row : list) {
//								if (row.getId().equals(value)) {
//									line.setText(row.getName());
//									break;
//								} else {
//									line.setText(value);
//								}
//							}
//						} else {
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

}
