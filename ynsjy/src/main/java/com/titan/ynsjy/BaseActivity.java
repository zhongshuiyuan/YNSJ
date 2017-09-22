package com.titan.ynsjy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.GraphicsLayer.RenderingMode;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.baselibrary.util.DialogParamsUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.gis.GeometryUtil;
import com.titan.gis.MeasureUtil;
import com.titan.ynsjy.adapter.FeatureResultAdapter;
import com.titan.ynsjy.custom.MorePopWindow;
import com.titan.ynsjy.daoImpl.LocationDaoImpl;
import com.titan.ynsjy.dialog.CoordinateDialog;
import com.titan.ynsjy.dialog.FeatureDelDialog;
import com.titan.ynsjy.dialog.LayerSelectDialog;
import com.titan.ynsjy.dialog.MergeFeatureDialog;
import com.titan.ynsjy.dialog.RenderSetDialog;
import com.titan.ynsjy.dialog.SettingDialog;
import com.titan.ynsjy.drawTool.DrawEvent;
import com.titan.ynsjy.drawTool.DrawEventListener;
import com.titan.ynsjy.drawTool.DrawTool;
import com.titan.ynsjy.entity.ActionMode;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.mview.IBaseView;
import com.titan.ynsjy.mview.ILayerView;
import com.titan.ynsjy.mview.IYzlView;
import com.titan.ynsjy.mview.LayerControlView;
import com.titan.ynsjy.presenter.BasePresenter;
import com.titan.ynsjy.presenter.DialogImpl;
import com.titan.ynsjy.presenter.GpsCollectPresenter;
import com.titan.ynsjy.presenter.LayerControlPresenter;
import com.titan.ynsjy.presenter.LayerLablePresenter;
import com.titan.ynsjy.presenter.NavigationPresenter;
import com.titan.ynsjy.presenter.RepairPresenter;
import com.titan.ynsjy.presenter.TrajectoryPresenter;
import com.titan.ynsjy.util.ArcGISQueryUtils;
import com.titan.ynsjy.util.ArcGISUtils;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.SytemUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by li on 2016/5/26.
 * activity基类
 */
@RuntimePermissions
public abstract class BaseActivity extends AppCompatActivity implements LayerSelectDialog.SetOnItemClickListener,
        View.OnClickListener, DrawEventListener, IYzlView, LayerControlView,ILayerView,IBaseView {
    //系统模式0:新增审计 1:审计历史 2:轨迹管理
    public  int SYSMODE=0;
    /*系统投影坐标系*/
    public static SpatialReference spatialReference=null;
    //坐标
    private LocationDisplayManager mLoctionDisplayManager;
            //= SpatialReference.create(4507);
    public Context mContext;
    public View childview;
    public static MapView mapView;

    /* 地图服务及地址 */
    //基础图
    public ArcGISLocalTiledLayer tiledLayer;
    //影像图
    public ArcGISLocalTiledLayer imgTiledLayer;
    //public ArcGISTiledMapServiceLayer tiledLayer_online;//在线基础地图
    //public ArcGISTiledMapServiceLayer imageLayer_online;//在线影像地图
    /* GPS采集工具拦*/
    private Button gpstart, gpspend, gpsstop;
    private boolean gpsstart = false;//是否是首次点击开始按钮
    private boolean gps_start_flag = false;//开始按钮的状态
    private boolean gps_spend_flag = false;//暂停按钮的状态
    private boolean gps_stop_flag = false;//结束按钮的状态
    /*MorePopWindow选择*/
    //地名管理
    private MorePopWindow dimingMangerPopWindow ,guijiPopwindow, dataPopWindow;
    private MorePopWindow personCenterPopup, kjtjPopup, xbSearchPopup;
    /*轨迹时间选择 */
    //private TimePopupWindow pwTime;
    /* MyTouchListener */
    private MyTouchListener myTouchListener;
    /* 地图绘制层 */
    public GraphicsLayer graphicsLayer;
    /* 是否首次定位 */
    boolean isFirstLoc = true;
    /* 当前位置经纬度 */
    public static double currentLon = 0.0;// 经度
    public static double currentLat = 0.0;// 纬度
    public double altitude = 0.0;// 海拔
    /* 上一个定位点的经纬度 */
    public double bfPointLon = 0.0;
    public double bfPointLat = 0.0;
    /* 当前位置点 */
    public static Point currentPoint = new Point(0, 0);
    /*点击地图获取的点*/
    public Point mapPoint;
    /* 自动定位绘制误差圆及中心点 */
    public static Graphic locationGraphic, circleGraphic;
    public int locationID, circleID;
    public Polygon circlePolygon;
    /* 记录轨迹 */
    public Polyline gjPolyline;
    public Graphic gjGraphic;
    public int gjGraphicID;
    /* 定位中心点样式 */
    public MarkerSymbol locationMarkerSymbol = null;
    /* 样式 */
    public MarkerSymbol markerSymbol;
    public LineSymbol lineSymbol;
    public FillSymbol fillSymbol;
    public FillSymbol selectfillSymbol;
    /* mCallout */
    //public mCallout mCallout;
    //public List<mCalloutPopupWindow> mCalloutPopupWindows = new ArrayList<>();
    public DecimalFormat decimalFormat = new DecimalFormat(".000000");
    /* 标绘方式及geometry类型 */
    public static final int EMPTY = 0;
    public static final int POINT = 1;
    public static final int ENVELOPE = 2;
    public static final int POLYLINE = 3;
    public static final int POLYGON = 4;
    public static final int CIRCLE = 5;
    public static final int ELLIPSE = 6;
    public static final int FREEHAND_POLYGON = 7;
    public static final int FREEHAND_POLYLINE = 8;
    public static final int FREEHAND_POLYLINEGPS = 9;
    /* featureLayer图形几何类型 */
    public com.esri.core.geometry.Geometry.Type layerType;
    /* 标识进入系统名称 */
    public String activitytype;
    /*测量面积 */
    public Point startPoint;
    //地图操作模式
    public ActionMode actionMode = ActionMode.MODE_SELECT;

    public int graphicID;
    public Graphic drawGraphic;
    public int drawType;
    public boolean active;
    public Point point_all;
    //矩形选择框
    public Envelope envelope_all;
    //全局的线
    public Polyline polyline_all;
    public Polygon polygon_all;

    /* 图层控制 加载小班数据 */
    public HashMap<String, Boolean> childCheckBox = new HashMap<>();
    public List<String> childKeyList = new ArrayList<>();
    /* 保存加载图层的父级名称 自己目录的图层名称 */
    public static List<MyLayer> layerNameList = new ArrayList<>();
    //public static List<MyLayer> polygonLayerList = new ArrayList<>();
    /*选择图层所在MyLayer*/
    public static MyLayer myLayer;
    /* 估计查询 */
    //public String selectTime = "选择时间";
    /* 时间格式化 */
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
    //public static final int PlAYBACK = 5;
    /* 轨迹回放 */
    public Runnable hfrunnable = null;
    public Handler timer = new Handler();// 定时器
    /*数据所属 */
    //public List<HashMap<String, String>> sjssLlist = new ArrayList<>();
    /* 行政区域 */
    public List<HashMap<String, String>> xzqyLlist = new ArrayList<>();
    /* 基地性质 */
    public List<HashMap<String, String>> jdxzLlist = new ArrayList<>();
    /* 选择小班的GeodatabaseFeature */
    public static GeodatabaseFeature selGeoFeature = null;
    /* 选择小班 */
    public static Geometry selectGeometry = null;
    /*小班属性编辑时的id*/
    //public static long sefID;
    /*小班属性编辑时的所选择图层名称*/
    public static String seflayerName;
    /* 选择图斑的属性信息 */
    public static Map<String, Object> selectFeatureAts = null;
    /*选择图斑的属性字段 */
    //public static List<Field> selectfiledList = null;
    /* 选择图斑的集合 */
    public static List<GeodatabaseFeature> selGeoFeaturesList = new ArrayList<>();
    public static List<GeodatabaseFeature> getFeaturesList = new ArrayList<>();
    public static List<GeodatabaseFeature> allFeaturesList = new ArrayList<>();
    public Map<GeodatabaseFeature, String> selMap = new HashMap<>();
    /* 记录操作的小班及操作类型 */
    public ArrayList<Map<String, Object>> undonList = new ArrayList<>();
    /* FeatureLayer图层 */
    public static FeatureTemplate layerTemplate;
    public static Symbol layerSymbol;
    public Renderer layerRenderer;
    public Map<String, Object> layerFeatureAts;
    /*小班查询 */
    //private List<GeodatabaseFeature> list_xbsearch = new ArrayList<>();
    /*back键监听 */
    private long firstTime = 0;
    private static final int EDIT_FEATURE = 0x000004;
    //private String mCurrentPhotoPath = "";// 图片路径
    //public static Point touchpoint;
    /*GPS位置监听 */
    //private LocationManager locationManager;
    /*显示当前位置控件 */
    private TextView mylocationValue;
    private TextView mapscaleValue;
    /*otms文件夹下各个系统的配置数据*/
    public List<Row> proData = new ArrayList<>();
    /*空间统计用到的变量*/
    public DrawTool drawTool;
    public Callout mCallout;
    /*定位许可*/
    //private final int SDK_PERMISSION_REQUEST = 127;
    //private String permissionInfo;
    /*百度定位client*/
    public LocationClient mLocClient;
    public MyLocationListenner locationListenner = new MyLocationListenner();
    /*presenter*/
    public BasePresenter basePresenter;
    public RepairPresenter repairPresenter;
    public NavigationPresenter navigationPresenter;
    public GpsCollectPresenter gpsCollectPresenter;
    //public XbqueryPresenter xbqueryPresenter;
    public TrajectoryPresenter trajectoryPresenter;
    public LayerControlPresenter layerControlPresenter;
    public LayerLablePresenter lablePresenter;
    //public StatisticsSpacePresenter spacePresenter;
    /*baseActivity页面include*/
    public View xbqdInclude, tckzInclude, tckzImgInclude, minegjsearchInclude, otherGjSearchInclude,layerLableInclude,
            xbbjInclude, xbSearchZdyInclude, xbSearchJdInclude, gpsCaijiInclude, xdmSearchInclude,share_maptools;
    /*在include内的view但是需要做全局变量的*/
    public ImageView sjtbImgview, gjsearchImgview, dmMangerImgview, personCenter, xbbjImgview, tcxrImgview, xbsearchImgview;
    /*小班编辑RadioButton*/
    public RadioButton addFeatureBtn, addFeatureGbBtn, selectButton, qiegeButton, hebingButton, xiubanButton,
            repealButton, attributButton, deleteButton, copyButton,auditButton;
    //当前位置
    public ImageButton location_self;
    public static final int CREAT_BITMAP_FINISH = 1;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CREAT_BITMAP_FINISH:

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext=this;

        //setContentView(R.layout.activity_base);
        //去除arcgis文字
        ///ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        //ArcGISRuntime.setClientId("qwvvlkN4jCDmbEAO");//去除水印的
        /*初始化数据*/
        //initData();
        BaseActivityPermissionsDispatcher.initWithCheck(this);
        /* 定位设置 */
        //initLocation();
		/* 地图控件初始化 */
        //initView();
        /*初始化presenter*/
        //initPresenter();
		/* 添加地图 */
        //addLayer();
        //mapviewStatusChange();
		/* 定义样式 */
       // initSymbol();
        /*设置五参数*/
        //initGpsSettings();
        /**/
        //layerNameList.clear();

        //featureLayerList.clear();
        //featureLayer = null;

        //isHaveSBH();
    }
    /**
     * 初始化
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
    })
    void init(){
        /*初始化数据*/
       //BaseActivityPermissionsDispatcher.initDataWithCheck(this);
        /* 定位设置 */
        BaseActivityPermissionsDispatcher.initLocationWithCheck(this);
        /* 地图控件初始化 */
        initView();
        /*初始化presenter*/
        BaseActivityPermissionsDispatcher.initPresenterWithCheck(this);
		/* 添加地图 */
        BaseActivityPermissionsDispatcher.addLayerWithCheck(this);
		/* 定义样式 */
        initSymbol();
        layerNameList.clear();
    }
    public abstract View getParentView();

    /**
     * 初始化presenter
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
     void initPresenter() {
        basePresenter = new BasePresenter(BaseActivity.this, this);
        //xbqueryPresenter = new XbqueryPresenter(mContext,this);
        trajectoryPresenter = new TrajectoryPresenter(BaseActivity.this,this);
        gpsCollectPresenter = new GpsCollectPresenter(mContext, this);
        //navigationPresenter = new NavigationPresenter(mContext, this);
        repairPresenter = new RepairPresenter(mContext,this,basePresenter);
        lablePresenter = new LayerLablePresenter(mContext,this);
        //spacePresenter = new StatisticsSpacePresenter(mContext,this);
        layerControlPresenter = new LayerControlPresenter(mContext, this, this);
    }



    /**
     * 地图控件初始化
     */
    private void initView() {
        childview = getParentView();
        initMapView();
        /*放大缩小工具*/
       /* ZoomControlView zoomControlView = (ZoomControlView) childview.findViewById(R.id.zoomcontrolview);
        if (mapView != null && zoomControlView != null) {
            zoomControlView.setMapView(mapView);
        }*/

        /*图层控制*/
        tckzInclude = childview.findViewById(R.id.tckz_include);
        /*影像数据图层控制*/
        tckzImgInclude = childview.findViewById(R.id.imgtuceng_include);
        /*自身轨迹查询显示窗口*/
        minegjsearchInclude = childview.findViewById(R.id.guijisearch_include);
        /*其他轨迹查询显示窗口*/
        //otherGjSearchInclude = childview.findViewById(R.id.gjserchother_include);
        /*小班编辑工具*/
        xbbjInclude = childview.findViewById(R.id.xbbj_include);

        /*小地名搜索*/
        xdmSearchInclude = childview.findViewById(R.id.xdmsearch_include);
        /*GPS采集工具栏*/
        gpsCaijiInclude = childview.findViewById(R.id.gpscaiji_include);
        /*图层标注view*/
        layerLableInclude = childview.findViewById(R.id.shuxinglable_include);
        /*侧边工具栏*/
        share_maptools = childview.findViewById(R.id.share_maptools);
        /**topbar========================================================================*/
        /*图层控制按钮*/
        ImageView tckzImgView = (ImageView) childview.findViewById(R.id.tckz_imageview);
        tckzImgView.setOnClickListener(this);
        /*审计记录*/
        /*LinearLayout  sjjlImgView = (LinearLayout) childview.findViewById(R.id.sjjl_imageview);
        sjjlImgView.setOnClickListener(this);*/

        /*轨迹查询*/
        gjsearchImgview = (ImageView) childview.findViewById(R.id.share_gjcx);
        gjsearchImgview.setOnClickListener(this);
        /*坐标定位*/
        ImageView zbdwImgview = (ImageView) childview.findViewById(R.id.share_zbdw);
        zbdwImgview.setOnClickListener(this);
        /*系统设置*/
        ImageView settingImgview = (ImageView) childview.findViewById(R.id.share_xtsz);
        settingImgview.setOnClickListener(this);
        /*地名管理*/
        dmMangerImgview = (ImageView) childview.findViewById(R.id.share_dmgl);
        dmMangerImgview.setOnClickListener(this);

        /*小班编辑*/
        xbbjImgview = (ImageView) childview.findViewById(R.id.share_xbbj);
        xbbjImgview.setOnClickListener(this);
        /*小班查询 */
        xbsearchImgview = (ImageView) childview.findViewById(R.id.share_xbcx);
        xbsearchImgview.setOnClickListener(this);
        ImageView statisticsView =(ImageView) childview.findViewById(R.id.top_kongjiantongji);
        statisticsView.setOnClickListener(this);
        ImageView addnewlayerView =(ImageView) childview.findViewById(R.id.top_addnewlayer);
        addnewlayerView.setOnClickListener(this);
        ImageView jjsbxx =(ImageView) childview.findViewById(R.id.top_addjjxx);
        jjsbxx.setOnClickListener(this);
        location_self = (ImageButton) childview.findViewById(R.id.location_self);
        location_self.setOnClickListener(this);
        /**=====================================================================*/
        /*小班编辑子按钮*/
        //选择
        selectButton = (RadioButton) childview.findViewById(R.id.selectButton);
        //分割
        qiegeButton = (RadioButton) childview.findViewById(R.id.qiegeButton);
        //合并
        hebingButton = (RadioButton) childview.findViewById(R.id.hebingButton);
        //修班
        xiubanButton = (RadioButton) childview.findViewById(R.id.xiubanButton);
        //撤销
        repealButton = (RadioButton) childview.findViewById(R.id.repealButton);
        //属性查看
        attributButton = (RadioButton) childview.findViewById(R.id.attributButton);
        //新增审计
        auditButton = (RadioButton) childview.findViewById(R.id.auditButton);
        auditButton.setOnClickListener(this);
        deleteButton = (RadioButton) childview.findViewById(R.id.deleteButton);
        //copyButton = (RadioButton) childview.findViewById(R.id.copyButton);
        addFeatureBtn = (RadioButton) childview.findViewById(R.id.addfeature);
        //addFeatureGbBtn = (RadioButton) childview.findViewById(R.id.addfeaturegb);
        /**=====================================================================*/
        /*显示当前坐标view*/
        mylocationValue = (TextView) childview.findViewById(R.id.mylocationValue);
        /*显示当前比例尺view*/
        mapscaleValue = (TextView) childview.findViewById(R.id.mapScaleValue);
        /*图层渲染*/
        tcxrImgview = (ImageView) childview.findViewById(R.id.share_tcxr);
        tcxrImgview.setOnClickListener(this);
        //属性标注
        ImageView shuxingLable = (ImageView) childview.findViewById(R.id.shuxing_lable);
        shuxingLable.setOnClickListener(this);
        /*gps采集工具栏*/
        gpstart = (Button) childview.findViewById(R.id.share_gps_start);
        gpspend = (Button) childview.findViewById(R.id.share_gps_suspend);
        gpsstop = (Button) childview.findViewById(R.id.share_gps_stop);

    }

    /**
     * 初始化mapview
     */
    private void initMapView() {
        mapView = (MapView) childview.findViewById(R.id.map);
        //中国地图范围
        Envelope extent = new Envelope(-20037507.229594,-19971868.880409,20037507.229594,19971868.880409);
        mapView.setMaxExtent(extent);
        mLoctionDisplayManager=mapView.getLocationDisplayManager();
        mLoctionDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
        //loctionDisplayManager.setAccuracyCircleOn(true);
        mLoctionDisplayManager.start();

        mCallout=mapView.getCallout();
        mCallout.setStyle(R.xml.calloutstyle);
        mapviewStatusChange();


    }

    /**
     * 添加地图
     */
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION})
    public void addLayer() {
		/* 基础底图 */
        String titlePath = MyApplication.resourcesManager.getTitlePath();
        tiledLayer = basePresenter.addTitleLayer(titlePath);
        if(tiledLayer==null){
            ToastUtil.setToast(mContext,"底图文件不存在");
        }
        //spatialReference=mapView.getSpatialReference();
        /*触摸事件*/
        myTouchListener = new MyTouchListener(mContext, mapView);
        mapView.setOnTouchListener(myTouchListener);
		/* 设置地图背景 */
        mapView.setMapBackground(0xffffff, 0xffffff, 3, 3);
        /*添加graphicLayer*/
        graphicsLayer = basePresenter.addGraphicLayer();
        /*地图缩放*/
        mapView.setOnZoomListener(new OnZoomListener() {
            private static final long serialVersionUID = 6225276681409635976L;

            @Override
            public void preAction(float arg0, float arg1, double arg2) {
            }

            @Override
            public void postAction(float arg0, float arg1, double arg2) {
                double scale = mapView.getScale();
                DecimalFormat df = new DecimalFormat("0");
                String txt = "当前比例尺   1:" + df.format(scale);
                mapscaleValue.setText(txt);
            }
        });
    }


    /**
     * mapview 状态变化监听
     */
    public void mapviewStatusChange() {
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source instanceof FeatureLayer) {
                    if (STATUS.LAYER_LOADED == status) {
                        ProgressDialogUtil.stopProgressDialog(mContext);

                        spatialReference=mapView.getSpatialReference();
                        ToastUtil.setToast(mContext, "数据加载成功");
                    } else {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        ToastUtil.setToast(mContext, "数据加载错误");
                    }
                }
            }
        });
    }

    /**
     * 初始化symbol
     */
    public void initSymbol() {

        markerSymbol = new SimpleMarkerSymbol(Color.BLACK, 16, SimpleMarkerSymbol.STYLE.CIRCLE);
        //lineSymbol = new SimpleLineSymbol(Color.argb(100, 248, 116, 14), 5);// 248,116,14
        lineSymbol = new SimpleLineSymbol(Color.RED, 4);// 248,116,14
        fillSymbol = new SimpleFillSymbol(Color.BLACK);
        fillSymbol.setOutline(lineSymbol);
        fillSymbol.setAlpha(50);

        selectfillSymbol = new SimpleFillSymbol(Color.TRANSPARENT);
        selectfillSymbol.setOutline(lineSymbol);
        selectfillSymbol.setAlpha(10);

        LocationDisplayManager ls = mapView.getLocationDisplayManager();
        try {
            locationMarkerSymbol = ls.getDefaultSymbol();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    /**实时显示坐标*/
    public void setLocationView(){
        if (currentPoint == null || !currentPoint.isValid()) {
            return;
        }
        mylocationValue.setText("当前坐标:"
                + decimalFormat.format(currentPoint.getX()) + ","
                + decimalFormat.format(currentPoint.getY()));

    }

    /**
     * 定位的初始化
     */
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void initLocation() {
        LocationDaoImpl locationDao = new LocationDaoImpl();
        mLocClient=new LocationClient(mContext);
        locationDao.initLocation(mContext, mLocClient, locationListenner);
    }


    /**
     * 当前位置手动定位
     */
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void mylocation(View view) {
        Point pt=mLoctionDisplayManager.getPoint();
        mapView.centerAt(pt,false);

        /*clean(view);
        //initTouch();
        //ToastUtil.setToatLeft(this, "自身定位");
        if (currentPoint != null && currentPoint.isValid()) {

            mapRemove(new View(mContext));
            isFirstLoc = true;
            //mCallout.setStyle(R.xml.mCalloutstyle);
            Point point_ty =(Point) GeometryEngine.project(currentPoint,SpatialReference.create(4326),spatialReference);
            mapView.centerAt(point_ty,false);
            //mCallout.show(point_ty, basePresenter.loadmCalloutView(currentPoint));
            //BaseActivityPermissionsDispatcher.updataLocationWithCheck(this,point_ty);
        } else {
            ToastUtil.setToast(mContext, "无法获取位置信息");
        }*/
    }

    /**
     * 清除graphic
     */
    public void clean(View view) {
        initTouch();
        //ToastUtil.setToatLeft(mContext, "清除标绘");
        clearGraphic();
        if(mCallout.isShowing()){
            mCallout.hide();
        }
       /* for (int i = 0; i < mCalloutPopupWindows.size(); i++) {
            mCalloutPopupWindows.get(i).hide();
        }*/
        //mStops.clearFeatures();
    }

    /**
     * 清除mapview中的graphicLayer图层
     */
    public void clearGraphic() {
        graphicsLayer.removeAll();
        if (graphicsLayer != null && graphicsLayer.isInitialized()) {
            mapView.removeLayer(graphicsLayer);
        }
        mapView.addLayer(graphicsLayer);
        locationGraphic = null;
        circleGraphic = null;
        restory();
        //mapView.invalidate();
    }

    /**
     * 测量距离
     */
    public void ceju(View view) {
        clean(view);
        //initTouch();
        ToastUtil.setToatLeft(mContext, "测量距离");
        actionMode = ActionMode.MODE_CEJU;
        drawType = POLYLINE;
        activate(drawType);
    }



    /**
     * 测量面积
     */
    public void cemian(View view) {
        clean(view);
        //initTouch();
        actionMode = ActionMode.MODE_CEMIAN;
        startPoint = null;
        ToastUtil.setToatLeft(mContext, "测量面积");
        basePresenter.initCmPopuwindow();
    }






    /**
     * gps采集工具栏开始按钮
     */
    public void gpstart(View view) {

        /*if (gpsCollectPresenter.collection_type == 0) {
            //
            if ((!gps_start_flag && !gps_stop_flag) || (gps_start_flag && gps_stop_flag)) {
                gpsCollectPresenter.copyGjDb();
            }
            gps_start_flag = true;
            gpstart.setEnabled(false);
            gpspend.setEnabled(true);
            gpsstop.setEnabled(true);
            return;
        }*/
        if (layerNameList.size() <= 0) {
            ToastUtil.setToast(mContext, "未加载空间数据,请在图层控制中加载数据");
            return;
        }

        gps_start_flag = true;
        //设置UI状态
        gpstart.setEnabled(false);
        gpspend.setEnabled(true);
        gpsstop.setEnabled(true);

        /*if ((!gps_start_flag && !gps_stop_flag) || (gps_start_flag && gps_stop_flag)) {
            actionMode = ActionMode.MODE_EDIT_ADD;
            getFeatureLayer(actionMode);
        }*/

    }

    /**
     * gps采集工具栏暂停按钮
     */
    public void gpsuspend(View view) {
        if (gpsCollectPresenter != null) {
            if (gpsCollectPresenter.collection_type == 0) {
                gpstart.setEnabled(true);
                gpspend.setEnabled(false);
                gpsstop.setEnabled(true);
                gps_spend_flag = true;
                return;
            }
        }

        ToastUtil.setToast(mContext, "暂停记录轨迹");
        gpstart.setEnabled(true);
        gpspend.setEnabled(false);
        gpsstop.setEnabled(true);
        gps_spend_flag = true;
    }

    /**
     * gps采集工具栏结束按钮
     */
    public void gpstop(View view) {
        if (gpsCollectPresenter != null) {
            if (gpsCollectPresenter.collection_type == 0) {
                ToastUtil.setToast(mContext, "结束记录轨迹");
                gpstart.setEnabled(true);
                gpspend.setEnabled(false);
                gpsstop.setEnabled(false);
                gps_stop_flag = true;
                return;
            }
        }
        ToastUtil.setToast(mContext, "结束记录轨迹,并保存图斑");
        gpstart.setEnabled(true);
        gpspend.setEnabled(false);
        gpsstop.setEnabled(false);
        gps_stop_flag = true;
        gpsstart = false;

        if (polyline_all == null) {
            ToastUtil.setToast(mContext, "轨迹数据不存在");
            return;
        }
        int size = polyline_all.getPointCount();
        if (size > 2) {
            addGPSFeature();
            gpsCaijiInclude.setVisibility(View.INVISIBLE);
        } else {
            ToastUtil.setToast(mContext, "轨迹数据不符合构建面规则");
        }
    }

    /**
     * 异步类
     */
    private class MyAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {
            if (params[0].equals("uplonlat")) {
                /* 保存GPS坐标数据 */
                basePresenter.addGuijiPoint(currentPoint);

            }  /*else if (params[0].equals("xzqy")) {

                basePresenter.uploadxzqyData();

            } else if (params[0].equals("jdxz")) {

                basePresenter.uploadjdxzData();
            }*/
            return null;
        }
    }

    /**
     * 当前位置刷新
     */
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void updataLocation(Point currentPoint) {
        if (currentPoint == null || !currentPoint.isValid()) {
            return;
        }
		/* 跟随人员移动绘制行进路线 */
        if (gjPolyline == null) {
            gjPolyline = new Polyline();
            LineSymbol gjSymbol = new SimpleLineSymbol(Color.RED, 5);
            gjGraphic = new Graphic(gjPolyline, gjSymbol);
            gjGraphicID = graphicsLayer.addGraphic(gjGraphic);
            if (currentPoint.isValid()) {
                gjPolyline.startPath(currentPoint);
            }
        } else {
            if (currentPoint.isValid()) {
                gjPolyline.lineTo(currentPoint);
            }
        }

        boolean flag = MyApplication.sharedPreferences.getBoolean("zongji", false);
        if (flag && gjPolyline != null) {
            graphicsLayer.updateGraphic(gjGraphicID, gjPolyline);
        }
        if (currentPoint.isValid() && graphicsLayer != null) {
            graphicsLayer.updateGraphic(locationID, currentPoint);
            graphicsLayer.updateGraphic(circleID, circlePolygon);
        }

        if (isFirstLoc) {
            if (1 < mapView.getResolution()) {
                mapView.setResolution(1);
            }
            mapView.centerAt(currentPoint, true);
            //mapView.invalidate();
            isFirstLoc = false;
        }
    }

    /**
     * 定位监听
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            /*if (dxtTiledLayer != null && dxtTiledLayer.isInitialized()) {
                NetUtil.openOrcloseWifi(mContext, false);
            }*/
            if (location == null)
                return;
            if (location.getLongitude() == 4.9E-324 && location.getLatitude() == 4.9E-324)
                return;
            if (location.getLongitude() == 1.0 && location.getLatitude() == 1.0)
                return;
            double lon = location.getLongitude();
            double lat = location.getLatitude();

            if (graphicsLayer == null) {
                graphicsLayer = new GraphicsLayer(RenderingMode.STATIC);
                mapView.addLayer(graphicsLayer);
            }

            //bd0911 坐标系下的坐标
            //Point point_bd = new Point(lon,lat);
            //GPS坐标
            //Point point = getGPSpoint(location);
            currentPoint=new Point(lon,lat,location.getAltitude());
            //投影坐标
            //Point point_ty = (Point) GeometryEngine.project(currentPoint,SpatialReference.create(4326), spatialReference);
           /* currentPoint = P;
            currentLon = currentPoint.getX();
            currentLat = currentPoint.getY();
            altitude = currentPoint.getZ();*/

            if (gps_start_flag && currentPoint.isValid()) {
                //
                if (gpsCollectPresenter.collection_type == 0) {

                    if (gps_stop_flag||gps_spend_flag) {
                        return;
                    }
                    gpsCollectPresenter.addGjdateTodb(currentPoint);
                }
                if (gpsCollectPresenter.collection_type == 1) {
                    addPointToLine(currentPoint);
                }
            }

            //initGraphicLocation(point_ty);

            //updataLocation(point_ty);
            //BaseActivityPermissionsDispatcher.updataLocationWithCheck(BaseActivity.this,currentPoint);

            if (bfPointLon != currentLon || bfPointLat != currentLat) {
                double distance = BussUtil.distance(bfPointLat, bfPointLon, currentLat, currentLon);
                if (distance > 10) {
					/* 上传坐标到后台 */
                    new MyAsyncTask().execute("uplonlat");
                }
                setLocationView();
                bfPointLat = currentLat;
                bfPointLon = currentLon;
            }
        }
    }

    /**
     * 获取位置坐标 GPS定位 网络定位 百度定位
     */
    /*private Point getGPSpoint(BDLocation blocation) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        *//* 判别是取的gps坐标还是百度坐标 true 为百度坐标 *//*
        boolean dwflag = false;
        if (location != null) {
            //GPS坐标
            currentLat = location.getLatitude(); // 纬度 26.567741305680546
            currentLon = location.getLongitude(); // 经度 106.68937683886078
            altitude = location.getAltitude();// 1040.8563754250627
            dwflag = false;
        } else {
            //百度坐标
            currentLat = blocation.getLatitude(); // 纬度
            currentLon = blocation.getLongitude(); // 经度
            altitude = blocation.getAltitude();
            dwflag = true;
        }

        return getPoint(currentLon, currentLat, altitude,dwflag);
    }*/

    /**
     * gps采集路线
     */
    public void addPointToLine(Point point) {
        if (polyline_all==null||gps_stop_flag||gps_spend_flag) {
            return;
        }
        if (!gpsstart) {
            gpsstart = true;
            polyline_all.startPath(point);
        } else {
            polyline_all.lineTo(point);
        }

        if (graphicsLayer != null) {
            graphicsLayer.updateGraphic(graphicID, polyline_all);
            //mapView.invalidate();
        }

    }



    /**
     * 地图监听事件
     */
    private class MyTouchListener extends MapOnTouchListener {
        MapView map;

        private MyTouchListener(Context context, MapView view) {
            super(context, view);
            map = view;
           /* mCallout = map.getmCallout();
            mCallout.setStyle(call);*/
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Point point = map.toMapPoint(event.getX(), event.getY());
            if (point == null || point.isEmpty() || !point.isValid()) {
                return false;
            }

            if (active
                    && (drawType == POINT || drawType == ENVELOPE
                    || drawType == CIRCLE
                    || drawType == FREEHAND_POLYLINE || drawType == FREEHAND_POLYGON)
                    && event.getAction() == MotionEvent.ACTION_DOWN) {

                switch (drawType) {
                    case POINT:
                        point.setXY(point.getX(), point.getY());
                        break;
                    case ENVELOPE:
                        startPoint = point;
                        envelope_all.setCoords(point.getX(), point.getY(),
                                point.getX(), point.getY());
                        break;
                    case CIRCLE:
                        startPoint = point;
                        break;
                    case FREEHAND_POLYGON:
                        polygon_all.startPath(point);
                        break;
                    case FREEHAND_POLYLINE:
                        if (polyline_all == null) {
                            return false;
                        }
                        int size = polyline_all.getPointCount();
                        if (size == 0) {
                            polyline_all.startPath(point);
                        } else {
                            Polyline polyline = polyline_all;
                            double d1 = polyline.calculateLength2D();
                            polyline.lineTo(point);
                            double d2 = polyline.calculateLength2D();
                            double d = d2 - d1;
                            if (d > 6) {
                                polyline_all.lineTo(point);
                            }
                        }
                        break;
                }

                if (actionMode == ActionMode.MODE_SELECT) {
                    return true;
                } else if (actionMode == ActionMode.MODE_XIUBAN) {
                    return true;
                } else if (actionMode == ActionMode.MODE_QIEGE) {
                    return true;
                } else if (actionMode == ActionMode.MODE_EDIT_ADD && drawType == FREEHAND_POLYLINE) {
                    return true;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                if (layerType != null && actionMode == ActionMode.MODE_EDIT_ADD
                        && drawType == FREEHAND_POLYLINE && layerType.equals(Geometry.Type.POLYGON)) {
                    if (polyline_all == null) {
                        return false;
                    }

                    int size = polyline_all.getPointCount();
                    if (size >= 3) {
                        polygon_all = ArcGISUtils.LineToPolygon(mContext, polyline_all, mapView);
                        graphicsLayer.updateGraphic(graphicID, polygon_all);
                        //mapView.invalidate();
                        int size1 = polygon_all.getPointCount();
                        if (size1 >= 3) {
                            saveFeature(polygon_all);
                        } else {
                            ToastUtil.setToast(mContext, "勾绘有问题，请重新画");
                        }

                    } else {
                        polyline_all = null;
                    }
                    return true;
                }

                if (layerType != null && actionMode == ActionMode.MODE_EDIT_ADD
                        && drawType == FREEHAND_POLYLINE && layerType.equals(Geometry.Type.POLYLINE)) {
                    graphicsLayer.updateGraphic(graphicID, polygon_all);
                    //mapView.invalidate();
                    saveFeature(polygon_all);
                    return true;
                }

                /*if (layerType != null && actionMode == ActionMode.MODE_EDIT_ADD_GB
                        && drawType == FREEHAND_POLYGON && layerType.equals(Geometry.Type.POLYGON)) {
                    graphicsLayer.updateGraphic(graphicID, polygon_all);
                    mapView.invalidate();
                    saveFeature(polygon_all);
                    return true;
                }*/
                //小班选择
                /*if (actionMode == ActionMode.MODE_SELECT) {
                    if (layerNameList.size() > 0) {
                        getGeometryInfo(envelope_all);
                    }
                    return true;
                }*/
                if (actionMode == ActionMode.MODE_CEMIAN && drawType == FREEHAND_POLYLINE) {
                    int size = polyline_all.getPointCount();
                    if (size >= 3) {
                        graphicsLayer.removeGraphic(graphicID);
                        Polygon polygon = ArcGISUtils.LineToPolygon(mContext, polyline_all, mapView);
                        Graphic graphic = new Graphic(polygon, fillSymbol);
                        graphicsLayer.addGraphic(graphic);
                        double area = Math.abs(polygon.calculateArea2D());
                        DroolCeMian(polygon, area);
                    }
                    ToastUtil.setToast(mContext, "测量结束");
                    restory();
                    return true;
                }

                if (actionMode == ActionMode.MODE_CEMIAN && drawType == FREEHAND_POLYGON) {
                    double area = Math.abs(polygon_all.calculateArea2D());
                    if (area > 0) {
                        DroolCeMian(polygon_all, area);
                        graphicsLayer.updateGraphic(graphicID, polygon_all);
                    }
                    ToastUtil.setToast(mContext, "测量结束");
                    restory();
                    return true;
                }

                if (drawType == FREEHAND_POLYLINE) {
                    if (actionMode == ActionMode.MODE_XIUBAN ||
                            actionMode == ActionMode.MODE_QIEGE) {
                        saveFeature(polygon_all);
                        return true;
                    }
                }
            }
            return super.onTouch(v, event);
        }

        @Override
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {

            Point point = map.toMapPoint(to.getX(), to.getY());
            if (point == null || point.isEmpty()) {
                return false;
            }

            if (active
                    && (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
                    || drawType == POLYLINE
                    || drawType == FREEHAND_POLYLINE || drawType == CIRCLE)) {
                switch (drawType) {
                    case ENVELOPE:
                        if (startPoint == null || startPoint.isEmpty()) {
                            return false;
                        }
                        envelope_all.setXMin(startPoint.getX() > point.getX() ? point.getX() : startPoint.getX());
                        envelope_all.setYMin(startPoint.getY() > point.getY() ? point.getY() : startPoint.getY());
                        envelope_all.setXMax(startPoint.getX() < point.getX() ? point.getX() : startPoint.getX());
                        envelope_all.setYMax(startPoint.getY() < point.getY() ? point.getY() : startPoint.getY());
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID,envelope_all.copy());
                        }
                        break;
                    case FREEHAND_POLYGON:
                        polygon_all.lineTo(point);
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID, polygon_all);
                        }
                        break;
                    case FREEHAND_POLYLINE:
                        Polyline polyline = polyline_all;
                        double d1 = polyline.calculateLength2D();
                        polyline.lineTo(point);
                        double d2 = polyline.calculateLength2D();
                        double d = d2 - d1;
                        if (d < 6) {
                            polyline_all.removePoint(polyline_all.getPointCount() - 1);
                        }
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID, polyline_all);
                        }
                        break;
                }

                return true;
            }
            return super.onDragPointerMove(from, to);
        }

        @Override
        public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {

            Point point = map.toMapPoint(to.getX(), to.getY());
            if (point == null || point.isEmpty()) {
                return false;
            }

            if (active
                    && (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
                    || drawType == POLYLINE
                    || drawType == FREEHAND_POLYLINE || drawType == CIRCLE)) {
                switch (drawType) {
                    case ENVELOPE:
                        if (startPoint == null || startPoint.isEmpty()) {
                            return false;
                        }
                        envelope_all.setXMin(startPoint.getX() > point.getX() ? point.getX() : startPoint.getX());
                        envelope_all.setYMin(startPoint.getY() > point.getY() ? point.getY() : startPoint.getY());
                        envelope_all.setXMax(startPoint.getX() < point.getX() ? point.getX() : startPoint.getX());
                        envelope_all.setYMax(startPoint.getY() < point.getY() ? point.getY() : startPoint.getY());
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID,envelope_all.copy());
                        }
                        break;
                    case FREEHAND_POLYGON:
                        polygon_all.lineTo(point);
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID, polygon_all);
                        }
                        break;
                    case FREEHAND_POLYLINE:
                        Polyline polyline = polyline_all;
                        if (polyline == null || !polyline.isValid()) {
                            return false;
                        }
                        double d1 = polyline.calculateLength2D();
                        polyline.lineTo(point);
                        double d2 = polyline.calculateLength2D();
                        double d = d2 - d1;
                        if (d > 6) {
                            polyline_all.lineTo(point);
                            if (graphicsLayer != null) {
                                graphicsLayer.updateGraphic(graphicID, polyline_all);
                            }
                        }

                        break;
                }
                active = false;
            }
            return super.onDragPointerUp(from, to);
        }

        @Override
        public boolean onSingleTap(MotionEvent e) {

            if (mCallout.isShowing()) {
                mCallout.hide();
            }
            mapPoint = map.toMapPoint(e.getX(), e.getY());
            if (mapPoint == null || mapPoint.isEmpty()) {
                return false;
            }
            else if(actionMode==ActionMode.MODE_SELECT)
            {
                Log.e("tag",layerNameList.toString());
                if (layerNameList.size() > 0) {
                    layerNameList.get(0).getLayer().clearSelection();
                    Log.e("tag",layerNameList.toString());
                    getGeometryInfo(mapPoint);
                }

            }

            if (actionMode == ActionMode.MODE_QIEGE && drawType == POLYLINE) {
                // 切割小班
                if (startPoint == null) {
                    startPoint = mapPoint;
                    polyline_all.startPath(mapPoint);
                } else {
                    polyline_all.lineTo(mapPoint);
                }
                graphicsLayer.updateGraphic(graphicID, drawGraphic);
            } else if (actionMode == ActionMode.MODE_CEMIAN && drawType == POLYGON) {
                // 面积测量
                if (startPoint == null) {
                    startPoint = mapPoint;
                    polygon_all.startPath(mapPoint);
                } else
                    polygon_all.lineTo(mapPoint);
                DroolCeMian(mapPoint, polygon_all);
                graphicsLayer.updateGraphic(graphicID, polygon_all);
            } else if (actionMode == ActionMode.MODE_CEJU && drawType == POLYLINE) {
                // 距离测量
                if (startPoint == null) {
                    startPoint = mapPoint;
                    polyline_all.startPath(mapPoint);
                } else {
                    polyline_all.lineTo(mapPoint);
                }
                DroolCeJu(mapPoint, polyline_all);
                graphicsLayer.updateGraphic(graphicID, polyline_all);
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Point mapPoint = map.toMapPoint(e.getX(), e.getY());
            if (mapPoint == null || mapPoint.isEmpty()) {
                return false;
            }


            if (actionMode == ActionMode.MODE_CEJU) {
                polyline_all.lineTo(mapPoint);
                DroolCeJu(mapPoint, polyline_all);
                graphicsLayer.updateGraphic(graphicID, polyline_all);
                ToastUtil.setToast(mContext, "测量结束");
                restory();
                return false;
            }

            if (actionMode == ActionMode.MODE_CEMIAN) {
                if (drawType == POLYGON) {
                    polygon_all.lineTo(mapPoint);
                    DroolCeMian(mapPoint, polygon_all);
                    graphicsLayer.updateGraphic(graphicID, polygon_all);
                    ToastUtil.setToast(mContext, "测量结束");
                    restory();
                }
                return false;
            }
            return super.onDoubleTap(e);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_self:
                /*自身定位*/
                //mylocation(v);
                BaseActivityPermissionsDispatcher.mylocationWithCheck(this,v);
                break;
            case R.id.share_tcxr:
                /*图层渲染*/
                showLayerRenderSystem();
                //showPopupKjtj();
                break;
            case R.id.share_xbbj:
                /* 空间数据编辑 */
                if (xbbjInclude.getVisibility() == View.VISIBLE) {
                    xbbjInclude.setVisibility(View.GONE);
                } else {
                    xbbjInclude.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.share_dmgl:
                /* 地名搜素 */
                dimingMangerPopWindow = new MorePopWindow(this, R.layout.popup_share_addressmanger);
                dimingMangerPopWindow.showPopupWindow(dmMangerImgview);
                View dmsearch = dimingMangerPopWindow.getContentView().findViewById(R.id.xiaodimingsearch);
                dmsearch.setOnClickListener(this);
                View dmAdd = dimingMangerPopWindow.getContentView().findViewById(R.id.xiaodimingadd);
                dmAdd.setOnClickListener(this);

                break;
            case R.id.xiaodimingsearch:
                /* 地名搜素 - 小地名查询 */
                dimingMangerPopWindow.dismiss();
                xdmSearchInclude.setVisibility(View.VISIBLE);
                basePresenter.initXdmView(xdmSearchInclude);
                break;
            case R.id.xiaodimingadd:
                // 地名搜素--小地名添加
                dimingMangerPopWindow.dismiss();
                actionMode = ActionMode.MODE_ADD_ADDRESS;
                break;
            case R.id.share_xtsz:
                /*系统设置*/
                showDialogSettings();
                //gpsCollectPresenter.showGPSCollectType();
                break;
            case R.id.tckz_imageview:
                /* 图层控制*/
                tckzInclude.setVisibility(View.VISIBLE);
                layerControlPresenter.initOtmsData();
                break;

            case R.id.share_gjcx:
                /* 轨迹查询 */
//                guijiPopwindow = new MorePopWindow(this, R.layout.popup_share_gujichaxun);
//                guijiPopwindow.showPopupWindow(gjsearchImgview);
//                guijiPopwindow.getContentView().findViewById(R.id.mine_guiji).setOnClickListener(this);
//                guijiPopwindow.getContentView().findViewById(R.id.show_guijizs).setOnClickListener(this);

//                minegjsearchInclude.setVisibility(View.VISIBLE);
//                trajectoryPresenter.initMyTrajectorySearch(minegjsearchInclude);
                new DialogImpl().showGjcxView(BaseActivity.this, minegjsearchInclude, mapView, graphicsLayer);
                break;
            case R.id.mine_guiji:
                /* 轨迹查询--自身轨迹查询 */
                guijiPopwindow.dismiss();
                minegjsearchInclude.setVisibility(View.VISIBLE);
                trajectoryPresenter.initMyTrajectorySearch(minegjsearchInclude);

                break;
            case R.id.show_guijizs:
                /* 轨迹查询--轨迹展示 */
                guijiPopwindow.dismiss();
                //initGuijiSearchView();
                break;
            case R.id.tile_extent:
                /* 定位至基础地图所在范围 */
                zoomTitleLayer();
                break;
            case R.id.image_extent:
                /* 定位至影像图所在范围 */
                layerControlPresenter.zoomImageLayer();
                break;
            case R.id.share_zbdw:
			    /* 坐标定位 */
                showZbdwDialog();
                break;
            case R.id.shuxing_lable:
                /*属性标注*/
                showFeatureLayer(ActionMode.MODE_ADD_LABLE,layerNameList);
                break;
            default:
                break;
        }
    }



    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALBUM:// 从相册选取
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(selectedImage,
                            filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    EditPhotoDialog dialog = new EditPhotoDialog(mContext,
                            R.style.Dialog, picturePath, currentPoint);
                    dialog.show();
                    personCenterPopup.dismiss();
                }
                break;
            case PicUpDialog.PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    PicUpDialog.initSelectPics(mContext,data);
                }
                break;
            case EDIT_FEATURE:
                if(resultCode==1){
                    String gname = layerControlPresenter.gname;
                    String cname = layerControlPresenter.cname;
                    final String path = layerControlPresenter.path;
                    for (int i = 0; i < layerNameList.size(); i++) {
                        if (gname.equals(layerNameList.get(i).getPname())&&cname.equals(layerNameList.get(i).getCname())){
                            boolean encryption = layerNameList.get(i).isFlag();
                            if(encryption){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SytemUtil.jiamicript(path);
                                    }
                                }).start();
                            }
                            mapView.removeLayer(layerNameList.get(i).getLayer());
                        }
                    }
                    boolean flag = SytemUtil.checkGeodatabase(path);
                    if (flag) {
                        SytemUtil.decript(path);
                    }
                    layerControlPresenter.loadGeodatabase(path,flag,gname,cname);
                }
            default:
                break;
        }
    }*/

    /**
     * 选择小班
     */
    public void selectFeature(View view) {
        initTouch();
        if (selGeoFeaturesList.size() > 0) {
            selGeoFeaturesList.clear();
        }
        for (final MyLayer layer : layerNameList) {
            if (layer != null && layer.getLayer().isInitialized()) {
                layer.getLayer().clearSelection();
            }
        }
        actionMode = ActionMode.MODE_SELECT;
        drawType = ENVELOPE;
        activate(drawType);
        showFeatureLayer(actionMode,layerNameList);
    }

    /**
     * 新增小班
     */
    public void addFeature(View view) {
        initTouch();
        if (polyline_all != null) {
            graphicsLayer.updateGraphic(graphicID, polyline_all);
            //mapView.invalidate();
        }
        actionMode = ActionMode.MODE_EDIT_ADD;
        getFeatureLayer(actionMode);
        if (layerNameList.size() == 0) {
            addFeatureBtn.setChecked(false);
        }
    }

    /**
     * 选择featureLayer
     */
    public void getFeatureLayer(ActionMode mode) {
        //clear();
        /*int size = layerNameList.size();
        if (size == 0) {
            ToastUtil.setToast(mContext, "未加载空间数据,请在图层控制中加载数据");
            return;
        }*/
        if (layerNameList.size() > 0) {
            showFeatureLayer(mode, layerNameList);
        }else {
            ToastUtil.setToast(mContext, "未加载空间数据,请在图层控制中加载数据");
        }
    }

    /**
     * 删除小班
     */
    public void deleteFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 1) {
            selGeoFeature = selGeoFeaturesList.get(0);
            selectGeometry = selGeoFeature.getGeometry();
            selectFeatureAts = selGeoFeature.getAttributes();
            long id = selGeoFeature.getId();
            showDeleteFeatureDialog(id, 0, null, null, null);
        } else if (size > 1) {
            showDelFeatureResult(selGeoFeaturesList);
        } else {
            deleteButton.setChecked(false);
            ToastUtil.setToast(mContext, "未选中小班");
        }
    }

    /**
     * 撤销
     */
    public void repealFeature(View view) {
        initTouch();
        clear();
        //actionMode = ActionMode.MODE_NULL;
        int size = undonList.size();
        if (size == 0) {
            repealButton.setChecked(false);
            ToastUtil.setToast(mContext, "无法回退");
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Map<String, Object> map = undonList.get(i);
            if (map.get("type").equals("add")) {
                FeatureLayer layer = (FeatureLayer) map.get("layer");
                long id = Long.parseLong(map.get("id").toString());
                try {
                    layer.getFeatureTable().deleteFeature(id);
                    undonList.remove(undonList.get(i));
                    break;
                } catch (TableException e) {
                    e.printStackTrace();
                }
            } else if (map.get("type").equals("delete")) {
                FeatureLayer layer = (FeatureLayer) map.get("layer");
                long id = Long.parseLong(map.get("id").toString());
                Geometry geometry = (Geometry) map.get("geometry");
                Symbol symbol = (Symbol) map.get("symbol");
                Graphic graphic = new Graphic(geometry, symbol, (Map<String, Object>) map.get("attribute"), (int) id);
                try {
                    layer.getFeatureTable().addFeature(graphic);
                } catch (TableException e) {
                    e.printStackTrace();
                }
                undonList.remove(undonList.get(i));
                break;
            } else if (map.get("type").equals("update")) {
                FeatureLayer layer = (FeatureLayer) map.get("layer");
                long id = Long.parseLong(map.get("id").toString());
                Geometry geometry = (Geometry) map.get("geometry");
                Symbol symbol = (Symbol) map.get("symbol");
                Graphic graphic = new Graphic(geometry, symbol, (int) id);
                try {
                    layer.getFeatureTable().updateFeature(id, graphic);
                    undonList.remove(undonList.get(i));
                    break;
                } catch (TableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 地图复位
     */
    public void mapRemove(View view) {
        initTouch();
        clearGraphic();
        actionMode = ActionMode.MODE_SELECT;
        drawType = EMPTY;
        activate(drawType);
        //mapView.invalidate();
    }



    /**
     * 分割小班
     */
    public void qiegeFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 0) {
            qiegeButton.setChecked(false);
            ToastUtil.setToast(mContext, "请选中一个图斑进行分割操作");
            return;
        }
        if (size > 1) {
            selectButton.setChecked(true);
            ToastUtil.setToast(mContext, "只能对一个小班进行分割操作");
            return;
        }
        GeodatabaseFeature feature = selGeoFeaturesList.get(0);
        if (feature.getGeometry().getType() != Geometry.Type.POLYGON) {
            ToastUtil.setToast(mContext, "所选图斑不是面图形");
            return;
        }
        actionMode = ActionMode.MODE_QIEGE;
        getSelParams(selGeoFeaturesList, 0);

        if (selectGeometry != null && selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
            drawType = FREEHAND_POLYLINE;
            activate(drawType);
        } else {
            if (selectGeometry == null) {
                ToastUtil.setToast(mContext, "未选择切割图形");
                return;
            }
            if (!selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
                ToastUtil.setToast(mContext, "所选图形不符合切割规则");
            }
        }

    }

    /**
     * 合并小班
     */
    public void mergeFeature(View view) {
        initTouch();
        if (selGeoFeaturesList == null || selGeoFeaturesList.size() < 2) {
            if (selGeoFeaturesList.size() == 0) {
                hebingButton.setChecked(false);
            } else {
                selectButton.setChecked(true);
            }
            ToastUtil.setToast(mContext, "请选择两个及以上图斑进行合并");
            return;
        }

        showHeBing(selGeoFeaturesList, view);

    }

    /**
     * 修斑
     */
    public void xiubFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 0) {
            xiubanButton.setChecked(false);
            ToastUtil.setToast(mContext, "请选中一个小班面进行修斑操作");
            return;
        }
        if (size > 1) {
            selectButton.setChecked(true);
            ToastUtil.setToast(mContext, "只能对一个小班面进行修斑操作");
            return;
        }
        GeodatabaseFeature feature = selGeoFeaturesList.get(0);
        if (feature.getGeometry().getType() != Geometry.Type.POLYGON) {
            ToastUtil.setToast(mContext, "所选图斑不是面图形");
            return;
        }
        actionMode = ActionMode.MODE_XIUBAN;
        getSelParams(selGeoFeaturesList, 0);
        drawType = FREEHAND_POLYLINE;
        activate(drawType);
    }

    /**
     * 属性查询
     */
    public void attributeFeture(View view) {
        //initTouch();

        //List<GeodatabaseFeature> data = new ArrayList<>();
        //data.addAll(selGeoFeaturesList);
        int size = selGeoFeaturesList.size();
        if (size > 0) {
            if (size == 1) {
                //toFeatureResult(selGeoFeaturesList);
                showCallout(selGeoFeaturesList.get(0));
            } else {
                basePresenter.showListFeatureResult(selGeoFeaturesList,1);
            }
            attributButton.setChecked(false);
        } else {
            attributButton.setChecked(false);
            ToastUtil.setToast(mContext, "未选中任何图斑");
        }
    }

    /**
     * 属性查看
     * @param geodatabaseFeature
     */
    protected abstract void showCallout(GeodatabaseFeature geodatabaseFeature);

    /**
     * 小班保存
     */
    public void saveFeature(Polygon polygon_all) {

        if (actionMode == ActionMode.MODE_EDIT_ADD) {
            // 新增分点线面
            if (drawType == POINT) {
                basePresenter.addFeaturePoint(point_all);
            } else if (drawType == FREEHAND_POLYGON) {//
                basePresenter.addFeaturePolygon(polygon_all);
            } else if (drawType == FREEHAND_POLYLINE && myLayer.getLayer().getGeometryType().equals(Geometry.Type.POLYGON)) {
                basePresenter.addFeaturePolygon(polygon_all);
            } else if (drawType == FREEHAND_POLYLINE
                    && myLayer.getLayer().getGeometryType().equals(Geometry.Type.POLYLINE)) {
                basePresenter.addFeatureLine(polyline_all);
            }
        } else if (actionMode == ActionMode.MODE_QIEGE) {
            //分割
            saveQgFeature(polyline_all, selectGeometry);
        } else if (actionMode == ActionMode.MODE_XIUBAN) {
            //修班
            saveXbFeature(polyline_all);
        }
        polygon_all = null;
        polyline_all = null;
        point_all = null;
        clear();
        //mapView.invalidate();
        if (actionMode == ActionMode.MODE_XIUBAN) {
            xiubFeature(new View(mContext));
        }

        if (actionMode == ActionMode.MODE_QIEGE) {
            qiegeFeature(new View(mContext));
        }

    }



    /**
     * 切割面保存方法
     */
    public void saveQgFeature(Polyline line, Geometry geom) {
        if (line == null || !line.isValid())
            return;
		/* 判断两个图形是否有相交部分 */
        boolean flag = GeometryEngine.intersects(geom, line, spatialReference);
		/* flag为true时线段与所选小班有交互 反之无交互 */
        if (!flag) {
            ToastUtil.setToast(mContext, "无法分割对象,两个图斑无交集");
            return;
        }
        clear();
		/* 获取到线与面相交的部分 */
        Polyline cutGeom = (Polyline) GeometryEngine.intersect(geom, line, spatialReference);
        if (cutGeom == null || cutGeom.isEmpty() || !cutGeom.isValid()) {
            return;
        }
		/* 判断交线是否自相交 */
        int line_size = cutGeom.getPointCount();
        Point startCut = cutGeom.getPoint(0);
        Point endCut = cutGeom.getPoint(line_size - 1);
        boolean sflag = GeometryEngine.touches(startCut, geom, spatialReference);
        boolean eflag = GeometryEngine.touches(endCut, geom, spatialReference);
        boolean zxjflag = ArcGISUtils.isIntersect(cutGeom, mapView);

        if (zxjflag) {
			/* 线段自交 */
			/* 判断首位交点是否都是在选择geometry面内,如果有一个不在面内则不符合切割规则 */
            ToastUtil.setToast(mContext, "线段自交,封闭切割");

            Polygon polygon = ArcGISUtils.LineToPolygon(mContext, line, mapView);
            Geometry difGeometry = GeometryEngine.difference(geom, polygon, spatialReference);

            Geometry geometry = GeometryEngine.difference(geom, difGeometry, spatialReference);

            basePresenter.updateFeature(difGeometry, selGeoFeature, myLayer);

            basePresenter.addFeatureOnLayer(geometry, selectFeatureAts);

        } else {
			/* 线段不自交 */
			/* 判断首位交点是否都是在选择geometry面内,如果有一个不在面内则不符合切割规则 */
            if (!(sflag && eflag)) {
                ToastUtil.setToast(mContext, "不满足图斑切割规则");
                return;
            }
            Polygon curpolygon = (Polygon) geom;
            int psize = curpolygon.getPointCount();
            int s_s_index = 0, s_e_index = 0, e_e_index = 0, e_s_index = 0;
            for (int i = 0; i < psize - 1; i++) {
                Polyline lineTemp = new Polyline();
                Point point1 = curpolygon.getPoint(i);
                lineTemp.startPath(point1);
                Point point2 = null;
                if (i == psize) {
                    point2 = curpolygon.getPoint(0);
                } else {
                    point2 = curpolygon.getPoint(i + 1);
                }
                lineTemp.lineTo(point2);

                boolean intersect_s = GeometryEngine.intersects(startCut,
                        lineTemp, spatialReference);
                if (intersect_s) {
                    s_s_index = i;
                    s_e_index = i + 1;
                    break;
                }
            }

            for (int i = 0; i < psize - 1; i++) {
                Polyline lineTemp = new Polyline();
                Point point1 = curpolygon.getPoint(i);
                lineTemp.startPath(point1);
                Point point2 = null;
                if (i == psize) {
                    point2 = curpolygon.getPoint(0);
                } else {
                    point2 = curpolygon.getPoint(i + 1);
                }
                lineTemp.lineTo(point2);

                boolean intersect_s = GeometryEngine.intersects(endCut,
                        lineTemp, spatialReference);
                if (intersect_s) {
                    e_s_index = i;
                    e_e_index = i + 1;
                    break;
                }
            }
			/* 以线的起点为新Geometry的起点 获取线的右侧的面 */
            Polygon yPolygon = new Polygon();
            for (int i = 0; i < line_size; i++) {
                if (i == 0) {
                    yPolygon.startPath(cutGeom.getPoint(i));
                } else {
                    yPolygon.lineTo(cutGeom.getPoint(i));
                }
            }

            if (e_s_index > s_e_index) {
                for (int i = e_s_index; i >= s_e_index; i--) {
                    yPolygon.lineTo(curpolygon.getPoint(i));
                }
            } else if (s_s_index > e_e_index) {
                for (int i = e_e_index; i <= s_s_index; i++) {
                    yPolygon.lineTo(curpolygon.getPoint(i));
                }
            }

            Geometry diff = GeometryEngine.difference(geom, yPolygon, spatialReference);
            Geometry geometry = GeometryEngine.difference(geom, diff, spatialReference);
            basePresenter.updateFeature(diff, selGeoFeature, myLayer);

            basePresenter.addFeatureOnLayer(geometry, selectFeatureAts);
        }
    }



    /**
     * 添加GPS跑动轨迹形成的面或者线
     */
    public void addGPSFeature() {
        if (myLayer == null) {
            ToastUtil.setToast(mContext, "未选择图层");
            return;
        }
        if (layerType == null) {
            ToastUtil.setToast(mContext, "未选择图层");
            return;
        }
        if (polyline_all == null) {
            ToastUtil.setToast(mContext, "未获取正确的轨迹");
            return;
        }
        if (layerType.equals(Geometry.Type.POLYLINE) || layerType.equals(Geometry.Type.LINE)) {
            basePresenter.addFeatureLine(polyline_all);
        } else if (layerType.equals(Geometry.Type.POLYGON)) {
            //polygon_all = ArcGISUtils.LineToPolygon(mContext,polyline_all, mapView);
            /*Polygon polygon = new Polygon();
            int size = polyline_all.getPointCount();
            polygon.startPath(polyline_all.getPoint(0));
            for (int i = 1; i < size; i++) {
                polygon.lineTo(polyline_all.getPoint(i));
            }
            polygon_all = polygon;
            if (polygon.isEmpty() || !polygon.isValid()) {
                ToastUtil.setToast(mContext, "GPS轨迹数据不符合构建面图形规则");
                return;
            }*/
            Polygon resultPolygon=GeometryUtil.polyline2Polygon(polyline_all,spatialReference);
            if(resultPolygon!=null){
                Graphic graphic=new Graphic(resultPolygon, com.titan.gis.SymbolUtil.areaSymbol);
                basePresenter.addGraphic2Layer(graphic);
                double area= MeasureUtil.measureArea(resultPolygon,spatialReference,null);
                basePresenter.addGraphicLabel(mContext,resultPolygon,"面积:"+area+"平方米");
            }else {
                ToastUtil.setToast(mContext,"轨迹绘面失败");
            }
            polyline_all = null;
        }
    }





    /**
     * 初始化系统设置dialog窗口
     */
    public void showDialogSettings() {
        SettingDialog settingDialog = new SettingDialog(BaseActivity.this, R.style.Dialog, this);
        BussUtil.setDialogParams(mContext, settingDialog, 0.5, 0.65);
    }




    /**
     * 创建一个半径为5m的圆
     */
    public Polygon creatPolygon(Point point) {
        return GeometryEngine.buffer(point, spatialReference, 5, null);
    }

    /**
     * 坐标定位弹出窗口
     */
    public void showZbdwDialog() {
        CoordinateDialog coordinateDialog = new CoordinateDialog(mContext, R.style.Dialog, this);
        BussUtil.setDialogParams(mContext, coordinateDialog, 0.6, 0.65);
    }

    /**
     * 缩放至基础图层所在位置
     */
    public void zoomTitleLayer() {
        if (tiledLayer != null && tiledLayer.isVisible()) {
            mapView.setExtent(tiledLayer.getFullExtent());
            //mapView.invalidate();
        } else {
            ToastUtil.setToast(mContext, "基础图未加载,请检查地图文件是否存在");
        }
    }


    /**加载otms文件夹下数据*/
    public void initOtmsChild(List<File> groups, List<Map<String, List<File>>> childs,int gPosition, int cPosition) {
        String parentName = groups.get(gPosition).getName();
        String childName = childs.get(gPosition).get(parentName).get(cPosition).getName().split("\\.")[0];
        String path = childs.get(gPosition).get(parentName).get(cPosition).getPath();

        boolean ischeck = childCheckBox.get(path);
        layerControlPresenter.changeCBoxStatus(ischeck, path, parentName, childName);
    }

    /**缩放至图元所在位置*/
    public void ZoomToGeom(Geometry geometry) {
        mapView.setExtent(geometry);

    }


    /**
     * 测距点击地图时显示距离和关闭按钮
     */
    public View loadCalloutPoint(Point point, Polyline polyline) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.callout, null);

        ImageView close = (ImageView) view.findViewById(R.id.img_delete);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawType = POLYLINE;
                activate(drawType);
                mCallout.hide();
            }
        });

        DecimalFormat df = new DecimalFormat("0.00");
        double dd = polyline.calculateLength2D();

        TextView txt = (TextView) view.findViewById(R.id.showDistence);
        String value = df.format(dd) + "米";
        txt.setText(value);
        return view;
    }

    /**
     * 测面时进行绘制操作
     */
    public void DroolCeMian(final Point point, final Polygon polygon) {
        ArcGISUtils.DroolMian(mContext, MyApplication.screen, graphicsLayer, point, polygon);
    }

    /**
     * 侧面时进行绘制操作
     */
    public void DroolCeMian(final Polygon polygon, final double area) {
        ArcGISUtils.DroolMian(mContext, MyApplication.screen, graphicsLayer, polygon, area);
    }

    /**
     * 测距
     * @param point
     * @param polyline
     */
    public void DroolCeJu(final Point point, final Polyline polyline) {
        ArcGISUtils.DroolLine(mContext, MyApplication.screen, graphicsLayer, point, polyline);
        int size = polyline.getPointCount();
        if (size > 1) {
            mCallout.show(point, loadCalloutPoint(point, polyline_all));
        }
    }

    /**数据初始化*/
    public void restory() {
        this.active = false;
        this.drawType = -1;
        this.point_all = null;
        this.envelope_all = null;
        this.polygon_all = null;
        this.polyline_all = null;
        this.drawGraphic = null;
        startPoint = null;
        actionMode = ActionMode.MODE_SELECT;
    }

    /**清除标绘*/
    public void clear() {
        if (graphicsLayer != null) {
            graphicsLayer.removeAll();
            locationGraphic = null;
            circleGraphic = null;
        }
    }

    /**勾绘数据清空*/
    public void deactivate() {
        clear();
        this.active = false;
        this.drawType = -1;
        this.point_all = null;
        this.envelope_all = null;
        this.polygon_all = null;
        this.polyline_all = null;
        this.drawGraphic = null;
        this.startPoint = null;
    }

    /**初始化勾绘数据*/
    public void activate(int drawType) {
        if (mapView == null)
            return;

        this.deactivate();

        this.drawType = drawType;
        this.active = true;
        switch (this.drawType) {
            case POINT:
                this.point_all = new Point();
                drawGraphic = new Graphic(this.point_all, this.markerSymbol);
                break;
            case ENVELOPE:
                this.envelope_all = new Envelope();
                if (actionMode == ActionMode.MODE_SELECT) {
                    drawGraphic = new Graphic(this.envelope_all, this.selectfillSymbol);
                } else {
                    drawGraphic = new Graphic(this.envelope_all, this.fillSymbol);
                }
                break;
            case POLYGON:
            case CIRCLE:
            case FREEHAND_POLYGON:
                this.polygon_all = new Polygon();
                drawGraphic = new Graphic(this.polygon_all, this.fillSymbol);
                break;
            case FREEHAND_POLYLINEGPS:
                this.polyline_all = new Polyline();
                drawGraphic = new Graphic(this.polyline_all, this.lineSymbol);
                break;
            case POLYLINE:
            case FREEHAND_POLYLINE:
                this.polyline_all = new Polyline();
                drawGraphic = new Graphic(this.polyline_all, this.lineSymbol);
                break;
        }

        if (graphicsLayer != null) {
            graphicID = this.graphicsLayer.addGraphic(drawGraphic);
        }

    }



    /**显示小班查询数据*/
    /*private void showFeaturInfo(Geometry geometry, FeatureResult result) {
        Iterator<Object> iterator = result.iterator();
        GeodatabaseFeature feature = null;
        while (iterator.hasNext()) {
            feature = (GeodatabaseFeature) iterator.next();
            Map<String, Object> attributes = feature.getAttributes();
            final List<Field> fieldList = feature.getTable().getFields();
            View views = basePresenter.loadAttributeView(fieldList, attributes);
            Point point = (Point) geometry;
            mCallout.setStyle(R.xml.mCalloutstyle);
            mCallout.show(point, views);
        }
    }
*/
    /**
     * 图层选择
     * 编辑图层选择窗口， 选择要编辑的图层
     */
    public void showFeatureLayer(ActionMode mode, List<MyLayer> layerList) {//
        if(layerList.size() == 0){
            ToastUtil.setToast(mContext,"请先加载图层");
            return;
        }
        if(mode == ActionMode.MODE_ADD_LABLE){
            //图层标注
            myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("fxh", layerList);
            layerType = myLayer.getLayer().getGeometryType();
            layerLableInclude.setVisibility(View.VISIBLE);
            lablePresenter.showLayerAials(layerLableInclude,myLayer);
        }else if(actionMode==ActionMode.MODE_SELECT){
            //myLayer = layerList.get(0);
            myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("fxh", layerList);
            layerType = myLayer.getLayer().getGeometryType();
        }
        else{
            LayerSelectDialog selectDialog = new LayerSelectDialog(mContext,R.style.Dialog,layerList,mode);
            selectDialog.setLayerOnItemClickListener(this);
            selectDialog.setCancelable(false);
            BussUtil.setDialogParams(mContext, selectDialog, 0.5, 0.5);
        }
    }

    /**
     * 添加点数据时 提示是否使用当前点坐标
     */
    public void showAddCurrentPoint(final int drawType, final Point point) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("使用当前点!");
        builder.setTitle("信息提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                point_all = point;
                saveFeature(new Polygon());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ToastUtil.setToast(mContext, "请手动点击地图添加点");
                activate(drawType);
            }
        });
        builder.create().show();
    }



    /**
     * 选择小班查询方法
     */
    public void showGeometryInfo(Geometry geometry, FeatureLayer layer, final ActionMode mode) {
        ArcGISQueryUtils.getSelectFeatures(geometry, spatialReference, layer, new CallbackListener<FeatureResult>() {
            @Override
            public void onError(Throwable arg0) {
                ToastUtil.setToast(mContext, "查询出错");
            }

            @Override
            public void onCallback(FeatureResult featureResult) {
                if (featureResult.featureCount() == 0) {
                    ToastUtil.setToast(mContext, "没有选中任何图斑");
                    return;
                }
                final List<GeodatabaseFeature> list = new ArrayList<GeodatabaseFeature>();
                Iterator<Object> iterator = featureResult.iterator();
                GeodatabaseFeature geodatabaseFeature = null;
                while (iterator.hasNext()) {
                    geodatabaseFeature = (GeodatabaseFeature) iterator.next();
                    list.add(geodatabaseFeature);
                }
                graphicsLayer.removeAll();

            }
        });
    }



    /**
     * 当查询结果只有一个时直接跳转到属性页面
     */
    /*public void toFeatureResult(List<GeodatabaseFeature> list) {
        try {

            getSelParams(list, 0);

            Intent intent = null;
            if (selectGeometry.getType().equals(Geometry.Type.POINT)) {
                intent = new Intent(mContext, PointEditActivity.class);
            } else if (selectGeometry.getType().equals(Geometry.Type.POLYLINE)) {
                intent = new Intent(mContext, LineEditActivity.class);
            } else if (selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
                intent = new Intent(mContext, XbEditActivity.class);
            } else {
                intent = new Intent(mContext, XbEditActivity.class);
            }

            String pname = myLayer.getPname();// 工程名称
            String path = myLayer.getPath();
            String cname = myLayer.getCname();
            MyFeture feture = new MyFeture(pname, path, cname, selGeoFeature, myLayer);
            Bundle bundle = new Bundle();
            bundle.putSerializable("myfeture", feture);
            bundle.putSerializable("parent", "Base");
            bundle.putSerializable("id", selGeoFeature.getId() + "");
            intent.putExtras(bundle);
            startActivityForResult(intent,EDIT_FEATURE);
        }catch (Exception e){
            Log.e("tag",e.toString());
            ToastUtil.setToast(mContext,"属性查询异常"+e);
        }

    }*/

    /**
     * 提示是否刪除小班
     */
    public void showDeleteFeatureDialog(final long id, final int position,
                                        final FeatureResultAdapter adapter, final Dialog dddialog, final ListView listView) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("确认删除吗!");
        builder.setTitle("信息提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    GeodatabaseFeature feature = selGeoFeaturesList.get(position);
                    MyLayer myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer(feature.getTable().getTableName(),layerNameList);
                    FeatureTable featureTable = myLayer.getTable();

                    featureTable.deleteFeature(id);

                    basePresenter.recordXb(id, "delete", feature.getAttributes(), feature.getGeometry(), myLayer.getLayer());
                    if (adapter != null) {
                        selGeoFeaturesList.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        selGeoFeaturesList.remove(position);
                    }
                    int size = selGeoFeaturesList.size();
                    if (size == 0 && dddialog != null) {
                        dddialog.dismiss();
                    }
                    mapRemove(new View(mContext));
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        BaseUtil.getIntance(mContext).setHeight(adapter, listView);
                    }
                    dialog.dismiss();
                } catch (TableException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selGeoFeaturesList.size() == 1) {
                    selectButton.setChecked(true);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
        }
        mapView.destroyDrawingCache();
        super.onDestroy();
    }


    /**
     * 合并小班选择保留哪个小班属性
     */
    public void showHeBing(final List<GeodatabaseFeature> list, final View view) {

        MergeFeatureDialog mergeFeatureDialog = new MergeFeatureDialog(mContext,R.style.Dialog,list,this);
        DialogParamsUtil.setDialogParamsCenter(mContext,mergeFeatureDialog,0.4,0.5);
    }

    /**
     * 添加feature在图层上
     */
    /*public void addFeatureToOtherLayer(GeodatabaseFeature feature) {
        try {
            if (!feature.getGeometry().isValid() || feature.getGeometry().isEmpty()) {
                return;
            }
            FeatureLayer layer = myLayer.getLayer();
            GeodatabaseFeatureTable table =  myLayer.getTable();
            long id = table.addFeature(feature);

			*//* 添加小班后 记录添加小班的id 备撤销时删除 *//*
            basePresenter.recordXb(id, "add", feature.getAttributes(), feature.getGeometry(), layer);
        } catch (TableException e) {
            e.printStackTrace();
        }
        mapView.invalidate();
    }*/

    /**
     * 小班修斑保存方法
     */
    public void saveXbFeature(Polyline drawline) {
        try{
            if (selectGeometry == null) {
                ToastUtil.setToast(mContext, "请选择小班");
                return;
            }
            // 所画草图线的点个数
            int drawSize = drawline.getPointCount();
            if (drawSize < 2) {
                ToastUtil.setToast(mContext, "草图线段点数少于2,无法修整面");
                return;
            }
            Polygon selectPpolygon = (Polygon) selectGeometry;
            int pathSize = selectPpolygon.getPathCount();//2
            if (pathSize == 1) {
                repairPresenter.saveXBoPathFeature(drawline, selectPpolygon);
            } else if (pathSize > 1) {
                saveXbAllPath(drawline);
            }

        }catch(Exception e) {
             ToastUtil.setToast(mContext,"修班出现异常"+e);
        }


    }

    /**多条path时小班修改保存*/
    public void saveXbAllPath(Polyline drawline) {
        Graphic graphic2 = null;
        Polygon selectPpolygon = (Polygon) selectGeometry;
        int pathSize = selectPpolygon.getPathCount();
        for (int i = 0; i < pathSize; i++) {
            int start = selectPpolygon.getPathStart(i);//0 63
            int end = selectPpolygon.getPathEnd(i);//63 98
            Polygon polygon = new Polygon();
            polygon.startPath(selectPpolygon.getPoint(start));
            for (int k = start + 1; k < end; k++) {
                polygon.lineTo(selectPpolygon.getPoint(k));
            }
            boolean flag = GeometryEngine.intersects(drawline, polygon, spatialReference);
            if (flag) {
                if (graphic2 != null) {
                    Graphic graphic3 = repairPresenter.saveXBoPathFeature(drawline, polygon);
                    Geometry geom = GeometryEngine.difference(graphic2.getGeometry(), graphic3.getGeometry(), spatialReference);
                    basePresenter.updateFeature(geom, selGeoFeature, myLayer);
                } else {
                    graphic2 = repairPresenter.saveXBoPathFeature(drawline, polygon);
                }
            } else if (graphic2 != null) {
                Geometry geometry = GeometryEngine.difference(graphic2.getGeometry(), selGeoFeature.getGeometry(), spatialReference);
                Geometry geom = GeometryEngine.difference(graphic2.getGeometry(), geometry, spatialReference);
                basePresenter.updateFeature(geom, selGeoFeature, myLayer);
            }
        }
    }

    @Override
    protected void onPause() {
        if(mLoctionDisplayManager!=null&& mLoctionDisplayManager.isStarted())
            mLoctionDisplayManager.stop();
        if(mCallout!=null&&mCallout.isShowing()){
            mCallout.hide();
        }
        super.onPause();
    }


    /**
     * 小班查询方法
     */
    /*public void getGeometryInfo(Geometry geometry) {
        selGeoFeaturesList.clear();
        getGeometryInfo(geometry, myLayer);
    }*/

    /**根据勾绘区域查询图层小班*/
    public void getGeometryInfo(Geometry geometry) {
        selGeoFeaturesList.clear();
        //myLayer = layerNameList.get(0);
        Log.e("tag","query");
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("fxh", layerNameList);
        ProgressDialogUtil.startProgressDialog(mContext);
        ArcGISQueryUtils.getSelectFeatures(geometry,mapView.getSpatialReference(), myLayer.getLayer(), new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                long size = objects.featureCount();
                Log.e("tag",myLayer.getTable().getTableName()+","+size);
                ProgressDialogUtil.stopProgressDialog(mContext);
                if (size > 0) {
                    Iterator<Object> iterator = objects.iterator();
                    GeodatabaseFeature geodatabaseFeature;
                    while (iterator.hasNext()) {
                        geodatabaseFeature = (GeodatabaseFeature)iterator.next();
                        selGeoFeaturesList.add(geodatabaseFeature);
                        Log.e("tag",geodatabaseFeature.toString());
                        selMap.put(geodatabaseFeature, myLayer.getCname());
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            myLayer.getLayer().clearSelection();
                            //ProgressDialogUtil.stopProgressDialog(mContext);
                            ToastUtil.setToast(mContext, "没有查询到数据");
                        }
                    });
                }
            }

            @Override
            public void onError(final Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.setToast(mContext, "查询出错"+throwable);
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });

            }
        });
        //清楚选择数据框
        graphicsLayer.removeGraphic(graphicID);
    }

    /**
     * 删除选中时如果选中了多个小班弹出选择删除哪个小班
     */
    public void showDelFeatureResult(final List<GeodatabaseFeature> list) {
        FeatureDelDialog delDialog = new FeatureDelDialog(mContext,R.style.Dialog,list,this,selMap);
        BussUtil.setDialogParams(mContext, delDialog, 0.55, 0.55);
    }

    /**
     * 获取选中小班的数据
     */
    public GeodatabaseFeature getSelParams(List<GeodatabaseFeature> list, int position) {

        selGeoFeature = list.get(position);
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer(selGeoFeature.getTable().getTableName(),layerNameList);
        if (myLayer == null) {
            ToastUtil.setToast(mContext, "数据图层已经移除，请重新选择小班");
            return null;
        }
//        long id = feature.getId();
//        try {
//            //selGeoFeature = (GeodatabaseFeature) myLayer.getTable().getFeature(id);
//            selGeoFeature=feature;
//            selectGeometry = feature.getGeometry();
//            selectFeatureAts = feature.getAttributes();
//            //selectfiledList = myLayer.getTable().getFields();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return selGeoFeature;
    }




    @Override
    public void handleDrawEvent(DrawEvent event) {
        // 将画好的图形（已经实例化了Graphic），添加到drawLayer中并刷新显示
        ProgressDialogUtil.startProgressDialog(mContext);
        Graphic graphic = event.getDrawGraphic();
        graphicsLayer.addGraphic(graphic);

    }

    /**
     * 重新setTouch   myTouchListener
     */
    public void initTouch() {
        if (drawTool != null) {
            drawTool.activate(0);
            mapView.setOnTouchListener(myTouchListener);
        }
    }



    /**
     * 显示图层渲染设置页面
     */
    public void showLayerRenderSystem() {
        RenderSetDialog setDialog = new RenderSetDialog(mContext,R.style.Dialog,this,layerControlPresenter);
        BussUtil.setDialogParams(mContext, setDialog, 0.55, 0.55);
    }



    /**
     * 点击地图获取地图点坐标数据并展示
     */
    //@NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    /*public void addmCalloutPoint(MapView map, Point mappoint) {
        XmlPullParser parser = getResources().getXml(R.xml.mCalloutpopuwindowstyle);

        mCalloutStyle style = new mCalloutStyle(mContext, Xml.asAttributeSet(parser));
        View view = basePresenter.loadmCalloutPopuwindow(mappoint);
        //View view = loadmCalloutView(mappoint);
        final mCalloutPopupWindow mCalloutP = new mCalloutPopupWindow(view, mCalloutPopupWindow.MODE.OVERFLOW, style);
        mCalloutP.showmCallout(map, mappoint, 0, 0);
        mCalloutPopupWindows.add(mCalloutP);

        ImageView close = (ImageView) view.findViewById(R.id.mCalloutpopuwindow_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalloutP.hide();
                mCalloutPopupWindows.remove(mCalloutP);
            }
        });

        BussUtil.closemCalloutPopu(view, mCalloutP);
    }*/



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);

    }

    @Override
    public View getGpsCollectionView() {
        return gpsCaijiInclude;
    }

    @Override
    public View getParChildView() {
        return childview;
    }

    @Override
    public ArcGISLocalTiledLayer getTitleLayer() {
        return tiledLayer;
    }


    @Override
    public ArcGISLocalTiledLayer getImgLayer() {
        return imgTiledLayer;
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public GraphicsLayer getGraphicLayer() {
        return graphicsLayer;
    }

    @Override
    public List<MyLayer> getLayerNameList() {
        return layerNameList;
    }

    @Override
    public View getImgeLayerView() {
        return tckzImgInclude;
    }

    @Override
    public View getTckzView() {
        return tckzInclude;
    }

    @Override
    public HashMap<String, Boolean> getLayerCheckBox() {
        return childCheckBox;
    }

    @Override
    public List<Row> getSysLayerData() {
        return proData;
    }

    @Override
    public List<String> getLayerKeyList() {
        return childKeyList;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return MyApplication.sharedPreferences;
    }

    @Override
    public Callout getCallout() {
        return mCallout;
    }

    @Override
    public double getCurrenLat() {
        return currentLat;
    }

    @Override
    public double getCurrentLon() {
        return currentLon;
    }


    @Override
    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

   /* @Override
    public RouteTask getRouteTask() {
        return mRouteTask;
    }*/

    @Override
    public void removeGraphicLayer() {
        if (graphicsLayer != null && graphicsLayer.isInitialized()) {
            mapView.removeLayer(graphicsLayer);
        }
    }

    @Override
    public void addGraphicLayer() {
        if (graphicsLayer != null) {
            mapView.addLayer(graphicsLayer);
        } else {
            graphicsLayer = new GraphicsLayer(GraphicsLayer.RenderingMode.STATIC);
            mapView.addLayer(graphicsLayer);
        }
    }

    @Override
    public Envelope getCurrentEnvelope() {
        Polygon polygon = tiledLayer.getExtent();
        Envelope envelope = new Envelope();
        polygon.queryEnvelope(envelope);
        return envelope;
    }

    @Override
    public View getGpsCaijiInclude() {
        return gpsCaijiInclude;
    }



    @Override
    public ArcGISLocalTiledLayer getBaseTitleLayer() {
        return tiledLayer;
    }

    @Override
    public ArcGISLocalTiledLayer getImgTitleLayer() {
        return imgTiledLayer;
    }

    @Override
    public List<MyLayer> getLayerList() {
        return layerNameList;
    }

    @Override
    public Point getCurrentPoint() {
        return currentPoint;
    }

    @Override
    public DrawTool getDrawTool() {
        return drawTool;
    }

    @Override
    public void addImageLayer(String path) {
        imgTiledLayer = new ArcGISLocalTiledLayer(path);
        mapView.addLayer(imgTiledLayer);
    }


    @Override
    public void setLayerOnItemClickListener(AdapterView<?> parent, View view, int position, long id,ActionMode mode,List<MyLayer> layerList,LayerSelectDialog dialog) {
        actionMode = mode;
        myLayer = layerList.get(position);
        layerType = myLayer.getLayer().getGeometryType();
        //String layername = myLayer.getLname();


        SytemUtil.getEditSymbo(BaseActivity.this, myLayer.getLayer());
        dialog.dismiss();
         if (mode == ActionMode.MODE_EDIT_ADD) {
					/* 新增图斑 */
            if (layerType.equals(Geometry.Type.POINT)) {
                drawType = POINT;
                if (currentPoint != null && currentPoint.isValid()) {
                    showAddCurrentPoint(drawType, currentPoint);
                } else {
                    ToastUtil.setToast(mContext, "未取得当前位置坐标,手动添加点");
                    activate(drawType);
                }

            } else if (layerType.equals(Geometry.Type.POLYLINE)) {
                if (gps_start_flag) {
                    gpstart.setVisibility(View.GONE);
                    gpspend.setVisibility(View.VISIBLE);
                    drawType = FREEHAND_POLYLINEGPS;
                    ToastUtil.setToast(mContext, "开始记录轨迹");
                } else {
                    drawType = FREEHAND_POLYLINE;
                }
                activate(drawType);
            } else if (layerType.equals(Geometry.Type.POLYGON)) {
                // drawType = FREEHAND_POLYGON;
                if (gps_start_flag) {
                    gpstart.setVisibility(View.GONE);
                    gpspend.setVisibility(View.VISIBLE);
                    drawType = FREEHAND_POLYLINEGPS;
                    ToastUtil.setToast(mContext, "开始记录轨迹");
                } else {
                    drawType = FREEHAND_POLYLINE;
                }
                activate(drawType);
            }

        }else if(mode == ActionMode.MODE_ADD_LABLE){
            //显示标注view
            //lablePresenter.queryFeatures(myLayer);
            layerLableInclude.setVisibility(View.VISIBLE);
            lablePresenter.showLayerAials(layerLableInclude,myLayer);
        }else if(mode == ActionMode.MODE_SELECT){
            seflayerName = layerNameList.get(position).getTable().getTableName();
        }
    }




}