package com.titan.ynsjy.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.TableException;
import com.gis_luq.lib.Draw.DrawEvent;
import com.gis_luq.lib.Draw.DrawEventListener;
import com.gis_luq.lib.Draw.DrawTool;
import com.gis_luq.lib.Draw.Util.CutPolygonL;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.gis.GeometryUtil;
import com.titan.gis.GisUtil;
import com.titan.gis.SymbolUtil;
import com.titan.model.TitanField;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.databinding.ActivityAuditBinding;
import com.titan.ynsjy.dialog.EditPhoto;
import com.titan.ynsjy.entity.ActionMode;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by hanyw on 2017/9/2/002.
 * 新增审计
 */
@RuntimePermissions
public class AuditActivity extends AppCompatActivity implements View.OnClickListener,DrawEventListener {
    //拍照
    private static final int TAKE_PICTURE = 1;
    /**
     * 新增审计
     */
    @BindView(R.id.audit_pic_browse)//图片浏览
            TextView auditPicBrowse;
    @BindView(R.id.audit_take_pic)//拍照
            TextView auditTakePic;
    @BindView(R.id.audit_sure)//确定
            TextView auditSure;
    @BindView(R.id.audit_cancel)//取消
            TextView auditCancel;


    private Context mContext;
    //private View compareView;
    private Feature feature;//小班
    //审计图层（固定）
    private GeodatabaseFeatureTable featureTable;
    //private MyLayer myLayer;
    //审计图层
    private FeatureLayer auditLayer;

    private long fid;//原始数据小班id
    private String picPath;//图片文件夹地址
    private long newId;//新增小班id
    private String imagePath = "";//图片地址
    //审计类型 0:审计原始数据 1:新增审计 2:编辑审计
    private int auditType =0;
    private ActivityAuditBinding binding;
    //审计的原始数据
    private GeodatabaseFeature editfeature;

    private GraphicsLayer editlayer;
    private Graphic editgraphic;
    //地图监听事件
    //private MapTouchListener mapTouchListener;
    //地图操作模式
    public ActionMode actionMode = ActionMode.MODE_SELECT;
    //绘制工具
    private  DrawTool drawTool;
    //属性信息
    public static List<TitanField> fieldList;

    //编辑方式选择
    private Dialog mEditChoiseDialog;
    //是否是审计图层
    private boolean isAuditLayer=false;
    //基础图
    private ArcGISLocalTiledLayer titlelayer;
    //影像图
    private ArcGISLocalTiledLayer imagelayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding= DataBindingUtil.setContentView(this,R.layout.activity_audit);

        ButterKnife.bind(this);
        getData();
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        binding.mapviewAudit.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object source, STATUS status) {

                if(STATUS.LAYER_LOADING_FAILED==status&&source instanceof ArcGISLocalTiledLayer){
                    //ProgressDialogUtil.stopProgressDialog(mContext);
                    ToastUtil.setToast(mContext, "添加底图失败:"+String.valueOf(source));
                }

            }
        });
        /* 基础底图 */
        String titlePath = MyApplication.resourcesManager.getTitlePath();
        titlelayer=new ArcGISLocalTiledLayer(titlePath);
         /* 影像底图 */
        String imagePath = MyApplication.resourcesManager.getImagePath();
        imagelayer=new ArcGISLocalTiledLayer(imagePath);
        binding.mapviewAudit.addLayer(titlelayer);
        binding.mapviewAudit.addLayer(imagelayer);
        editlayer=new GraphicsLayer();
        editgraphic=new Graphic(editfeature.getGeometry(), SymbolUtil.fillSymbol);
        editlayer.addGraphic(editgraphic);
        binding.mapviewAudit.addLayer(editlayer);

        binding.mapviewAudit.setExtent(editgraphic.getGeometry());
        binding.mapviewAudit.setOnTouchListener(new MapTouchListener(mContext,binding.mapviewAudit));
        binding.tvAuditedit.setOnClickListener(this);
        //绘制工具初始化
        drawTool = new DrawTool(binding.mapviewAudit);
        drawTool.addEventListener(this);
        drawTool.setFillSymbol(SymbolUtil.fillSymbol);
        //属性适配
       /* if(!isAuditLayer)
        fieldList=GisUtil.getFields(editgraphic.getAttributes(),featureTable.getFields());*/

        binding.setListfield(fieldList);
        AdapterListObj attrAdapter= new AdapterListObj<>(fieldList, mContext);
        binding.rclAttr.setAdapter(attrAdapter);
        binding.rclAttr.setLayoutManager(new LinearLayoutManager(mContext));
    }



    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AuditActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel,R.id.fragment_videotape})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_pic_browse:
                //多媒体信息浏览
                lookpictures(this);
                break;
            case R.id.audit_take_pic:
                //拍照
                AuditActivityPermissionsDispatcher.photographWithCheck(this);
                break;
            case R.id.audit_sure:
                //确定
                saveData();
                break;
            case R.id.audit_cancel:
                //取消
                this.finish();
                break;
            case R.id.fragment_videotape:
                //录像
                videotape();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //获取操作的数据库
            Geodatabase currentGaodatabase=null;
            for (Geodatabase gdb:BaseActivity.layerControlPresenter.getGeodatabaseList()) {
                for(GeodatabaseFeatureTable tb:gdb.getGeodatabaseTables()){
                    if(tb.getTableName().equals(BaseActivity.currentlayer.getName())){
                        currentGaodatabase=gdb;
                        break;
                    }

                }
            }

            EditPhoto dialog = new EditPhoto(mContext, imagePath, fid,currentGaodatabase, BaseActivity.currentPoint,BaseActivity.spatialReference);
            dialog.show();
        }
    }

    /**
     * 录像
     */
    private void videotape() {
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(ResourcesManager.getImagePath(picPath) + "/" + ResourcesManager.getVideoName(String.valueOf(fid)));
        Log.e("tag", "path" + ResourcesManager.getImagePath(picPath) + "/" + ResourcesManager.getVideoName(String.valueOf(fid)));
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivity(intent);
    }

    /**
     * 多媒体浏览
     */
    public void lookpictures(Activity activity) {
        List<String> lst = ResourcesManager.getImagesFiles(picPath, String.valueOf(fid)); //getImages(picPath);
        if (lst == null || lst.size() == 0) {
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, PicSampActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("picPath", picPath);
        startActivity(intent);
    }

    /**
     * 拍照
     */
    @NeedsPermission({Manifest.permission.CAMERA})
    void photograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = picPath + "/" + ResourcesManager.getPicName(String.valueOf(fid));
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PICTURE);
       /* Intent intent=new Intent(AuditActivity.this, CameraActivity.class);
        startActivity(intent);*/
    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void showRecordDenied() {
        ToastUtil.setToast(mContext, "拒绝后将无法打开相机，您可以在手机中手动授予权限");
    }

    /**
     * 获取修改表
     */
    private void getData() {
        //auditLayer
        List<GeodatabaseFeatureTable> tables= BaseActivity.layerControlPresenter.getGeodatabaseList().get(0).getGeodatabaseTables();
        for (GeodatabaseFeatureTable table:tables){
            if (table.getTableName().equals("edit")||table.getTableName().contains(mContext.getResources().getString(R.string.auditlayer))){
                featureTable=table;
            }
        }
        Intent intent = getIntent();
        auditType = intent.getIntExtra("auditType", 0);
        picPath = MyApplication.resourcesManager.getSJImagePath();
        fid = intent.getLongExtra("fid", 0);
        isAuditLayer=intent.getBooleanExtra("isauditlayer",false);
        switch (auditType){
            case 0:
                //原始图班审计、编辑属性
                //picPath = MyApplication.resourcesManager.getSJImagePath();
                editfeature=BaseActivity.selGeoFeature;
                if(isAuditLayer){
                    fieldList=GisUtil.getFields(editfeature.getAttributes(),featureTable.getFields());
                }else {
                    fieldList=GisUtil.getFields(null,featureTable.getFields());
                }
                break;
            case 1:
                //新增
                try {
                    editfeature=new GeodatabaseFeature(null,YzlActivity.mAddGraphic.getGeometry(),featureTable);
                    fieldList=GisUtil.getFields(null,featureTable.getFields());

                } catch (TableException e) {
                    //e.printStackTrace();
                    ToastUtil.showShort(mContext,"获取数据失败");
                }
                break;
        }


    }

    /**
     * 保存数据
     */
    private void saveData() {
        //
        Log.e("field",fieldList.toString());
        for (TitanField field:fieldList){
            Log.e("field","name:"+field.getName()+"value:"+field.getValue());
        }
        if(editlayer.getNumberOfGraphics()>1){
            //多个图形
            int[] ids=editlayer.getGraphicIDs();
            for (int id : ids) {
                saveFeature(editlayer.getGraphic(id).getGeometry());
            }

        }else {
            if(isAuditLayer){
                updateFeature();
            }else {
                createFeature(editgraphic.getGeometry());

            }
            //upEditLayerData();
        }

    }

    /**
     * 更新审计数据
     */
    private void updateFeature() {
        try {
            featureTable.updateFeature(editfeature.getId(),setData());
            ToastUtil.setToast(mContext, "数据保存成功");
            this.finish();
        } catch (TableException e) {
            ToastUtil.setToast(mContext,"更新审计数据"+e);
        }
    }

    /**
     * 新建数据
     */
    private void saveFeature(Geometry geometry) {
        //Graphic g = new Graphic(geometry, editgraphic.getAttributes());
        try {

            newId = featureTable.createNewFeature(setData(),geometry).getId();
            feature = featureTable.getFeature(newId);
        } catch (TableException e) {
            //e.printStackTrace();
            ToastUtil.setToast(mContext,"新增审计图形数据失败"+e);
        }
    }

    /**
     * 新建数据
     */
    private void createFeature(Geometry geometry) {
        Graphic graphic = new Graphic(geometry, null,setData());
        try {
            newId = featureTable.addFeature(graphic);
            Log.e("newId",newId+"");
            feature = featureTable.getFeature(newId);
            Log.e("map",feature.getAttributes().toString());
            ToastUtil.setToast(mContext, "数据保存成功");
            this.finish();
        } catch (TableException e) {
            //e.printStackTrace();
            ToastUtil.setToast(mContext,"新增审计图形数据失败"+e);
        }
    }

    /**
     * @return 修改的数据集
     */
    private Map<String, Object> setData() {

        Map<String, Object> map = new HashMap<>();
        map.put("FK_EDIT_UID", fid);
        for (TitanField field:fieldList){
            map.put(field.getName(),field.getValue());
        }
        return map;
    }

    /**
     * 更新数据
     */
    /*private void upEditLayerData() {
        Graphic graphic = new Graphic(null, null, setData());
        try {
            featureTable.updateFeature(newId, graphic);
            ToastUtil.setToast(mContext, "数据保存成功");
            this.finish();
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(mContext, "数据保存失败");
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_auditedit:
                //空间编辑
                if(mEditChoiseDialog==null){
                    mEditChoiseDialog= new MaterialDialog.Builder(mContext)
                            .title(mContext.getString(R.string.editype))
                            .items((CharSequence[]) mContext.getResources().getStringArray(R.array.edittype))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    switch (position){
                                       /* case 0:
                                            //新增
                                            actionMode=ActionMode.MODE_EDIT_ADD;
                                            drawTool.activate(DrawTool.FREEHAND_POLYGON);
                                            //ProgressDialogUtil.startProgressDialog(mContext);
                                            break;*/
                                        case 1:
                                            //修班
                                            actionMode=ActionMode.MODE_XIUBAN;
                                            drawTool.activate(DrawTool.FREEHAND_POLYLINE);
                                            //ProgressDialogUtil.startProgressDialog(mContext);

                                            break;
                                        case 2:
                                            //分割
                                            actionMode=ActionMode.MODE_QIEGE;
                                            drawTool.activate(DrawTool.FREEHAND_POLYLINE);

                                            break;
                                    }
                                }
                            })
                            .cancelable(true)
                            .build();

                }
                mEditChoiseDialog.show();
                break;
        }

    }

    @Override
    public void handleDrawEvent(DrawEvent event) throws TableException, FileNotFoundException {
        // 将画好的图形（已经实例化了Graphic），添加到drawLayer中并刷新显示
        editlayer.addGraphic(event.getDrawGraphic());
        ProgressDialogUtil.startProgressDialog(mContext);
        switch (actionMode){
            case MODE_XIUBAN:
                //修班
                Polygon polygon= (Polygon) editgraphic.getGeometry();
                Polyline polyline = (Polyline) event.getDrawGraphic().getGeometry();
                Polygon subtractor=GeometryUtil.polyline2Polygon(polyline,editlayer.getSpatialReference());
                if(subtractor!=null){
                    Geometry result=GeometryEngine.difference(editgraphic.getGeometry(),subtractor,editlayer.getSpatialReference());
                    editlayer.updateGraphic((int) editgraphic.getId(),result);
                    editlayer.recycle();
                }else {
                    ToastUtil.setToast(mContext,"绘制线不符合修班规则");
                }
                /*List<Polygon> results=CutPolygonL.Cut(polygon,polyline);
                if(results.size()>0){
                    editlayer.updateGraphic((int) editgraphic.getId(),results.get(0));
                    for (int i = 1; i <results.size(); i++) {
                        editlayer.addGraphic(new Graphic(results.get(i),editgraphic.getSymbol()));
                    }
                }else {
                    ToastUtil.setToast(mContext,"分割失败");
                }*/
                break;
            case MODE_EDIT_ADD:
                event.getDrawGraphic().getGeometry().copyTo(editgraphic.getGeometry());
                //editgraphic.getGeometry().copyTo(event.getDrawGraphic().getGeometry());
                editlayer.updateGraphic((int) editgraphic.getId(),editgraphic.getSymbol());
                //新增小班
                break;
            case MODE_QIEGE:
                //分割
                Polygon polygon1= (Polygon) editgraphic.getGeometry();
                Polyline polyline1 = (Polyline) event.getDrawGraphic().getGeometry();
                List<Polygon> results=CutPolygonL.Cut(polygon1,polyline1);
                for (int i = 0; i <results.size() ; i++) {
                    if(i==0){
                        editlayer.updateGraphic((int) editgraphic.getId(),results.get(i));
                    }else {
                        editlayer.addGraphic(new Graphic(results.get(i),editgraphic.getSymbol()));
                    }

                }
                /*EditTools.previousSegmentation(editfeature.getGeometry(), polygon1, editlayer.getSpatialReference(), new EditTools.segmentationCallback() {
                    @Override
                    public void onSuccess(List<Geometry> geometryList) {
                        //editlayer.removeAll();
                        //editlayer.updateGraphic(ge);
                        for (int i = 0; i <geometryList.size() ; i++) {
                            if(i==0){
                                editlayer.updateGraphic((int) editgraphic.getId(),geometryList.get(i));
                            }else {
                                editlayer.addGraphic(new Graphic(geometryList.get(i),editgraphic.getSymbol()));
                            }

                        }

                    }

                    @Override
                    public void onFailure(String info) {
                        ToastUtil.showShort(mContext,info);

                    }
                });*/
                break;


        }
        //drawTool.sendDrawEndEvent();
        //清空绘制图形
        drawTool.deactivate();
        ProgressDialogUtil.stopProgressDialog(mContext);
        //binding.mapviewAudit.setOnTouchListener(new MapTouchListener(mContext,binding.mapviewAudit));
    }

    /**
     * 地图监听事件
     */
    private class MapTouchListener extends MapOnTouchListener {
        MapView mapView;
        public MapTouchListener(Context context, MapView view) {
            super(context, view);
            this.mapView=view;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Point point = mapView.toMapPoint(event.getX(), event.getY());
            if (point == null || point.isEmpty() || !point.isValid()) {
                return false;
            }
            return super.onTouch(v, event);
        }


    }
}
