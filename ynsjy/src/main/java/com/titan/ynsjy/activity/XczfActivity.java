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
 * 巡查执法页面
 */
public class XczfActivity extends BaseActivity {

	View parentView;
	Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_xczf, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		
		mContext = XczfActivity.this;
		ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_zfxc));
		
		activitytype = getIntent().getStringExtra("name");
		proData = BussUtil.getConfigXml(mContext,activitytype);
	}

	@Override
	public View getParentView() {
		return parentView;
	}

}
