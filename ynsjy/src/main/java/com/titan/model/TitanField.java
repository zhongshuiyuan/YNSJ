package com.titan.model;

/**
 * Created by whs on 2017/9/22
 * 存储空间字段
 */

public class TitanField {
    //别名
    private  String alias;
    //名称
    private  String name;
    //值
    private  String value;
    //是否有中文别名
    private  boolean hasalias;
    //字段类型
    private  int fieldtype;

    public TitanField() {
    }

    public TitanField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isHasalias() {
        return hasalias;
    }

    public void setHasalias(boolean hasalias) {
        this.hasalias = hasalias;
    }

    public int getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(int fieldtype) {
        this.fieldtype = fieldtype;
    }
}
