package com.titan.gis.layermanager;


/**
 * Created by whs on 2017/5/11
 * 图层控制接口
 */

public interface LayerManager {
    /**
     * 关闭对话框
     */
    void close();

    /**
     * 刷新adapter
     */
    void adapterRefresh();

    /**
     * 底图选择
     * @param position
     */
    //void showBaseMap(int position);

    /**
     * 图层选择
     * @param index
     * @param isvisable
     */
    //void onCheckLayer(int index, boolean isvisable);

}
