package com.titan.ynsjy.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.AuditInfoFragmentActivity;
import com.titan.ynsjy.adapter.AuditHistoryExpandAdapter;

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
    ExpandableListView auditHistoryAll;
    Unbinder unbinder;
    @BindView(R.id.audit_catalog_sure)
    TextView auditCatalogSure;
    private Context mContext;
    private View view;
    private List<Feature> selectList = new ArrayList<>();
    private Map<String, Object> attrMap;
    private boolean isTwoPane;//是否双页

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        view = inflater.inflate(R.layout.audit_history_catalog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

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
                    setLayout(attrMap, feature);
                }else {
                    //审计历史记录比较时双选
                    String featureId = String.valueOf(feature.getId());
                    Feature selFeature = map.get(list.get(groupPosition)).get(childPosition);
                    if (cbMap.get(featureId)) {
                        cbMap.put(featureId, false);
                        selectList.remove(selFeature);
                    } else if (selectList.size() < 2) {
                        selectList.add(selFeature);
                        cbMap.put(featureId, true);
                    } else {
                        Feature s = selectList.get(0);
                        selectList.remove(s);
                        cbMap.put(String.valueOf(s.getId()), false);
                        selectList.add(selFeature);
                        cbMap.put(featureId, true);
                    }
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void showCompare(Map<String, Object> attrMap) {
        AuditHistoryInfoFragment compareFragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_all_compare);
        compareFragment.refresh(attrMap, selectList.get(0));
        setLayout(attrMap, selectList.get(1));
    }

    private void setLayout(Map<String, Object> attrMap, Feature feature) {
        if (isTwoPane) {
            AuditHistoryInfoFragment fragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_all_info);
            fragment.refresh(attrMap, feature);
            fragment.editMode(false);
        }else {
            AuditInfoFragmentActivity.actionStart(mContext, attrMap, feature);
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

    @OnClick(R.id.audit_catalog_sure)
    public void onViewClicked() {
        showCompare(attrMap);
    }
}
