package com.titan.model;

import java.util.ArrayList;

/**
 * Created by whs on 2017/9/12
 * 审计信息
 */

public class AuditInfo {
    //ID 主键
    private String objectid;
    //ID 外键 原始小班ID
    private String resourceid;
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

    public ArrayList<String> toListString(){
        //String[] title = { "编号","审计人员","审计时间","审计地址","描述信息","修改前情况","修改后情况","备注"};
        ArrayList<String> result=new ArrayList<>();
        result.add(this.getObjectid()==null?"":this.getObjectid());
        result.add(this.getAuditer()==null?"":this.getAuditer());
        result.add(this.getTime()==null?"":this.getTime());
        result.add(this.getAddress()==null?"":this.getAddress());
        result.add(this.getInfo()==null?"":this.getInfo());
        result.add(this.getBeforinfo()==null?"":this.getBeforinfo());
        result.add(this.getAfterinfo()==null?"":this.getAfterinfo());
        result.add(this.getRemark()==null?"":this.getRemark());
        return result;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
}
