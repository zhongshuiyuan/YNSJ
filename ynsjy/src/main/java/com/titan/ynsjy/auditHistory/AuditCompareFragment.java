package com.titan.ynsjy.auditHistory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.titan.ynsjy.adapter.AuditAdapter;
import com.titan.ynsjy.databinding.FragAuditCompareBinding;
import com.titan.ynsjy.dialog.AuditInfoEditDialog;
import com.titan.ynsjy.util.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细内容对比页面
 */

public class AuditCompareFragment extends Fragment implements AuditCompare{
    private Context mContext;
    private String[] attrArray = new String[]{"OBJECTID","AUDIT_PEOPLE","MODIFYTIME","AUDIT_COORDINATE",
            "MODIFYINFO","INFO","BEFOREINFO","AFTERINFO","REMARK"};
    private List<Map<String, Object>> dataList;

    public static AuditCompareFragment singleton;
    private FragAuditCompareBinding binding;
    private AuditInfoEditDialog dialog;

    private AuditViewModel auditViewModel;
    public static AuditCompareFragment newInstance() {
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
        //view = inflater.inflate(R.layout.audit_history_info, container, false);
        //unbinder = ButterKnife.bind(this, view);
        //return view;
    }

    public void setViewModel(AuditViewModel viewModel) {
        auditViewModel = viewModel;
    }

    public void setData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        dataList = (List<Map<String, Object>>) bundle.getSerializable("dataList");
        Log.e("tag",dataList+"dataList");
        try {
            if (dataList != null && dataList.size() == 2) {
                Log.e("tag","data");
//                for (int i = 0; i < dataList.size(); i++) {
//
//                }
                Map<String, Object> map1 = dataList.get(0);
                Map<String, Object> map2 = dataList.get(1);
//                AuditInfo info1 = new AuditInfo();
//                info1.setAuditer(EDUtil.getAttrValue(map1, "AUDIT_PEOPLE"));
//                info1.setObjectid(EDUtil.getAttrValue(map1, "OBJECTID"));
//                info1.setAddress(EDUtil.getAttrValue(map1, "AUDIT_COORDINATE"));
//                info1.setTime(EDUtil.getAttrValue(map1, "MODIFYTIME"));
//                info1.setReason(EDUtil.getAttrValue(map1, "MODIFYINFO"));
//                info1.setInfo(EDUtil.getAttrValue(map1, "INFO"));
//                info1.setBeforinfo(EDUtil.getAttrValue(map1, "BEFOREINFO"));
//                info1.setAfterinfo(EDUtil.getAttrValue(map1, "AFTERINFO"));
//                info1.setRemark(EDUtil.getAttrValue(map1, "REMARK"));
//                Log.e("tag","mcontext:"+mContext);
                ListView listView1 = binding.auditHistoryFirst.auditInfoList;
                AuditAdapter adapter1 = new AuditAdapter(mContext,map1,auditViewModel);
                listView1.setVisibility(View.VISIBLE);
                listView1.setAdapter(adapter1);
                ListView listView2 = binding.auditHistorySecond.auditInfoList;
                AuditAdapter adapter2 = new AuditAdapter(mContext,map2,auditViewModel);
                listView2.setVisibility(View.VISIBLE);
                listView2.setAdapter(adapter2);

//                Field[] fields = info2.getClass().getDeclaredFields();
//                for(Field field : fields) {
//                    field.setAccessible(true); // 这句使我们可以访问似有成员变量
//                    Object property = field.get(info2);
//                    Log.e("tag",property.toString());
//                }
            }
        }catch (Exception e){
            Log.e("tag","setdata:"+e);
        }
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void sure() {
        ToastUtil.setToast(mContext,"compare");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void showEditDialog(String alias, String value) {
        dialog = AuditInfoEditDialog.newInstance(alias,value);
        dialog.show(getFragmentManager(),"editDialog");
    }

    @Override
    public void closeDialog() {
        dialog.dismiss();
    }
}
