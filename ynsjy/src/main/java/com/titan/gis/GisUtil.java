package com.titan.gis;

import android.graphics.Bitmap;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Field;
import com.titan.model.TitanField;
import com.titan.ynsjy.util.ReUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by whs on 2017/9/11
 * GIS公用方法
 */

public class GisUtil {
    /**
     * 遍历获取属性信息
     * @param attmap
     * @param fields
     * @return
     */
    public  static List<TitanField> getFields(Map attmap, List<Field> fields){
        List<TitanField> fieldList=new ArrayList<>();
        for (Field field : fields) {
            TitanField titanField=new TitanField();
            String alias = field.getAlias();
            //获取包含中文别名的字段
            if (!ReUtil.hasChinese(alias)) {
                titanField.setHasalias(false);
            } else {
                titanField.setHasalias(true);
            }
            String value="";
            if(attmap==null&&attmap.get(field.getName())!=null){
                value = attmap.get(field.getName()).toString();
            }
            titanField.setName(field.getName());
            titanField.setAlias(alias);
            titanField.setValue(value);
            fieldList.add(titanField);
        }
        return fieldList;

    }



    /**
     * 添加图层返回图层索引
     * @param mapView
     * @param layer
     * @return
     */
    public static int addLayer(MapView mapView, Layer layer){
       return mapView.addLayer(layer);
    }
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
