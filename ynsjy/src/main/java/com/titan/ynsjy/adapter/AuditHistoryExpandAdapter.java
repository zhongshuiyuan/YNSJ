package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 */

public class AuditHistoryExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> list;
    private Map<String, List<Feature>> map;

    public AuditHistoryExpandAdapter(Context context, List<String> list, Map<String, List<Feature>> map) {
        this.mContext = context;
        this.list = list;
        this.map = map;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return map.get(list.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return map.get(list.get(groupPosition)).get(childPosition);
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
        String strSx = list.get(groupPosition);
        tvSxzd.setText(strSx);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_history_all_item, parent, false);
        }
        TextView tv_people = ViewHolderUtil.get(convertView, R.id.audit_people);
        TextView tv_time = ViewHolderUtil.get(convertView, R.id.audit_time);
        TextView tv_latlon = ViewHolderUtil.get(convertView, R.id.audit_latlon);
        TextView tv_modify = ViewHolderUtil.get(convertView, R.id.audit_modify);
        TextView tv_before = ViewHolderUtil.get(convertView, R.id.audit_before);
        TextView tv_after = ViewHolderUtil.get(convertView, R.id.audit_after);
        TextView tv_info = ViewHolderUtil.get(convertView, R.id.audit_info);
        TextView tv_mark = ViewHolderUtil.get(convertView, R.id.audit_mark);
        //tv_people.setText(getAttrValue("",groupPosition,childPosition));
        tv_time.setText(  getAttrValue("MODIFYTIME",groupPosition,childPosition));
        //tv_latlon.setText(getAttrValue("",groupPosition,childPosition));
        tv_modify.setText(getAttrValue("MODIFYINFO",groupPosition,childPosition));
        tv_before.setText(getAttrValue("BEFOREINFO",groupPosition,childPosition));
        tv_after.setText( getAttrValue("AFTERINFO",groupPosition,childPosition));
        tv_info.setText(  getAttrValue("INFO",groupPosition,childPosition));
        tv_mark.setText(  getAttrValue("REMARK",groupPosition,childPosition));
        return convertView;
    }

    private String getAttrValue(String attr,int groupPosition,int childPosition){
        return map.get(list.get(groupPosition)).get(childPosition).getAttributeValue(attr).toString();
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
