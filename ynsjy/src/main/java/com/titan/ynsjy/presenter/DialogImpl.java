package com.titan.ynsjy.presenter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.titan.baselibrary.util.DialogParamsUtil;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.MyExpandableListAdapter;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.entity.GjPoint;
import com.titan.ynsjy.mview.IDialogDao;
import com.titan.ynsjy.timepaker.TimePopupWindow;
import com.titan.ynsjy.util.GeometryUtil;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.Util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by li on 2017/2/23.
 * 坐标定位diaolog
 */

public class DialogImpl implements IDialogDao {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    List<GjPoint> list = new ArrayList<GjPoint>();
    Handler handler = new Handler();
    Runnable runnable;
    @Override
    public void showGjcxView(final BaseActivity activity, final View view, MapView mapView, GraphicsLayer layer) {
        view.setVisibility(View.VISIBLE);
        ImageView close = (ImageView) view.findViewById(R.id.close_guijisearch);
        close.setOnClickListener(new CancleLisence(view,layer));


        final Button startBtn = (Button) view.findViewById(R.id.startTime);
        final Button endBtn = (Button) view.findViewById(R.id.endTime);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSelectTimePopuwindow(activity,startBtn,true);
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSelectTimePopuwindow(activity,endBtn,false);
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.select_lately);
        final String[] strArray = activity.getResources().getStringArray(R.array.hour_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.myspinner, strArray);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String timenearly = strArray[i];
                    Date dDate = new Date();
                    dDate.setHours(dDate.getHours()- Integer.parseInt(timenearly));
                    startBtn.setText(format.format(dDate));
                    endBtn.setText(format.format(new Date()));
                } else {
                    final Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    startBtn.setText(format.format(cal.getTime()));
                    endBtn.setText(format.format(new Date()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ExpandableListView listView = (ExpandableListView)view.findViewById(R.id.zuji_expandableListView);

        TextView replayBtn = (TextView) view.findViewById(R.id.btn_replay);
        TextView guijiBtn = (TextView) view.findViewById(R.id.guijibutton);
        TextView zujiBtn = (TextView) view.findViewById(R.id.zujibutton);
        guijiBtn.setOnClickListener(new GujiListener(activity,startBtn,endBtn,replayBtn,mapView,layer,listView));
        zujiBtn.setOnClickListener(new GujiListener(activity,startBtn,endBtn,replayBtn,mapView,layer,listView));
        replayBtn.setOnClickListener(new GujiListener(activity,startBtn,endBtn,replayBtn,mapView,layer,listView));

    }

    class GujiListener implements View.OnClickListener{
        BaseActivity activity;
        TextView startBtn;
        TextView endBtn;
        TextView replayBtn;
        MapView mapView;
        GraphicsLayer graphicsLayer;
        ExpandableListView listView;
        GujiListener(BaseActivity activity,TextView startBtn,TextView endBtn,TextView replayBtn,MapView mapView, GraphicsLayer layer,ExpandableListView expandableListView){
            this.activity = activity;
            this.startBtn = startBtn;
            this.endBtn = endBtn;
            this.mapView = mapView;
            this.graphicsLayer = layer;
            this.listView = expandableListView;
            this.replayBtn = replayBtn;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.guijibutton:
                    getGjData(activity,startBtn,endBtn,replayBtn,mapView,graphicsLayer);
                    break;
                case R.id.zujibutton:
                    getZujiData(activity,startBtn,endBtn,replayBtn,mapView,graphicsLayer,listView);
                    break;
                case R.id.btn_replay:
                    gjReplayData(activity,mapView,graphicsLayer);
                    break;
                default:
                    break;
            }
        }
    }
    /*轨迹回放*/
    int id = -1;
    Point bfPoint;
    List<Integer> gidList = new ArrayList<>();
    public void gjReplayData(final BaseActivity activity, final MapView mapView, final GraphicsLayer layer){
        layer.removeGraphic(id);
        for(int i=0;i<gidList.size();i++){
            layer.removeGraphic(gidList.get(i));
        }
        id = -1;
        int count = list.size();
        final Polyline polyline = new Polyline();
        final LineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 3);;
        for(int i=count-1;i>=0;i--){
            GjPoint gjPoint = list.get(i);
            final double lon = Double.parseDouble(gjPoint.getLon());
            final double lat = Double.parseDouble(gjPoint.getLat());
            Point point = new Point(lon,lat);

            Graphic graphic = new Graphic(point,new SimpleMarkerSymbol(Color.RED,10, SimpleMarkerSymbol.STYLE.CIRCLE));
            if(i==count-1){
                polyline.startPath(point);
            }else{

                Point midpoint = Util.getMidPoint(bfPoint,point);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PictureMarkerSymbol markerSymbol = getBiticon(activity,bfPoint,point);
                Graphic graphic2 = new Graphic(midpoint,markerSymbol);
                polyline.lineTo(midpoint);
                layer.updateGraphic(id,polyline);
                gidList.add(layer.addGraphic(graphic2));

                polyline.lineTo(point);
            }

            bfPoint = point;
            int size = polyline.getPointCount();
            Graphic graphic1 = new Graphic(polyline,lineSymbol);

            if(id == -1){
                gidList.add(layer.addGraphic(graphic));
                id = layer.addGraphic(graphic1);
            }else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gidList.add(layer.addGraphic(graphic));
                layer.updateGraphic(id,polyline);
            }
        }
    }
    /*获取旋转后的图片*/
    public PictureMarkerSymbol getBiticon(Context context,Point p1,Point p2){
        /**
         * 计算角度
         */
        Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(),R.drawable.droparrow4);
        double lat1 = p1.getY(), lat2 = p2.getY(); // 点1坐标;
        double lon1 = p1.getX(), lon2 = p2.getX();// 点2坐标
        double x = Math.abs(lat1 - lat2);
        double y = Math.abs(lon1 - lon2);
        double z = Math.sqrt(x * x + y * y);
        int jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));// 最终角度
        x = lon1 - lon2;
        y = lat1 - lat2;

        if (x > 0 && y < 0) {// 在第二象限
            jiaodu = 0 - jiaodu;
        }
        if (x > 0 && y > 0) {// 在第三象限
            jiaodu = jiaodu + 90;
        }
        if (x < 0 && y > 0) {// 在第四象限
            jiaodu = 180 + (90 - jiaodu);
        }

        /**
         * 旋转图标
         */
        Matrix matrix = new Matrix();
        matrix.postRotate(jiaodu);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,bitmapOrg.getWidth(), bitmapOrg.getHeight(),matrix,true);
        Drawable drawable =new BitmapDrawable(resizedBitmap);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(context,drawable);
        return symbol;
    }

    /*轨迹查询按钮*/
    public void getGjData(Context context,TextView startBtn,TextView endBtn,TextView replayBtn,MapView mapView, GraphicsLayer layer){
        long timeStart=0,timeEnd=0;
        String start="",end="";
        try {
            start = startBtn.getText().toString();
            timeStart = format.parse(start).getTime();
            end = endBtn.getText().toString();
            timeEnd = format.parse(end).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(timeStart > timeEnd){
            ToastUtil.setToast(context,"开始时间不能大于结束时间");
            return;
        }
        list.clear();
        list = DataBaseHelper.selectPointGuiji(context, MyApplication.macAddress, start, end);
        if (list.size() == 0)
            return;
        replayBtn.setEnabled(true);
        int length = list.size();
        Polyline polyline = new Polyline();
        Polygon polygon = null;
        for (int i = length - 1; i >= 0; i--) {
            double lon = Double.parseDouble(list.get(i).getLon());
            double lat = Double.parseDouble(list.get(i).getLat());

            if (i == 0) {// 终点
                Point endtPoint = new Point(lon, lat);
                PictureMarkerSymbol endPicture = new PictureMarkerSymbol(
                        context.getResources().getDrawable(R.drawable.nav_route_result_end_point));
                endPicture.setOffsetY(18);
                layer.addGraphic(new Graphic(endtPoint, endPicture));
                polyline.lineTo(endtPoint);
                //processbar.setMax(listExpandable.size());
            } else if (i == length - 1) {// 起点
                Point startPoint = new Point(lon, lat);
                polygon = creatPolygon(startPoint);
                PictureMarkerSymbol startpicture = new PictureMarkerSymbol(
                        context.getResources().getDrawable(R.drawable.nav_route_result_start_point));
                startpicture.setOffsetY(18);
                layer.addGraphic(new Graphic(startPoint, startpicture));
                polyline.startPath(startPoint);
                //listExpandable.add(list.get(i));
            } else {
                Point point = new Point(lon, lat);
                SpatialReference reference = SpatialReference.create(3857);
                boolean flag = GeometryEngine.intersects(polygon, point,reference);
                if (!flag) {
                    polyline.lineTo(point);
                    polygon = creatPolygon(point);
                }
            }
        }
        try {
            GeometryUtil.addPolylineToGraphicsLayer(polyline, Color.BLUE, 2, layer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Polygon polygon1 = GeometryEngine.buffer(polyline,SpatialReference.create(3857),500,null);
        mapView.setExtent(polygon1);
    }

    /** 时间选择popupwindow */
    public void initSelectTimePopuwindow(Context context,final Button button, boolean isBefore) {
        TimePopupWindow timePopupWindow = new TimePopupWindow(context, TimePopupWindow.Type.ALL);
        timePopupWindow.setCyclic(true);
        // 时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                button.setText(format.format(date));
            }
        });
        timePopupWindow.showAtLocation(button, Gravity.BOTTOM, 0, 0,new Date(), isBefore);
    }

    /*获取足迹数据并展示*/
    public void getZujiData(Context context,TextView startBtn,TextView endBtn,TextView replayBtn,MapView mapView, GraphicsLayer layer,ExpandableListView listView){
        long timeStart=0,timeEnd=0;
        String start="",end="";
        try {
            start = startBtn.getText().toString();
            timeStart = format.parse(start).getTime();
            end = endBtn.getText().toString();
            timeEnd = format.parse(end).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(timeStart > timeEnd){
            ToastUtil.setToast(context,"开始时间不能大于结束时间");
            return;
        }
        list.clear();
        list = DataBaseHelper.selectPointGuiji(context, MyApplication.macAddress, start, end);
        int length = list.size();
        if (list.size() == 0)
            return;
        replayBtn.setEnabled(true);
        listView.setVisibility(View.VISIBLE);
        showExpandableListView(context,listView,list,mapView,layer);
    }

    /** 展示expandablelistview数据 */
    public void showExpandableListView(final Context ctx,ExpandableListView expandable,List<GjPoint> list,
                                       final MapView mapView,final GraphicsLayer layer) {
        int m = list.size();
        Set<String> groupSet = new HashSet<String>();
        final List<HashMap<Object, String>> childArray = new ArrayList<HashMap<Object, String>>();
        for (int i = 0; i < m; i++) {
            groupSet.add(list.get(i).getTime().toString().substring(0, 10));
        }
        final String[] groups = new String[groupSet.size()];
        final String[][] childs = new String[groups.length][];
        final String[][] lons = new String[groups.length][];
        final String[][] lats = new String[groups.length][];
        Iterator<String> iterator = groupSet.iterator();
        while (iterator.hasNext()) {
            for (int i = 0; i < groupSet.size(); i++) {
                groups[i] = iterator.next();
            }
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> dateList = new ArrayList<Date>();
        for (int i = 0; i < groups.length; i++) {
            try {
                dateList.add(df.parse(groups[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date tempDate = null;
        for (int i = dateList.size() - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (dateList.get(j + 1).after(dateList.get(j))) {
                    tempDate = dateList.get(j);
                    dateList.set(j, dateList.get(j + 1));
                    dateList.set(j + 1, tempDate);
                }
            }
        }
        for (int i = 0; i < dateList.size(); i++) {
            groups[i] = df.format(dateList.get(i));
        }

        for (int i = 0; i < groups.length; i++) {
            int k = 0;
            for (int j = 0; j < m; j++) {
                if (list.get(j).getTime().substring(0, 10).equals(groups[i])) {
                    String str = list.get(j).getTime();
                    HashMap<Object, String> map = new HashMap<Object, String>();
                    map.put(groups[i], str.substring(11, str.length()));
                    map.put("lon", list.get(j).getLon());
                    map.put("lat", list.get(j).getLat());
                    childArray.add(map);
                    k++;
                }
            }
            childs[i] = new String[k];
            lons[i] = new String[k];
            lats[i] = new String[k];
        }

        for (int i = 0; i < groups.length; i++) {
            int k = 0;
            for (int j = 0; j < childArray.size(); j++) {
                if (childArray.get(j).get(groups[i]) != null) {
                    childs[i][k] = childArray.get(j).get(groups[i]);
                    lons[i][k] = childArray.get(j).get("lon");
                    lats[i][k] = childArray.get(j).get("lat");
                    k++;
                }
            }
        }

        MyExpandableListAdapter treeAadpter = new MyExpandableListAdapter(ctx, groups, childs);
        expandable.setGroupIndicator(null);
        treeAadpter.notifyDataSetChanged();
        expandable.setAdapter(treeAadpter);
        expandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        final int parent, final int children, long arg4) {
                layer.removeAll();
                double lon = Double.parseDouble(lons[parent][children]);
                double lat = Double.parseDouble(lats[parent][children]);
                Point point = new Point(lon, lat);
                PictureMarkerSymbol pic = new PictureMarkerSymbol(ctx.getResources().getDrawable(R.drawable.icon_gcoding));
                pic.setOffsetY(20);
                Graphic graphic = new Graphic(point, pic);
                if (graphic != null) {
                    layer.addGraphic(graphic);
                }
                mapView.setExtent(point);
                mapView.invalidate();
                return false;
            }
        });
    }

    /** 创建一个半径为5m的圆 */
    public Polygon creatPolygon(Point point) {
        SpatialReference reference = SpatialReference.create(3857);
        //Unit 为null时 单位为 米
        return GeometryEngine.buffer(point, reference, 5, null);
    }

    @Override
    public void showZbdwView(final Context context, double lon, double lat,final MapView mapView, final GraphicsLayer layer) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.location_input);
        final RadioButton radio_reft = (RadioButton) dialog.findViewById(R.id.radio_btn_left);
        radio_reft.setChecked(true);
        final RadioButton radio_center = (RadioButton) dialog.findViewById(R.id.radio_btn_center);
        final RadioButton radio_right = (RadioButton) dialog.findViewById(R.id.radio_btn_right);
        final LinearLayout layout_type01 = (LinearLayout) dialog.findViewById(R.id.layout_type01);
        final LinearLayout layout_type02 = (LinearLayout) dialog.findViewById(R.id.layout_type02);
        final LinearLayout layout_type03 = (LinearLayout) dialog.findViewById(R.id.layout_type03);
        radio_reft.setTextColor(context.getResources().getColor(R.color.blue));
        layout_type01.setVisibility(View.VISIBLE);
        layout_type02.setVisibility(View.GONE);
        layout_type03.setVisibility(View.GONE);
        radio_reft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    radio_reft.setTextColor(context.getResources().getColor(R.color.blue));
                    radio_center.setTextColor(context.getResources().getColor(R.color.balck));
                    radio_right.setTextColor(context.getResources().getColor(R.color.balck));
                    layout_type01.setVisibility(View.VISIBLE);
                    layout_type02.setVisibility(View.GONE);
                    layout_type03.setVisibility(View.GONE);
                }

            }
        });
        radio_center.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    radio_reft.setTextColor(context.getResources().getColor(R.color.balck));
                    radio_center.setTextColor(context.getResources().getColor(R.color.blue));
                    radio_right.setTextColor(context.getResources().getColor(R.color.balck));
                    layout_type01.setVisibility(View.GONE);
                    layout_type02.setVisibility(View.VISIBLE);
                    layout_type03.setVisibility(View.GONE);
                }
            }
        });

        radio_right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean arg1) {
                if (arg1) {
                    radio_reft.setTextColor(context.getResources().getColor(R.color.balck));
                    radio_center.setTextColor(context.getResources().getColor(R.color.balck));
                    radio_right.setTextColor(context.getResources().getColor(R.color.blue));
                    layout_type01.setVisibility(View.GONE);
                    layout_type02.setVisibility(View.GONE);
                    layout_type03.setVisibility(View.VISIBLE);
                }
            }
        });
        final EditText edit_jd = (EditText) dialog.findViewById(R.id.edit_jd);
        final EditText edit_wd = (EditText) dialog.findViewById(R.id.edit_wd);
        final EditText edit_jd_d = (EditText) dialog.findViewById(R.id.edit_jd_d);
        final EditText edit_jd_f = (EditText) dialog.findViewById(R.id.edit_jd_f);
        final EditText edit_jd_m = (EditText) dialog.findViewById(R.id.edit_jd_m);
        final EditText edit_wd_d = (EditText) dialog.findViewById(R.id.edit_wd_d);
        final EditText edit_wd_f = (EditText) dialog.findViewById(R.id.edit_wd_f);
        final EditText edit_wd_m = (EditText) dialog.findViewById(R.id.edit_wd_m);
        // 米制x
        final EditText edit_x = (EditText) dialog.findViewById(R.id.edit_x);
        // 米制y
        final EditText edit_y = (EditText) dialog.findViewById(R.id.edit_y);

        DecimalFormat format = new DecimalFormat("0.000000");
        TextView lonview = (TextView) dialog.findViewById(R.id.location_lon);
        lonview.setText(format.format(lon));

        TextView latview = (TextView) dialog.findViewById(R.id.location_lat);
        latview.setText(format.format(lat));


        // 确定按钮
        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //
                Point point = null;
                double x = 0, y = 0;
                if (radio_reft.isChecked()) {
                    // 经纬度格式
                    if (TextUtils.isEmpty(edit_jd.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    //
                    x = Double.valueOf(edit_jd.getText().toString().trim());
                    y = Double.valueOf(edit_wd.getText().toString().trim());

                    // 判断经纬度是否在中国境内
                    if (x > 135 || x < 74 || y > 54 || y < 3) {
                        ToastUtil.setToast(context, "坐标范围不在中国内,请重新输入");
                        return;
                    }

                } else if (radio_center.isChecked()) {
                    // 度分秒格式
                    if (TextUtils.isEmpty(edit_jd_d.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_jd_f.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_jd_m.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd_d.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd_f.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd_m.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    //
                    x = Integer.valueOf(edit_jd_d.getText().toString().trim())
                            + (double) Integer.valueOf(edit_jd_f.getText()
                            .toString().trim()) / 60
                            + (double) Integer.valueOf(edit_jd_m.getText()
                            .toString().trim()) / 3600;
                    y = Integer.valueOf(edit_wd_d.getText().toString().trim())
                            + (double) Integer.valueOf(edit_wd_f.getText()
                            .toString().trim()) / 60
                            + (double) Integer.valueOf(edit_wd_m.getText()
                            .toString().trim()) / 3600;

                    // 判断经纬度是否在中国境内
                    if (x > 135 || x < 74 || y > 54 || y < 3) {
                        ToastUtil.setToast(context, "坐标范围不在中国内,请重新输入");
                        return;
                    }

                } else if (radio_right.isChecked()) {
                    if (TextUtils.isEmpty(edit_x.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_y.getText().toString())) {
                        ToastUtil.setToast(context, "请输入对应数据");
                        return;
                    }
                    // 还有问题
                    x = Double.parseDouble(edit_x.getText().toString());
                    y = Double.parseDouble(edit_y.getText().toString());
                }

                if (radio_reft.isChecked()) {
                    /*Pro4jCoordinateImpl pro4jImpl = new Pro4jCoordinateImpl();
                    SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    point = pro4jImpl.getXian80Point(sp, new Point(x, y), 0);*/
                    point =(Point) GeometryEngine.project(new Point(x,y),SpatialReference.create(4326),mapView.getSpatialReference());
                } else if (radio_center.isChecked()) {
                    point = (Point) GeometryEngine.project(new Point(x,y),SpatialReference.create(4326),mapView.getSpatialReference());
                } else if (radio_right.isChecked()) {
                    point = (Point) GeometryEngine.project(new Point(x,y),SpatialReference.create(4326),mapView.getSpatialReference());
                }

                layer.removeAll();
                PictureMarkerSymbol pic = new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.icon_gcoding));
                pic.setOffsetY(20);
                Graphic graphic = new Graphic(point, pic);
                layer.addGraphic(graphic);
                mapView.setExtent(point, 0, true);
                dialog.dismiss();
            }
        });

        ImageView text_close = (ImageView) dialog.findViewById(R.id.text_close);
        text_close.setOnClickListener(new CancleLisence(dialog));
        DialogParamsUtil.setDialogParamsCenter(context,dialog,0.6, 0.65);
    }

    /**dialog的dismiss*/
    class CancleLisence implements View.OnClickListener{
        Dialog dialog;
        View closeView;
        GraphicsLayer graphicsLayer;
        public CancleLisence(Dialog dialog) {
            this.dialog = dialog;
        }
        public CancleLisence(View pView,GraphicsLayer graphicsLayer) {
            this.closeView = pView;
            this.graphicsLayer = graphicsLayer;
        }

        @Override
        public void onClick(View view) {
            if(dialog != null){
                graphicsLayer.removeAll();
                dialog.dismiss();
            }
            if(closeView != null){
                closeView.setVisibility(View.GONE);
            }
        }
    }
}
