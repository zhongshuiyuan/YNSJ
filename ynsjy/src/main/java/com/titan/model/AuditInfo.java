package com.titan.model;

/**
 * Created by whs on 2017/9/12
 * 审计信息
 */

public class AuditInfo {
    //ID 主键
    private String objectid;
    //审计人员
    private String auditer;
    //审计时间
    private String time;
    //审计地址
    private  String address;
    //修改原因
    private  String reason;
    //描述信息
    private String info;
    //修改前情况
    private String beforinfo;
    //修改后情况
    private String afterinfo;
    //备注
    private String remark;


    public String getAuditer() {
        return auditer;
    }

    public void setAuditer(String auditer) {
        this.auditer = auditer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBeforinfo() {
        return beforinfo;
    }

    public void setBeforinfo(String beforinfo) {
        this.beforinfo = beforinfo;
    }

    public String getAfterinfo() {
        return afterinfo;
    }

    public void setAfterinfo(String afterinfo) {
        this.afterinfo = afterinfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }
}
