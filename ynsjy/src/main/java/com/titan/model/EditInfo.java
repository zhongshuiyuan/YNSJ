package com.titan.model;


/**
 * 编辑信息
 */
public class EditInfo {
  //主键
  private long pk_Uid;
  //编辑数据ID
  private long fk_Edit_Uid;
  //修改原因
  private String modifyinfo;
  //修改时间
  private java.sql.Date modifytime;
  //修改前情况
  private String beforeinfo;
  //修改后情况
  private String afterinfo;
  //描述信息
  private String info;
  //备注
  private String remark;
  //空间数据(GeoJSON)
  private String geometry;


  public long getPk_Uid() {
    return pk_Uid;
  }

  public void setPk_Uid(long pk_Uid) {
    this.pk_Uid = pk_Uid;
  }


  public long getFk_Edit_Uid() {
    return fk_Edit_Uid;
  }

  public void setFk_Edit_Uid(long fk_Edit_Uid) {
    this.fk_Edit_Uid = fk_Edit_Uid;
  }


  public String getModifyinfo() {
    return modifyinfo;
  }

  public void setModifyinfo(String modifyinfo) {
    this.modifyinfo = modifyinfo;
  }


  public java.sql.Date getModifytime() {
    return modifytime;
  }

  public void setModifytime(java.sql.Date modifytime) {
    this.modifytime = modifytime;
  }


  public String getBeforeinfo() {
    return beforeinfo;
  }

  public void setBeforeinfo(String beforeinfo) {
    this.beforeinfo = beforeinfo;
  }


  public String getAfterinfo() {
    return afterinfo;
  }

  public void setAfterinfo(String afterinfo) {
    this.afterinfo = afterinfo;
  }


  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public String getGeometry() {
    return geometry;
  }

  public void setGeometry(String geometry) {
    this.geometry = geometry;
  }

}
