package com.titan.ynsjy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.AuditInfoActivity;
import com.titan.ynsjy.adapter.AuditHistoryExpandAdapter;
import com.titan.ynsjy.entity.MyLayer;
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
import butterknife.Unbinder;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计历史记录表
 */

public class AuditCatalogFragment extends Fragment {
    @BindView(R.id.audit_history_exlist)
    ExpandableListView auditHistoryAll;//所有审计历史记录显示列表
    Unbinder unbinder;
    @BindView(R.id.audit_catalog_sure)
    TextView auditCatalogSure;//确定
    private Context mContext;
    private View view;
    private MyLayer myLayer;
    private List<Map<String, Object>> selectList = new ArrayList<>();//已选择的历史记录
    private Map<String, Object> attrMap;//选择的审计记录属性集合
    private boolean isTwoPane;//是否双页
    private List<Feature> featureList;//审计记录列表
    private List<List<Feature>> childList;//编辑id对应图斑集合
    private List<String> fk_uidList;//编辑id列表
    private static final int QUERY_FINISH = 1;//查询完成
    private static final int QUERY_NODATA = 2;//没有查询到数据
    private Map<String, Boolean> cbMap;//checkbox状态
    AuditCatalogFragment catalogFragment;//所有审计历史记录显示页面
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FINISH:
                    catalogFragment.exRefresh(mContext, fk_uidList, childList, cbMap, 0);
                    break;
                case QUERY_NODATA:
                    ToastUtil.setToast(mContext, "没有查询到数据");
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        view = inflater.inflate(R.layout.audit_history_catalog, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        getData();
        queryData();
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
    public void queryData() {
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
                childList = featureSort();

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
    private List<List<Feature>> featureSort() {
        List<List<Feature>> list = new ArrayList<>();
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
            List<Feature> tempList = new ArrayList<>();
            for (Feature f : featureList) {
                if (f.getAttributeValue("FK_EDIT_UID").toString().equals(fk_uid)) {
                    cList.add(f);
                    tempList.add(f);
                }
            }
            featureList.removeAll(tempList);
            list.add(cList);
        }
        return list;
    }

    /**
     * @param mode 模式选择，true为比较模式，false为默认模式
     */
    public void modeChoice(int mode) {
        exRefresh(mContext, fk_uidList, childList, cbMap, mode);
    }

    /**
     * @param context   上下文
     * @param list      编辑id
     * @param childList 编辑id对应图斑
     * @param cbMap     checkbox状态
     * @param type      页面显示状态，false为默认模式，true为比较模式
     */
    public void exRefresh(Context context, final List<String> list, final List<List<Feature>> childList,
                          final Map<String, Boolean> cbMap, final int type) {
        //选择布局模式
        if (type==0) {
            auditCatalogSure.setVisibility(View.GONE);
        } else {
            auditCatalogSure.setVisibility(View.VISIBLE);
        }
        final AuditHistoryExpandAdapter adapter = new AuditHistoryExpandAdapter(context, list, type, childList, cbMap);
        auditHistoryAll.setAdapter(adapter);
        auditHistoryAll.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                attrMap = childList.get(groupPosition).get(childPosition).getAttributes();
                Feature feature = childList.get(groupPosition).get(childPosition);
                //选择布局模式
                if (type==0) {
                    setLayout(attrMap);
                } else if (type==1){
                    //审计历史记录比较时双选
                    String featureId = String.valueOf(feature.getId());
                    Feature selFeature = childList.get(groupPosition).get(childPosition);
                    if (cbMap.get(featureId)) {
                        cbMap.put(featureId, false);
                        selectList.remove(selFeature.getAttributes());
                    } else if (selectList.size() < 2) {
                        selectList.add(selFeature.getAttributes());
                        cbMap.put(featureId, true);
                    } else {
                        Map<String, Object> s = selectList.get(0);
                        selectList.remove(s);
                        cbMap.put(String.valueOf(s.get("OBJECTID")), false);
                        selectList.add(selFeature.getAttributes());
                        cbMap.put(featureId, true);
                    }
                    adapter.notifyDataSetChanged();
                }else if (type==2){
                    String featureId = String.valueOf(feature.getId());
                    Feature selFeature = childList.get(groupPosition).get(childPosition);
                    if (cbMap.get(featureId)) {
                        cbMap.put(featureId, false);
                        selectList.remove(selFeature.getAttributes());
                    }else {
                        selectList.add(selFeature.getAttributes());
                        cbMap.put(featureId, true);
                    }
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    /**
     * 显示选择的两个历史记录的详细信息
     */
    private void showCompare(Map<String, Object> map) {
        AuditHistoryInfoFragment compareFragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_all_compare);
        compareFragment.refresh(selectList.get(0));
        setLayout(map);
    }

    /**
     * @param map 选择的审计记录
     */
    private void setLayout(Map<String, Object> map) {
        if (isTwoPane) {
            AuditHistoryInfoFragment fragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_all_info);
            fragment.refresh(map);
            fragment.editMode(false);
        } else {
            AuditInfoActivity.actionStart(mContext, map);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTwoPane = getActivity().findViewById(R.id.audit_info_fragment) != null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 比较模式，选择小班
     */
    @OnClick(R.id.audit_catalog_sure)
    public void onViewClicked() {
        if (selectList.size() == 2) {
            showCompare(attrMap);
            return;
        }
        ToastUtil.setToast(mContext, "请选择两个记录");
    }
}
