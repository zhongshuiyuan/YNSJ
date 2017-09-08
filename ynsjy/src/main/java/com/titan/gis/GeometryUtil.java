package com.titan.gis;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * Created by whs on 2017/9/8
 * 几何编辑相关工具类
 */

public class GeometryUtil {
    /**
     * 线转面
     * @param polyline
     * @return
     */
    public static Polygon polyline2Polygon(Polyline polyline, SpatialReference spatialReference){
        Polyline mPolyline= (Polyline) GeometryEngine.simplify(polyline,spatialReference);
        Polygon polygon=new Polygon();
        int size = polyline.getPointCount();
        polygon.startPath(polyline.getPoint(0));
        for (int i = 1; i < size; i++) {
            polygon.lineTo(polyline.getPoint(i));
        }
        return  polygon.isValid()?polygon:null;
    }

    /**
     * highlight graphic objects in random colors
     */
    public static void highlightGeometriesWithColor(Geometry[] results,
                                                    GraphicsLayer graphicsLayer, int color) throws Exception {
        highlightGeometriesWithColor(results, graphicsLayer, color, 16);
    }

    public static void highlightGeometriesWithColor(Geometry[] results,
                                                    GraphicsLayer graphicsLayer, int color, int size) throws Exception {

        if (results == null || results.length == 0)
            return;

        for (int i = 0; i < results.length; i++) {
            Geometry geom = results[i];
            String typeName = geom.getType().name();
            // addPolylineToGraphicsLayer((Polyline) geom, color, size / 4,
            // graphicsLayer);
			/*
			 * Create appropriate symbol, based on geometry type
			 */
            if (typeName.equalsIgnoreCase("point")) {

                addPointToGraphicsLayer((Point) geom, graphicsLayer, color,
                        size, SimpleMarkerSymbol.STYLE.CIRCLE);

            } else if (typeName.equalsIgnoreCase("polyline")) {
                addPolylineToGraphicsLayer((Polyline) geom, color, size / 4,
                        graphicsLayer);

            } else if (typeName.equalsIgnoreCase("multipoint")) {
                addMultiPointToGraphicsLayer((MultiPoint) geom, SimpleMarkerSymbol.STYLE.CIRCLE,
                        size, color, graphicsLayer);
            }

            else if (typeName.equalsIgnoreCase("polygon")) {

                SimpleFillSymbol sfs = new SimpleFillSymbol(color);
                sfs.getOutline().setWidth(size / 4);
                sfs.getOutline().setColor(color);
                sfs.setStyle(SimpleFillSymbol.STYLE.SOLID);
                sfs.setAlpha(50);
                graphicsLayer.addGraphic(new Graphic(geom, sfs));
            }

            else if (typeName.equalsIgnoreCase("envelope")) {

                SimpleFillSymbol sfs = new SimpleFillSymbol(color);
                sfs.getOutline().setWidth(size / 4);
                graphicsLayer.addGraphic(new Graphic(geom, sfs));
            }
        }

    }// end of method

    public static void addPolylineToGraphicsLayer(Polyline pl, int color,
                                                  int size, GraphicsLayer graphicsLayer) throws Exception {

        SimpleLineSymbol sls = new SimpleLineSymbol(color, size,
                com.esri.core.symbol.SimpleLineSymbol.STYLE.SOLID);
        Graphic g = new Graphic(pl, sls);
        graphicsLayer.addGraphic(g);
    }

    public static void addPolygonToGraphicsLayer(Polygon pg, int color,
                                                 int size, int alpha, GraphicsLayer graphicsLayer) throws Exception {

        SimpleFillSymbol sfs = new SimpleFillSymbol(color);
        sfs.setAlpha(alpha);
        Graphic g = new Graphic(pg, sfs);

        graphicsLayer.addGraphic(g);
    }

    /**
     * add points to graphic layer
     */
    public static void addPointToGraphicsLayer(Point point,
                                               GraphicsLayer graphicsLayer, int color, int size, SimpleMarkerSymbol.STYLE style)
            throws Exception {

        SimpleMarkerSymbol sms = new SimpleMarkerSymbol(color, size, style);
        Graphic graphic = new Graphic(point, sms);

        graphicsLayer.addGraphic(graphic);

    }// end of method

    /**
     * add multipoint to a graphic layer
     */
    public static void addMultiPointToGraphicsLayer(MultiPoint mPoint,
                                                    SimpleMarkerSymbol.STYLE style, int size, int color, GraphicsLayer graphicsLayer)
            throws Exception {
        int len = mPoint.getPointCount();
        Graphic[] graphics = new Graphic[len];

        for (int i = 0; i < len; i++) {
            Point geom = mPoint.getPoint(i);

            SimpleMarkerSymbol sms = new SimpleMarkerSymbol(color, size, style);
            graphics[i] = new Graphic(geom, sms);

            graphicsLayer.addGraphic(graphics[i]);

        }

    }// end of method

}
