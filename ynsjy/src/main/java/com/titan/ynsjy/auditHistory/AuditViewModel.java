package com.titan.ynsjy.auditHistory;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

/**
 * Created by hanyw on 2017/9/15/015.
 * 审计历史model
 */

public class AuditViewModel extends BaseObservable {
    public ObservableBoolean isMulti = new ObservableBoolean(false);//多选
    public ObservableBoolean isAll = new ObservableBoolean(false);//全选
    public ObservableField<String> alias = new ObservableField<>();//字段别名
    public ObservableField<String> editValue = new ObservableField<>();//修改值
    public ObservableField<String> name = new ObservableField<>();//字段名

    private AuditHistory mAuditHistory;
    private AuditCompare mAuditCompare;
    private AuditInfo mAuditInfo;

    public AuditViewModel(){}

    public AuditViewModel(AuditHistory audit){
        this.mAuditHistory = audit;
    }

    public AuditViewModel(AuditCompare compare){
        this.mAuditCompare = compare;
    }

    public AuditViewModel(AuditInfo info){
        this.mAuditInfo = info;
    }
    /**
     * 多选模式
     */
    public void multiSelect(){
        Log.e("tag","isMulti:"+isMulti.get());
        if (isMulti.get()){
            mAuditHistory.multiSelect(1);
        }else {
            mAuditHistory.multiSelect(0);
        }
    }

    /**
     * 全选
     */
    public void allSelect(){
        Log.e("tag","allselsct:"+isAll.get());
        if (isAll.get()){
            mAuditHistory.allSelect();
        }
    }

    /**
     * 对比页面返回
     */
    public void close(){
        mAuditCompare.close();
    }

    /**
     * 修改字段值--返回
     */
    public void closeDialog(){
        mAuditInfo.closeDialog();
    }

    /**
     * 修改字段值--确定
     */
    public void sure(){
        mAuditInfo.sure(name.get(),editValue.get());
    }

    /**
     * 修改字段值
     */
    public void editValue(String key,String tv_name,String value){
        alias.set(key);
        editValue.set(value);
        name.set(tv_name);
        if (mAuditInfo!=null){
            mAuditInfo.showEditDialog(key,value);
        }
//        if (mAuditCompare!=null){
//            mAuditCompare.showEditDialog(key, value);
//        }
    }
}
