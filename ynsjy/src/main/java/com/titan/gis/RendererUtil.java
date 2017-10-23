package com.titan.gis;

import android.graphics.Color;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.titan.ynsjy.mview.LayerControlView;
import com.titan.ynsjy.util.Util;

/**
 * Created by whs on 2017/9/11
 * 渲染器工具类
 * 渲染器基类 BaseRenderer
 * 分级渲染器 ClassBreaksRenderer
 * 字典渲染器 DictionaryRenderer
 * 简单渲染器 SimpleRenderer
 * 唯一值渲染器 UniqueValueRenderer
 */

public class RendererUtil {
    /**
     * 简单渲染
     * @param symbol
     * @return
     */
    public static SimpleRenderer getSimpleRenderer(Symbol symbol){
        return new SimpleRenderer(symbol);

    }

    /**获取历史Renderer*/
    public static Renderer getHisSymbol(LayerControlView controlView,FeatureLayer layer){

        int txt = controlView.getSharedPreferences().getInt(layer.getName()+"tmd", 50);
        //SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(sharedPreferences.getInt("color", Color.GREEN));
        int tcs = controlView.getSharedPreferences().getInt(layer.getName()+"tianchongse", Color.GREEN);
        int tcolor = Util.getColor(tcs, txt);//3158064  959459376
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(tcolor);
        Object obj = controlView.getSharedPreferences().getFloat(layer.getName()+"owidth", 4);
        float owidth = 0;
        if(obj != null){
            String str = obj.toString();
            boolean flag = Util.CheckStrIsDouble(str);
            if(flag){
                owidth = Float.parseFloat(str);
            }
        }
        int bjs = controlView.getSharedPreferences().getInt(layer.getName()+"bianjiese", Color.RED);
        simpleFillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));

        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(tcolor, (int) owidth, SimpleMarkerSymbol.STYLE.CIRCLE);
        markerSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));
        markerSymbol.setColor(bjs);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(tcolor, (int) owidth, SimpleLineSymbol.STYLE.SOLID);
        lineSymbol.setWidth(owidth);
        lineSymbol.setColor(tcolor);

        Renderer renderer = null;
        if(layer.getGeometryType().equals(Geometry.Type.POLYGON)){
            renderer = new SimpleRenderer(simpleFillSymbol);
        }else if(layer.getGeometryType().equals(Geometry.Type.POLYLINE)){
            renderer = new SimpleRenderer(lineSymbol);
        }else{
            renderer = new SimpleRenderer(markerSymbol);
        }

        return renderer;
    }
}
