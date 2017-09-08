package com.titan.ynsjy.util;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * map序列化
 */

public class SeralizableMapUtil implements Serializable {
    private Map<String,Object> map;
    public Map<String,Object> getMap()
    {
        return map;
    }
    public void setMap(Map<String,Object> map)
    {
        this.map=map;
    }
}
