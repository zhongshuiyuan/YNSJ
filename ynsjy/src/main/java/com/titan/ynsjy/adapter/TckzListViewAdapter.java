package com.titan.ynsjy.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.titan.model.TitanLayer;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/5/005.
 * 图层控制--三级
 */

public class TckzListViewAdapter extends BaseExpandableListAdapter {
    private List<File> pList;//一级内容
    private addLayerInMapview listener;
    private BaseActivity activity;
    private List<TitanLayer> list;//图层数据
    private Map<String,Boolean> cCheckBoxMap = new HashMap<>();//二级菜单checkbox状态集合
    private Map<String, MyBaseAdapter> adapterMap = new HashMap<>();//第三级listView适配器集合
    private Map<String, Boolean> hideMap = new HashMap<>();//第三级listView展开状态集合
    private List<Map<String, List<File>>> childs;//一二级所有内容
    private Map<String,ListView> lvMap = new HashMap<>();//第三级listview集合

    public TckzListViewAdapter(BaseActivity activity, List<File> pList, List<Map<String, List<File>>> childs, addLayerInMapview listener) {
        this.activity = activity;
        this.pList = pList;
        this.childs = childs;
        this.listener = listener;
    }

    public TckzListViewAdapter(BaseActivity activity, List<TitanLayer> list, addLayerInMapview listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getGroupCount() {
//        return pList.size();
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return childs.get(groupPosition).get(pList.get(groupPosition).getName()).size();
        return list.get(groupPosition).getSublayers().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
//        return pList.get(groupPosition);
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
//        return childs.get(groupPosition).get(pList.get(groupPosition).getName()).get(childPosition);
        return list.get(groupPosition).getSublayers().get(childPosition);
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
//        String strSx = pList.get(groupPosition).getName();
        String strSx = list.get(groupPosition).getName();
        tvSxzd.setText(strSx);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tckz_child_item, parent, false);
        }
        final CheckBox checkBox = ViewHolderUtil.get(convertView,R.id.tckz_child_cb);
        //final ListView listView = ViewHolderUtil.get(convertView, R.id.tckz_child_list);
        TextView textView = ViewHolderUtil.get(convertView, R.id.tckz_child_text);
        final ImageView imageView = ViewHolderUtil.get(convertView, R.id.tckz_child_open);
        LinearLayout layout = ViewHolderUtil.get(convertView, R.id.tckz_child_linear);
//        final String gname = pList.get(groupPosition).getName();
//        final String cname = childs.get(groupPosition).get(gname).get(childPosition).getName();
        final String gname = list.get(groupPosition).getName();
        //二级对象
        final TitanLayer cTitanLayer = list.get(groupPosition).getSublayers().get(childPosition);
        final String cname = cTitanLayer.getName();
        final String path = cTitanLayer.getUrl();
        final String tableName = cTitanLayer.getSublayers().get(0).getName();
        textView.setText(cname.split("\\.")[0]);

        final LinearLayout itemLayout = (LinearLayout) convertView.findViewById(R.id.tckz_child_item);
//        if (cTitanLayer.getType()==1){
//
//        }
//        if (!cTitanLayer.isHasSubLayer()){
//            checkBox.setVisibility(View.VISIBLE);
//            imageView.setImageResource(R.drawable.wb_feed_icon_gps);
//        }else {
            //设置列表展开状态和右侧按钮图标
            checkBox.setVisibility(View.GONE);
//        if (listView!=null){
////            itemLayout.addView(listView);
//            if (hideMap.get(gname + cname) != null && hideMap.get(gname + cname)) {
//                listView.setVisibility(View.VISIBLE);
//                imageView.setImageResource(R.drawable.gird_item_expand);
//            } else {
//                listView.setVisibility(View.GONE);
//                imageView.setImageResource(R.drawable.gird_item_collapse);
//            }
//        }

//        }
//        //checkbox状态初始化
//        if (!cCheckBoxMap.containsKey(gname + cname)) {
//            cCheckBoxMap.put(gname + cname, false);
//        }
//        checkBox.setChecked(cCheckBoxMap.get(gname + cname));
        //防第三级目录内容混乱
//        if (adapterMap.get(gname + cname) != null) {
//            listView.setAdapter(adapterMap.get(gname + cname));
//            ListViewParamsUtils.setListViewHeightBasedOnChildren(listView);
//        }

        //图层定位
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationLayer(gname,cname,tableName);
//            }
//        });


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listView;
                if (lvMap.get(gname+cname)==null){
                    listView = new ListView(activity);
                    listView.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    listView.setVisibility(View.GONE);
                    lvMap.put(gname+cname,listView);
                }else {
                    listView = lvMap.get(gname+cname);
                }
//                listView.setVisibility(View.GONE);
//                ListViewParamsUtils.setListViewHeightBasedOnChildren(listView);
//                TitanLayer titanLayer = list.get(groupPosition).getSublayers().get(childPosition);
                List<TitanLayer> tList = cTitanLayer.getSublayers();
//                if (cTitanLayer.isHasSubLayer()){
                    MyBaseAdapter adapter;
                    //初始化第三级listView的适配器
                    if (adapterMap.get(gname + cname) != null) {
                        adapter = adapterMap.get(gname + cname);
                    } else {
//                    adapter = new MyBaseAdapter(childs.get(groupPosition).get(gname).get(childPosition), listener, gname, cname);
                        adapter = new MyBaseAdapter(listener,gname,cname,tList);
                        adapterMap.put(gname + cname, adapter);
                    }
                    //展开第三级目录
                    if (listView.getVisibility() == View.GONE) {
                        imageView.setImageResource(R.drawable.gird_item_expand);
                        listView.setAdapter(adapter);
                        ListViewParamsUtils.setListViewHeightBasedOnChildren(listView);
                        itemLayout.addView(listView);
                        listView.setVisibility(View.VISIBLE);
                        hideMap.put(gname + cname, true);
                    } else {
                        imageView.setImageResource(R.drawable.gird_item_collapse);
                        listView.setVisibility(View.GONE);
                        itemLayout.removeView(listView);
                        hideMap.put(gname + cname, false);
                    }
//                }else {
//                    //图层加载
//                    if (cCheckBoxMap.get(gname + cname)) {
//                        listener.addLayer(false, path, gname, cname, tableName);
//                        checkBox.setChecked(false);
//                        cCheckBoxMap.put(gname + cname, false);
//                    } else {
//                        listener.addLayer(true, path, gname, cname, tableName);
//                        cCheckBoxMap.put(gname + cname, true);
//                        checkBox.setChecked(true);
//                    }
//                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 第三级适配器
     */
    private class MyBaseAdapter extends BaseAdapter {
        private File file;
        private addLayerInMapview listener;
        private List<GeodatabaseFeatureTable> tableList;
        private String gname;
        private String cname;
        private Map<String, Boolean> checkBoxMap = new HashMap<>();
        private List<TitanLayer> titanLayerList;

        private MyBaseAdapter(File file, addLayerInMapview listener, String gname, String cname) {
            this.file = file;
            this.listener = listener;
            this.gname = gname;
            this.cname = cname;
            getGDBData();
        }

        private MyBaseAdapter(addLayerInMapview listener, String gname, String cname, List<TitanLayer> list) {
            this.listener = listener;
            this.gname = gname;
            this.cname = cname;
            this.titanLayerList = list;
        }

        private void getGDBData() {
            try {
                Geodatabase geodatabase = new Geodatabase(file.getAbsolutePath());
                tableList = geodatabase.getGeodatabaseTables();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
//            return tableList.size();
            return titanLayerList.size();
        }

        @Override
        public Object getItem(int position) {
//            return tableList.get(position);
            return titanLayerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expandable_child, parent, false);
            }
            TextView textView = ViewHolderUtil.get(convertView, R.id.id_child_txt);
            final CheckBox checkBox = ViewHolderUtil.get(convertView, R.id.cb_child);
            ImageView img = ViewHolderUtil.get(convertView, R.id.featurelayer_extent);
//            final String tableName = tableList.get(position).getTableName();
//            final String path = file.getAbsolutePath();
            final String tableName = titanLayerList.get(position).getName();
            final String path = titanLayerList.get(position).getUrl();
            textView.setText(tableName);
            //checkbox状态初始化
            if (!checkBoxMap.containsKey(path + tableName)) {
                checkBoxMap.put(path + tableName, false);
            }
            checkBox.setChecked(checkBoxMap.get(path + tableName));

            List<GeodatabaseFeatureTable> geodatabaseTables = null;
            try {
                Geodatabase geodatabase = new Geodatabase(path);
                geodatabaseTables = geodatabase.getGeodatabaseTables();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //图层加载
            final List<GeodatabaseFeatureTable> finalGeodatabaseTables = geodatabaseTables;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxMap.get(path + tableName)) {
//                        listener.addLayer(false, path, gname, cname, tableName);
                        listener.addLayer(false, finalGeodatabaseTables.get(position));
                        checkBox.setChecked(false);
                        checkBoxMap.put(path + tableName, false);
                    } else {
//                        listener.addLayer(true, path, gname, cname, tableName);
                        listener.addLayer(true, finalGeodatabaseTables.get(position));
                        checkBoxMap.put(path + tableName, true);
                        checkBox.setChecked(true);
                    }
                }
            });

            //图层定位
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationLayer(gname,cname,tableName);
                }
            });
            return convertView;
        }
    }

    /**
     * 图层定位
     * @param gname
     * @param cname
     * @param tableName
     */
    private void locationLayer(String gname,String cname,String tableName) {
        for (int i = 0; i < BaseActivity.layerNameList.size(); i++) {
            String name = BaseActivity.layerNameList.get(i).getPname();
            String name1 = BaseActivity.layerNameList.get(i).getCname();
            String name2 = BaseActivity.layerNameList.get(i).getTname();
            if (name.equals(gname) && cname.contains(name1) && name2.equals(tableName)) {
                final Geometry geometry = BaseActivity.layerNameList.get(i).getLayer().getFullExtent();
                activity.ZoomToGeom(geometry);
                break;
            }
        }
    }
    /**
     * 加载图层接口
     */
    public interface addLayerInMapview {
        void addLayer(boolean flag, String path, String gname, String cname, String tableName);
        void addLayer(boolean isadd,GeodatabaseFeatureTable geotable);
    }

    /**
     * listview高度计算
     */
    private static class ListViewParamsUtils {
        static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            //初始化高度
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                //计算子项View的宽高，注意listview所在的要是linearlayout布局
                listItem.measure(0, 0);
                //统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            /*
         * listView.getDividerHeight()获取子项间分隔符占用的高度，有多少项就乘以多少个,
         * params.height最后得到整个ListView完整显示需要的高度
         * 最后将params.height设置为listview的高度
         */
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
            listView.setLayoutParams(params);
        }
    }
}
