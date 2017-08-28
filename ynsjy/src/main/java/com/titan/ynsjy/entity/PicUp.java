package com.titan.ynsjy.entity;

import java.io.Serializable;

/**
 * Created by li on 2017/7/15.
 * 上传图片实体类
 */

public class PicUp implements Serializable {

    private String SCSBBH;
    private String ZPDZ;
    private String MSXX;
    private String JD;
    private String WD;
    private String REMARK;

    public String getSCSBBH() {
        return SCSBBH;
    }

    public void setSCSBBH(String SCSBBH) {
        this.SCSBBH = SCSBBH;
    }

    public String getZPDZ() {
        return ZPDZ;
    }

    public void setZPDZ(String ZPDZ) {
        this.ZPDZ = ZPDZ;
    }

    public String getMSXX() {
        return MSXX;
    }

    public void setMSXX(String MSXX) {
        this.MSXX = MSXX;
    }

    public String getJD() {
        return JD;
    }

    public void setJD(String JD) {
        this.JD = JD;
    }

    public String getWD() {
        return WD;
    }

    public void setWD(String WD) {
        this.WD = WD;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }


}
