package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.Feature;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.model.AuditInfo;
import com.titan.util.ActivityUtils;
import com.titan.util.TitanFileFilter;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.audithistory.AuditCatalogFragment;
import com.titan.ynsjy.audithistory.AuditCompareActivity;
import com.titan.ynsjy.audithistory.AuditHistoryInfoFragment;
import com.titan.ynsjy.audithistory.AuditHistoryViewModel;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.ExcelUtil;
import com.titan.ynsjy.util.FileUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.UtilTime;
import com.titan.ynsjy.util.ViewModelHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hanyw on 2017/9/7/007.
 * 审计历史界面
 */

public class AuditHistoryActivity extends AppCompatActivity  implements AuditCatalogFragment.onRefreshDetial {
    @BindView(R.id.audit_add_close)
    TextView auditAddClose;//返回
    @BindView(R.id.audit_add_edit)
    TextView auditAddEdit;//编辑
    @BindView(R.id.audit_add_save)
    TextView auditAddSave;//保存
    @BindView(R.id.audit_add_compare)
    TextView auditAddCompare;//比较
    @BindView(R.id.audit_add_cancel)
    TextView auditAddCancel;//取消


    private Context mContext;
    private View view;
    //private FrameLayout compareFragment;//审计历史比较页面
    public static final String HISTORY_VIEWMODEL_TAG = "HISTORY_VIEWMODEL_TAG";

    public static MyLayer myLayer;
    private List<Feature> featureList;//审计记录列表
    private List<List<Feature>> map;//编辑id对应图斑集合
    private List<String> fk_uidList;//编辑id列表

    private static final int QUERY_FINISH = 1;//查询完成
    private static final int QUERY_NODATA = 2;//没有查询到数据
    private static final int EXPORT_FIELD = 3;//导出失败
    private static final int EXPORT_SUCCESS = 4;


    private Map<String, Boolean> cbMap;//checkbox状态
    AuditCatalogFragment mAuditCatalogFragment;//所有审计历史记录显示页面

    private AuditHistoryViewModel auditViewModel;
    public AuditHistoryInfoFragment getInfoFragment() {
        return infoFragment;
    }

    AuditHistoryInfoFragment infoFragment;//单个审计记录详细信息显示页面
    //用来存储导出的数据
    private List<AuditInfo> auditInfoList=new ArrayList<>();
    //用来暂存小班路径
    private List<String> imgpaths=new ArrayList<>();
    //功能选择 1:默认模式 2:单选模式 3:多选模式
    private int Type=1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EXPORT_FIELD:
                    //导出数据失败
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    ToastUtil.setToast(mContext,"导出数据失败"+msg.obj);
                    break;
                case EXPORT_SUCCESS:
                    //导出数据成功
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    ToastUtil.setToast(mContext,"导出数据成功");
                    break;
                default:
                    break;
            }
        }
    };
    //导出excel表头
    private static String[] title = { "编号","审计人员","审计时间","审计地址","描述信息","修改前情况","修改后情况","备注"};
    //审计历史数据
    public static GeodatabaseFeatureTable audithistorytable;

    //审计原始数据
    public static GeodatabaseFeatureTable auditreourcetable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audithistory);
        this.mContext = this;
        //getData();
        initData();
        //审计索引
        mAuditCatalogFragment=findOrCreateAuditCatalogFragment();
        auditViewModel = findOrCreateAuditViewModel();
        //审计详细
        mAuditCatalogFragment.setViewModel(auditViewModel);
        infoFragment = findOrCreateInfoFragmentFragment();
        ButterKnife.bind(this);

    }

    @NonNull
    public AuditCatalogFragment findOrCreateAuditCatalogFragment() {
        AuditCatalogFragment tasksFragment =
                (AuditCatalogFragment) getSupportFragmentManager().findFragmentById(R.id.audit_index_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = AuditCatalogFragment.newInstance(audithistorytable);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.audit_index_frame);
        }
        return tasksFragment;
    }


    @NonNull
    public AuditHistoryInfoFragment findOrCreateInfoFragmentFragment() {
        AuditHistoryInfoFragment tasksFragment =
                (AuditHistoryInfoFragment) getSupportFragmentManager().findFragmentById(R.id.audit_detail_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = AuditHistoryInfoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.audit_detail_frame);
        }
        return tasksFragment;
    }



    public AuditHistoryViewModel findOrCreateAuditViewModel(){
        @SuppressWarnings("unchecked")
        ViewModelHolder<AuditHistoryViewModel> viewModel =
                (ViewModelHolder<AuditHistoryViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(HISTORY_VIEWMODEL_TAG);
        if (viewModel != null && viewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return viewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            AuditHistoryViewModel auditViewModel = new AuditHistoryViewModel(mAuditCatalogFragment);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(auditViewModel),
                    HISTORY_VIEWMODEL_TAG);
            return auditViewModel;
        }
    }



    /**
     * 获取编辑表数据
     */
    private void getData() {
        initData();
       /* Intent intent = getIntent();
        int type = intent.getIntExtra("functionType",1);
        if (type==0){
            myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
        }else {
            initData();
            *//*List<String> list = ResourcesManager.getInstance(mContext).getOtmsFolderName();
            if (list.contains("审计眼")){
                String path = ResourcesManager.getInstance(mContext).getFolderPath("/otms")+"/审计眼/test.geodatabase";
                try {
                    Geodatabase geodatabase = new Geodatabase(path);
                    List<GeodatabaseFeatureTable> tableList = geodatabase.getGeodatabaseTables();
                    //geodatabase.getGeodatabaseTables().get()
                    for (GeodatabaseFeatureTable gdbFeatureTable : tableList) {
                        *//**//*if (!gdbFeatureTable.hasGeometry()) {
                            ToastUtil.setToast(mContext,"没有数据");
                            continue;
                        }*//**//*
                        if (gdbFeatureTable.getTableName().equals("edit")){
                            FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                            setMyLayer("审计眼","test",path,layer,gdbFeatureTable);
                        }
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    ToastUtil.setToast(mContext,"获取数据异常"+e);
                }

            }else {
                ToastUtil.setToast(mContext,"没有发现审计数据");
            }*//*
        }*/
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String path = ResourcesManager.getInstance(mContext).getFolderPath("/otms/审计眼");
        File file=new File(path);
        if(file.exists()){
            File[] files = file.listFiles(new TitanFileFilter.GeodatabaseFileFilter());
            if(files.length>0){
                try {
                    Geodatabase geodatabase=new Geodatabase(files[0].getAbsolutePath());
                    for (GeodatabaseFeatureTable gdbFeatureTable : geodatabase.getGeodatabaseTables()) {
                        if (gdbFeatureTable.getTableName().equals(mContext.getResources().getString(R.string.edit))){
                            audithistorytable=gdbFeatureTable;
                            /*FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                            setMyLayer("审计眼","test",path,layer,gdbFeatureTable);*/
                        }
                        if (gdbFeatureTable.getTableName().equals(mContext.getResources().getString(R.string.resource))){
                            auditreourcetable=gdbFeatureTable;
                        }
                    }

                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    ToastUtil.setToast(mContext,"数据不存在");

                }
            }
        }else {
            ToastUtil.setToast(mContext,"数据不存在");
        }


    }


    //设置MyLayer的相关信息
    private void setMyLayer(String gname,String cname,String path,FeatureLayer layer,GeodatabaseFeatureTable featureTable){
        myLayer = new MyLayer();
        myLayer.setPname(gname);
        myLayer.setCname(cname);
        myLayer.setPath(path);
        myLayer.setLname(layer.getName());
        myLayer.setSelectColor(layer.getSelectionColor());
        myLayer.setRenderer(layer.getRenderer());
        myLayer.setLayer(layer);
        myLayer.setTable(featureTable);
    }

    @OnClick({R.id.audit_add_close, R.id.audit_add_edit, R.id.audit_add_save, R.id.audit_add_compare, R.id.audit_add_cancel,R.id.audit_export})
    public void onViewClicked(View view) {
        FrameLayout layout = (FrameLayout) findViewById(R.id.audit_compare_frame);
        List<Map<String, Object>> list = mAuditCatalogFragment.getSelectList();
        switch (view.getId()) {
            case R.id.audit_add_close:
                //返回
                this.finish();
                break;
            case R.id.audit_add_edit:
                //编辑
                infoFragment.editMode(true);
                //compareFragment.setVisibility(View.GONE);
                auditAddSave.setVisibility(View.VISIBLE);
                auditAddEdit.setVisibility(View.GONE);
                auditAddCompare.setVisibility(View.GONE);
                auditAddCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.audit_add_save:
                //保存
                infoFragment.save(myLayer.getTable());
                infoFragment.editMode(false);
                mAuditCatalogFragment.modeChoice(0);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.VISIBLE);
                auditAddCompare.setVisibility(View.VISIBLE);
                auditAddCancel.setVisibility(View.GONE);
                //queryData();
                break;
            case R.id.audit_add_compare:
                //比较
                //mAuditCatalogFragment.modeChoice(1);
                if (list.size()!=2){
                    ToastUtil.setToast(mContext,"请选择两条记录做比较");
                    return;
                }
                Intent intent = new Intent(AuditHistoryActivity.this, AuditCompareActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataList", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.audit_add_cancel:
                //取消
                infoFragment.editMode(false);
                mAuditCatalogFragment.modeChoice(0);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.VISIBLE);
                auditAddCompare.setVisibility(View.VISIBLE);
                auditAddCancel.setVisibility(View.GONE);
                break;
            case R.id.audit_export:
                //mAuditCatalogFragment.modeChoice(2);
                if (list==null||list.size()<=0){
                    ToastUtil.setToast(mContext,"请选择至少一条数据");
                    return;
                }else {
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            export();
                        }
                    }).start();
                }
                break;
        }
    }
    /**
     * 导出数据
     */
    private void export(){
        initExportData(mAuditCatalogFragment.getSelectList());
        //导出数据
        String path= null;
        Message msg=new Message();
        try {
            path = MyApplication.resourcesManager.getExportPath(UtilTime.getSystemtime2());
            File file = new File(path);
            makeDir(file);
            ExcelUtil.initExcel(path + "/导出数据.xls", title,"数据统计");
            String fileName = path + "/导出数据.xls";
            boolean exportexcel=ExcelUtil.writeObjListToExcel(getRecordData(auditInfoList), fileName, mContext);
            if(!exportexcel){
                msg.what=EXPORT_FIELD;
                msg.obj="导出Excel出错";
                handler.sendMessage(msg);
            }
            copyImg2Export(path);
        } catch (Exception e) {
            //e.printStackTrace();
            msg.what=EXPORT_FIELD;
            msg.obj="导出数据异常"+e;
            handler.sendMessage(msg);

            //ToastUtil.setToast(mContext,"导出数据异常"+e);
        }


    }
    public  void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }



    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     * @return
     */
    private  ArrayList<ArrayList<String>> getRecordData(List<AuditInfo> auditInfos) {
       // private static String[] title = { "编号","审计人员","审计时间","审计地址","描述信息","修改前情况","修改后情况","备注"};
        ArrayList<ArrayList<String>> recordList = new ArrayList<>();
        for (int i = 0; i <auditInfos.size(); i++) {
            AuditInfo auditInfo = auditInfos.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(auditInfo.getObjectid());
            beanList.add(auditInfo.getAuditer());
            beanList.add(auditInfo.getTime());
            beanList.add(auditInfo.getAddress());
            beanList.add(auditInfo.getInfo());
            beanList.add(auditInfo.getBeforinfo());
            beanList.add(auditInfo.getAfterinfo());
            beanList.add(auditInfo.getRemark());
            recordList.add(beanList);
        }
        return recordList;
    }



    /**
     * 拷贝多媒体信息到导出目录
     */
    private void copyImg2Export(String exportpath) {
        Message msg=new Message();
        for(String path:imgpaths){
            Log.e("path",path);
            File file=new File(path);
            String filename=file.getName();
            try {
                FileUtil.copyFile(path,exportpath+"/"+filename);
            } catch (IOException e) {
                e.printStackTrace();
                msg.what=EXPORT_FIELD;
                msg.obj=e;
                handler.sendMessage(msg);
            }
        }
        msg.what=EXPORT_SUCCESS;
        handler.sendMessage(msg);

    }


    /**
     * 初始化导出数据
     * @param selectList
     */
    private void initExportData(List<Map<String, Object>> selectList) {
        String data=myLayer.getPath()+"/images";
        auditInfoList.clear();
        imgpaths.clear();
        for (Map<String,Object> item:selectList){
            AuditInfo auditInfo=new AuditInfo();
            auditInfo.setObjectid(String.valueOf(item.get("OBJECTID")));
            auditInfo.setTime(item.get("MODIFYTIME").toString());
            auditInfoList.add(auditInfo);
            List<String> pathlist= ResourcesManager.getImagesFiles(data,auditInfo.getObjectid());
            if(pathlist!=null){
                imgpaths.addAll(pathlist);
            }
        }
    }

    @Override
    public void onRefreshDetial(Map<String, Object> map,boolean flag) {
        //auditAddEdit.setVisibility(View.VISIBLE);
        infoFragment.editMode(false);
        infoFragment.setMyVisibility(flag);
        infoFragment.refresh(map,audithistorytable);
    }

    @Override
    public void onShowCompare(List<Map<String, Object>> selectList, Map<String, Object> map) {
        //infoFragment.refresh(selectList.get(0));
        //setLayout(map);
        //mAuditCatalogFragment.setLayout(selectList.get(1));
        //compareFragment.setLayout().s();
    }
}
