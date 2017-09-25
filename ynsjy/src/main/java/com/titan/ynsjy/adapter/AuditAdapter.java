package com.titan.ynsjy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.BR;
import com.titan.ynsjy.R;
import com.titan.ynsjy.auditHistory.AuditViewModel;
import com.titan.ynsjy.databinding.AuditItemBinding;
import com.titan.ynsjy.util.EDUtil;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/2/002.
 * 审计历史详细信息适配器
 */

public class AuditAdapter extends BaseAdapter {
    private Context mContext;
    private String[] array = new String[]{"AUDIT_PEOPLE","MODIFYTIME","AUDIT_COORDINATE",
            "MODIFYINFO","INFO","BEFOREINFO","AFTERINFO","REMARK"};
    private String[] aliasArray = new String[]{"审计人员","审计时间","审计地址",
            "修改原因","描述信息","修改之前","修改之后","备注"};
    private Map<String,Object> map;
    private AuditViewModel auditViewModel;

    public AuditAdapter(Context context, Map<String, Object> map,AuditViewModel auditViewModel) {
        this.mContext = context;
        this.map = map;
        this.auditViewModel=auditViewModel;
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return array[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AuditItemBinding binding;
        if (convertView==null){
            binding = DataBindingUtil
                    .inflate(LayoutInflater.from(mContext),R.layout.audit_item,parent,false);
        }else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(BR.key,aliasArray[position]);
        binding.setVariable(BR.value,map.get(array[position]));
        binding.setVariable(BR.name,array[position]);
        binding.setViewmodel(auditViewModel);
        return binding.getRoot();
    }

}
