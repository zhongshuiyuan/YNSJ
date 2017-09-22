package com.titan.gis;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2017/9/20
 */

public class CalculateUtil {
    /**
     * 需要判断线切入面的交点顺序和原来面的点集顺序是是否是相同的
     * 然后进行分割
     * @param gonGeo
     * @param intersect
     * @param sp
     * @return
     */
    public static List<Geometry> segmentation(Geometry gonGeo, Geometry intersect, SpatialReference sp) {
        // 获得路径
        MultiPath gonMulti = (MultiPath) gonGeo;
        MultiPath lineMulti = (MultiPath) intersect;
        Polygon polygonFirst = new Polygon();
        Polygon polygonSecond = new Polygon();
        int start = 0;
        int end = 0;
        // 线的顺序和面的顺序是否反向
        boolean isReverse = false;
        // 记录交点所在的下标
        int[] index = new int[2];
        int count = 0;
        // 用交点去遍历面的线 取得 各个交点所在的线的下标
        for (int j = 0; j < lineMulti.getPointCount(); j++) {
            Point point = lineMulti.getPoint(j);
            for (int i = 0; i < gonMulti.getPointCount(); i++) {
                Point pointFirst = gonMulti.getPoint(i);
                Point pointSecond;
                if (i + 1 == gonMulti.getPointCount()) {
                    pointSecond = gonMulti.getPoint(0);
                } else {
                    pointSecond = gonMulti.getPoint(i + 1);
                }
                Line lineInGon = new Line();
                lineInGon.setStart(pointFirst);
                lineInGon.setEnd(pointSecond);
                // 面的线
                Polyline polyline = new Polyline();
                polyline.addSegment(lineInGon, false);
                if (GeometryEngine.intersects(point, polyline, sp)) {
                    if(count<2){
                        index[count] = i;
                        count++;
                    }
                }
            }
        }
        // 取到交点下标后 排序
        if (index[0] < index[1]) {
            start = index[0];
            end = index[1];
            isReverse = false;
        } else if (index[0] > index[1]) {
            start = index[1];
            end = index[0];
            isReverse = true;
        } else if (index[0] == index[1]) {
            start = index[0];
            end = index[1];
        }
        for (int i = 0; i < gonMulti.getPointCount(); i++) {
            if (start == end) {  // 交点在同一个线上
                if (i != start) {
                    Line line = new Line();
                    line.setStart(gonMulti.getPoint(i));
                    if (i + 1 == gonMulti.getPointCount()) {
                        line.setEnd(gonMulti.getPoint(0));
                    } else {
                        line.setEnd(gonMulti.getPoint(i + 1));
                    }
                    polygonFirst.addSegment(line, false);
                }
                if (i == start) {
                    double distance1 = GeometryEngine.distance(gonMulti.getPoint(i), lineMulti.getPoint(0), sp);
                    double distance2 = GeometryEngine.distance(gonMulti.getPoint(i), lineMulti.getPoint(lineMulti.getPointCount() - 1),sp);
                    // 判断是否反向
                    isReverse = distance1 > distance2;
                    // 第一个面的点
                    Line line = new Line();
                    if (isReverse) {
                        line.setStart(gonMulti.getPoint(i));
                        line.setEnd(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                    } else {
                        line.setStart(gonMulti.getPoint(i));
                        line.setEnd(lineMulti.getPoint(0));
                    }
                    polygonFirst.addSegment(line, false);
                    // 加入面中的线段
                    if (isReverse) {
                        for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                            Line lineFirst = new Line();
                            lineFirst.setStart(lineMulti.getPoint(lineMulti.getPointCount() - k - 1));
                            lineFirst.setEnd(lineMulti.getPoint(lineMulti.getPointCount() - k - 2));
                            polygonFirst.addSegment(lineFirst, false);
                        }
                    } else {
                        for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                            Line lineFirst = new Line();
                            lineFirst.setStart(lineMulti.getPoint(k));
                            lineFirst.setEnd(lineMulti.getPoint(k + 1));
                            polygonFirst.addSegment(lineFirst, false);
                        }
                    }
                    // 加入穿过之后的线
                    Line lineAfter = new Line();
                    if (i + 1 == gonMulti.getPointCount()) {
                        if (isReverse) {
                            lineAfter.setStart(lineMulti.getPoint(0));
                            lineAfter.setEnd(gonMulti.getPoint(0));
                        } else {
                            lineAfter.setStart(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                            lineAfter.setEnd(gonMulti.getPoint(0));
                        }
                    } else {
                        if (isReverse) {
                            lineAfter.setStart(lineMulti.getPoint(0));
                            lineAfter.setEnd(gonMulti.getPoint(i + 1));
                        } else {
                            lineAfter.setStart(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                            lineAfter.setEnd(gonMulti.getPoint(i + 1));
                        }
                    }
                    polygonFirst.addSegment(lineAfter, false);
                    // 第二个面的闭合
                    Line lineSecond = new Line();
                    lineSecond.setStart(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                    lineSecond.setEnd(lineMulti.getPoint(0));
                    polygonSecond.addSegment(lineSecond, false);
                    for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                        Line line1 = new Line();
                        line1.setStart(lineMulti.getPoint(k));
                        line1.setEnd(lineMulti.getPoint(k + 1));
                        polygonSecond.addSegment(line1, false);
                    }
                }
            } else {  // 交点不在同一个线上
                if (i < start) {  // 小于第一个交点的点连接至第一个交点
                    Line line = new Line();
                    line.setStart(gonMulti.getPoint(i));
                    line.setEnd(gonMulti.getPoint(i + 1));
                    polygonFirst.addSegment(line, false);
                }
                if (i == start) { // 等于则连接第一个交点 这个要加三个路径
                    Line line = new Line();
                    if (isReverse) {
                        line.setStart(gonMulti.getPoint(i));
                        line.setEnd(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                    } else {
                        line.setStart(gonMulti.getPoint(i));
                        line.setEnd(lineMulti.getPoint(0));
                    }
                    polygonFirst.addSegment(line, false);
                    // 加入面中的线段
                    if (isReverse) {
                        for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                            Line lineFirst = new Line();
                            lineFirst.setStart(lineMulti.getPoint(lineMulti.getPointCount() - k - 1));
                            lineFirst.setEnd(lineMulti.getPoint(lineMulti.getPointCount() - k - 2));
                            polygonFirst.addSegment(lineFirst, false);
                        }
                    } else {
                        for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                            Line lineFirst = new Line();
                            lineFirst.setStart(lineMulti.getPoint(k));
                            lineFirst.setEnd(lineMulti.getPoint(k + 1));
                            polygonFirst.addSegment(lineFirst, false);
                        }
                    }
                    // 第二个面加入
                    Line lineSecond = new Line();
                    if (isReverse) {
                        lineSecond.setStart(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                        lineSecond.setEnd(gonMulti.getPoint(i + 1));
                    } else {
                        lineSecond.setStart(lineMulti.getPoint(0));
                        lineSecond.setEnd(gonMulti.getPoint(i + 1));
                    }
                    polygonSecond.addSegment(lineSecond, false);
                }
                if (i > start && i < end) { // 在两个交点之间的时候
                    Line line = new Line();
                    line.setStart(gonMulti.getPoint(i));
                    line.setEnd(gonMulti.getPoint(i + 1));
                    polygonSecond.addSegment(line, false);
                }
                if (i == end) {
                    Line line = new Line();
                    line.setStart(gonMulti.getPoint(i));
                    if (isReverse) {
                        line.setEnd(lineMulti.getPoint(0));
                    } else {
                        line.setEnd(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                    }
                    polygonSecond.addSegment(line, false);
                    // 反向加入面中的线
                    if (isReverse) {
                        for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                            Line lineSecond = new Line();
                            lineSecond.setStart(lineMulti.getPoint(k));
                            lineSecond.setEnd(lineMulti.getPoint(k + 1));
                            polygonSecond.addSegment(lineSecond, false);
                        }
                    } else {
                        for (int k = 0; k < lineMulti.getPointCount() - 1; k++) {
                            Line lineSecond = new Line();
                            lineSecond.setStart(lineMulti.getPoint(lineMulti.getPointCount() - k - 1));
                            lineSecond.setEnd(lineMulti.getPoint(lineMulti.getPointCount() - k - 2));
                            polygonSecond.addSegment(lineSecond, false);
                        }
                    }
                    // 第一个面的线
                    Line lineFirst = new Line();
                    if (isReverse) {
                        lineFirst.setStart(lineMulti.getPoint(0));
                    } else {
                        lineFirst.setStart(lineMulti.getPoint(lineMulti.getPointCount() - 1));
                    }
                    if (i + 1 == gonMulti.getPointCount()) {
                        lineFirst.setEnd(gonMulti.getPoint(0));
                    } else {
                        lineFirst.setEnd(gonMulti.getPoint(i + 1));

                    }
                    polygonFirst.addSegment(lineFirst, false);

                }
                if (i > end) {    // 这里完成第一个面的路径
                    Line line = new Line();
                    line.setStart(gonMulti.getPoint(i));
                    if (i + 1 == gonMulti.getPointCount()) {    // 超出下标则到0点去
                        line.setEnd(gonMulti.getPoint(0));
                    } else {
                        line.setEnd(gonMulti.getPoint(i + 1));
                    }
                    polygonFirst.addSegment(line, false);
                }
            }
        }
        polygonFirst.removePoint(polygonFirst.getPointCount() - 1);
        polygonSecond.removePoint(polygonSecond.getPointCount() - 1);
        List<Geometry> gonLists = new ArrayList<Geometry>();
        gonLists.add(polygonFirst);
        gonLists.add(polygonSecond);
        return gonLists;
    }
}
