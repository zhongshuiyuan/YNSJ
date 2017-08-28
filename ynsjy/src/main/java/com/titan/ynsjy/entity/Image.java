package com.titan.ynsjy.entity;

/**
 * Created by li on 2017/4/5.
 *
 * 选择图片
 */

public class Image {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    private String name;
    private String base;

    public Image(){

    }

    public Image(String name, String base){
        this.name = name;
        this.base = base;
    }
}
