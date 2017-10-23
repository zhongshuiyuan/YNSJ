package com.titan.gis.layermanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.titan.gis.ListViewUtil;
import com.titan.model.TitanLayer;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.ViewHolderUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图层控制adapter
 */
public class LayersAdapter extends BaseExpandableListAdapter {

    private List<TitanLayer> groups;//数据集
    private Context mContext;
    private Map<String, ExpandableListView> sViewMap = new HashMap<>();//二级ExpandableListview集合
    private Map<String, LayersAdapter> eAdapterMap = new HashMap<>();//二级ExpandableListview的adapter集合
    private Map<String, Boolean> expStateMap = new HashMap<>();//expandablelistview展开状态，true表示展开
    private LayerAddOrLocation listener;//图层加载监听
    private Map<String, Boolean> checkMap = new HashMap<>();//图层加载状态，true表示已加载


    public LayersAdapter(Context context, List<TitanLayer> groups,
                         LayerAddOrLocation listener) {
        this.mContext = context;
        this.groups = groups;
        this.listener = listener;
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
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_group, null);
        }
        TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_layersname);
        ImageView iv_add = ViewHolderUtil.get(convertView, R.id.iv_add);
        final String name = groups.get(groupPosition).getName();
        if (name.endsWith(".otms") || name.endsWith(".geodatabase")) {
            tv_name.setText(name.split("\\.")[0]);
        } else {
            tv_name.setText(name);
        }
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
        final List<TitanLayer> layers = groups.get(groupPosition).getSublayers();
        final String titanLayerUrl = layers.get(childPosition).getUrl();
        final String name = layers.get(childPosition).getName();
        if (titanLayerUrl.endsWith(".otms") || titanLayerUrl.endsWith(".geodatabase")
                || titanLayerUrl.endsWith(".tpk")) {
            //加载数据库子项
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_child, parent, false);
            final CheckedTextView ctv_layername = ViewHolderUtil.get(convertView, R.id.ctv_layersname);
            ImageView iv_local = ViewHolderUtil.get(convertView, R.id.iv_local);
            ctv_layername.setText(name);
            //CheckBox的状态
            if (checkMap.containsKey(titanLayerUrl + name)) {
                ctv_layername.setChecked(checkMap.get(titanLayerUrl + name));
            }

            //图层加载
            ctv_layername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctv_layername.toggle();
                    checkMap.put(titanLayerUrl + name, ctv_layername.isChecked());
                    if (titanLayerUrl.endsWith(".tpk")) {
                        if (name.contains("image")) {
                            listener.addTiledLayer(ctv_layername.isChecked(), titanLayerUrl, 2);
                        } else if (name.contains("title")) {
                            listener.addTiledLayer(ctv_layername.isChecked(), titanLayerUrl, 1);
                        }
                    } else {
                        Geodatabase geodatabase;
                        List<GeodatabaseFeatureTable> gdbList = null;
                        try {
                            geodatabase = new Geodatabase(titanLayerUrl);
                            gdbList = geodatabase.getGeodatabaseTables();
                        } catch (Exception e) {
                            ToastUtil.setToast(mContext, "图层打开异常" + e);
                        }
                        if (gdbList != null) {
                            if (ctv_layername.isChecked()) {
                                listener.addLayer(true, gdbList.get(childPosition));
                            } else {
                                listener.addLayer(false, gdbList.get(childPosition));
                            }
                        } else {
                            ToastUtil.setToast(mContext, "数据为空");
                        }
                    }
                }
            });
            //图层定位
            iv_local.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctv_layername.isChecked()) {
                        if (titanLayerUrl.endsWith(".tpk")) {
                            listener.locationTiledLater(titanLayerUrl);
                        } else {
                            Geodatabase geodatabase;
                            List<GeodatabaseFeatureTable> gdbList = null;
                            try {
                                geodatabase = new Geodatabase(titanLayerUrl);
                                gdbList = geodatabase.getGeodatabaseTables();
                            } catch (Exception e) {
                                ToastUtil.setToast(mContext, "图层打开异常" + e);
                            }
                            listener.location(gdbList.get(childPosition));
                        }
                    }
                }
            });
        } else {
            //加载文件夹子项
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_group, parent, false);
            TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_layersname);
            ImageView iv_add = ViewHolderUtil.get(convertView, R.id.iv_add);
            final LinearLayout group = (LinearLayout) convertView.findViewById(R.id.item_group);
            tv_name.setText(name);
            //保证listview的布局状态
            if (sViewMap.get(titanLayerUrl) != null) {
                ExpandableListView listView = sViewMap.get(titanLayerUrl);
                ViewGroup viewGroup = (ViewGroup) listView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(listView);
                }
                group.addView(listView);
            }
            //加载子级列表
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expStateMap.get(titanLayerUrl) != null && expStateMap.get(titanLayerUrl)) {
                        group.removeView(sViewMap.get(titanLayerUrl));
                        sViewMap.remove(titanLayerUrl);
                        expStateMap.put(titanLayerUrl, false);
                    } else {
                        LayersAdapter adapter;
                        final ExpandableListView listView;
                        //获取Expandablelistview
                        if (sViewMap.get(titanLayerUrl) != null) {
                            listView = sViewMap.get(titanLayerUrl);
                            listView.setVisibility(View.VISIBLE);
                        } else {
                            listView = new ExpandableListView(mContext);
                            listView.setLayoutParams(
                                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT));
                        }
                        //适配器
                        if (eAdapterMap.get(titanLayerUrl) != null) {
                            adapter = eAdapterMap.get(titanLayerUrl);
                        } else {
                            adapter = new LayersAdapter(mContext,
                                    layers.get(childPosition).getSublayers(), listener);
                            eAdapterMap.put(titanLayerUrl, adapter);
                        }
                        listView.setGroupIndicator(null);
                        //展开
                        groupExpandListener(listView);
                        //收起
                        collapseListener(listView);
                        //adapter必须在listview的高度计算之后设置，否则子项会显示不全
                        listView.setAdapter(adapter);
                        //计算最外层listview高度
                        ListViewUtil.setListViewHeightBasedOnChildren(listView);
                        //添加控件之前确保控件没有父布局
                        if (listView.getParent() != null) {
                            ViewGroup viewGroup = (ViewGroup) listView.getParent();
                            viewGroup.removeView(listView);
                        }
                        //必须在adapter设置后添加控件，否则显示不出
                        group.addView(listView);
                        sViewMap.put(titanLayerUrl, listView);
                        expStateMap.put(titanLayerUrl, true);
                    }
                }
            });
        }
        return convertView;
    }

    //收起子级时计算高度
    private void collapseListener(final ExpandableListView listView) {
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                ListViewUtil.setListViewHeightBasedOnChildren(listView);
            }
        });
    }

    //展开子级时计算高度
    private void groupExpandListener(final ExpandableListView listView) {
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                ListViewUtil.setListViewHeightBasedOnChildren(listView);
            }
        });
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

    /**
     * 加载图层接口
     */
    public interface LayerAddOrLocation {
        //矢量图层添加
        void addLayer(boolean isAdd, GeodatabaseFeatureTable geotable);

        //矢量图定位
        void location(GeodatabaseFeatureTable geotable);

        //影像图、基础图添加,1:基础图，2:影像图
        void addTiledLayer(boolean isAdd, String path, int type);

        //影像图、基础图定位
        void locationTiledLater(String path);
    }
}
