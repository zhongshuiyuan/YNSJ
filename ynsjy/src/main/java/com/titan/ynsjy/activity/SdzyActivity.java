package com.titan.ynsjy.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.BussUtil;

/**
 * Created by li on 2016/5/26.
 * 湿地资源页面
 */
public class SdzyActivity extends BaseActivity {

	View parentview;
	Context mContext;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentview = getLayoutInflater().inflate(R.layout.activity_sdzy, null);
		super.onCreate(savedInstanceState);
		setContentView(parentview);
		mContext = SdzyActivity.this;
		/*变更系统背景*/
		ImageView topview = (ImageView) parentview.findViewById(R.id.topview);
		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_sdzy));
		
		activitytype = getIntent().getStringExtra("name");
		/*获取配置的数据*/
		proData = BussUtil.getConfigXml(mContext,activitytype);
		
	}

	@Override
	public View getParentView() {
		return parentview;
	}

}
