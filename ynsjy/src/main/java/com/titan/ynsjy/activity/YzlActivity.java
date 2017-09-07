package com.titan.ynsjy.activity;

import android.os.Bundle;
import android.view.View;

import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.BussUtil;

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
		proData = BussUtil.getConfigXml(mContext,"yzl");
	}

	@Override
	public View getParentView() {
		return parentView;
	}

}
