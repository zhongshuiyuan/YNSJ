package com.titan.ynsjy.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.AuditInfoFragmentActivity;
import com.titan.ynsjy.adapter.AuditHistoryExpandAdapter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计历史记录表
 */

public class AuditCatalogFragment extends Fragment {
    @BindView(R.id.audit_history_exlist)
    ExpandableListView auditHistoryAll;
    Unbinder unbinder;
    private Context mContext;
    private View view;
    private List<String> list;
    private Map<String, List<Feature>> map;
    private boolean isTwoPane;//是否双页

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        view = inflater.inflate(R.layout.audit_history_catalog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void exRefresh(Context context, final List<String> list, final Map<String, List<Feature>> map) {
        AuditHistoryExpandAdapter adapter = new AuditHistoryExpandAdapter(context, list, map);
        auditHistoryAll.setAdapter(adapter);
        auditHistoryAll.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Map<String, Object> attrMap = map.get(list.get(groupPosition)).get(childPosition).getAttributes();
                if (isTwoPane) {
                    AuditHistoryInfoFragment fragment = (AuditHistoryInfoFragment) getFragmentManager().findFragmentById(R.id.audit_history_info);
                    fragment.refresh(attrMap);
                } else {
                    AuditInfoFragmentActivity.actionStart(mContext, attrMap);
                }
                return false;
            }
        });
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
}
