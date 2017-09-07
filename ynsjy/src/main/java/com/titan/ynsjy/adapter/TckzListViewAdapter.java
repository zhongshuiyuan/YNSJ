package com.titan.ynsjy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/5/005.
 */

public class TckzListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<File> pList;
    private List<String> cList;
    private Map<String,List<String>> map = new HashMap<>();
    private Map<String,Map<String,List<String>>> mapMap;
    public TckzListViewAdapter(Context context, List<File> pList){
        this.mContext = context;
        this.pList = pList;
        this.cList = getcList();
        this.map = getMap();
        this.mapMap = getMapMap();
    }

    private List<String> getcList(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("qaz"+i);
        }
        return list;
    }
    private Map<String,List<String>> getMap(){
        Map<String,List<String>> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put("zxc"+i,cList);
        }
        return map;
    }

    private Map<String,Map<String,List<String>>> getMapMap(){
        Map<String,Map<String,List<String>>> mapMap = new HashMap<>();
        for (File f :pList) {
            mapMap.put(f.getName(),map);
        }
        return mapMap;
    }

    @Override
    public int getGroupCount() {
        return pList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.e("tag","1");
        return map.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.e("tag","2");
        return mapMap.get(pList.get(groupPosition).getName());
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log.e("tag","3");
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
        String strSx = pList.get(groupPosition).getName();
        tvSxzd.setText(strSx);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.e("tag","4");
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tckz_child_item, parent, false);
        }
        ExpandableListView ex_listView = ViewHolderUtil.get(convertView, R.id.tckz_expandlist);
        ChildExpandAdapter adapter = new ChildExpandAdapter(mContext,cList,map);
        ex_listView.setAdapter(adapter);
        ex_listView.setMinimumHeight(180);
        //ListViewParamsUtils.setListViewHeightBasedOnChildren(ex_listView,groupPosition,childPosition,isLastChild,convertView,parent);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ChildExpandAdapter extends BaseExpandableListAdapter{
        private Context mContext;
        private List<String> cPList;
        private Map<String,List<String>> map;
        public ChildExpandAdapter(Context context,List<String> cPList,Map<String,List<String>> map){
            this.mContext = context;
            this.map = map;
            this.cPList = getcPList();
        }
        private List<String> getcPList(){
            List<String> list = new ArrayList<>();
            for (String s: map.keySet()) {
                list.add(s);
            }
            return list;
        }
        @Override
        public int getGroupCount() {
            return cPList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return map.get(cPList.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return cPList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return map.get(cPList.get(groupPosition)).get(childPosition);
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
            String strSx = cPList.get(groupPosition);
            tvSxzd.setText(strSx);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expandable_child, parent, false);
            }
            TextView tv_child = ViewHolderUtil.get(convertView, R.id.id_child_txt);
            CheckBox checkBox = ViewHolderUtil.get(convertView, R.id.cb_child);
            ImageView img = ViewHolderUtil.get(convertView, R.id.featurelayer_extent);
            Log.e("tag",": "+cPList+","+map);
            tv_child.setText(map.get(cPList.get(groupPosition)).get(childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    /*listview高度计算*/
    private static class ListViewParamsUtils {
        static void setListViewHeightBasedOnChildren(ExpandableListView listView,int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            //ListAdapter listAdapter = listView.getAdapter();
            ExpandableListAdapter adapter = listView.getExpandableListAdapter();
            if (adapter == null) {
                return;
            }
            //初始化高度
            int totalHeight = 0;
            for (int i = 0; i < adapter.getChildrenCount(groupPosition); i++) {
                View listItem = adapter.getChildView(groupPosition,i,isLastChild,convertView,parent);
                //计算子项View的宽高，注意listview所在的要是linearlayout布局
                listItem.measure(0, 0);
                //统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            /*
         * listView.getDividerHeight()获取子项间分隔符占用的高度，有多少项就乘以多少个,
         * 5是在listview中填充的子view的padding值
         * params.height最后得到整个ListView完整显示需要的高度
         * 最后将params.height设置为listview的高度
         */
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getChildrenCount(groupPosition))) + 5;
            listView.setLayoutParams(params);
        }
    }
}
