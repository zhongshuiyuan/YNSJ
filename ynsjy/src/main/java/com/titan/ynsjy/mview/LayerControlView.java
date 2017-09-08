package com.titan.ynsjy.mview;

import android.content.SharedPreferences;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.Row;

import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/5/5
 */

public interface LayerControlView extends IBaseView{

    View getParChildView();

    ArcGISLocalTiledLayer getTitleLayer();

    //ArcGISLocalTiledLayer getDxtLayer();

    ArcGISLocalTiledLayer getImgLayer();

    void addImageLayer(String path);

    MapView getMapView();

    List<MyLayer> getLayerNameList();

    View getImgeLayerView();

    View getTckzView();

    HashMap<String, Boolean> getLayerCheckBox();

    List<Row> getSysLayerData();

    List<String> getLayerKeyList();

    SharedPreferences getSharedPreferences();
}
