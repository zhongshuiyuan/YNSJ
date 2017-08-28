package com.titan.ynsjy.service;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by li on 2017/5/5.
 * 接口
 */

public interface RetrofitService {

    // 用户信息
    @GET("/GISService.asmx/Get_HLY_INFO")
    Observable<String> getUserNumber();

    // 上传多媒体信息
    @FormUrlEncoded
    @POST("/GISService.asmx/UPPatrolEvent")
    Observable<String> uPPatrolEvent(@Field("jsonText") String jsonText);

    //获取设备信息
    @GET("/GISService.asmx/getMobileInfo")
    Observable<String> getMobileInfo(@Query("SBH") String SBH);

    //查询设备是否入库
    @GET("/GISService.asmx/isHaveSBH")
    Observable<String> isHaveSBH(@Query("SBH") String SBH);

    //上传坐标信息
    @GET("/GISService.asmx/UPLonLat")
    Observable<String> upLonLat(@Query("SBH") String SBH,@Query("LON") String LON,@Query("LAT") String LAT);

    // 上传设备信息
    @GET("/GISService.asmx/addSBH")
    Observable<String> sendSbInfo(@Query("SBH") String SBH, @Query("XLH") String XLH, @Query("SBMC") String SBMC, @Query("SYZNAME") String SYZNAME, @Query("SYZPHONE") String SYZPHONE, @Query("DZ") String DZ, @Query("CID") String CID);

    //更新设备信息
    @GET("/GISService.asmx/updateSBinfo")
    Observable<String> updateSbinfo(@Query("SYZNAME") String SYZNAME,@Query("SBH") String SBH,@Query("SYZPHONE") String SYZPHONE,@Query("DZ") String DZ);

    @GET("/GYLYEQ/Service/Service.asmx/addMoblieSysInfo")
    Observable<String> addMoblieSysInfo(@Query("sysname") String sysname, @Query("tel") String tel, @Query("dw") String dw, @Query("retime") String retime, @Query("sbmc") String sbmc, @Query("sbh") String sbh, @Query("bz") String bz);

    @GET("/GYLYEQ/Service/Service.asmx/selSBUserInfo")
    Observable<String> selMobileSysInfo(@Query("sbh") String sbh, @Query("xlh") String xlh, @Query("sbmc") String type);

}
