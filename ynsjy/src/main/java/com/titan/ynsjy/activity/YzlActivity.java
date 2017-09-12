package com.titan.ynsjy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Feature;
import com.esri.core.symbol.SimpleFillSymbol;
import com.titan.gis.GisUtil;
import com.titan.gis.RendererUtil;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.BitmapTool;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;

/**
 * Created by li on 2016/5/26.
 * 营造林页面
 */
public class YzlActivity extends BaseActivity {

	private static final int DRAW_BITMAP_FIELD = 2;
	private View parentView;
	private static final int DRAW_BITMAP_FINISH = 1;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==DRAW_BITMAP_FINISH){
				//auditAdd();
				auditAddOrCompare(false);
			}
			if (msg.what==DRAW_BITMAP_FIELD){
				ToastUtil.setToast(mContext,"获取影像截图失败"+msg.obj);
				//auditAdd();
				//auditAddOrCompare(false);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_yzl, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		 mContext = this;
		//ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		//topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_yzl));
		activitytype = getIntent().getStringExtra("name");
        //根据配置文件获取文件
		proData = BussUtil.getConfigXml(mContext,"yzl");
		auditButton = (RadioButton) parentView.findViewById(R.id.auditButton);
		auditButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imgTiledLayer==null){
					ToastUtil.setToast(mContext,"没有加载影像图");
					return;
				}
				if (!BaseUtil.checkFeaturelayerExist("edit",layerNameList)){
					ToastUtil.setToast(mContext,"没有加载图层数据");
					return;
				}
				if (selGeoFeaturesList.size()<0){
					ToastUtil.setToast(mContext,"没有选择小班");
					return;
				}
				if (selGeoFeaturesList.size()==1){
					getSelParams(selGeoFeaturesList,0);
					startAudit(selGeoFeature);
				} else {
					basePresenter.showListFeatureResult(selGeoFeaturesList,0);
				}
			}
		});
	}

	/**
	 * 新增审计
	 */
	public void startAudit(final Feature feature){

		mapView.setExtent(feature.getGeometry(),0,false);

        for(MyLayer layer:layerNameList){
            if(layer.getLayer().getGeometryType()== Geometry.Type.POLYGON )
            layer.getLayer().setRenderer(RendererUtil.getSimpleRenderer(new SimpleFillSymbol(Color.TRANSPARENT)));
        }
		new Handler().postDelayed(new Runnable(){
			public void run() {
				final Bitmap bitmap=GisUtil.getDrawingMapCache(feature.getGeometry(),mapView);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Message message = new Message();
						try{
							//String path=myLayer.getPath()+"/images/T_imgs";
							String path=MyApplication.resourcesManager.getImagePath(feature.getId());
							BitmapTool.saveBitmap(path,bitmap);
							///ToastUtil.setToast(mContext,"获取影像范围成功");
							message.what = DRAW_BITMAP_FINISH;
							handler.sendMessage(message);
						}catch(Exception e) {
							//ToastUtil.setToast(mContext,"获取影像数据异常"+e);
							Log.e("tag",e.toString());
							message.what = DRAW_BITMAP_FIELD;
							message.obj=e;
							handler.sendMessage(message);
						}
					}
				}).start();
			}
		}, 2000);

	}

	@Override
	public View getParentView() {
		return parentView;
	}

	/**
	 * @param type 审计类型 false为新增审计,true为审计历史
	 */
	public void auditAddOrCompare(boolean type) {
		Intent intent = new Intent(mContext, AuditActivity.class);
		intent.putExtra("fid", selGeoFeature.getId());
		intent.putExtra("picPath", ResourcesManager.getImagePath(myLayer.getPath()));
		intent.putExtra("auditType",type);
		mContext.startActivity(intent);
	}

	@Override
	public void startAddAudit() {
		startAudit(selGeoFeature);
	}
}
