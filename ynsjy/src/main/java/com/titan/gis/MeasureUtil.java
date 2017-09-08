package com.titan.gis;

import android.content.Context;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.AreaUnit;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.SpatialReference;

/**
 * 测量工具类
 */
public class MeasureUtil {
    //距离单位
    private LinearUnit linearUnit=null;
    //面积单位
    private AreaUnit areaUnit=null;
    private Context mContext;
    private MapView mMapview;
    private MeasureType measureType;

    /**
     * 测量类型
     */
    private enum MeasureType{
        //面积
        AREA,
        //长度
        LENGTH
    }

    /**
     * 绘制类型
     */
    private enum DrawType{
        //轨迹
        TRACK,
        //几何
        GEOMETRY
    }


    public MeasureUtil(Context mContext,MapView mapView) {
        this.mContext = mContext;
        this.mMapview= mapView;
    }

    /**
     * 开始测量面积
     */
    private void startMeasureArea(){
        this.measureType=MeasureType.AREA;
        this.mMapview.setOnTouchListener(new MeasureOnTouchListener(mContext,mMapview));



    }

    /**
     * 测量面积
     * @return
     * 默认单位是平方米
     */
    public static double measureArea(Geometry geometry, SpatialReference spatialReference,AreaUnit unit){
        //GeometryEngine.geodesicArea(geometry,spatialReference,unit);
        return  GeometryEngine.geodesicArea(geometry,spatialReference,unit);
    }


    private  class MeasureOnTouchListener extends MapOnTouchListener{

        public MeasureOnTouchListener(Context context, MapView view) {
            super(context, view);
        }
    }

}
