package com.titan.gis;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2017/9/19
 * 几何编辑工具类
 */

public class EditTools {
    //返回错误信息
    public  static String msg="";
    /**
     * 分割操作
     *
     * @param gonGeo      传入需要分割的面
     * @param lineGeo     传入画出的线
     * @param gonGraphics 存有需要分割的面的集合  用于返回分割完后的面集
     */
    private List<Geometry> segmentation(Geometry gonGeo, Geometry lineGeo, List<Geometry> gonGraphics, SpatialReference sp) {
        // 第一次相交操作 取得相交之后的线段
        Geometry intersect = GeometryEngine.intersect(gonGeo, lineGeo, sp);
        MultiPath intersectMulti = (MultiPath) intersect;
        // 线的路径
        for (int i = 0; i < intersectMulti.getPathCount(); i++) {
            int pathStart = intersectMulti.getPathStart(i);
            int pathEnd = intersectMulti.getPathEnd(i);
            Polyline polyline = new Polyline();
            // 完成一个路径的遍历
            for (int j = pathStart; j < pathEnd - 1; j++) {
                Line line = new Line();
                line.setStart(intersectMulti.getPoint(j));
                line.setEnd(intersectMulti.getPoint(j + 1));
                polyline.addSegment(line, false);
            }
            // 拿路径去和面集合遍历
            List<Integer> indexList = new ArrayList<Integer>();
            List<Geometry> segmentationList = new ArrayList<Geometry>();
            for (int j = 0; j < gonGraphics.size(); j++) {
                if (GeometryEngine.intersects(gonGraphics.get(j), polyline, sp)) {
                    Geometry intersectLine = GeometryEngine.intersect(gonGraphics.get(j), polyline, sp);
                    // 分割
                    if (((MultiPath) intersectLine).getPointCount() >= 2) {
                        segmentationList.addAll(CalculateUtil.segmentation(gonGraphics.get(j), intersectLine, sp));
                        // 这个面被处理过后 就记录下标
                        indexList.add(j);
                    }
                }
            }
            // 加入本次处理后的两个面
            gonGraphics.addAll(segmentationList);
            // 处理过的面的下标 去掉
            for (int j = 0; j < indexList.size(); j++) {
                gonGraphics.remove(indexList.get(indexList.size() - j - 1).intValue());
            }
        }
        return gonGraphics;
    }
}
