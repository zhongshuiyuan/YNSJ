package com.titan.model;


/**
 * 照片类
 */
public class Photo {
  //主键
  private long pk_Uid;
  //对应数据ID
  private long fk_Fxh_Uid;
  //拍摄时间
  private java.sql.Date time;
  //描述信息
  private String info;
  //备注
  private String remark;
  //地址
  private String addeess;
  //存储路径
  private String uri;
  //集合数据
  private long geometry;
  //修改记录对应ID
  private long fk_Edit_Uid;
  //经度
  private String lon;
  //纬度
  private String lat;

  public long getPk_Uid() {
    return pk_Uid;
  }

  public void setPk_Uid(long pk_Uid) {
    this.pk_Uid = pk_Uid;
  }


  public long getFk_Fxh_Uid() {
    return fk_Fxh_Uid;
  }

  public void setFk_Fxh_Uid(long fk_Fxh_Uid) {
    this.fk_Fxh_Uid = fk_Fxh_Uid;
  }


  public java.sql.Date getTime() {
    return time;
  }

  public void setTime(java.sql.Date time) {
    this.time = time;
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


  public String getAddeess() {
    return addeess;
  }

  public void setAddeess(String addeess) {
    this.addeess = addeess;
  }


  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }


  public long getGeometry() {
    return geometry;
  }

  public void setGeometry(long geometry) {
    this.geometry = geometry;
  }


  public long getFk_Edit_Uid() {
    return fk_Edit_Uid;
  }

  public void setFk_Edit_Uid(long fk_Edit_Uid) {
    this.fk_Edit_Uid = fk_Edit_Uid;
  }

  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }
}
