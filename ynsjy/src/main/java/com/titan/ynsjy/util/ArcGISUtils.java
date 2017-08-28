package com.titan.ynsjy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.ScreenTool.Screen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ArcGis帮助工具类
 * */
public class ArcGISUtils {

	/**100以前去除水印，注意水印去除后，数据编辑的接口不能再使用*/
	public void setWatermark(){
		/*100以后去除文字的*/
		//ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
		/*100以前去除水印的*/
		ArcGISRuntime.setClientId("qwvvlkN4jCDmbEAO");//去除水印的
	}

	/**判断线段是否自交*/
	public static boolean isIntersect(Polyline line, MapView mapView) {
		boolean flag = false;
		int size = line.getPointCount();
		for (int i = 0; i < size - 2; i++) {
			Polyline line1 = new Polyline();
			line1.startPath(line.getPoint(i));
			line1.lineTo(line.getPoint(i + 1));
			for (int j = i+2; j < size - 1; j++) {
				Polyline line2 = new Polyline();
				line2.startPath(line.getPoint(j));
				line2.lineTo(line.getPoint(j + 1));

				boolean bl = GeometryEngine.intersects(line1, line2,mapView.getSpatialReference());
				if (bl) {
					flag = bl;
					return bl;
				}
			}
		}
		return flag;
	}

	/** 判断线段是否自交,形成闭合面 */
	public static Polygon LineToPolygon(Context context,Polyline line, MapView mapView) {
		/* 交点 */
		Point point = null;
		int size = line.getPointCount();
		int a = 0, b = 0, c = 0, d = 0;
		boolean end = false;
		for (int i = 0; i < size - 2; i++) {
			if (end) {
				break;
			}
			Polyline line1 = new Polyline();
			line1.startPath(line.getPoint(i));
			line1.lineTo(line.getPoint(i + 1));
			for (int j = i+2; j < size - 1; j++) {
				Polyline line2 = new Polyline();
				line2.startPath(line.getPoint(j));
				line2.lineTo(line.getPoint(j + 1));
				boolean bl = GeometryEngine.intersects(line2, line1,mapView.getSpatialReference());
				if (bl) {
					a = i;b = i + 1;
					c = j;d = j + 1;
					point = getLineIntersection(line.getPoint(a), line.getPoint(b),line.getPoint(c), line.getPoint(d));
					if(point != null && point.isValid()){
						end = true;
						break;
					}
				}
			}
		}

		Polygon polygon = new Polygon();
		if (point == null) {
			polygon.startPath(line.getPoint(0));
			for (int i = 1; i < size; i++) {
				polygon.lineTo(line.getPoint(i));
			}
		} else {
			if (!point.isValid()) {
				polygon.startPath(line.getPoint(b));
				for (int i = b + 1; i < d; i++) {
					polygon.lineTo(line.getPoint(i));
				}
			} else {
				polygon.startPath(point);
				for (int i = b; i < d; i++) {
					polygon.lineTo(line.getPoint(i));
				}
			}
		}
		return polygon;
	}

	/**
	 * 线是否垂直于x轴
	 */
	public static boolean isPerpendicularX(Polyline line) {
		if (checkLegal(line)) {
			return line.getPoint(0).getX() == line.getPoint(1).getX();
		} else {
			try {
				throw new Exception("线段不合法");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	/**
	 * 检测该线段的合法性<br>
	 *
	 * @return 合法-true;
	 */
	public static boolean checkLegal(Polyline line) {
		//排除两个点是同一个点
		if (line.getPoint(0).getX() == line.getPoint(1).getX() && line.getPoint(0).getY() == line.getPoint(1).getY()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取直线的斜率<br>
	 *
	 * @param line
	 * @return
	 */
	public static double getSlop(Polyline line) {
		if(!isPerpendicularX(line)) {//斜率存在
			return (line.getPoint(0).getY()-line.getPoint(1).getY())/(line.getPoint(0).getX()-line.getPoint(1).getX());
		} else{
			try {
				throw new Exception("斜率不存在");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 获取于y轴的截距(斜率存在的情况下)
	 * @return
	 */
	public static double getInterceptY(Polyline line) {
		return line.getPoint(0).getY() - getSlop(line) * line.getPoint(0).getX();
	}

	/**
	 * 获取与x轴的截距
	 * @return
	 */
	public static double getInterceptX(Polyline line) {
		if (!isPerpendicularX(line)) {//斜率存在
			if (getSlop(line) != 0) {//不平行于x轴
				return -(getInterceptY(line)/getSlop(line));
			} else {
				try {
					throw new  Exception("平行于x轴");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			return line.getPoint(0).getX();
		}
		return 0;
	}


	/** 线段自交形成闭合面时起点索引 */
	public static int LineIndex(Polyline line, MapView mapView) {
		/* 交点*/
		Point point = null;
		int size = line.getPointCount();
		int a = 0,b = 0,c=0,d=0;
		boolean end = false;
		for (int i = 0; i < size - 2; i++) {
			Polyline line1 = new Polyline();
			line1.startPath(line.getPoint(i));
			line1.lineTo(line.getPoint(i + 1));
			for (int j = 2; j < size - 1; j++) {
				Polyline line2 = new Polyline();
				line2.startPath(line.getPoint(j));
				line2.lineTo(line.getPoint(j + 1));
				boolean bl = GeometryEngine.intersects(line1, line2,mapView.getSpatialReference());
				if (bl) {
					a = i;b=i+1;c=j;d=j+1;
					point = getIntersection(line.getPoint(a), line.getPoint(b), line.getPoint(c), line.getPoint(d));
					end = true;
					break;
				}
			}
			if(end){
				break;
			}
		}
		Polygon polygon = new Polygon();
		polygon.startPath(point);
		for (int i = b; i < d; i++) {
			polygon.lineTo(line.getPoint(i));
		}
		return a;
	}

	/** 获取切点与选择面的交点所处线两个端点的位置索引*/
	public static Integer[] getPointAndPolygon(Polygon curpolygon,Point point,MapView mapView){
		Integer[] array = new Integer[2];
		int psize = curpolygon.getPointCount();
		for (int i = 0; i < psize-1; i++) {
			Polyline lineTemp = new Polyline();
			Point point1 = curpolygon.getPoint(i);
			lineTemp.startPath(point1);
			Point point2 = null;
			if(i == psize){
				point2 = curpolygon.getPoint(0);
			}else{
				point2 = curpolygon.getPoint(i+1);
			}
			lineTemp.lineTo(point2);
			boolean intersect_s = GeometryEngine.intersects(point, lineTemp, mapView.getSpatialReference());
			if(intersect_s){
				array[0] = i;
				array[1] = i+1;
				break;
			}
		}
		return array;
	}

	/**
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.invalidate();
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	public static void DroolMian(Context mContext, Screen screen,
								 GraphicsLayer graphicsLayerLocation, Point point, Polygon polygon) {
		int size = 0;
		if (screen.getWidthPixels() > 1280) {
			size = 12;
		} else {
			size = 18;
		}
		Drawable color = mContext.getResources().getDrawable(R.drawable.point_pic);
		if (polygon.getPointCount() >= 3) {
			DecimalFormat decimalFormat = new DecimalFormat(".000");
			String str = " "+decimalFormat.format(Math.abs(polygon.calculateArea2D())) + "平方米";
			PictureMarkerSymbol pic = SymbolUtil.TextPicSymobelArea(mContext,str, Color.BLUE, size, SymbolUtil.MODE.BOTTOM);
			Graphic graphic = new Graphic(point, pic);
			graphicsLayerLocation.addGraphic(graphic);
		} else {
			if (polygon.getPathCount() == 0) {
				PictureMarkerSymbol pictureMarkerSymbol = SymbolUtil
						.TextPicSymobel(mContext, "起点", Color.BLUE, size,SymbolUtil.MODE.BOTTOM);
				Graphic gp = new Graphic(point, pictureMarkerSymbol);
				graphicsLayerLocation.addGraphic(gp);
			}
		}
		PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(mContext, color);
		Graphic gp = new Graphic(point, markerSymbol);
		graphicsLayerLocation.addGraphic(gp);
	}

	public static void DroolMian(Context mContext, Screen screen,
								 GraphicsLayer graphicsLayerLocation, Polygon polygon, double area) {
		int size = 0;
		if (screen.getWidthPixels() > 1280) {
			size = 12;
		} else {
			size = 18;
		}

		DecimalFormat decimalFormat = new DecimalFormat(".000");
		String str = " "+decimalFormat.format(area) + "平方米";
		PictureMarkerSymbol pic = SymbolUtil.TextPicSymobelArea(mContext, str,
				Color.BLUE, size, SymbolUtil.MODE.BOTTOM);
		Envelope envelope = new Envelope();
		polygon.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		Graphic graphic = new Graphic(point, pic);
		graphicsLayerLocation.addGraphic(graphic);
	}

	public static void DroolLine(Context mContext, Screen screen,
								 GraphicsLayer graphicsLayerLocation, Point point, Polyline polyline) {
		DecimalFormat decimalFormat = new DecimalFormat(".000");
		PictureMarkerSymbol pic;
		Drawable color = mContext.getResources().getDrawable(R.drawable.point_pic);
		int size = 0;
		if (screen.getWidthPixels() > 1280) {
			size = 14;
		} else {
			size = 18;
		}

		PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(mContext,color);
		Graphic gp = new Graphic(point, markerSymbol);
		graphicsLayerLocation.addGraphic(gp);

		if (polyline.getPointCount() == 0) {
			pic = SymbolUtil.TextPicSymobel(mContext, "起点", Color.BLUE, size,
					SymbolUtil.MODE.BOTTOM);
		} else {
			int count = polyline.getPointCount();
			if(count < 3){
				return;
			}
			point = polyline.getPoint(count-2);
			Polyline line = new Polyline();
			line.startPath(polyline.getPoint(0));
			for(int i=1;i<count-1;i++){
				line.lineTo(polyline.getPoint(i));
			}
			String str ="\n"+ decimalFormat.format(Math.abs(line.calculateLength2D())).toString() + "米";
			pic = SymbolUtil.TextPicSymobel(mContext, str, Color.BLUE, size,SymbolUtil.MODE.BOTTOM);
			//pic = new PictureMarkerSymbol(color);
		}
		Graphic graphic = new Graphic(point, pic);
		graphicsLayerLocation.addGraphic(graphic);

	}

	/** */
	public static List<Geometry> getGeometry(Polyline pl, MapView mapView) {
		List<Geometry> list = new ArrayList<Geometry>();
		int m = pl.getPointCount();
		for (int i = 0; i < m; i++) {
			Polyline pl_s = new Polyline();
			pl_s.startPath(pl.getPoint(i));
			if (m > i + 1) {
				pl_s.lineTo(pl.getPoint(i + 1));
			}
			if (m > i + 2) {
				for (int j = i + 2; j < m; j++) {
					if (j < m - 1) {
						Polyline pl_e = new Polyline();
						pl_e.startPath(pl.getPoint(j));
						pl_e.lineTo(pl.getPoint(j + 1));
						boolean result = GeometryEngine.intersects(pl_s, pl_e,
								mapView.getSpatialReference());
						if (result) {
							Point point = getIntersection(pl.getPoint(i),
									pl.getPoint(i + 1), pl.getPoint(j),
									pl.getPoint(j + 1));
							Polyline polyline = new Polyline();
							polyline.startPath(point);
							for (int k = i + 1; k <= j; k++) {
								polyline.lineTo(pl.getPoint(k));
							}
							polyline.lineTo(point);
							Geometry geometry = GeometryEngine.simplify(
									polyline, mapView.getSpatialReference());
							list.add(geometry);
						}
					}
				}
			}
		}
		return list;
	}
	/** 获取两条线段的交点 */
	public static Point getIntersection(Point a, Point b, Point c, Point d) {
		Point intersection = new Point();
		intersection
				.setX(((b.getX() - a.getX()) * (c.getX() - d.getX())
						* (c.getY() - a.getY()) - c.getX()
						* (b.getX() - a.getX()) * (c.getY() - d.getY()) + a
						.getX() * (b.getY() - a.getY()) * (c.getX() - d.getX()))
						/ ((b.getY() - a.getY()) * (c.getX() - d.getX()) - (b
						.getX() - a.getX()) * (c.getY() - d.getY())));
		intersection
				.setY(((b.getY() - a.getY()) * (c.getY() - d.getY())
						* (c.getX() - a.getX()) - c.getY()
						* (b.getY() - a.getY()) * (c.getX() - d.getX()) + a
						.getY() * (b.getX() - a.getX()) * (c.getY() - d.getY()))
						/ ((b.getX() - a.getX()) * (c.getY() - d.getY()) - (b
						.getY() - a.getY()) * (c.getX() - d.getX())));
		return intersection;
	}

	/** 获取两条线段的交点 */
	public static Point getLineIntersection(Point a,Point c,Point b,Point d){
		double e=0,aaa = 0;
		double f = g(a.getX(), c.getX()) ? 1E10 : (a.getY() - c.getY()) / (a.getX() - c.getX());
		double k = g(b.getX(), d.getX()) ? 1E10 : (b.getY() - d.getY()) / (b.getX() - d.getX());
		double l = a.getY() - f * a.getX();
		double h = b.getY() - k * b.getX();

		if (g(f, k)) {
			if (g(l, h)) {
				double aa,bb,cc,dd;
				boolean f1 = Math.min(a.getX(), c.getX()) < Math.max(b.getX(), d.getX());
				boolean f2 = Math.max(a.getX(), c.getX()) > Math.min(b.getX(), d.getX());
				if (g(a.getX(), c.getX())){
					boolean flag = Math.min(a.getY(), c.getY()) < Math.max(b.getY(),d.getY());
					boolean flag1 = Math.max(a.getY(),c.getY()) > Math.min(b.getY(), d.getY());
					if (flag || flag1){
						aa = Math.min(a.getY(), c.getY());
						bb = Math.min(b.getY(), d.getY());
						cc = Math.max(a.getY(), c.getY());
						dd = Math.max(b.getY(), d.getY());
						aaa = (a.getY() + c.getY() + b.getY() + d.getY() - Math.min(aa, bb) - Math.max(cc, dd)) / 2;
						e = (aaa - l) / f;
						return new Point(e, aaa);
					}else {
						return null;
					}
				}else if (f1 || f2){
					aa = Math.min(a.getX(), c.getX());
					bb = Math.min(b.getX(), d.getX());
					cc = Math.max(a.getX(), c.getX());
					dd = Math.max(b.getX(), d.getX());
					e = (a.getX() + c.getX() + b.getX() + d.getX() - Math.min(aa, bb) - Math.max(bb,dd)) / 2;
					aaa = f * e + l;
					return new Point(e, aaa);
				}else{
					return null;
				}
			}
			return null;
		}
		boolean f3 = g(f, 1E10);
		if(f3){
			e = a.getX();
			aaa = k * e + h;
		}else{
			boolean f4 = g(k, 1E10);
			if(f4){
				e = b.getX();
				aaa = f * e + l;
			}else{
				e = -(l - h) / (f - k);
				if(a.getY() == c.getY()){
					aaa = a.getY();
				}else{
					if(b.getY() == d.getY()){
						aaa = b.getY();
					}else{
						aaa = f * e + l;
					}
				}
			}
		}
		return new Point(e, aaa);
	}

	public static boolean g(double a,double c){
		return 1E-8 > Math.abs(a - c);
	}

	public static boolean isIntersect(Point point, Point lineStartPoint,Point lineEndPoint) {
		boolean result1 = false;
		boolean result2 = false;
		double X21, Y21, X10, Y10;
		X21 = lineEndPoint.getX() - lineStartPoint.getX();
		Y21 = lineEndPoint.getY() - lineStartPoint.getY();
		X10 = point.getX() - lineStartPoint.getX();
		Y10 = point.getY() - lineStartPoint.getY();
		double mm = X21 * Y10 - X10 * Y21;
		DecimalFormat df = new DecimalFormat("0.0000000");
		if (df.format(Math.abs(mm)).equals("0.0000000")) {
			result1 = true;
		}
		double xMin = Math.min(lineStartPoint.getX(), lineEndPoint.getX());
		double xMax = Math.max(lineEndPoint.getX(), lineStartPoint.getX());
		double yMin = Math.min(lineStartPoint.getY(), lineEndPoint.getY());
		double yMax = Math.max(lineEndPoint.getY(), lineStartPoint.getY());
		if (xMin <= point.getX() && point.getX() <= xMax
				&& yMin <= point.getY() && point.getY() <= yMax) {
			result2 = true;
		} else {
			result2 = false;
		}
		return result1 && result2;
	}

	public static boolean containsPoint(Point point, Point[] vertices) {
		int verticesCount = vertices.length;
		int nCross = 0;
		for (int i = 0; i < verticesCount; ++i) {
			Point p1 = vertices[i];
			Point p2 = vertices[(i + 1) % verticesCount];

			if (p1.getY() == p2.getY()) {
				continue;
			}
			if (point.getY() < Math.min(p1.getY(), p2.getY())) {
				continue;
			}
			if (point.getY() >= Math.max(p1.getY(), p2.getY())) {
				continue;
			}
			double x = (point.getY() - p1.getY()) * (p2.getX() - p1.getX())
					/ (p2.getY() - p1.getY()) + p1.getX();
			if (x > point.getX()) {
				nCross++;
			}
		}
		return (nCross % 2 == 1);
	}

}
