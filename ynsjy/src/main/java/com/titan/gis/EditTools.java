package com.titan.gis;

import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SegmentIterator;
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
    //分割回调
    public interface segmentationCallback{
        void onSuccess(List<Geometry> geometryList);
        void onFailure(String info);
    }

    /**
     * 分割预操作
     * @param gonGeo
     * @param lineGeo
     */
    public static void previousSegmentation(Geometry gonGeo, Geometry lineGeo, SpatialReference sp, segmentationCallback callback) {
        // 这个面的集合用来做遍历
        List<Geometry> gonGraphics = new ArrayList<Geometry>();
        // 取线的最后一个点 判断是否穿过面
        MultiPath multiPath = (MultiPath) lineGeo;
        MultiPath multiPathGon = (MultiPath) gonGeo;
        boolean isIsland = false;

        Point pointLast = multiPath.getPoint(multiPath.getPointCount() - 1);
        Point pointFirst = multiPath.getPoint(0);
        // 判断分割首要条件为线与面相交,并且线的第一个点与最后一个点不和面相交
        if (!GeometryEngine.intersects(gonGeo, pointFirst,sp)
                && !GeometryEngine.intersects(gonGeo, pointLast, sp)
                && GeometryEngine.intersects(gonGeo, lineGeo, sp)
                ) {
            // 遍历多路径 生成面集合
            SegmentIterator segmentIterator = multiPathGon.querySegmentIterator();
            while (segmentIterator.nextPath()) {
                Polygon polygonPath = new Polygon();
                while (segmentIterator.hasNextSegment()) {
                    polygonPath.addSegment(segmentIterator.nextSegment(), false);
                }
                polygonPath.removePoint(polygonPath.getPointCount() - 1);
                gonGraphics.add(polygonPath);
            }
            if (gonGraphics.size() > 1) {
                // 对自身集合遍历 判断是否是环岛面
                for (int i = 0; i < gonGraphics.size(); i++) {
                    for (int j = 0; j < gonGraphics.size(); j++) {
                        if (i != j && GeometryEngine.contains(gonGraphics.get(i),
                                gonGraphics.get(j),sp)) {
                            isIsland = true;
                            break;
                        }
                    }
                }
            }
            if (isIsland) { // 如果是环岛面
                List<List<Geometry>> doubleGeo = new ArrayList<List<Geometry>>();
                // 第一步 区分大面和小面
                // 对面集合进行按面积大小的冒泡排序
                for (int k = 0; k < gonGraphics.size(); k++) {
                    for (int z = 0; z < gonGraphics.size() - 1 - k; z++) {
                        double index = Math.abs(gonGraphics.get(z).calculateArea2D());
                        double index1 = Math.abs(gonGraphics.get(z + 1).calculateArea2D());
                        if (index < index1) {
                            gonGraphics.add(z, gonGraphics.get(z + 1));
                            gonGraphics.remove(z + 2);
                        }
                    }
                }
                // 对面进行分组
                for (int i = 0; i < gonGraphics.size(); i++) {
                    List<Geometry> singleGeo = new ArrayList<Geometry>();
                    if (gonGraphics.get(i).calculateArea2D() > 0) {// 环岛小面的面积是负数
                        for (int j = 0; j < gonGraphics.size(); j++) {
                            if (i != j && GeometryEngine.contains(gonGraphics.get(i),
                                    gonGraphics.get(j),sp)) {
                                if (singleGeo.isEmpty()) {// 如果暂时没有面 则是未放入大面
                                    singleGeo.add(gonGraphics.get(i));// 取到大面
                                }
                                singleGeo.add(gonGraphics.get(j));// 取到被包含的小面
                            }
                        }
                        if (singleGeo.isEmpty()) {// 是空的情况 就是单一的普通的面
                            singleGeo.add(gonGraphics.get(i));
                        }// 不是空的情况 就是有环岛的面
                        doubleGeo.add(singleGeo);
                    }
                }
                Log.i("111", "Size: " + doubleGeo.size());
                // 存放结果的集合
                List<Geometry> result = new ArrayList<Geometry>();
                for (int a = 0; a < doubleGeo.size(); a++) {
                    List<Geometry> smallGeo = new ArrayList<Geometry>();
                    List<Geometry> geometries = doubleGeo.get(a);
                    Geometry bigGeo = new Polygon();
                    for (int i = 0; i < geometries.size(); i++) {
                        if (i == 0) {
                            bigGeo = geometries.get(i);// 取到大面
                        } else {
                            smallGeo.add(geometries.get(i));// 加入小面
                        }
                    }
                    // 第二步 大面作分割
                    List<Geometry> bigGeos = new ArrayList<Geometry>();
                    bigGeos.add(bigGeo);
                    bigGeos = segmentation(bigGeo, lineGeo, bigGeos,sp);
                    // 第三步 小面作分割
                    List<List<Geometry>> smallData = new ArrayList<List<Geometry>>();
                    for (int i = 0; i < smallGeo.size(); i++) {
                        List<Geometry> smallGeos = new ArrayList<Geometry>();// 存储一个小面分割后的面集合
                        smallGeos.add(smallGeo.get(i));// 加入当前需要被分割的小面
                        smallGeos = segmentation(smallGeo.get(i), lineGeo, smallGeos,sp);// 获取分割后的结果
                        smallData.add(smallGeos);
                    }
                    // 第四步 大面和小面的集合作差
                    for (int i = 0; i < bigGeos.size(); i++) {
                        Geometry big = bigGeos.get(i);
                        for (int j = 0; j < smallData.size(); j++) { // 循环小面的二维集合
                            for (int k = 0; k < smallData.get(j).size(); k++) {// 遍历小面的一个集合的面
                                big = GeometryEngine.difference(big, smallData.get(j).get(k), sp);
                            }
                        }
                        // 一个大面遍历完所有小面后 加入结果集合
                        if (((MultiPath) big).getPointCount() != 0) {// 判断是否是空图形
                            result.add(big);
                        }
                    }
                }
                //segmentationOver(result);
                callback.onSuccess(result);

            } else {
                List<Geometry> segmentation = segmentation(gonGeo, lineGeo, gonGraphics,sp);
                // 分割操作完成后
                callback.onSuccess(segmentation);
                //segmentationOver(segmentation);

            }
        } else {
            callback.onFailure("需要画一条穿过面的线");
            //ToastUtil.showShort(this, "需要画一条穿过面的线");
            /*editGraphicsLayer.removeGraphic(graphicsSelected.get(1).getUid());
            highGraphicsLayer.removeAll();
            graphicsSelected.clear();
            allDisSelector();*/
        }
    }
    /**
     * 分割操作
     * @param gonGeo      传入需要分割的面
     * @param lineGeo     传入画出的线
     * @param gonGraphics 存有需要分割的面的集合  用于返回分割完后的面集
     */
    public static List<Geometry> segmentation(Geometry gonGeo, Geometry lineGeo, List<Geometry> gonGraphics, SpatialReference sp) {
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
