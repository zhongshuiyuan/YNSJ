package com.titan.ynsjy.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.ExpandableAdapter;
import com.titan.ynsjy.adapter.ImgTucengAdapter;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.mview.LayerControlView;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.SytemUtil;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.titan.ynsjy.BaseActivity.seflayerName;

/**
 * Created by li on 2017/5/5.
 * 图层控制Presenter
 */

public class LayerControlPresenter{

    private Context mContext;
    private LayerControlView controlView;
    /** 初始化otms中小班数据 */
    public Map<String, ArcGISLocalTiledLayer> imgTileLayerMap = new HashMap<>();
    /** 影像图文件地址 */
    public HashMap<String, Boolean> imgCheckMap = new HashMap<>();

    public String gname;
    public String cname;
    public String path;

    private View childView;
    private View.OnClickListener onClickListener;

    public LayerControlPresenter(Context ctx, LayerControlView view, View.OnClickListener onClickListener){
        this.mContext = ctx;
        this.controlView = view;
        this.onClickListener = onClickListener;
        childView = controlView.getParChildView();
    }


    /** 图层控制加载otms数据 */
    public void initOtmsData() {
        // 基础图
        CheckBox cb_sl = (CheckBox) childView.findViewById(R.id.cb_sl);
        if (controlView.getTitleLayer() != null) {
            cb_sl.setChecked(controlView.getTitleLayer().isVisible());
        }
        // 基础图
        cb_sl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, final boolean flag) {
                if(controlView.getTitleLayer() != null){
                    controlView.getTitleLayer().setVisible(flag);
                }
            }
        });
        // 基础图 缩放到地图范围
        ImageView tileView = (ImageView)childView.findViewById(R.id.tile_extent);
        tileView.setOnClickListener(onClickListener);

        //地形图
        CheckBox cb_dxt = (CheckBox) childView.findViewById(R.id.cb_dxt);
        if (controlView.getDxtLayer() != null) {
            cb_dxt.setChecked(controlView.getDxtLayer().isVisible());
        }
        // 地形图
        cb_dxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, final boolean flag) {
                if(controlView.getDxtLayer() != null && controlView.getDxtLayer().isInitialized()){
                    controlView.getDxtLayer().setVisible(flag);
                }
            }
        });
        // 地形图 缩放到地图范围
        ImageView dxtTileView = (ImageView) childView.findViewById(R.id.dxt_extent);
        dxtTileView.setOnClickListener(onClickListener);

        // 影像图
        CheckBox cb_yx = (CheckBox) childView.findViewById(R.id.cb_ys);
        if (BussUtil.objEmperty(controlView.getImgLayer())) {
            cb_yx.setChecked(controlView.getImgLayer().isVisible());
        }
        // 影像
        cb_yx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                List<File> fileList = MyApplication.resourcesManager.getImgTitlePath();
                if (arg1) {
                    if (fileList.size() == 1) {
                        File file = new File(fileList.get(0).getPath());
                        if(!file.exists()){
                            ToastUtil.setToast(mContext, "影像数据文件不存在");
                            return;
                        }
                        if(controlView.getDxtLayer() != null && controlView.getDxtLayer().isInitialized()){
                            controlView.getMapView().removeLayer(controlView.getDxtLayer());
                        }
                        if(controlView.getGraphicLayer() != null && controlView.getGraphicLayer().isInitialized()){
                            controlView.getMapView().removeLayer(controlView.getGraphicLayer());
                        }

                        if(controlView.getLayerNameList().size() > 0){
                            for(MyLayer myLayer :controlView.getLayerNameList()){
                                controlView.getMapView().removeLayer(myLayer.getLayer());
                            }
                        }

                        controlView.addImageLayer(fileList.get(0).getPath());

                        if(controlView.getDxtLayer() != null){
                            controlView.getMapView().addLayer(controlView.getDxtLayer());
                        }

                        if(controlView.getLayerNameList().size() > 0){
                            for(MyLayer myLayer :controlView.getLayerNameList()){
                                controlView.getMapView().addLayer(myLayer.getLayer());
                            }
                        }
                        controlView.addGraphicLayer();
                    } else {
                        showImgLayerSelect(fileList, imgTileLayerMap);
                    }
                } else {
                    if (BussUtil.objEmperty(controlView.getImgLayer())) {
                        if (controlView.getImgLayer().isVisible()) {
                            controlView.getImgLayer().setVisible(false);
                        }
                    } else {
                        controlView.getImgeLayerView().setVisibility(View.GONE);
                        if (fileList != null) {
                            for (int i = 0; i < fileList.size(); i++) {
                                String name = fileList.get(i).getName();
                                if (imgTileLayerMap.get(name) != null) {
                                    controlView.getMapView().removeLayer(imgTileLayerMap.get(name));
                                    imgTileLayerMap.remove(name);
                                }
                                imgCheckMap.put(name,false);
                            }
                        }
                    }
                }
            }
        });
        // 影像图所放到地图范围
        ImageView imageView = (ImageView) childView.findViewById(R.id.image_extent);
        imageView.setOnClickListener(onClickListener);

        initOtmsData("");

        ImageView closeView = (ImageView) childView.findViewById(R.id.close_tuceng);
        closeView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                controlView.getTckzView().setVisibility(View.GONE);
            }
        });

    }

    /**影像数据选择*/
    private void showImgLayerSelect(final List<File> list,final Map<String, ArcGISLocalTiledLayer> imgTileLayerMap) {
        controlView.getImgeLayerView().setVisibility(View.VISIBLE);

        initImgCheckBoxData(list);
        final ImgTucengAdapter adapter = new ImgTucengAdapter((BaseActivity) mContext,list,imgCheckMap,imgTileLayerMap);
        ListView listView = (ListView) controlView.getImgeLayerView().findViewById(R.id.img_tcselector);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                boolean isChecked = imgCheckMap.get(list.get(position).getName());
                if (isChecked) {
                    imgCheckMap.put(list.get(position).getName(), false);
                    ArcGISLocalTiledLayer layerindex = imgTileLayerMap.get(list.get(position).getName());
                    if(layerindex != null && layerindex.isInitialized()){
                        controlView.getMapView().removeLayer(layerindex);
                        imgTileLayerMap.remove(list.get(position).getName());
                    }
                } else {
                    imgCheckMap.put(list.get(position).getName(), true);
                    String path = list.get(position).getPath();
                    if(!new File(path).exists()){
                        ToastUtil.setToast(mContext, "影像数据文件不存在");
                        return;
                    }
                    if(controlView.getDxtLayer() != null && controlView.getDxtLayer().isInitialized()){
                        controlView.getMapView().removeLayer(controlView.getTitleLayer());
                    }
                    if(controlView.getGraphicLayer() != null && controlView.getGraphicLayer().isInitialized()){
                        controlView.getMapView().removeLayer(controlView.getGraphicLayer());
                    }

                    if(controlView.getLayerNameList().size() > 0){
                        for(MyLayer myLayer :controlView.getLayerNameList()){
                            controlView.getMapView().removeLayer(myLayer.getLayer());
                        }
                    }
                    ArcGISLocalTiledLayer arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(path);
                    controlView.getMapView().addLayer(arcGISLocalTiledLayer);

                    if(controlView.getLayerNameList().size() > 0){
                        for(MyLayer myLayer :controlView.getLayerNameList()){
                            if(myLayer.getLayer() != null && myLayer.getLayer().isInitialized()){
                                controlView.getMapView().addLayer(myLayer.getLayer());
                            }
                        }
                    }

                    controlView.addGraphicLayer();

                    imgTileLayerMap.put(list.get(position).getName(),arcGISLocalTiledLayer);
                }
                adapter.notifyDataSetChanged();
            }
        });

        ImageView imageView = (ImageView) childView.findViewById(R.id.btselect_info_close);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                controlView.getImgeLayerView().setVisibility(View.GONE);
            }
        });
    }

    /** 图层控制  根据区县 加载otms中资源数据 */
    public void initOtmsData(String qname) {
        final List<File> groups = MyApplication.resourcesManager.getOtmsFolder(controlView.getSysLayerData());
        if(groups == null || groups.size() ==0){
            return;
        }
//        ExpandableListView tc_exp = (ExpandableListView) childView.findViewById(R.id.tc_expandlistview);
//        TckzListViewAdapter expandableAdapter = new TckzListViewAdapter(mContext, groups);
//        tc_exp.setAdapter(expandableAdapter);
        final List<Map<String, List<File>>> childs = MyApplication.resourcesManager.getChildeData(mContext,groups);
        if (childs == null || childs.size() == 0)
            return;
        ExpandableListView tc_exp = (ExpandableListView) childView.findViewById(R.id.tc_expandlistview);
        tc_exp.setGroupIndicator(null);
        initCheckbox(groups, childs);

        final ExpandableAdapter expandableAdapter = new ExpandableAdapter((BaseActivity)mContext, groups, childs, controlView.getLayerCheckBox());
        tc_exp.setAdapter(expandableAdapter);

        setExpendHeight(expandableAdapter, tc_exp);

        tc_exp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int gPosition, int cPosition, long id) {
                CheckBox cBox = (CheckBox) v.findViewById(R.id.cb_child);
                cBox.toggle();// 切换CheckBox状态！！！！！！！！！！

                String parentName = groups.get(gPosition).getName();
                String childName = childs.get(gPosition).get(parentName).get(cPosition).getName().split("\\.")[0];
                path = childs.get(gPosition).get(parentName).get(cPosition).getPath();

                boolean ischeck = controlView.getLayerCheckBox().get(path);
                changeCBoxStatus(ischeck, path, parentName, childName);

                expandableAdapter.notifyDataSetChanged();// 通知数据发生了变化
                return false;
            }
        });
    }

    private void initImgCheckBoxData(List<File> list) {
        if (imgCheckMap.size() == 0) {
            for (File file : list) {
                imgCheckMap.put(file.getName(), false);
            }
        }
    }

    /** 初始化数据选择 */
    public void initCheckbox(List<File> groups,List<Map<String, List<File>>> childs) {

        if (controlView.getLayerCheckBox().size() == 0) {
			/* 第一次初始化数据 */
            for (int i = 0; i < groups.size(); i++) {// 初始时,让所有的子选项均未被选中
                String gname = groups.get(i).getName();
                for (int k = 0; k < childs.size(); k++) {
                    List<File> list = childs.get(k).get(gname);
                    if (list == null)
                        continue;
                    for (File file : list) {
                        String path = file.getPath();
                        controlView.getLayerCheckBox().put(path, false);
                        controlView.getLayerKeyList().add(path);
                    }
                    break;
                }
            }
        } else {
            controlView.getLayerKeyList().clear();
            for (int i = 0; i < groups.size(); i++) {// 初始时,让所有的子选项均未被选中
                String gname = groups.get(i).getName();
                for (int k = 0; k < childs.size(); k++) {
                    List<File> list = childs.get(k).get(gname);
                    if (list == null)
                        continue;
                    for (File file : list) {
                        String path = file.getPath();
                        if(controlView.getLayerCheckBox().get(path) == null){
                            controlView.getLayerCheckBox().put(path, false);
                            controlView.getLayerKeyList().add(path);
                        }else{
                            boolean flag = controlView.getLayerCheckBox().get(path);
                            if (flag) {
                                controlView.getLayerKeyList().add(path);
                            } else {
                                controlView.getLayerKeyList().add(path);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**动态设置控件的高度*/
    private void setExpendHeight(ExpandableAdapter adapter,ExpandableListView listview){
        int listViewHeight = 0;
        int adaptCount = adapter.getGroupCount();
        for(int i=0;i<adaptCount;i++){
            View temp = adapter.getGroupView(i, true, null, listview);
            temp.measure(0,0);
            listViewHeight += temp.getMeasuredHeight();
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listview.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.FILL_PARENT;
        if(listViewHeight > 350){
            layoutParams.height = 350;
        }else{
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        listview.setLayoutParams(layoutParams);
    }

    /** checkbox状态变化 */
    public void changeCBoxStatus(boolean flag, final String path,final String gpname, final String childName) {
        if(flag){
            controlView.getLayerCheckBox().put(path, false);
            for (int j = 0; j < controlView.getLayerNameList().size();j++) {
                 gname = controlView.getLayerNameList().get(j).getPname();
                 cname = controlView.getLayerNameList().get(j).getCname();
                if(gpname.equals(gname)&&childName.equals(cname)){
                    boolean encryption = controlView.getLayerNameList().get(j).isFlag();
                    if(encryption){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SytemUtil.jiamicript(path);
                            }
                        }).start();
                    }
                    controlView.getMapView().removeLayer(controlView.getLayerNameList().get(j).getLayer());
                    controlView.getLayerNameList().remove(controlView.getLayerNameList().get(j));
                    j--;
                }
            }
        }else{
			/*数据加载之前判断数据是否加密，若加密先进行解密操作*/
            if(!new File(path).exists()){
                ToastUtil.setToast(mContext,"数据不存在请检查!");
                return;
            }
            boolean flag2 = SytemUtil.checkGeodatabase(path);
            if (flag2) {
                SytemUtil.decript(path);
            }
            loadGeodatabase(path,flag2,gpname,childName);
        }
    }

    /** 加载geodatabase数据 */
    private Geodatabase geodatabase;
    public void loadGeodatabase(String path, boolean flag, String gname,String cname) {
        boolean ff = false;
            try {
                geodatabase = new Geodatabase(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (RuntimeException e){
                e.printStackTrace();
                ToastUtil.setToast(mContext,"数据库错误");
            }
            if (geodatabase == null)
                return;
            List<GeodatabaseFeatureTable> list = geodatabase.getGeodatabaseTables();
            for (GeodatabaseFeatureTable gdbFeatureTable : list) {
                if (gdbFeatureTable.hasGeometry()) {
                    final FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                    if(controlView.getTitleLayer() != null && controlView.getTitleLayer().isInitialized()){
                        SpatialReference sp1 = controlView.getTitleLayer().getSpatialReference();
                        SpatialReference sp2 = layer.getDefaultSpatialReference();
                        if(!sp1.equals(sp2)){
                            ToastUtil.setToast(mContext, "加载数据与基础底图投影系不同,无法加载");
                            continue;
                        }
                    }

                    Renderer renderer = getHisSymbol(layer);
                    layer.setRenderer(renderer);

                    ff = true;
                    controlView.getMapView().addLayer(layer);

                    setMyLayer(gname,cname,path,flag,layer,gdbFeatureTable);
                }
            }
//        }

        controlView.removeGraphicLayer();
        controlView.addGraphicLayer();

        controlView.getMapView().invalidate();
        if(ff){
            ProgressDialogUtil.startProgressDialog(mContext);
            controlView.getLayerCheckBox().put(path, true);
        }

        /**检测数据文件夹下是否有config.xml文件如果没有复制进去*/
        String fpath = new File(path).getParent();
        File file = new File(fpath+"/config_oo.xml");
        if(!file.exists()){
            Util.copyFile(mContext, fpath, "config_oo.xml", "config.xml");
        }
    }

    //设置MyLayer的相关信息
    private void setMyLayer(String gname,String cname,String path,boolean flag,FeatureLayer layer,GeodatabaseFeatureTable featureTable){
        MyLayer myLayer = new MyLayer();
        myLayer.setPname(gname);
        myLayer.setCname(cname);
        myLayer.setPath(path);
        myLayer.setFlag(flag);
        myLayer.setLname(layer.getName());
        myLayer.setSelectColor(layer.getSelectionColor());
        myLayer.setRenderer(layer.getRenderer());
        myLayer.setLayer(layer);
        myLayer.setTable(featureTable);
        seflayerName = featureTable.getTableName();
        controlView.getLayerNameList().add(myLayer);
    }

    /**获取历史Renderer*/
    public Renderer getHisSymbol(FeatureLayer layer){

        int txt = controlView.getSharedPreferences().getInt(layer.getName()+"tmd", 50);
        //SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(sharedPreferences.getInt("color", Color.GREEN));
        int tcs = controlView.getSharedPreferences().getInt(layer.getName()+"tianchongse", Color.GREEN);
        int tcolor = Util.getColor(tcs, txt);//3158064  959459376
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(tcolor);
        Object obj = controlView.getSharedPreferences().getFloat(layer.getName()+"owidth", 4);
        float owidth = 0;
        if(obj != null){
            String str = obj.toString();
            boolean flag = Util.CheckStrIsDouble(str);
            if(flag){
                owidth = Float.parseFloat(str);
            }
        }
        int bjs = controlView.getSharedPreferences().getInt(layer.getName()+"bianjiese", Color.RED);
        simpleFillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));

        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(tcolor, (int) owidth, SimpleMarkerSymbol.STYLE.CIRCLE);
        markerSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));
        markerSymbol.setColor(bjs);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(tcolor, (int) owidth, com.esri.core.symbol.SimpleLineSymbol.STYLE.SOLID);
        lineSymbol.setWidth(owidth);
        lineSymbol.setColor(tcolor);

        Renderer renderer = null;
        if(layer.getGeometryType().equals(Geometry.Type.POLYGON)){
            renderer = new SimpleRenderer(simpleFillSymbol);
        }else if(layer.getGeometryType().equals(Geometry.Type.POLYLINE)){
            renderer = new SimpleRenderer(lineSymbol);
        }else{
            renderer = new SimpleRenderer(markerSymbol);
        }

        return renderer;
    }

    /** 缩放至基础图层所在位置 */
    public void zoomImageLayer() {
        if(imgTileLayerMap.size() >0){
            Envelope env = new Envelope();
            for(String key : imgTileLayerMap.keySet()){
                ArcGISLocalTiledLayer layer = imgTileLayerMap.get(key);
                Envelope env0 = new Envelope();
                layer.getFullExtent().queryEnvelope(env0);
                Geometry[] geometries = new Geometry[]{env0,env};
                GeometryEngine.union(geometries, controlView.getTitleLayer().getSpatialReference()).queryEnvelope(env);
            }
            controlView.getMapView().setExtent(env);
            controlView.getMapView().invalidate();
            return;
        }

        if (controlView.getImgLayer() == null) {
            ToastUtil.setToast(mContext, "影像数据未加载,请在图层控制中加载数据");
            return;
        }
        if (controlView.getTitleLayer().isVisible()) {
            controlView.getMapView().setExtent(controlView.getTitleLayer().getFullExtent());
            controlView.getMapView().invalidate();
        } else {
            ToastUtil.setToast(mContext, "影像图未加载,请在图层控制中加载数据");
        }
    }

//    @Override
//    public void upLayerData() {
//        boolean flag = SytemUtil.checkGeodatabase(path);
//        if (flag) {
//            SytemUtil.decript(path);
//        }
//        loadGeodatabase(path,true,gname,cname);
//    }
}
