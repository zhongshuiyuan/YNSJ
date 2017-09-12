package com.titan.ynsjy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.AuditInfoActivity;
import com.titan.ynsjy.adapter.AuditHistoryExpandAdapter;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
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
    private List<Map<String, Object>> selectList = new ArrayList<>();//已选择的历史记录
    private Map<String, Object> attrMap;//选择的审计记录属性集合
    private boolean isTwoPane;//是否双页

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        view = inflater.inflate(R.layout.audit_history_catalog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * @param context 上下文
     * @param list 编辑id
     * @param map 编辑id对应图斑
     * @param cbMap checkbox状态
     * @param type 页面显示状态，false为默认模式，true为比较模式
     */
    public void exRefresh(Context context, final List<String> list, final Map<String, List<Feature>> map,
                          final Map<String, Boolean> cbMap, final boolean type) {
        //选择布局模式
        if (!type) {
            auditCatalogSure.setVisibility(View.GONE);
        }else {
            auditCatalogSure.setVisibility(View.VISIBLE);
        }
        final AuditHistoryExpandAdapter adapter = new AuditHistoryExpandAdapter(context, list, type,map, cbMap);
        auditHistoryAll.setAdapter(adapter);
        auditHistoryAll.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                attrMap = map.get(list.get(groupPosition)).get(childPosition).getAttributes();
                Feature feature = map.get(list.get(groupPosition)).get(childPosition);
                //选择布局模式
                if (!type) {
                    //setLayout(attrMap, feature);
                }else {
                    //审计历史记录比较时双选
                    String featureId = String.valueOf(feature.getId());
                    Feature selFeature = map.get(list.get(groupPosition)).get(childPosition);
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
                }
                return false;
            }
        });
    }

    /**
     * 显示选择的两个历史记录的详细信息
     */
    private void showCompare(Map<String, Object> map) {
        AuditHistoryInfoFragment compareFragment =  (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_all_compare);
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
        }else {
            AuditInfoActivity.actionStart(mContext,map);
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
        if (selectList.size()==2){
            showCompare(attrMap);
            return;
        }
        ToastUtil.setToast(mContext,"请选择两个记录");
    }
}
