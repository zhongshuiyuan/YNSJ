package com.titan.ynsjy.mview;

import android.content.Context;
import android.view.View;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.titan.ynsjy.BaseActivity;

/**
 * Created by li on 2017/2/23.
 * 应用接口
 */

public interface IDialogDao {
    /*坐标定位*/
    void showZbdwView(Context context, double lon, double lat, MapView mapView, GraphicsLayer layer);
    /*轨迹查询*/
    void showGjcxView(BaseActivity context, View view, MapView mapView, GraphicsLayer layer);
}
