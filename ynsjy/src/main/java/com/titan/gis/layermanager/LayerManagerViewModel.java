package com.titan.gis.layermanager;

import android.content.Context;
import android.databinding.ObservableField;

import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.local.LDataSource;
import com.titan.model.TitanLayer;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2017/5/11
 * 图层管理
 */

public class LayerManagerViewModel extends BaseViewModel {

    //图层
    public ObservableField<List<TitanLayer>> mLayerList=new ObservableField<>();

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

    }

    /**
     * 获取图层信息
     */
    private void getLayers() {
        //mLayerList.get().add();
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
        //baseLyaers.getSublayers().add(new TitanLayer("基础图层1"));
        //baseLyaers.getSublayers().add(new TitanLayer("基础图层2"));
        //allLayers.add(baseLyaers);
        //影像图
        TitanLayer imgLyaers=new TitanLayer("影像图层");
        List<TitanLayer> imgSublayers=new ArrayList<>();
        imgSublayers.add(new TitanLayer("影像图层1"));
        imgSublayers.add(new TitanLayer("影像图层2"));
        imgLyaers.setSublayers(imgSublayers);
        allLayers.add(imgLyaers);
        //专题图
        TitanLayer ztLyaers=new TitanLayer("专题图层");
        List<TitanLayer> ztSublayers=new ArrayList<>();
        ztSublayers.add(new TitanLayer("专题图层1"));
        ztSublayers.add(new TitanLayer("专题图层2"));
        ztLyaers.setSublayers(ztSublayers);

        allLayers.add(ztLyaers);

        mLayerList.set(allLayers);

    }


}
