package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.titan.ynsjy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hanyw on 2017/9/13/013.
 * 功能选择
 */

public class FunctionChoiceActivity extends AppCompatActivity {

    @BindView(R.id.function_add_audit)
    LinearLayout functionAddAudit;//新增审计
    @BindView(R.id.function_my_audit)
    LinearLayout functionMyAudit;//审计历史
    @BindView(R.id.function_my_track)
    LinearLayout functionMyTrack;//轨迹

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_choice);
        this.mContext = this;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.function_add_audit, R.id.function_my_audit, R.id.function_my_track})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.function_add_audit:
                onNext(YzlActivity.class);
                break;
            case R.id.function_my_audit:
                onNext(AuditHistoryActivity.class);
                break;
            case R.id.function_my_track:
                onNext(YzlActivity.class);
                break;
        }
    }

    private void onNext(Class activity){
        Intent intent = new Intent(mContext,activity);
        startActivity(intent);
    }
}
