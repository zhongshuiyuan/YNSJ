package com.titan.gis;

import android.graphics.Bitmap;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

/**
 * Created by whs on 2017/9/11
 * GIS公用方法
 */

public class GisUtil {

    /**
     * 获取几何面截图
     * @param geometry
     * @param mapView
     * @return
     */
    public static Bitmap getDrawingMapCache(Geometry geometry, MapView mapView) {
        //获取影像缩略图
        //Bitmap bitmap=imgTiledLayer.getThumbnail();
        Envelope env=new Envelope();
        geometry.queryEnvelope(env);
        Point pt_ul=mapView.toScreenPoint(env.getUpperLeft());
        Point pt_lr=mapView.toScreenPoint(env.getLowerRight());
        int width= (int) (pt_lr.getX()-pt_ul.getX());
        int height= (int) (pt_lr.getY()-pt_ul.getY());
        Bitmap bitmap =mapView.getDrawingMapCache((float) pt_ul.getX(),(float) pt_ul.getY(),width,height);
        return bitmap;
    }
}
