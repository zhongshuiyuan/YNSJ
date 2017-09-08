package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.titan.ynsjy.R;
import com.titan.ynsjy.fragment.AuditHistoryInfoFragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细信息展示
 */

public class AuditInfoFragmentActivity extends AppCompatActivity {
    private Context mContext;
    private View view;
    private Map<String ,Object> map;
    public static void actionStart(Context context, Map<String ,Object> map) {
        Intent intent = new Intent(context, AuditInfoFragmentActivity.class);
        Bundle bundle = new Bundle();
        //SeralizableMapUtil mapUtil = new SeralizableMapUtil();
        //mapUtil.setMap(map);
        bundle.putSerializable("featureMap", (Serializable) map);
        //intent.putExtra("feature", map);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.audit_history_info_fragment,null);
        setContentView(view);
        map = (HashMap<String ,Object>)getIntent().getExtras().getSerializable("feature");
        AuditHistoryInfoFragment fragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_info);
        fragment.refresh(map);
    }
}
