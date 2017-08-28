package com.titan.ynsjy.entity;

/**
 * Created by li on 2017/2/23.
 * 轨迹点
 */

public class GjPoint {

    private int id;
    private String lon;
    private String lat;
    private String sbh;
    private String time;
    private String state;

    public GjPoint(){

    }

    public GjPoint(int id, String lon, String lat, String sbh, String time, String state){
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.sbh = sbh;
        this.time = time;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getSbh() {
        return sbh;
    }

    public void setSbh(String sbh) {
        this.sbh = sbh;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
