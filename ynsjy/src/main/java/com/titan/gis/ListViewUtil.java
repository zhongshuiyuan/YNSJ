package com.titan.gis;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by hanyw on 2017/10/17/017.
 * listview工具类
 */

public class ListViewUtil {
    /**
     * 计算listview高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
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
