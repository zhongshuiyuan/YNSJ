package com.titan.ynsjy.mview;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.titan.ynsjy.drawTool.DrawTool;
import com.titan.ynsjy.entity.MyLayer;

import java.util.List;

/**
 * Created by li on 2017/6/1.
 * 加载图层接口
 */

public interface ILayerView extends IBaseView {

    //获取电子底图
    ArcGISLocalTiledLayer getBaseTitleLayer();
    //获取影像底图
    ArcGISLocalTiledLayer getImgTitleLayer();
    //获取地形图底图
    ArcGISLocalTiledLayer getDxtTitleLayer();
    //获取加载的小班图层列表
    List<MyLayer> getLayerList();
    //获取DrawTool
    DrawTool getDrawTool();


}
