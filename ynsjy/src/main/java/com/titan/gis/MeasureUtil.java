package com.titan.gis;

import android.content.Context;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

/**
 * 测量工具类
 */
public class MeasureUtil {
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
     * 测量面积
     */
    private void startMeasureArea(){
        this.measureType=MeasureType.AREA;
        this.mMapview.setOnTouchListener(new MeasureOnTouchListener(mContext,mMapview));



    }


    private  class MeasureOnTouchListener extends MapOnTouchListener{

        public MeasureOnTouchListener(Context context, MapView view) {
            super(context, view);
        }
    }

}
