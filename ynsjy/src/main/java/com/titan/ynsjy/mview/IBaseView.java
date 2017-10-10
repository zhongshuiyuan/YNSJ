package com.titan.ynsjy.mview;

import android.view.View;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

/**
 * Created by li on 2017/5/9.
 * base基类接口
 */

public interface IBaseView {

    Callout getCallout();

    //double getCurrentLon();

   // double getCurrenLat();

    Point getCurrentPoint();

    GraphicsLayer getGraphicLayer();

    void removeGraphicLayer();

    void addGraphicLayer();

    SpatialReference getSpatialReference();

    View getGpsCaijiInclude();

    MapView getMapView();

    Envelope getCurrentEnvelope();
    //开启审计
    void startAddAudit();
    //添加图形到绘制层
    //void addGraphic(Graphic graphic);
}
