package com.titan.ynsjy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.fragment.AuditCatalogFragment;
import com.titan.ynsjy.fragment.AuditHistoryInfoFragment;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ExcelUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hanyw on 2017/9/7/007.
 * 审计历史界面
 */

public class AuditHistoryActivity extends AppCompatActivity {
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
    private MyLayer myLayer;
    private FrameLayout compareFragment;//审计历史比较页面
    AuditCatalogFragment catalogFragment;//所有审计历史记录显示页面
    AuditHistoryInfoFragment infoFragment;//单个审计记录详细信息显示页面
    //导出excel表头
    private static String[] title = { "编号","姓名","性别","年龄","班级","数学","英语","语文" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        view = LayoutInflater.from(this).inflate(R.layout.activity_audit_history, null);
        setContentView(view);
        ButterKnife.bind(this);
        init();
        getData();
    }

    /**
     * 初始化页面和数据
     */
    private void init() {
        //审计历史记录列表页面
        catalogFragment = (AuditCatalogFragment) getSupportFragmentManager().findFragmentById(R.id.audit_catalog);
        //审计历史记录详细信息显示页面
        infoFragment = (AuditHistoryInfoFragment) getSupportFragmentManager().findFragmentById(R.id.audit_history_all_info);
        //两个审计历史记录对比页面
        compareFragment = (FrameLayout) view.findViewById(R.id.audit_compare_fragment);
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
                compareFragment.setVisibility(View.GONE);
                auditAddSave.setVisibility(View.VISIBLE);
                auditAddEdit.setVisibility(View.GONE);
                auditAddCompare.setVisibility(View.GONE);
                auditAddCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.audit_add_save:
                //保存
                infoFragment.save(myLayer.getTable());
                catalogFragment.queryData();
                break;
            case R.id.audit_add_compare:
                //比较
                catalogFragment.modeChoice(1);
                compareFragment.setVisibility(View.VISIBLE);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.GONE);
                auditAddCompare.setVisibility(View.GONE);
                auditAddCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.audit_add_cancel:
                //取消
                infoFragment.editMode(false);
                catalogFragment.modeChoice(0);
                compareFragment.setVisibility(View.GONE);
                auditAddSave.setVisibility(View.GONE);
                auditAddEdit.setVisibility(View.VISIBLE);
                auditAddCompare.setVisibility(View.VISIBLE);
                auditAddCancel.setVisibility(View.GONE);
                break;

            case R.id.audit_export:
                //导出excel文件
                //file = new File(getSDPath() + "/Record");
                //makeDir(file);
                String path="";
                ExcelUtil.initExcel(path + "/导出数据.xls", title);
                //fileName = getSDPath() + "/Record/成绩表.xls";
                //ExcelUtil.writeObjListToExcel(getRecordData(), fileName, this);
                break;
        }
    }
}
