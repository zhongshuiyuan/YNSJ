package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.fragment.AuditHistoryInfoFragment;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细信息展示
 */

public class AuditInfoFragmentActivity extends AppCompatActivity {
    @BindView(R.id.audit_back)
    TextView auditBack;
    private Context mContext;
    private View view;
    private Map<String, Object> map;

    public static void actionStart(Context context, Map<String, Object> map, Feature feature) {
        Intent intent = new Intent(context, AuditInfoFragmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("featureMap", (Serializable) map);
        bundle.putSerializable("feature", (Serializable) feature);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.audit_history_info_fragment, null);
        setContentView(view);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        map = (Map<String, Object>) bundle.getSerializable("featureMap");
        Feature feature = (Feature) bundle.getSerializable("feature");
        AuditHistoryInfoFragment fragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_info);
        fragment.refresh(map,feature);
    }

    @OnClick(R.id.audit_back)
    public void onViewClicked() {
        this.finish();
    }
}
