package com.titan.ynsjy.presenter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Feature;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.AttributeAdapter;
import com.titan.ynsjy.adapter.FeatureResultAdapter;
import com.titan.ynsjy.adapter.SearchXdmAdapter;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.db.DbHelperService;
import com.titan.ynsjy.dialog.ShouCangDialog;
import com.titan.ynsjy.edite.activity.LineEditActivity;
import com.titan.ynsjy.edite.activity.PointEditActivity;
import com.titan.ynsjy.edite.activity.XbEditActivity;
import com.titan.ynsjy.entity.MyFeture;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.Station;
import com.titan.ynsjy.entity.XdmSearchHistory;
import com.titan.ynsjy.mview.IBaseView;
import com.titan.ynsjy.service.Webservice;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.CursorUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.SymbolUtil;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.UtilTime;

import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionFactory;
import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.proj.Projection;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.titan.ynsjy.BaseActivity.myLayer;
import static com.titan.ynsjy.BaseActivity.seflayerName;

/**
 * Created by li on 2017/5/9.
 * basePresenter 基础Presenter
 */
public class BasePresenter {

    private BaseActivity baseActivity;
    private MapView mapView;
    private IBaseView iBaseView;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    public BasePresenter(BaseActivity ctx, IBaseView view){
        this.baseActivity = ctx;
        this.mapView = view.getMapView();
        this.iBaseView = view;
    }

    /**添加tpk文件地图*/
    public ArcGISLocalTiledLayer addTitleLayer(String path){
        if (new File(path).exists()) {
            ArcGISLocalTiledLayer tiledLayer = new ArcGISLocalTiledLayer(path);
            mapView.addLayer(tiledLayer);
            return tiledLayer;
        }else{
            return new ArcGISLocalTiledLayer(path);
        }
    }

    /**添加graphicLayer图层*/
    public GraphicsLayer addGraphicLayer(){
        GraphicsLayer graphicsLayer = new GraphicsLayer(GraphicsLayer.RenderingMode.STATIC);
        mapView.addLayer(graphicsLayer);
        return graphicsLayer;
    }
    /** 当前点数据展示 */
    public View loadCalloutView(final Point point) {
        DecimalFormat decimalFormat = new DecimalFormat(".000000");
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.callout_mylocation, null);
        final View wgsView = view.findViewById(R.id.callout_mylocation_wgs);
        final View xianView = view.findViewById(R.id.callout_mylocation_xian80);
        TextView wgslon = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lon);
        String lon = decimalFormat.format(iBaseView.getCurrentLon()) + "";
        wgslon.setText(lon);
        TextView wgslat = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lat);
        String lat = decimalFormat.format(iBaseView.getCurrenLat()) + "";
        wgslat.setText(lat);
        TextView xianlon = (TextView) view.findViewById(R.id.callout_mylocation_80_lon);
        String x = decimalFormat.format(point.getX()) + "";
        xianlon.setText(x);
        TextView xianlat = (TextView) view.findViewById(R.id.callout_mylocation_80_lat);
        String y = decimalFormat.format(point.getY()) + "";
        xianlat.setText(y);

        TextView wgslon1 = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lon1);
        wgslon1.setText(BaseUtil.decimalTodu(iBaseView.getCurrentLon()));

        TextView wgslat1 = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lat1);
        wgslat1.setText(BaseUtil.decimalTodu(iBaseView.getCurrenLat()));
        //
        Button addscpoint = (Button) view.findViewById(R.id.addscpoint);
        addscpoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!point.isValid()){
                    ToastUtil.setToast(baseActivity, "未获取到当前位置坐标");
                    return;
                }
                ShouCangDialog dialog=new ShouCangDialog(baseActivity,point,ShouCangDialog.SqlType.ADD);
                BussUtil.setDialogParams(baseActivity, dialog, 0.7, 0.8);
            }
        });

        ImageView close = (ImageView) view.findViewById(R.id.callout_mylocation_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iBaseView.getCallout().hide();
            }
        });
        return view;
    }

    /** 长点击事件展示位置信息的popuwindow */
    public View loadCalloutPopuwindow(final Point mappoint){
        DecimalFormat decimalFormat = new DecimalFormat(".000000");
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.calloutpopuwindow, null);
        final View wgsView = view.findViewById(R.id.calloutpopuwindow_xian80);

        //wgs84坐标
        TextView wgslon1 = (TextView) view.findViewById(R.id.calloutpopuwindow_wgs_lon);
        wgslon1.setText(BaseUtil.decimalTodu(iBaseView.getCurrentLon()));
        TextView wgslat1 = (TextView) view.findViewById(R.id.calloutpopuwindow_wgs_lat);
        wgslat1.setText(BaseUtil.decimalTodu(iBaseView.getCurrenLat()));
        //西安80坐标系
        TextView xianlon = (TextView) view.findViewById(R.id.calloutpopuwindow_80_x);
        String x80 = decimalFormat.format(mappoint.getX()) + "";
        xianlon.setText(x80);
        TextView xianlat = (TextView) view.findViewById(R.id.calloutpopuwindow_80_y);
        String y80 = decimalFormat.format(mappoint.getY()) + "";
        xianlat.setText(y80);

        Button addscpoint = (Button) view.findViewById(R.id.addscpoint);
        addscpoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ShouCangDialog dialog=new ShouCangDialog(baseActivity,mappoint,ShouCangDialog.SqlType.ADD);
                BussUtil.setDialogParams(baseActivity, dialog, 0.7, 0.8);
            }
        });
        return view;
    }

    /** 添加轨迹坐标点 */
    public void addGuijiPoint(Point currentPoint) {
        String state = "1";
        if(currentPoint == null || !currentPoint.isValid()){
            return;
        }
        if (MyApplication.getInstance().hasNetWork()) {// 在线保存

            String recodeTime = format.format(new Date());
            Webservice webservice = new Webservice(baseActivity);
            state = webservice.upPoint(MyApplication.macAddress,
                    currentPoint.getX() + "", currentPoint.getY() + "",
                    format.format(new Date()), MyApplication.mobileXlh,
                    MyApplication.mobileType);
            if (state.equals("0")) {
                // 录入失败
            } else if (state.equals("1")) {
                // 录入成功
            }

            /* 离线保存 */
            if (state.equals("1")) {
                DataBaseHelper.addPointGuiji(baseActivity,
                        MyApplication.macAddress, currentPoint.getX(),
                        currentPoint.getY(), recodeTime, state);
            } else {
                DataBaseHelper.addPointGuiji(baseActivity,
                        MyApplication.macAddress, currentPoint.getX(),
                        currentPoint.getY(), recodeTime, "0");
            }
        }
    }

    /** 从相册选取图片进行编辑 */
    private static final int ALBUM = 0x000002;
    public void fromAlum() {
        //系统相册
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //picture.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        baseActivity.startActivityForResult(picture, ALBUM);
    }

    /** 拍照 */
    private static final int TAKE_PHOTO = 0x000001;
    public String takephoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定存放拍摄照片的位置
        File file = createImageFile();
        if (file != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            baseActivity.startActivityForResult(intent, TAKE_PHOTO);
            return file.getAbsolutePath();
        }
        return "";
    }

    /** 存放文件位置 */
    private File createImageFile() {
        String path = MyApplication.resourcesManager.getFolderPath("/phone");
        if(path.equals("文件夹可用地址")){
            try {
                path = MyApplication.resourcesManager.getTootPath()+ ResourcesManager.ROOT_MAPS+"/phone";
                ResourcesManager.createFolder(path);
            } catch (jsqlite.Exception e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File image = new File(path+"/"+ String.valueOf(System.currentTimeMillis()) + ".jpg");
        return image;
    }

    /** 加载小班属性数据 */
    public View loadAttributeView(List<Field> fieldList, Map<String, Object> attributes) {
        String xian="",xiang="";
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        ArrayList<Field> list2 = new ArrayList<Field>();
        for (int i = 0; i < fieldList.size(); i++) {
            String alias = fieldList.get(i).getAlias();
            if (alias.equals("OBJECTID") || alias.equals("GLOBALID")) {
                continue;
            }
            list2.add(fieldList.get(i));
            Map<String, String> map = new HashMap<String, String>();
            CodedValueDomain domain = (CodedValueDomain) fieldList.get(i).getDomain();
            if(domain != null){
                Map<String, String> values = domain.getCodedValues();
                String name = fieldList.get(i).getName();
                for(String key : values.keySet()){
                    if(key.equals(attributes.get(name))){
                        map.put(alias, values.get(key));
                        break;
                    }else{
                        map.put(alias, fieldList.get(i).getName());
                    }
                }
            }else{
                map.put(alias, fieldList.get(i).getName());
            }
            list.add(map);
        }
        final View view = LayoutInflater.from(baseActivity).inflate(R.layout.polygon_attributeinfo, null);
        ListView attribute_listview = (ListView) view.findViewById(R.id.attribute_listview);
        AttributeAdapter adapter = new AttributeAdapter(list2, attributes,list, baseActivity, "当前图层");
        attribute_listview.setAdapter(adapter);
        ImageButton button = (ImageButton) view.findViewById(R.id.attributeclose);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                iBaseView.getCallout().hide();
            }
        });
        return view;
    }

    /**
     * pro4j进行坐标转换
     */
    public ProjCoordinate meth(double lon, double lat, double al) {
        String x = MyApplication.sharedPreferences.getString("dx", "0");
        double x1 = Double.parseDouble(x.equals("") ? "0": x);
        String y = MyApplication.sharedPreferences.getString("dy", "0");
        double y1 = Double.parseDouble(y.equals("") ? "0" : y);
        String z = MyApplication.sharedPreferences.getString("dz", "0");
        double z1 = Double.parseDouble(z.equals("") ? "0" : z);

        String[] wgs840 = new String[]{"+proj=longlat", "+ellps=WGS84", "+datum=WGS84", "+no_defs"};
        Datum datum0 = Datum.WGS84;
        Projection proj840 = ProjectionFactory.fromPROJ4Specification(wgs840);

        CoordinateReferenceSystem wgs84cs0 = new CoordinateReferenceSystem("WGS84", wgs840, datum0, proj840);
//		String[] wgs841 = new String[] { "+proj=merc", "+lon_0=0", "+k=1",
//				"+x_0=0", "+y_0=0", "+datum=WGS84", "+units=m", "+no_defs" };
//		Datum datum1 = Datum.WGS84;
//		Projection proj841 = ProjectionFactory.fromPROJ4Specification(wgs841);

//		CoordinateReferenceSystem wgs84cs1 = new CoordinateReferenceSystem(
//				"WGS84", wgs841, datum1, proj841);

//		String[] wgs842 = new String[] { "+proj=merc", "+lon_0=0", "+k=1",
//				"+x_0=0", "+y_0=0", "+datum=WGS84", "+units=m", "+no_defs" };
        // 3395 wgs1984 world wercator +proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0
        // +datum=WGS84 +units=m +no_defs
        // ,"+towgs84=22,-107.4036667,-37.915,3.961,0,0,0"
//		Datum datum2 = Datum.WGS84;
//		Projection proj842 = ProjectionFactory.fromPROJ4Specification(wgs842);
//
//		CoordinateReferenceSystem wgs84cs2 = new CoordinateReferenceSystem(
//				"WGS84", wgs842, datum2, proj842);

        // +proj=longlat +a=6378140 +b=6356755.288157528 +no_defs
        String[] xian801 = new String[]{"+proj=longlat", "+a=6378140",
                "+b=6356755.288157528", "+no_defs"};
        Ellipsoid ellipsoid1 = new Ellipsoid("xian80", 6378140, 0.0, 298.257,
                "xian80");
        Projection proj801 = ProjectionFactory.fromPROJ4Specification(xian801);

        Datum datum801 = new Datum("xian80", x1, y1, z1, ellipsoid1, "xian80");
        CoordinateReferenceSystem xian80cs1 = new CoordinateReferenceSystem(
                "xian80", xian801, datum801, proj801);

        String[] xian80 = new String[]{"+proj=tmerc", "+lat_0=0",
                "+lon_0=105", "+k=1", "+x_0=500000", "+y_0=0", "+a=6378140",
                "+b=6356755.288157528", "+units=m", "+no_defs"};
        Ellipsoid ellipsoid = new Ellipsoid("xian80", 6378140, 0.0, 298.257,
                "xian80");
        Projection proj80 = ProjectionFactory.fromPROJ4Specification(xian80);

        Datum datum80 = new Datum("xian80", x1, y1, z1, ellipsoid, "xian80");
        CoordinateReferenceSystem xian80cs = new CoordinateReferenceSystem(
                "xian80", xian80, datum80, proj80);

        ProjCoordinate src = new ProjCoordinate(lon, lat, al);
        ProjCoordinate dst = new ProjCoordinate();

        BasicCoordinateTransform transformation = new BasicCoordinateTransform(wgs84cs0, xian80cs1);
        ProjCoordinate ddd = transformation.transform(src, dst);
        //System.out.println("未投影西安80 " + ddd.x + " === " + ddd.y + "---- " + ddd.z);
        ProjCoordinate src1 = new ProjCoordinate(ddd.x, ddd.y, ddd.z);

        BasicCoordinateTransform transformation1 = new BasicCoordinateTransform(xian80cs1, xian80cs);
        ProjCoordinate ddd1 = transformation1.transform(src1, dst);
        //System.out.println("西安80 投影" + ddd1.x + " === " + ddd1.y + "---- " + ddd1.z);
        return ddd1;
    }


    /**
     * 弹出结果展示窗口
     */
    public void showListFeatureResult(final List<GeodatabaseFeature> list) {
        final Dialog dialog = new Dialog(baseActivity, R.style.Dialog);
        dialog.setContentView(R.layout.featureresult_view);
        dialog.setCanceledOnTouchOutside(true);
        final ListView listView = (ListView) dialog.findViewById(R.id.featureresult_listview);
        FeatureResultAdapter adapter = new FeatureResultAdapter(baseActivity, list, baseActivity.selMap);
        listView.setAdapter(adapter);

        BaseUtil.getIntance(baseActivity).setHeight(adapter, listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (iBaseView.getGraphicLayer() != null) {
                    iBaseView.getGraphicLayer().removeAll();
                }
                baseActivity.getSelParams(list, position);

                Intent intent = null;
                if (BaseActivity.selectGeometry.getType().equals(Geometry.Type.POINT)) {
                    intent = new Intent(baseActivity, PointEditActivity.class);
                } else if (BaseActivity.selectGeometry.getType().equals(Geometry.Type.POLYLINE)) {
                    intent = new Intent(baseActivity, LineEditActivity.class);
                } else if (BaseActivity.selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
                    intent = new Intent(baseActivity, XbEditActivity.class);
                } else {
                    intent = new Intent(baseActivity, XbEditActivity.class);
                }

                String pname = BaseActivity.myLayer.getPname();// 工程名称
                String path = BaseActivity.myLayer.getPath();
                String cname = BaseActivity.myLayer.getCname();
                MyFeture feture = new MyFeture(pname, path, cname, BaseActivity.selGeoFeature, BaseActivity.myLayer);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myfeture", feture);
                bundle.putSerializable("parent", "Base");
                bundle.putSerializable("id", BaseActivity.selGeoFeature.getId() + "");
                intent.putExtras(bundle);
                baseActivity.startActivityForResult(intent,4);
                feture = null;
            }
        });

        adapter.notifyDataSetChanged();
        BussUtil.setDialogParams(baseActivity, dialog, 0.55, 0.55);
    }

    /**
     * 测量面积测量调绘方式选择
     */
    public void initCmPopuwindow() {

        final PopupWindow pop = new PopupWindow(baseActivity);

        View view = baseActivity.getLayoutInflater().inflate(R.layout.item_cemian_popuwindows, null);
        final LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup_cemian);
        pop.setWidth(MyApplication.screen.getWidthPixels() / 3);//LayoutParams.MATCH_PARENT
        pop.setHeight(MyApplication.screen.getHeightPixels() / 2);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        pop.showAtLocation(baseActivity.childview, Gravity.CENTER, 0, 0);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent_cemian);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_dbx);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_zyqx);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_freeline);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.restory();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.drawType = BaseActivity.POLYGON;
                baseActivity.activate(baseActivity.drawType);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.drawType = BaseActivity.FREEHAND_POLYGON;
                baseActivity.activate(baseActivity.drawType);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.drawType = BaseActivity.FREEHAND_POLYLINE;
                baseActivity.activate(baseActivity.drawType);
            }
        });
    }

    /**
     * 初始化小地名查询控件
     */
    public void initXdmView(final View xdmSearchInclude){

        final EditText searchTxt = (EditText) xdmSearchInclude.findViewById(R.id.xdm_searchText);
        CursorUtil.setEditTextLocation(searchTxt);
        final ListView listResult = (ListView) xdmSearchInclude.findViewById(R.id.listView_xdm_search);
        final DbHelperService<XdmSearchHistory> service = new DbHelperService<XdmSearchHistory>(baseActivity, XdmSearchHistory.class);
        final List<XdmSearchHistory> list = service.getObjectsByWhere(null);
        final SearchXdmAdapter<XdmSearchHistory> adapter = new SearchXdmAdapter<>(list, baseActivity);
        listResult.setAdapter(adapter);
        listResult.setVisibility(View.GONE);
        searchTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                adapter.notifyDataSetChanged();
                listResult.setVisibility(View.VISIBLE);

                listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        String txt = list.get(arg2).getName();
                        searchTxt.setText(txt);
                        CursorUtil.setEditTextLocation(searchTxt);
                    }
                });
            }
        });

        ImageView searchView = (ImageView) xdmSearchInclude.findViewById(R.id.xdm_search_view);
        searchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(searchTxt.getText())) {
                    ToastUtil.setToast(baseActivity, "请输入查询地名");
                    return;
                }
                String searchValue = searchTxt.getText().toString();
                ArrayList<String> lst = new ArrayList<>();
                if (!lst.contains(searchValue)) {
                    XdmSearchHistory t = new XdmSearchHistory();
                    t.setName(searchTxt.getText().toString());
                    t.setTime(UtilTime.getSystemtime2());
                    boolean flag = service.add(t);
                }
                searchXdmMethod(searchValue, listResult);
            }
        });
        ImageView close = (ImageView) xdmSearchInclude.findViewById(R.id.close_xdm_search);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xdmSearchInclude.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 小地名查询
     */
    private void searchXdmMethod(final String searchTxt, final ListView listResult) {

        DbHelperService<Station> service = new DbHelperService<>(baseActivity, Station.class);
        HashMap<String, String> where = new HashMap<>();
        where.put("name like", "%" + searchTxt + "%");
        final List<Station> list = service.getObjectsByWhere(where);

        //final List<HashMap<String, Object>> list = DataBaseHelper.getAddressInfo(mContext, searchTxt);
        if (list.size() == 0) {
            ToastUtil.setToast(baseActivity, "无此类地名");
            return;
        }

        SearchXdmAdapter<Station> adapter = new SearchXdmAdapter<>(list, baseActivity);
        listResult.setAdapter(adapter);

        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterview, View view,
                                    final int position, long l) {
                dwToMap(list, position);
            }
        });

//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				if (BussUtil.isEmperty(searchTxt)) {
//					// 记录到历史查询数据中
//					boolean result = DataBaseHelper.selDataHistoryByString(mContext,searchTxt);
//					if (!result) {
//						DataBaseHelper.addDataToHistory(mContext, searchTxt);
//					}
//				}
//			}
//		}).start();
    }

    /**
     * 定位到地图
     */
    private void dwToMap(final List<Station> list, final int position) {
        baseActivity.runOnUiThread(new Runnable() {
            public void run() {
                baseActivity.clearGraphic();
                Double x = Double.valueOf(list.get(position).getX());
                Double y = Double.valueOf(list.get(position).getY());
                Point point = new Point(x, y);
                PictureMarkerSymbol pictureMarkerSymbol = SymbolUtil.TextPicSymobel(baseActivity,
                        list.get(position).getName(), Color.RED, 16, R.drawable.icon_gcoding, SymbolUtil.MODE.BOTTOM);
                Graphic graphic = new Graphic(point, pictureMarkerSymbol);

                iBaseView.getGraphicLayer().addGraphic(graphic);
                int currentLevel = baseActivity.tiledLayer.getCurrentLevel();
                if (currentLevel > 14) {
                    mapView.zoomTo(point, 0);
                } else {
                    mapView.zoomTo(point, 14 - currentLevel);
                }
                showAddressPopup(list.get(position).getName(), point);
            }
        });
    }

    /**
     * 搜索结果点击显示导航
     */
    private void showAddressPopup(String addressName, final Point point) {
        final View view = baseActivity.childview.findViewById(R.id.address_navigation_include);
        view.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) view.findViewById(R.id.addressviewreturn);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                view.setVisibility(View.GONE);
            }
        });

        TextView address = (TextView) view.findViewById(R.id.item_popupwindows_address);
        address.setText(addressName);
        Button daohang = (Button) view.findViewById(R.id.address_daohang);
        daohang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                baseActivity.mRouteTask = baseActivity.navigationPresenter.initRoutAndGeocoding();
                baseActivity.mStops.clearFeatures();
                Polyline polyline = new Polyline();
                if (BaseActivity.currentPoint != null && BaseActivity.currentPoint.isValid()) {
                    baseActivity.addFeatureNavi(BaseActivity.currentPoint);
                    polyline.startPath(BaseActivity.currentPoint);
                }
                baseActivity.navigationPresenter.drawLineToMap(point, polyline);
            }
        });
    }

//    /**
//     * 加载数据所属数据
//     */
//    public void uploadsjssData() {
//
//        if (MyApplication.getInstance().hasNetWork() && StartActivity.bsuserbase != null) {
//            Webservice web = new Webservice(baseActivity);
//            String result = web.getSjssData(StartActivity.bsuserbase.getDATASHARE().trim());
//            if (result.equals(Webservice.netException)) {
//                //ToastUtil.setToast(mContext, Webservice.netException);
//            } else if (result.equals("无数据")) {
//                //ToastUtil.setToast(mContext, "无数据");
//            } else {
//                baseActivity.sjssLlist = BussUtil.getSjssData(result);
//            }
//        }
//    }

    /**
     * 加载驯养繁殖基地基地性质数据
     */
    public void uploadjdxzData() {
        if (MyApplication.getInstance().hasNetWork()) {
            Webservice web = new Webservice(baseActivity);
            String result = web.getJdxzData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
            } else {
                baseActivity.jdxzLlist = BussUtil.getJdxzData(result);
            }
        }
    }

    /**
     * 加载行政区域数据数据
     */
    public void uploadxzqyData() {

        if (MyApplication.getInstance().hasNetWork()) {
            Webservice web = new Webservice(baseActivity);
            String result = web.getXzqyData("1");
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
            } else {
                baseActivity.xzqyLlist = BussUtil.getSjssData(result);
            }
        }
    }

    /**
     * 加载图层为一个图层时默认选择这个图层，获取这个图层的数据
     */
    public void getMylayer() {
        BaseActivity.myLayer = BaseActivity.layerNameList.get(0);
        baseActivity.layerType = BaseActivity.myLayer.getLayer().getGeometryType();
        String layername = BaseActivity.myLayer.getLname();

        ToastUtil.setToast(baseActivity, layername);
        //SytemUtil.getEditSymbo(baseActivity, BaseActivity.myLayer.getLayer());
    }

    /**
     * 新增线
     * polyline_all 绘制的线图形
     */
    public void addFeatureLine(Polyline polyline_all) {
        if (polyline_all == null) {
            ToastUtil.setToast(baseActivity, "请勾绘线");
            return;
        }

        int size = polyline_all.getPointCount();
        MultiPath multiPath = new Polyline();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                multiPath.startPath(polyline_all.getPoint(i));
            } else {
                multiPath.lineTo(polyline_all.getPoint(i));
            }
        }

        Geometry geom = GeometryEngine.simplify(multiPath, iBaseView.getSpatialReference());
        addFeatureOnLayer(geom, baseActivity.layerFeatureAts);

        baseActivity.mapRemove(new View(baseActivity));
    }

    /**
     * 新增面
     */
    public void addFeaturePolygon(Polygon polygon) {
        if (polygon == null) {
            ToastUtil.setToast(baseActivity, "请先勾绘图斑");
            return;
        }
        if (polygon.isEmpty()) {
            return;
        }
        if (polygon.getPointCount() < 3) {
            return;
        }
        if (!polygon.isValid()) {
            return;
        }

        Geometry geom = GeometryEngine.simplify(polygon, iBaseView.getSpatialReference());
        addFeatureOnLayer(geom, baseActivity.layerFeatureAts);
        baseActivity.mapRemove(new View(baseActivity));
    }

    /**
     * 图形更新，
     * 勾绘后的geom
     * 更新前的feature
     * myLayer feature所在图层
     */
    public Graphic updateFeature(Geometry geom, GeodatabaseFeature feature, MyLayer myLayer) {
        Graphic graphic = null;
        try {
            graphic = new Graphic(geom, feature.getSymbol(), feature.getAttributes());
            if (!geom.isValid() || geom.isEmpty()) {
                return graphic;
            }
            FeatureTable featureTable = myLayer.getTable();
            featureTable.updateFeature(feature.getId(), graphic);
			/* 添加小班后 记录添加小班的id 备撤销时删除 */
            recordXb(feature.getId(), "update", feature.getAttributes(), feature.getGeometry(), myLayer.getLayer());
        } catch (TableException e) {
            ToastUtil.setToast(baseActivity, e.getMessage());
            return null;
        }
        return graphic;
    }

    /**
     * 删除feature的方法
     * id  feature的id
     */
    public void delFeature(GeodatabaseFeature feature) {
        MyLayer myLayer = BaseUtil.getIntance(baseActivity).getFeatureInLayer(seflayerName, BaseActivity.layerNameList);
        FeatureTable featureTable = myLayer.getTable();
        try {
            featureTable.deleteFeature(feature.getId());
            recordXb(feature.getId(), "delete", feature.getAttributes(), feature.getGeometry(), myLayer.getLayer());
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录变化的 以备撤销使用
     * @param id             小班id
     * @param type           修改类型 添加 更新 删除
     * @param attr           变动小班的属性信息
     * @param geom           变动小班
     * @param layer          变动小班所在图层
     */
    public void recordXb(long id, String type, Map<String, Object> attr,Geometry geom, FeatureLayer layer) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        map.put("attribute", attr);
        map.put("geometry", geom);
        map.put("layer", layer);
        baseActivity.undonList.add(map);
    }

    /**
     * 添加feature在图层上
     * geom 绘制的图形
     * selFeatureAts  图层属性
     */
    public void addFeatureOnLayer(Geometry geom, Map<String, Object> selFeatureAts) {
        try {
            if (geom.isEmpty() || !geom.isValid()) {
                return;
            }

            Envelope envelope = new Envelope();
            geom.queryEnvelope(envelope);
            if (envelope.isEmpty() || !envelope.isValid()) {
                return;
            }

            GeodatabaseFeatureTable table =  BaseActivity.myLayer.getTable();
            GeodatabaseFeature g = table.createFeatureWithTemplate(BaseActivity.layerTemplate, geom);
            Symbol symbol = BaseActivity.myLayer.getRenderer().getSymbol(g);
            // symbol为null也可以 why？
            Map<String, Object> editAttributes = null;
            if (selFeatureAts == null) {
                editAttributes = g.getAttributes();
            } else {
                editAttributes = selFeatureAts;
            }
            //TODO
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = format.format(new Date());
            editAttributes.put("WYBH", str);

            Graphic addedGraphic = new Graphic(geom, symbol, editAttributes);
            long id = table.addFeature(addedGraphic);

            Feature feature = table.getFeature(id);
           Geometry geometry = feature.getGeometry();
            Log.e("tag",myLayer.toString());
            if (geometry.isEmpty() || !geometry.isValid()) {
                table.deleteFeature(id);
            } else {
				/* 添加小班后 记录添加小班的id 备撤销时删除 */
                recordXb(id, "add", editAttributes, geom, BaseActivity.myLayer.getLayer());
            }
        } catch (TableException e) {
            e.printStackTrace();
        }
        mapView.invalidate();
    }

    /**
     * 添加feature在图层上
     */
    public void addFeatureGbmOnLayer(Geometry geom, Map<String, Object> selectFeatureAts) {
        try {
            if (!geom.isValid() || geom.isEmpty()) {
                return;
            }
            if (geom.getType() == Geometry.Type.POLYGON) {
                Polygon polygon = (Polygon) geom;
                if (polygon.getPointCount() < 3) {
                    return;
                }
                double dd = geom.calculateArea2D();
                if (dd <= 0) {
                    return;
                }
            } else if (geom.getType() == Geometry.Type.POLYLINE || geom.getType() == Geometry.Type.LINE) {
                Polyline polygon = (Polyline) geom;
                if (polygon.getPointCount() < 2) {
                    return;
                }
                double dd = geom.calculateLength2D();
                if (dd <= 0) {
                    return;
                }
            }
//            GeodatabaseFeatureTable table = BaseActivity.myLayer.getTable();
//            GeodatabaseFeature g = table.createFeatureWithTemplate(BaseActivity.layerTemplate, geom);
            Feature g =  BaseActivity.selGeoFeature;
            Symbol symbol = BaseActivity.myLayer.getRenderer().getSymbol(g);
            // symbol为null也可以 why？
//            if (selectFeatureAts == null) {
//                selectFeatureAts = g.getAttributes();
//            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = format.format(new Date());
            selectFeatureAts.put("WYBH", str);

            Geometry[] geometries = new Geometry[BaseActivity.selGeoFeaturesList.size() + 1];
            for (int i = 0; i < BaseActivity.selGeoFeaturesList.size(); i++) {
                geometries[i] = BaseActivity.selGeoFeaturesList.get(i).getGeometry();
            }
            geometries[BaseActivity.selGeoFeaturesList.size()] = geom;

            Geometry geometry = GeometryEngine.union(geometries, iBaseView.getSpatialReference());
            for (int i = 0; i < BaseActivity.selGeoFeaturesList.size(); i++) {
                Geometry geometry2 = BaseActivity.selGeoFeaturesList.get(i).getGeometry();
                geometry = GeometryEngine.difference(geometry, geometry2, BaseActivity.spatialReference);
            }

            FeatureTable featureTable = BaseActivity.myLayer.getTable();
            Graphic addedGraphic = new Graphic(geometry, symbol, selectFeatureAts);
            long id = featureTable.addFeature(addedGraphic);

			/* 添加小班后 记录添加小班的id 备撤销时删除 */
            recordXb(id, "add", selectFeatureAts, geometry, BaseActivity.myLayer.getLayer());
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(baseActivity, e.getMessage());
        }

        mapView.invalidate();
    }

    /**
     * 新增点
     */
    public void addFeaturePoint(Point point_all) {
        Geometry geom = GeometryEngine.simplify(point_all, iBaseView.getSpatialReference());
        if (!geom.isValid()) {
            ToastUtil.setToast(baseActivity, "未选择添加位置");
            return;
        }

        String pname = BaseActivity.myLayer.getPname();
        try {
            DecimalFormat format = new DecimalFormat("0.000000");
//            GeodatabaseFeatureTable table =(GeodatabaseFeatureTable) BaseActivity.myLayer.getTable();
//            GeodatabaseFeature g = table.createFeatureWithTemplate(BaseActivity.layerTemplate, geom);
            List<Field> pointFields = BaseActivity.myLayer.getTable().getFields();
            for (Field field : pointFields) {
                if (field.getAlias().contains("横坐标") || field.getAlias().contains("经度")) {
                    baseActivity.layerFeatureAts.put(field.getName(), format.format(point_all.getX()));
                    continue;
                } else if (field.getAlias().contains("纵坐标") || field.getAlias().contains("纬度")) {
                    baseActivity.layerFeatureAts.put(field.getName(), format.format(point_all.getY()));
                    continue;
                } else if (pname.contains("营造林") && (field.getAlias().contains("样地类型") || field.getName().equals("YDLX"))) {
                    if (iBaseView.getCurrentPoint() != null && point_all.equals(iBaseView.getCurrentPoint())) {
                        baseActivity.layerFeatureAts.put(field.getName(), 2);
                    } else {
                        baseActivity.layerFeatureAts.put(field.getName(), 1);
                    }
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addFeatureOnLayer(geom, baseActivity.layerFeatureAts);
        baseActivity.mapRemove(new View(baseActivity));
    }

    /**
     * 检测面上的点 是否共线
     */
    public boolean checkGeom(Polygon polygon) {
        int size = polygon.getPointCount();
        Point spoint = polygon.getPoint(0);
        Point epoint = polygon.getPoint(size - 1);
        double aa = (epoint.getY() - spoint.getY()) / (epoint.getX() - spoint.getX());
        for (int i = 0; i < size - 2; i++) {
            Point p1 = polygon.getPoint(i);
            for (int j = i + 1; j < size - 1; j++) {
                Point p2 = polygon.getPoint(j);
                double bb = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
                if (bb != aa) {
                    return false;
                }
            }
        }
        return true;
    }

}
