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
        dataList = (List<Map<String, Object>>) bundle.getSerializable("dataList");

        Log.e("tag",dataList+"dataList");
        try {
            if (dataList != null && dataList.size() == 2) {
                Map<String, Object> map1 = dataList.get(0);
                Map<String, Object> map2 = dataList.get(1);
                AuditInfo info1 = new AuditInfo();
                List<AuditInfo> auditInfos=new ArrayList<>();

                for (Map<String,Object> map :dataList){
                    AuditInfo auditInfo=new AuditInfo();
                    auditInfo.setAuditer(EDUtil.getAttrValue(map1, "AUDIT_PEOPLE"));
                    auditInfo.setObjectid(EDUtil.getAttrValue(map1, "OBJECTID"));
                    auditInfo.setAddress(EDUtil.getAttrValue(map1, "AUDIT_COORDINATE"));
                    auditInfo.setTime(EDUtil.getAttrValue(map1, "MODIFYTIME"));
                    auditInfo.setReason(EDUtil.getAttrValue(map1, "MODIFYINFO"));
                    auditInfo.setInfo(EDUtil.getAttrValue(map1, "INFO"));
                    auditInfo.setBeforinfo(EDUtil.getAttrValue(map1, "BEFOREINFO"));
                    auditInfo.setAfterinfo(EDUtil.getAttrValue(map1, "AFTERINFO"));
                    auditInfo.setRemark(EDUtil.getAttrValue(map1, "REMARK"));
                    auditInfos.add(auditInfo);
                }
                binding.setAuditInfo(auditInfos.get(0));
                binding.setAuditInfo2(auditInfos.get(1));




                //binding.setAuditInfo(info1);
              /*  AuditInfo info2 = new AuditInfo();
                info2.setAuditer(EDUtil.getAttrValue(map2, "AUDIT_PEOPLE"));
                info2.setObjectid(EDUtil.getAttrValue(map2, "OBJECTID"));
                info2.setAddress(EDUtil.getAttrValue(map2, "AUDIT_COORDINATE"));
                info2.setTime(EDUtil.getAttrValue(map2, "MODIFYTIME"));
                info2.setReason(EDUtil.getAttrValue(map2, "MODIFYINFO"));
                info2.setInfo(EDUtil.getAttrValue(map2, "INFO"));
                info2.setBeforinfo(EDUtil.getAttrValue(map2, "BEFOREINFO"));
                info2.setAfterinfo(EDUtil.getAttrValue(map2, "AFTERINFO"));
                info2.setRemark(EDUtil.getAttrValue(map2, "REMARK"));*/
                //binding.setAuditInfo2(info2);

               /* Field[] fields = info2.getClass().getDeclaredFields();
                for(Field field : fields) {
                    field.setAccessible(true); // 这句使我们可以访问似有成员变量
                    Object property = field.get(info2);
                    Log.e("tag",property.toString());
                }*/
            }
        }catch (Exception e){
            Log.e("tag","setdata:"+e);
        }
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
