package com.titan.ynsjy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.titan.model.TitanField;
import com.titan.ynsjy.BR;
import com.titan.ynsjy.R;
import com.titan.ynsjy.databinding.AuditItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyw on 2017/9/2/002.
 * 审计历史详细信息适配器
 */

public class AuditAdapter extends BaseAdapter {
    private Context mContext;
    //完整字段
    private List<TitanField> fieldList;
    //可见字段
    private List<TitanField> visablefields=new ArrayList<>();

    public AuditAdapter(Context context,List<TitanField> fields) {
        this.mContext = context;
        this.fieldList=fields;
        for (TitanField field:fields){
            if(field.isHasalias())
                visablefields.add(field);
        }
        Log.e("field",visablefields.toString());

    }

    @Override
    public int getCount() {
        return visablefields==null?0:visablefields.size();
    }

    @Override
    public Object getItem(int position) {
        return visablefields.get(position);
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
        binding.setVariable(BR.key,visablefields.get(position).getAlias());
        binding.setVariable(BR.value,visablefields.get(position).getValue());
        return binding.getRoot();
    }

}
