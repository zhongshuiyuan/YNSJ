package com.titan.ynsjy.edite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.esri.core.geometry.GeometryEngine;
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
import com.titan.ynsjy.adapter.LayerAdapter;
import com.titan.ynsjy.entity.MyFeture;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.listviewinedittxt.LineAdapter;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by li on 2017/6/16.
 * 面属性编辑
 */
public class XbEditActivity extends BaseEditActivity {

	/**图片字段*/
	private TextView btnreturn,photograph,seepicture,tvyddcb,xbarea,yddlb;
	private DecimalFormat df = new DecimalFormat("0.00");
	/**小班识别字段*/
	public List<Row> gcmc = null;

	private SharedPreferences preferences;

	private String yddLayername = "";

	//	private FeatureLayer yddFeatureLayer;
//	private MyLayer ydMyLayer;
	public long numSize = 0;
	public List<GeodatabaseFeature> ydlist = new ArrayList<GeodatabaseFeature>();
	public Map<GeodatabaseFeature, MyLayer> ydMap = new HashMap<GeodatabaseFeature, MyLayer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		childView = getLayoutInflater().inflate(R.layout.fragment_notepad,null);
		super.onCreate(savedInstanceState);
		setContentView(childView);
		mContext = XbEditActivity.this;


		/*获取图斑的唯一编号*/
		Object obj = selGeoFeature.getAttributes().get("WYBH");
		if(obj != null){
			currentxbh = obj.toString();
		}

		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		mAdapter = new LineAdapter(XbEditActivity.this,mLines,myFeture);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);
		btnreturn.setOnClickListener(new MyListener());
		photograph = (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new MyListener());
		seepicture = (TextView) findViewById(R.id.ld_see_pic);
		seepicture.setOnClickListener(new MyListener());

		xbarea = (TextView) findViewById(R.id.tv_xbarea);
		double area = selGeoFeature.getGeometry().calculateArea2D();
		xbarea.setText(df.format(Math.abs(area))+"/"+df.format(Math.abs(area*0.0015))+
				"/"+df.format(Math.abs(area*0.0001))+"  平方米/亩/公顷");
		//txtviewxbh = (TextView) findViewById(R.id.tv_xbh);

		//getXbhData(pname);

		picPath = getImagePath(path);
		cpoyZhaop();
		//new MyAsyncTask().execute("getImagePath");

		if(currentxbh.equals("")){
			ToastUtil.setToast(mContext, "小班唯一号为空,请输入小班唯一号");
		}

		getYdLayer();

		if(pname.contains("营造林")){
			tvyddcb = (TextView) findViewById(R.id.tvyddcb);
			tvyddcb.setVisibility(View.VISIBLE);
			tvyddcb.setOnClickListener(new MyListener());
		}

		yddlb = (TextView) findViewById(R.id.yangdidian);
		yddlb.setOnClickListener(new MyListener());

		//getMustField();
	}

	@Override
	public View getParentView() {
		return childView;
	}

	class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btnreturn:
					finishThis();
					break;
				case R.id.ld_see_pic:
					/*图片浏览*/
					lookpictures(XbEditActivity.this);
					break;
				case R.id.tvyddcb:
					/* 添加样地点 */
					showYddLayer();
					break;
				case R.id.yangdidian:
					getYangdi();
					break;
				default:
					break;
			}
		}
	}

	/** 样地调查表数据填写*/
	public void yddcb(final String ydlayerName){
		Builder builder = new Builder(mContext);
		builder.setMessage("使用当前点!");
		builder.setTitle("信息提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(mContext, YzlYddActivity.class);
				myFeture.setFeature((GeodatabaseFeature)featureLayer.getFeature(selGeoFeature.getId()));
				intent.putExtra("xbh", currentxbh);//小班唯一编号
				intent.putExtra("myfeture", myFeture);
				intent.putExtra("yddname", ydlayerName);
				intent.putExtra("numSize", numSize);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				XbEditActivity.this.finish();
			}
		});
		builder.create().show();

	}
	/**展示样地点图层*/
	public void showYddLayer(){
		List<MyLayer> list = new ArrayList<MyLayer>();
		for(MyLayer myLayer : BaseActivity.layerNameList){
			Type type = myLayer.getTable().getGeometryType();
			String name = myLayer.getCname();
			if(type.equals(Type.POINT) && cname.equals(name)){
				list.add(myLayer);
			}
		}
		int size = list.size();
		if (size == 1) {
			if(BaseActivity.currentPoint == null || !BaseActivity.currentPoint.isValid()){
				ToastUtil.setToast(mContext, "未获取到当前位置坐标");
				return;
			}
			MyLayer layer = list.get(0);
			yddLayername = layer.getTable().getTableName();
			boolean flag = GeometryEngine.intersects(selGeoFeature.getGeometry(), BaseActivity.currentPoint, BaseActivity.mapView.getSpatialReference());
			if(flag){
				yddcb(yddLayername);
			}else{
				ToastUtil.setToast(mContext, "当前位置不在所选小班范围内");
			}
		}else{
			if (size > 1) {
				showFeatureLayer(list);
				return;
			}
		}
	}

	/** 编辑图层选择窗口， 选择要编辑的图层 */
	public void showFeatureLayer(final List<MyLayer> list) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_featureselect);
		dialog.setCanceledOnTouchOutside(true);

		ListView listview = (ListView) dialog.findViewById(R.id.listview_layers);
		LayerAdapter adapter = new LayerAdapter(list, mContext);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				if(BaseActivity.currentPoint == null || !BaseActivity.currentPoint.isValid()){
					ToastUtil.setToast(mContext, "未获取到当前位置坐标");
					return;
				}

				MyLayer layer = list.get(position);
				yddLayername = layer.getTable().getTableName();
				dialog.dismiss();
				boolean flag = GeometryEngine.intersects(selGeoFeature.getGeometry(), BaseActivity.currentPoint, BaseActivity.mapView.getSpatialReference());
				if(flag){
					yddcb(yddLayername);
				}else{
					ToastUtil.setToast(mContext, "当前位置不在所选小班范围内");
				}
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
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

	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){
		this.pcLine = line;
		updateJjzp(pctext,line,bfText);
	}

	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(selGeoFeature.getId());
		Map<String, Object> attribute = feature.getAttributes();
		boolean flag = true;
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
			long id = feature.getId();
			featureTable.updateFeature(id, updateGraphic);
		} catch (TableException e)
		{
			flag = false;
			e.printStackTrace();
		}
		if(flag){
			line.setText(pctext);
			mAdapter.notifyDataSetChanged();
			ToastUtil.setToast(mContext, "照片编号更新成功");
		}else{
			ToastUtil.setToast(mContext, "照片编号更新失败,请重新更新");
		}
	}

	/**activity 回调*/
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

			dealPhotoFile(mCurrentPhotoPath);

			updateZPBH();
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
			editor.putStringSet(pname, rows).commit();
		}
	}

	/** 选择小班查询方法 */
	public void getGeometryInfo(Geometry geometry,final MyLayer layer) {
		numSize = 0;
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
							numSize = result.featureCount();
							Iterator<Object> iterator = result.iterator();
							while (iterator.hasNext()) {
								GeodatabaseFeature feature = (GeodatabaseFeature) iterator.next();
								ydlist.add(feature);
								ydMap.put(feature, layer);
							}
						}
					}
				});
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		finishThis();
	}

	/** 异步类 */
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(final String... params) {
			if (params[0].equals("getImagePath")) {
				cpoyZhaop();
			}
			return null;
		}
	}
	/**检查照片编号是否与唯一编号一致*/
	public void cpoyZhaop(){
		Map<String, Object> attrs = myFeture.getFeature().getAttributes();
		String Ywybh = "";
		for(String str : attrs.keySet()){
			Object obj = attrs.get(str);
			if(obj != null && obj.toString().trim().contains(".jpg")){
				String[] strs = obj.toString().trim().split("_");
				if(strs.length > 1){
					Ywybh = strs[0];
					break;
				}
			}
		}

		if(!Ywybh.equals("") && !Ywybh.equals(currentxbh)){
			File file = new File(picPath);
			File file2 = new File(file.getParent()+"/"+Ywybh);
			if(file2.exists()){
				File[] files = file2.listFiles();
				for(File ff : files){
					File file3 = new File(picPath, ff.getName());
					copyFile(ff, file3);
				}
			}
		}
	}

	/**复制文件到平板中*/
	private void copyFile(File sourceFile, File targetFile){
		try {
			InputStream db = new FileInputStream(sourceFile);
			FileOutputStream fos = new FileOutputStream(targetFile);
			byte[] buffer = new byte[8129];
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		mAdapter.getGeometryInfo(myFeture.getFeature().getGeometry());
	}

	/**获取样地数据*/
	public void getYangdi(){
		if(numSize > 1){
			showYangdiData();
		}else if(numSize == 1){
			if(parentStr.equals("PointEdit")){
				this.finish();
				return;
			}
			GeodatabaseFeature feature = ydlist.get(0);
			toYangdiInfo(feature,ydMap.get(feature));
		}else{
			ToastUtil.setToast(mContext, "不存在样地");
		}
	}

	/**当样地个数大于1个时显示样地列表*/
	public void showYangdiData(){
		Dialog dialog = new Dialog(mContext,R.style.Dialog);
		dialog.setContentView(R.layout.dialog_yd_list);
		ListView yd_lstview = (ListView) dialog.findViewById(R.id.yd_data_listview);

		EdFeatureResultAdapter adapter = new EdFeatureResultAdapter(mContext, ydlist, "样地点列表");
		yd_lstview.setAdapter(adapter);

		yd_lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adv, View v, int position,
									long id) {
				GeodatabaseFeature feature = ydlist.get(position);
				if(parentId == feature.getId()){
					finish();
					return;
				}
				toYangdiInfo(feature,ydMap.get(feature));
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/**跳转到样地属性页*/
	public void toYangdiInfo(GeodatabaseFeature feature,MyLayer layer){
		Intent intent = new Intent(mContext, PointEditActivity.class);
		MyFeture feture = new MyFeture(pname, path, cname, feature,layer);
		Bundle bundle = new Bundle();
		bundle.putSerializable("myfeture", feture);
		bundle.putSerializable("parent", "XbEdit");
		bundle.putSerializable("pWybh", currentxbh);
		bundle.putSerializable("id", fid+"");
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**获取样地所在的图层及样地数据*/
	public void getYdLayer(){
		for(MyLayer myLayer : BaseActivity.layerNameList){
			Type type = myLayer.getTable().getGeometryType();
			String name = myLayer.getCname();
			if(type.equals(Type.POINT) && cname.equals(name)){
				getGeometryInfo(selGeoFeature.getGeometry(),myLayer);
			}
		}
	}


}
