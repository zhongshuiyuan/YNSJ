package com.titan.ynsjy.audithistory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.model.AuditInfo;
import com.titan.ynsjy.databinding.FragAuditCompareBinding;
import com.titan.ynsjy.util.EDUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by hanyw on 2017/9/15/015.
 * 审计历史对比页面
 */

public class AuditCompareFragment extends Fragment implements AuditCompare {
    private Context mContext;
    private String[] attrArray = new String[]{"OBJECTID","AUDIT_PEOPLE","MODIFYTIME","AUDIT_COORDINATE",
            "MODIFYINFO","INFO","BEFOREINFO","AFTERINFO","REMARK"};
    private List<Map<String, Object>> dataList;

    public static AuditCompareFragment singleton;
    private FragAuditCompareBinding binding;

    private AuditHistoryViewModel auditViewModel;
    public static AuditCompareFragment newIntance() {
        if (singleton == null) {
            singleton = new AuditCompareFragment();
        }
        return singleton;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext = getActivity();
        binding = FragAuditCompareBinding.inflate(inflater, container, false);
        binding.setViewModel(auditViewModel);
        setData();
        return binding.getRoot();
    }

    public void setViewModel(AuditHistoryViewModel viewModel) {
        auditViewModel = viewModel;
    }

    public void setData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        dataList = (List<Map<String, Object>>) getActivity().getIntent().getSerializableExtra("dataList");
        Log.e("datalist",dataList.toString());
        Log.e("tag",dataList+"dataList");
        try {
            if (dataList != null && dataList.size() >= 2) {
                //Map<String, Object> map1 = dataList.get(0);
                //Map<String, Object> map2 = dataList.get(1);
                //AuditInfo info1 = new AuditInfo();
                List<AuditInfo> auditInfos=new ArrayList<>();

                for (Map<String,Object> map :dataList){
                    AuditInfo auditInfo=new AuditInfo();
                    auditInfo.setAuditer(EDUtil.getAttrValue(map, "AUDIT_PEOPLE"));
                    auditInfo.setObjectid(EDUtil.getAttrValue(map, "OBJECTID"));
                    auditInfo.setAddress(EDUtil.getAttrValue(map, "AUDIT_COORDINATE"));
                    auditInfo.setTime(EDUtil.getAttrValue(map, "MODIFYTIME"));
                    auditInfo.setReason(EDUtil.getAttrValue(map, "MODIFYINFO"));
                    auditInfo.setInfo(EDUtil.getAttrValue(map, "INFO"));
                    auditInfo.setBeforinfo(EDUtil.getAttrValue(map, "BEFOREINFO"));
                    auditInfo.setAfterinfo(EDUtil.getAttrValue(map, "AFTERINFO"));
                    auditInfo.setRemark(EDUtil.getAttrValue(map, "REMARK"));
                    auditInfos.add(auditInfo);
                }
                binding.setAuditInfo(auditInfos.get(0));
                binding.setAuditInfo2(auditInfos.get(1));

            }
            else {
                ToastUtil.showShort(getActivity(),"比较数据不符合要求");
            }
        }catch (Exception e){
            ToastUtil.showShort(getActivity(),"初始化数据异常"+e);
            Log.e("tag","setdata:"+e);
        }
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
