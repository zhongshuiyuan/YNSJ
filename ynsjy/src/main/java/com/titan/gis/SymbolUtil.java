package com.titan.gis;

import android.content.Context;
import android.graphics.Color;

import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.titan.ynsjy.R;

/**
 * Created by whs on 2017/9/8
 * 样式类
 */

public class SymbolUtil {
    //测量面积样式
    public static SimpleFillSymbol areaSymbol=new SimpleFillSymbol(Color.BLUE);
    //一般填充样式
    public static FillSymbol fillSymbol=new SimpleFillSymbol(Color.GREEN).setAlpha(80);

    /**
     * 获取兴趣点图标
     * @param context
     * @return
     */
    public static PictureMarkerSymbol getPoiSymbol(Context context){
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.icon_gcoding));
        pictureMarkerSymbol.setOffsetY(20);
        return pictureMarkerSymbol;
    }




}
