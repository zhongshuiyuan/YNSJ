package com.titan.ynsjy.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.model.AuditInfo;
import com.titan.util.ActivityUtils;
import com.titan.ynsjy.AuditHistory.AuditCatalogFragment;
import com.titan.ynsjy.AuditHistory.AuditCompareFragment;
import com.titan.ynsjy.AuditHistory.AuditHistoryInfoFragment;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ExcelUtil;
import com.titan.ynsjy.util.FileUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hanyw on 2017/9/7/007.
 * 审计历史界面
 */

public class AuditHistoryActivity extends AppCompatActivity   {
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

    private MyLayer myLayer;
    private List<Feature> featureList;//审计记录列表
    private List<List<Feature>> map;//编辑id对应图斑集合
    private List<String> fk_uidList;//编辑id列表

    private static final int QUERY_FINISH = 1;//查询完成
    private static final int QUERY_NODATA = 2;//没有查询到数据
    private static final int EXPORT_FIELD = 3;//导出失败
    private static final int EXPORT_SUCCESS = 4;


    private Map<String, Boolean> cbMap;//checkbox状态
    AuditCatalogFragment mAuditCatalogFragment;//所有审计历史记录显示页面
    AuditHistoryInfoFragment infoFragment;//单个审计记录详细信息显示页面
    private  AuditCompareFragment compareFragment;
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
                case QUERY_FINISH:
                    mAuditCatalogFragment.exRefresh(mContext, fk_uidList, map, cbMap, Type);
                    break;
                case QUERY_NODATA:
                    ToastUtil.setToast(mContext, "没有查询到数据");
                    break;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audithistory);
        this.mContext = this;

        mAuditCatalogFragment=findOrCreateAuditCatalogFragment();
        //mAuditCatalogFragment = (AuditCatalogFragment) getSupportFragmentManager().findFragmentById(R.id.audit_catalog);
        infoFragment = findOrCreateInfoFragmentFragment();
        //compareFragment = (FrameLayout) view.findViewById(R.id.audit_detail_frame);

        //mAuditCatalogFragment = findOrCreateViewFragment();
        //mViewModel = findOrCreateViewModel();
        // Link View and ViewModel
        //mFragment.setViewModel(mViewModel);
        ButterKnife.bind(this);
        init();
        getData();
    }

    @NonNull
    public AuditCatalogFragment findOrCreateAuditCatalogFragment() {
        AuditCatalogFragment tasksFragment =
                (AuditCatalogFragment) getSupportFragmentManager().findFragmentById(R.id.audit_index_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = AuditCatalogFragment.newInstance();
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

    @NonNull
    public AuditCompareFragment findOrCreateConpareFragmentFragment() {
        AuditCompareFragment tasksFragment =
                (AuditCompareFragment) getSupportFragmentManager().findFragmentById(R.id.audit_detail_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = AuditCompareFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.audit_detail_frame);
        }
        return tasksFragment;
    }


    /**
     * 初始化页面和数据
     */
    private void init() {
        //审计历史记录列表页面
        //catalogFragment = findOrCreateAuditCatalogFragment();
        //审计历史记录详细信息显示页面
        //infoFragment = (AuditHistoryInfoFragment) getSupportFragmentManager().findFragmentById(R.id.audit_history_all_info);
        //两个审计历史记录对比页面
        //compareFragment = (FrameLayout) view.findViewById(R.id.audit_detail_frame);
    }

    /**
     * 获取编辑表
     */
    private void getData() {
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
    }

    @OnClick({R.id.audit_add_close, R.id.audit_add_edit, R.id.audit_add_save, R.id.audit_add_compare, R.id.audit_add_cancel,R.id.audit_export})
    public void onViewClicked(View view) {
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
                //queryData();
                break;
            case R.id.audit_add_compare:
                //比较
                mAuditCatalogFragment.modeChoice(1);
                compareFragment=findOrCreateConpareFragmentFragment();
                //compareFragment.setVisibility(View.VISIBLE);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.GONE);
                auditAddCompare.setVisibility(View.GONE);
                auditAddCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.audit_add_cancel:
                //取消
                infoFragment.editMode(false);
                mAuditCatalogFragment.exRefresh(mContext, fk_uidList, map, cbMap, 1);
                //compareFragment.setVisibility(View.GONE);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.VISIBLE);
                auditAddCompare.setVisibility(View.VISIBLE);
                auditAddCancel.setVisibility(View.GONE);
                break;

            case R.id.audit_export:
                ProgressDialogUtil.startProgressDialog(mContext);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        export();
                    }
                }).start();
                break;
        }
    }

    /**
     * 导出数据
     */
    private void export(){

        initExportData(mAuditCatalogFragment.getSelectList());
        //导出数据
        String path=MyApplication.resourcesManager.getExportPath(new Date().toString());
        ExcelUtil.initExcel(path + "/导出数据.xls", title,"数据统计");
        String fileName = path + "/导出数据.xls";
        ExcelUtil.writeObjListToExcel(auditInfoList, fileName, mContext);
        copyImg2Export(path);
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
            auditInfo.setObjectid((String) item.get("OBJECTID"));
            auditInfoList.add(auditInfo);
            List<String> pathlist= ResourcesManager.getImagesFiles(data,auditInfo.getObjectid());
            imgpaths.addAll(pathlist);
        }
    }
}
