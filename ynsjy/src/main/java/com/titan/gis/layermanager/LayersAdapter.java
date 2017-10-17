package com.titan.gis.layermanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.titan.model.TitanLayer;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayersAdapter extends BaseExpandableListAdapter {

    private List<TitanLayer> groups;
    private Context mContext;
    private TitanLayer childs;
    private Map<String, ExpandableListView> sViewMap = new HashMap<>();//二级ExpandableListview集合
    private Map<String,LayersAdapter> adapterMap = new HashMap<>();//二级ExpandableListview的adapter集合



    /*private OnItemClickListener mItemClickListener;

    public  interface  OnItemClickListener{
        void  onGroupChecked();
        void  onChildChecked(boolean isadd, GeodatabaseFeatureTable geotable);

    }
    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
*/


    public LayersAdapter(Context context, List<TitanLayer> groups) {
        this.mContext = context;
        this.groups = groups;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder groupHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_group, null);
            groupHolder = new GroupViewHolder();

            groupHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_layersname);
            groupHolder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupViewHolder) convertView.getTag();
        }

        groupHolder.tv_name.setText(groups.get(groupPosition).getName());
        return convertView;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getSublayers().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

//        ChildViewHolder viewHolder;
//        if (null == convertView) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_child, null);
//            viewHolder = new ChildViewHolder();
//            Log.e("child","id:"+groupPosition+"id:"+childPosition);
//            viewHolder.ctv_layername = (CheckedTextView) convertView.findViewById(R.id.ctv_layersname);
//            viewHolder.iv_local = (ImageView) convertView.findViewById(R.id.iv_local);
//            viewHolder.ctv_layername.setText(groups.get(groupPosition).getSublayers().get(childPosition).getName());
//            //convertView.setTag(viewHolder);
//
//        }
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_child, parent, false);
        CheckedTextView ctv_layername = ViewHolderUtil.get(convertView, R.id.ctv_layersname);
        ImageView iv_local = ViewHolderUtil.get(convertView, R.id.iv_local);
        final List<TitanLayer> layers = groups.get(groupPosition).getSublayers();
        final String name = layers.get(childPosition).getName();
        ctv_layername.setText(name);
        final LinearLayout pLayout = (LinearLayout) convertView.findViewById(R.id.item_layer_child);
        final LinearLayout item_Layout = (LinearLayout) convertView.findViewById(R.id.item_layer_item);

        ctv_layername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayersAdapter adapter;
                final ExpandableListView listView;
                //获取Expandablelistview
                if (sViewMap.get(name)!=null){
                    listView = sViewMap.get(name);
                    listView.setVisibility(View.VISIBLE);
                }else {
                    listView = new ExpandableListView(mContext);
                    listView.setLayoutParams(
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    sViewMap.put(name, listView);
                    pLayout.addView(listView);
                }
                //适配器
                if (adapterMap.get(name)!=null){
                    adapter = adapterMap.get(name);
                }else {
                    adapter = new LayersAdapter(mContext, layers);
                    adapterMap.put(name,adapter);
                }
                listView.setGroupIndicator(null);
                //展开
                listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        ListViewParamsUtils.setListViewHeightBasedOnChildren(listView);
                    }
                });
                //收起
                listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        item_Layout.setVisibility(View.VISIBLE);
//                        listView.setVisibility(View.GONE);
                        ListViewParamsUtils.setListViewHeightBasedOnChildren(listView);
                    }
                });
                listView.setAdapter(adapter);
                listView.expandGroup(0);
                item_Layout.setVisibility(View.GONE);
            }
        });
        return convertView;

    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getSublayers().size();
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private class ChildViewHolder {
        CheckedTextView ctv_layername;
        ImageView iv_local;
    }

    private class GroupViewHolder {
        TextView tv_name;
        ImageView iv_add;
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
