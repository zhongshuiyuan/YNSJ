package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计历史列表适配器
 */

public class AuditHistoryExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> parentList;
    private List<List<Feature>> childList;
    private Map<String,Boolean> cbMap;
    private int type;

    public AuditHistoryExpandAdapter(Context context, List<String> parentList, int type,
                                     List<List<Feature>> childList,Map<String,Boolean> cbMap) {
        this.mContext = context;
        this.parentList = parentList;
        this.childList = childList;
        this.cbMap = cbMap;
        this.type = type;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
        }
        TextView tvSxzd = ViewHolderUtil.get(convertView, R.id.tv1);
        String strSx = parentList.get(groupPosition);
        tvSxzd.setText(strSx);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_history_all_item, parent, false);
        }
        final CheckBox cb_people = ViewHolderUtil.get(convertView, R.id.audit_item_check);
        TextView tv_value = ViewHolderUtil.get(convertView, R.id.audit_item_value);
        String id = getAttrValue("OBJECTID",groupPosition,childPosition);
        String time = getAttrValue("MODIFYTIME",groupPosition,childPosition);
        tv_value.setText(time);
//        cb_people.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //cb_people.toggle();
//
//                Log.e("tag","qwd"+cb_people.isChecked());
//            }
//        });
        cb_people.setChecked(cbMap.get(id));
//        if (type==1){
//            cb_people.setVisibility(View.VISIBLE);
//        }else {
//            cb_people.setVisibility(View.GONE);
//        }
        return convertView;
    }

    private String getAttrValue(String attr,int groupPosition,int childPosition){
        return String.valueOf(childList.get(groupPosition).get(childPosition).getAttributeValue(attr));
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
