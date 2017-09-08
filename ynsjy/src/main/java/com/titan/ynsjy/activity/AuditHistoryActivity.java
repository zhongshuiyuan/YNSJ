package com.titan.ynsjy.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.fragment.AuditCatalogFragment;
import com.titan.ynsjy.util.ArcGISQueryUtils;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细信息展示
 */

public class AuditHistoryActivity extends AppCompatActivity {
    @BindView(R.id.audit_add_close)
    TextView auditAddClose;

    private Context mContext;
    private MyLayer myLayer;
    private List<Feature> featureList;
    private Map<String, List<Feature>> map;
    private List<String> fk_uidList;
    private static final int QUERY_FINISH = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FINISH:
//                    AuditHistoryExpandAdapter adapter = new AuditHistoryExpandAdapter(AuditHistoryActivity.this, fk_uidList, map);
//                    Log.e("tag", fk_uidList + "," + map);
//                    adapter.notifyDataSetChanged();
                    AuditCatalogFragment fragment = (AuditCatalogFragment) getFragmentManager().findFragmentById(R.id.audit_catalog);
                    fragment.exRefresh(mContext,fk_uidList,map);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.audit_history_all);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getData();
        queryData();
    }

    @OnClick(R.id.audit_add_close)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_add_close:
                this.finish();
                break;
        }
    }

    private void getData() {
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
    }

    private void queryData() {
        ArcGISQueryUtils.getQueryFeaturesAll(myLayer.getTable(), new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                Log.e("tag", objects.featureCount() + ":");
                if (objects.featureCount() <= 0) {
                    ToastUtil.setToast(mContext, "没有查询到数据");
                    return;
                }
                Log.e("tag", objects.featureCount() + ":");
                featureList = new ArrayList<>();
                for (Object object : objects) {
                    Feature feature = (Feature) object;
                    featureList.add(feature);
                }
                map = featureSort();
                Message message = new Message();
                message.what = QUERY_FINISH;
                handler.sendMessage(message);
                Log.e("tag", "end");
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.setToast(mContext, "数据查询出错");
            }
        });
    }

    private Map<String, List<Feature>> featureSort() {
        Log.e("tag", "sort:");
        Map<String, List<Feature>> map = new HashMap<>();
        List<Feature> cList;
        fk_uidList = new ArrayList<>();
        for (Feature f : featureList) {
            String fk_uid = f.getAttributeValue("FK_EDIT_UID").toString();
            if (fk_uidList.contains(fk_uid)) {
                continue;
            }
            fk_uidList.add(fk_uid);
        }

        for (String fk_uid : fk_uidList) {
            cList = new ArrayList<>();
            String uid = "";
            for (Feature f : featureList) {
                if (f.getAttributeValue("FK_EDIT_UID").toString().equals(fk_uid)) {
                    uid = fk_uid;
                    cList.add(f);
                    //featureList.remove(f);需要Iterator
                }
            }
            map.put(uid, cList);
        }
        return map;
    }
}
