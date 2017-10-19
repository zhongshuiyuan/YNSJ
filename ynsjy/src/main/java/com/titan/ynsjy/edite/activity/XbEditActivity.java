package com.titan.ynsjy.edite.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer.SelectionMode;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.AuditActivity;
import com.titan.ynsjy.activity.AuditHistoryActivity;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.listviewinedittxt.LineAdapter;
import com.titan.ynsjy.util.ResourcesManager;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by li on 2017/6/16.
 * 面属性编辑
 */
@RuntimePermissions
public class XbEditActivity extends BaseEditActivity{

	/**图片字段*/
	private TextView btnreturn,photograph,seepicture,tvyddcb,xbarea,yddlb,tv_audit,tv_auditHistory,tv_videotape;
	private DecimalFormat df = new DecimalFormat("0.00");
	/**小班识别字段*/
	public List<Row> gcmc = null;

	private SharedPreferences preferences;
	private String yddLayername = "";
	/**图片地址*/
	private String imagePath;
	/**数据是否改变*/
	private boolean isChanged = false;
	private Map<String ,List<String>> map;
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
		mAdapter = new LineAdapter(XbEditActivity.this,mLines,myFeture,false);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);//返回
		btnreturn.setOnClickListener(new MyListener());
		photograph = (TextView) findViewById(R.id.fragment_photograph);//拍照
		photograph.setOnClickListener(new MyListener());
		seepicture = (TextView) findViewById(R.id.ld_see_pic);//图片浏览
		seepicture.setOnClickListener(new MyListener());
		tv_audit = (TextView) findViewById(R.id.ld_audit);//审计
		tv_audit.setOnClickListener(new MyListener());
		tv_auditHistory = (TextView) findViewById(R.id.ld_audit_history);//审计历史
		tv_auditHistory.setOnClickListener(new MyListener());
		tv_videotape = (TextView) findViewById(R.id.fragment_videotape);//录像
		tv_videotape.setOnClickListener(new MyListener());

		xbarea = (TextView) findViewById(R.id.tv_xbarea);
		double area = selGeoFeature.getGeometry().calculateArea2D();
		xbarea.setText(df.format(Math.abs(area))+"/"+df.format(Math.abs(area*0.0015))+
				"/"+df.format(Math.abs(area*0.0001))+"  平方米/亩/公顷");
		//txtviewxbh = (TextView) findViewById(R.id.tv_xbh);

		//getXbhData(pname);

		picPath = ResourcesManager.getImagePath(path);
		cpoyZhaop();
		//new MyAsyncTask().execute("getImagePath");

//		if(currentxbh.equals("")){
//			ToastUtil.setToast(mContext, "小班唯一号为空,请输入小班唯一号");
//		}

		//getYdLayer();

		if(pname.contains("营造林")){
			tvyddcb = (TextView) findViewById(R.id.tvyddcb);
			//tvyddcb.setVisibility(View.VISIBLE);
			tvyddcb.setOnClickListener(new MyListener());
		}

		yddlb = (TextView) findViewById(R.id.yangdidian);
		yddlb.setOnClickListener(new MyListener());

		//getMustField();
		//setActivityResult();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		XbEditActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
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
					//finishThis();
					finish();
					break;
				case R.id.ld_see_pic:
					/*图片浏览*/
					//lookpictures(XbEditActivity.this);
					BaseEditActivityPermissionsDispatcher.lookpicturesWithCheck(XbEditActivity.this,XbEditActivity.this);
					break;
				case R.id.ld_audit_history:
					/*审计历史*/
					//auditAddOrCompare(true);
					Intent intent = new Intent(XbEditActivity.this, AuditHistoryActivity.class);
					startActivity(intent);
					break;
				case R.id.ld_audit:
					/* 审计 */
					auditAddOrCompare(false);
					break;
				case R.id.fragment_photograph:
					/* 拍照 */
					//takephoto(mLines.get(5),zpeditText);
					//photograph();
					XbEditActivityPermissionsDispatcher.photographWithCheck(XbEditActivity.this);
					break;
				case R.id.fragment_videotape:
					/* 录像 */

					break;
				default:
					break;
			}
		}
	}

	/**
	 * @param type 审计类型 false为新增审计
	 */
	private void auditAddOrCompare(boolean type) {
		Intent intent = new Intent(XbEditActivity.this, AuditActivity.class);
		intent.putExtra("fid", fid);
		intent.putExtra("picPath",picPath);
		intent.putExtra("auditType",type);
		startActivity(intent);
	}

	@NeedsPermission({Manifest.permission.CAMERA})
	void photograph() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imagePath = ResourcesManager.getImagePath(path)+"/"+ ResourcesManager.getPicName(String.valueOf(fid));
		Uri uri = Uri.fromFile(new File(imagePath));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	@OnPermissionDenied({Manifest.permission.CAMERA})
    void showRecordDenied(){
        ToastUtil.setToast(mContext,"拒绝后将无法da打开相机，您可以在手机中手动授予权限");
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
		} catch (TableException e) {
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
			//EditPhoto dialog = new EditPhoto(mContext,imagePath,fid,null);
			//dialog.show();
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

//	@Override
//	public void onBackPressed() {
//		//super.onBackPressed();
//		//finishThis();
//		finish();
//	}

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
}
