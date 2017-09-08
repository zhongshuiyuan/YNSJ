package com.titan.ynsjy.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.titan.ynsjy.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细内容展示页面
 */

public class AuditHistoryInfoFragment extends Fragment {
    @BindView(R.id.audit_people)
    EditText auditPeople;
    @BindView(R.id.audit_time)
    EditText auditTime;
    @BindView(R.id.audit_latlon)
    EditText auditLatlon;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.audit_reason)
    EditText auditReason;
    @BindView(R.id.audit_info)
    EditText auditInfo;
    @BindView(R.id.audit_edit_before)
    EditText auditEditBefore;
    @BindView(R.id.audit_edit_after)
    EditText auditEditAfter;
    @BindView(R.id.audit_mark)
    EditText auditMark;
    Unbinder unbinder;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.audit_history_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void refresh(Map<String, Object> map) {
        //Map<String, Object> map = feature.getAttributes();
        auditPeople.setText(map.get("").toString());
        auditPeople.setText(map.get("").toString());
        auditTime.setText(map.get("").toString());
        auditLatlon.setText(map.get("").toString());
        textView.setText(map.get("").toString());
        auditReason.setText(map.get("").toString());
        auditInfo.setText(map.get("").toString());
        auditEditBefore.setText(map.get("").toString());
        auditEditAfter.setText(map.get("").toString());
        auditMark.setText(map.get("").toString());
        auditPeople.setEnabled(false);
        auditTime.setEnabled(false);
        auditLatlon.setEnabled(false);
        textView.setEnabled(false);
        auditReason.setEnabled(false);
        auditInfo.setEnabled(false);
        auditEditBefore.setEnabled(false);
        auditEditAfter.setEnabled(false);
        auditMark.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
