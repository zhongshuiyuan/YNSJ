package com.titan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by whs on 2017/7/3
 * 图层类
 */

public class TitanLayer implements Serializable {
    //图层名称
    private String name;
    //图层地址
    private String url;
    //图层索引
    private int index;
    //可见性
    private boolean visiable;
    //图层状态
    private int status;
    //图层标识 1:基础图 2:影像图 3:专题图层
    private int flag;
    //图层类型
    //1:geodatatable;2:shapefile;3:地图服务;
    private int type;
    //子图层
    private List<TitanLayer> sublayers;

    public TitanLayer(String name) {
        this.name = name;
    }

    public TitanLayer() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public List<TitanLayer> getSublayers() {
        return sublayers;
    }

    public void setSublayers(List<TitanLayer> sublayers) {
        this.sublayers = sublayers;
    }
}
