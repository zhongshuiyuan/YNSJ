package com.titan.ynsjy.util;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.table.FeatureTable;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.Order;
import com.esri.core.tasks.query.QueryParameters;
import com.titan.ynsjy.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/5/005.
 * 图层查询帮助类
 */

public class ArcGISQueryUtils {
    /**
     * 查询图层框选区域内的小班
     * @param geometry 框选区域
     * @param spatialReference 空间参考系
     * @param layer 查询图层
     * @param callbackListener 查询回调
     */
    public static void getSelectFeatures(Geometry geometry, SpatialReference spatialReference, FeatureLayer layer, CallbackListener<FeatureResult> callbackListener) {
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(geometry);
        queryParams.setReturnGeometry(true);
        queryParams.setWhere("1=1");
        queryParams.setOutSpatialReference(spatialReference);
        layer.selectFeatures(queryParams, FeatureLayer.SelectionMode.NEW, callbackListener);
    }

    /**
     * 查询表中框选区域内小班的id
     * @param geometry 框选区域
     * @param spatialReference 空间参考系
     * @param table 查询表
     * @param callbackListener 查询回调
     */
    public static void getQueryIds(Geometry geometry, SpatialReference spatialReference, FeatureTable table, CallbackListener<long[]> callbackListener){
        QueryParameters q = new QueryParameters();
        q.setWhere("1=1");
        q.setOutSpatialReference(spatialReference);
        q.setReturnGeometry(true);
        q.setGeometry(geometry);
        table.queryIds(q,callbackListener);
    }

    /**
     * 以升序或降序输出某字段的查询结果
     * @param table 查询表
     * @param field 查询字段
     * @param order 查询结果排序方式
     * @param callbackListener 查询回调
     */
    public static void getQueryFeatures(FeatureTable table,String field,Order order,CallbackListener<FeatureResult> callbackListener){
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setWhere("1=1");
        Map<String, Order> orderFields = new HashMap<>();
        orderFields.put(field, order);
        queryParams.setOrderByFields(orderFields);//设置输出字段值排序方式
        queryParams.setOutSpatialReference(BaseActivity.spatialReference);
        table.queryFeatures(queryParams,callbackListener);
    }

    /**
     * 获取表中所有feature
     * @param table 查询表
     * @param callbackListener 查询监听
     */
    public static void getQueryFeaturesAll(FeatureTable table,CallbackListener<FeatureResult> callbackListener){
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setWhere("1=1");
        queryParams.setOutSpatialReference(BaseActivity.spatialReference);
        table.queryFeatures(queryParams,callbackListener);
    }
}
