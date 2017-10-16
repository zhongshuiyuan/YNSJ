package com.titan.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.titan.model.TitanLayer;
import com.titan.util.FileUtils;
import com.titan.util.TitanFileFilter;
import com.titan.ynsjy.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2017/10/12
 */

public class LocalDataSource implements LDataSource {

    private static LocalDataSource INSTANCE;


    private Context mContext;

    private LocalDataSource(@NonNull Context context) {
        mContext=context;
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     *
     * @param type 1:基础地图 2:影像地图
     * @param callback
     */
    @Override
    public void getLoacalLayers(Integer type, getLayersCallback callback) {
        switch (type){
            case 1:
            try{

                File basefile=new File(MyApplication.resourcesManager.getBaseLayerPath());
                File[] baselayers=basefile.listFiles(new TitanFileFilter.BaseTpkFileFilter());
                List<TitanLayer> resultlayers=new ArrayList<>();
                for(File file:baselayers){
                    TitanLayer layer=new TitanLayer();
                    layer.setUrl(file.getAbsolutePath());
                    layer.setName(FileUtils.pathGetNoSuffixFileName(file.getPath()));
                }
                //MyApplication.sharedPreferences.edit().
                callback.onSuccess(resultlayers);

            }catch(Exception e) {
                callback.onFailure("获取图层异常:"+e.getMessage());
            }

            case 2:
                try{

                    File basefile=new File(MyApplication.resourcesManager.getBaseLayerPath());
                    File[] baselayers=basefile.listFiles(new TitanFileFilter.ImageTpkFileFilter());
                    List<TitanLayer> resultlayers=new ArrayList<>();
                    for(File file:baselayers){
                        TitanLayer layer=new TitanLayer();
                        layer.setUrl(file.getAbsolutePath());
                        layer.setName(FileUtils.pathGetNoSuffixFileName(file.getPath()));
                    }
                    //MyApplication.sharedPreferences.edit().
                    callback.onSuccess(resultlayers);

                }catch(Exception e) {
                    callback.onFailure("获取图层异常:"+e.getMessage());
                }


        }


    }
}
