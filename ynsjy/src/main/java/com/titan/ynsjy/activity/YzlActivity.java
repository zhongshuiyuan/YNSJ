package com.titan.ynsjy.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.SimpleFillSymbol;
import com.titan.gis.GisUtil;
import com.titan.gis.RendererUtil;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.BitmapTool;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;

/**
 * Created by li on 2016/5/26.
 * 营造林页面
 */
public class YzlActivity extends BaseActivity {
	
	private View parentView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_yzl, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		 mContext = YzlActivity.this;
		//ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		//topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_yzl));
		activitytype = getIntent().getStringExtra("name");
        //根据配置文件获取文件
		proData = BussUtil.getConfigXml(mContext,"yzl");
        Button btn= (Button) findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudit(selGeoFeaturesList.get(0).getGeometry());
            }
        });
	}

	/**
	 * 新增审计
	 */
	public void startAudit(Geometry geometry){


		mapView.setExtent(geometry);

        for(MyLayer layer:layerNameList){
            if(layer.getLayer().getGeometryType()== Geometry.Type.POLYGON )
            layer.getLayer().setRenderer(RendererUtil.getSimpleRenderer(new SimpleFillSymbol(Color.TRANSPARENT)));
        }
        Bitmap bitmap=GisUtil.getDrawingMapCache(geometry,mapView);
        try{
            //String path=myLayer.getPath()+"/images/T_imgs";
            String path=MyApplication.resourcesManager.getImagePath();
            BitmapTool.saveBitmap(path,bitmap);
            ToastUtil.setToast(mContext,"获取影像范围成功");
        }catch(Exception e) {
            ToastUtil.setToast(mContext,"获取影像数据异常"+e);
        }
	}

	@Override
	public View getParentView() {
		return parentView;
	}


}
