package com.titan.gis.layermanager;

import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.local.LDataSource;
import com.titan.model.TitanLayer;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by whs on 2017/5/11
 * 图层管理
 */

public class LayerManagerViewModel extends BaseViewModel {

    //图层
    public ObservableField<List<TitanLayer>> mLayerList=new ObservableField<>();
    //图层索引
    public ObservableField<Map<FeatureLayer, Integer>> layerindexmap = new ObservableField<>();
    //基础图、影像图索引
    public ObservableField<Map<String, ArcGISLocalTiledLayer>> tiledLayerIntegerMap = new ObservableField<>();
    public ObservableField<Map<String, ArcGISLocalTiledLayer>> imageLayerIntegerMap = new ObservableField<>();

    //图层
    //public ObservableField<TitanLayer> mLayer=new ObservableField<>();
    //选中底图index
    //public ObservableInt selectposition=new ObservableInt(0);
    //ObservableMap mCheckInfo=new ObservableArrayMap();

    private Context mContext;

    private LayerManager mLayermanager;

    private DataRepository mDataRepository;

    public LayerManagerViewModel(Context context, LayerManager layerManager, DataRepository dataRepository){
        this.mContext=context;
        this.mLayermanager=layerManager;
        this.mDataRepository=dataRepository;
        //this.mLayerList.set(layerlist);
        start();
    }

    /**
     * 关闭图层控制弹框
     */
    public void closeLayerControl(){
        mLayermanager.close();
    }


    /**
     * 初始化
     */
    public void start() {
        getLayers();
        layerindexmap.set(mDataRepository.getLayerindexmap());
        tiledLayerIntegerMap.set(mDataRepository.getTiledLayerIntegerMap());
        imageLayerIntegerMap.set(mDataRepository.getImageLayerIntegerMap());
    }

    /**
     * 获取图层信息
     */
    private void getLayers() {
        final List<TitanLayer> allLayers=new ArrayList<>();
        //基础图
        final TitanLayer baseLyaers=new TitanLayer("基础图层");
        mDataRepository.getLoacalLayers(1, new LDataSource.getLayersCallback() {
            @Override
            public void onFailure(String info) {
                ToastUtil.showShort(mContext,info);
            }

            @Override
            public void onSuccess(List<TitanLayer> layers) {
                baseLyaers.setSublayers(layers);
                allLayers.add(baseLyaers);
            }
        });
        //影像图
        final TitanLayer imgLyaers=new TitanLayer("影像图层");
        mDataRepository.getLoacalLayers(2, new LDataSource.getLayersCallback() {
            @Override
            public void onFailure(String info) {
                ToastUtil.showShort(mContext,info);
            }

            @Override
            public void onSuccess(List<TitanLayer> layers) {
                imgLyaers.setSublayers(layers);
                allLayers.add(imgLyaers);
            }
        });
        //专题图
        final TitanLayer ztLyaers=new TitanLayer("专题图层");
        mDataRepository.getLoacalLayers(3, new LDataSource.getLayersCallback() {
            @Override
            public void onFailure(String info) {
                ToastUtil.showShort(mContext,info);
            }

            @Override
            public void onSuccess(List<TitanLayer> layers) {
                ztLyaers.setSublayers(layers);
                allLayers.add(ztLyaers);
            }
        });
        mLayerList.set(allLayers);
    }
}
