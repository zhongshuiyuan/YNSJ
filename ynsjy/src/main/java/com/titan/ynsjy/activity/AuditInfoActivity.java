package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.AuditHistory.AuditHistoryInfoFragment;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计页面右侧详细信息展示
 */

public class AuditInfoActivity extends AppCompatActivity {
    @BindView(R.id.audit_back)
    TextView auditBack;//返回
    private Map<String, Object> map;

    /**
     * @param context 上下文
     * @param map 选择的审计记录属性集合
     */
    public static void actionStart(Context context, Map<String, Object> map) {
        Intent intent = new Intent(context, AuditInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("featureMap", (Serializable) map);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_history_info_fragment);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        map = (Map<String, Object>) bundle.getSerializable("featureMap");
        AuditHistoryInfoFragment fragment = (AuditHistoryInfoFragment) getSupportFragmentManager().findFragmentById(R.id.audit_history_info);
        fragment.refresh(map);
    }

    /**
     * 返回
     */
    @OnClick(R.id.audit_back)
    public void onViewClicked() {
        this.finish();
    }
}
