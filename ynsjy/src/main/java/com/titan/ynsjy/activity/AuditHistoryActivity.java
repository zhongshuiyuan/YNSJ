package com.titan.ynsjy.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.fragment.AuditCatalogFragment;
import com.titan.ynsjy.fragment.AuditHistoryInfoFragment;
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
 * 审计历史页面
 */

public class AuditHistoryActivity extends AppCompatActivity {
    @BindView(R.id.audit_add_close)
    TextView auditAddClose;//返回
    @BindView(R.id.audit_add_edit)
    TextView auditAddEdit;//编辑
    @BindView(R.id.audit_add_save)
    TextView auditAddSave;//保存
    @BindView(R.id.audit_add_compare)
    TextView auditAddCompare;//比较
    @BindView(R.id.audit_add_cancel)
    TextView auditAddCancel;//取消
    private Context mContext;
    private View view;
    private FrameLayout compareFragment;//审计历史比较页面
    private MyLayer myLayer;
    private List<Feature> featureList;//审计记录列表
    private Map<String, List<Feature>> map;//编辑id对应图斑集合
    private List<String> fk_uidList;//编辑id列表
    private static final int QUERY_FINISH = 1;//查询完成
    private static final int QUERY_NODATA = 2;//没有查询到数据
    private Map<String, Boolean> cbMap;//checkbox状态
    AuditCatalogFragment catalogFragment;//所有审计历史记录显示页面
    AuditHistoryInfoFragment infoFragment;//单个审计记录详细信息显示页面
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FINISH:
                    catalogFragment.exRefresh(mContext, fk_uidList, map, cbMap, false);
                    break;
                case QUERY_NODATA:
                    ToastUtil.setToast(mContext, "没有查询到数据");
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
        view = LayoutInflater.from(this).inflate(R.layout.audit_history_all, null);
        setContentView(view);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化页面和数据
     */
    private void init() {
        getData();
        queryData();
        catalogFragment = (AuditCatalogFragment) getFragmentManager().findFragmentById(R.id.audit_catalog);
        infoFragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_all_info);
        compareFragment = (FrameLayout) view.findViewById(R.id.audit_compare_fragment);
    }


    /**
     * 获取编辑表
     */
    private void getData() {
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
    }

    /**
     * 查询审计记录
     */
    private void queryData() {
        ArcGISQueryUtils.getQueryFeaturesAll(myLayer.getTable(), new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                Message message = new Message();
                long size = objects.featureCount();
                if (size <= 0) {
                    message.what = QUERY_NODATA;
                    handler.sendMessage(message);
                    return;
                }
                featureList = new ArrayList<>();
                cbMap = new HashMap<>();
                for (Object object : objects) {
                    Feature feature = (Feature) object;
                    featureList.add(feature);
                    cbMap.put(String.valueOf(feature.getId()), false);
                }
                map = featureSort();

                message.what = QUERY_FINISH;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.setToast(mContext, "数据查询出错");
            }
        });
    }

    /**
     * @return 按照编辑id对应分类好的map集合
     */
    private Map<String, List<Feature>> featureSort() {
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

    @OnClick({R.id.audit_add_close, R.id.audit_add_edit, R.id.audit_add_save, R.id.audit_add_compare,R.id.audit_add_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_add_close://返回
                this.finish();
                break;
            case R.id.audit_add_edit://编辑
                infoFragment.editMode(true);
                compareFragment.setVisibility(View.GONE);
                auditAddSave.setVisibility(View.VISIBLE);
                auditAddEdit.setVisibility(View.GONE);
                auditAddCompare.setVisibility(View.GONE);
                auditAddCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.audit_add_save://保存
                infoFragment.save(myLayer.getTable());
                queryData();
                break;
            case R.id.audit_add_compare://比较
                catalogFragment.exRefresh(mContext, fk_uidList, map, cbMap, true);
                compareFragment.setVisibility(View.VISIBLE);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.GONE);
                auditAddCompare.setVisibility(View.GONE);
                auditAddCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.audit_add_cancel://取消
                infoFragment.editMode(false);
                catalogFragment.exRefresh(mContext, fk_uidList, map, cbMap, false);
                compareFragment.setVisibility(View.GONE);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.VISIBLE);
                auditAddCompare.setVisibility(View.VISIBLE);
                auditAddCancel.setVisibility(View.GONE);
                break;
        }
    }
}
