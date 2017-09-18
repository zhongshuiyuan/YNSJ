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
    private Map<String,Object> map;
    private List<Feature> list = new ArrayList<>();
    private Map<String,Boolean> auditCheckMap;

    public AuditAdapter(Context context, Map<String, Object> map) {
        this.mContext = context;
        this.map = map;
        //getList();
    }

//    public List<String> getList(List<Feature> featureList) {
//        for (int i = 0; i < featureList.size(); i++) {
//            list.add(featureList.get(i).getAttributeValue("MODIFYTIME").toString());
//        }
//        return list;
//    }

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
            //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
            binding = DataBindingUtil
                    .inflate(LayoutInflater.from(mContext),R.layout.audit_item,parent,false);
        }else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(BR.key,array[position]);
        binding.setVariable(BR.value,map.get(array[position]));
//        binding.setKey(array[position]);
//        binding.setMap(map);
//        final TextView tv_key = ViewHolderUtil.get(convertView,R.id.tv1);
//        String key = EDUtil.getAttrValue(map,array[position]);
//        tv_key.setText(array[position]+" : "+key);
        return binding.getRoot();
    }

}
