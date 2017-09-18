package com.titan.ynsjy.auditHistory;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.util.Log;

/**
 * Created by hanyw on 2017/9/15/015.
 * 审计历史model
 */

public class AuditViewModel extends BaseObservable {
    public ObservableBoolean isMulti = new ObservableBoolean(false);//多选
    public ObservableBoolean isAll = new ObservableBoolean(false);//全选

    private AuditHistory mAuditHistory;
    private AuditCompare mAuditCompare;

    public AuditViewModel(){}

    public AuditViewModel(AuditHistory audit){
        this.mAuditHistory = audit;
    }

    public AuditViewModel(AuditCompare compare){
        this.mAuditCompare = compare;
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
        }else {
            mAuditHistory.multiSelect(0);
        }
    }

    /**
     * 对比页面返回
     */
    public void close(){
        mAuditCompare.close();
    }
}
