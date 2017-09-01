package com.titan.model;

import java.sql.Date;

/**
 * Created by hanyw on 2017/9/1/001.
 * 审计类
 */

public class Audit {
    //数据对应ID
    private long fk_Edit_Uid;
    //修改原因
    private String reason;
    //修改之前
    private String modifyBefore;
    //修改之后
    private String modifyAfter;
    //备注
    private String mark;
    //修改时间
    private java.sql.Date date;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModifyBefore() {
        return modifyBefore;
    }

    public void setModifyBefore(String modifyBefore) {
        this.modifyBefore = modifyBefore;
    }

    public String getModifyAfter() {
        return modifyAfter;
    }

    public void setModifyAfter(String modifyAfter) {
        this.modifyAfter = modifyAfter;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public long getFk_Edit_Uid() {
        return fk_Edit_Uid;
    }

    public void setFk_Edit_Uid(long fk_Edit_Uid) {
        this.fk_Edit_Uid = fk_Edit_Uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
