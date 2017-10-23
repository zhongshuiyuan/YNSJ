package com.titan.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.titan.model.TitanLayer;
import com.titan.util.FileUtils;
import com.titan.util.TitanFileFilter;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.util.BussUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by whs on 2017/10/12
 */

public class LocalDataSource implements LDataSource {

    private static LocalDataSource INSTANCE;

    private List<File> groups;
    private List<Map<String, List<File>>> childs;

    public Map<FeatureLayer, Integer> getLayerindexmap() {
        return layerindexmap;
    }

    //图层索引存储
    private Map<FeatureLayer, Integer> layerindexmap = new ConcurrentHashMap<>();

    public List<Geodatabase> getGeodatabaseList() {
        return geodatabaseList;
    }

    //数据库
    private List<Geodatabase> geodatabaseList;

    //基础图和影像图索引存储
    private Map<String, ArcGISLocalTiledLayer> tiledLayerIntegerMap = new ConcurrentHashMap<>();

    public Map<String, ArcGISLocalTiledLayer> getTiledLayerIntegerMap(){
        return tiledLayerIntegerMap;
    }
    //基础图和影像图索引存储
    private Map<String, ArcGISLocalTiledLayer> imageLayerIntegerMap = new ConcurrentHashMap<>();

    public Map<String, ArcGISLocalTiledLayer> getImageLayerIntegerMap(){
        return imageLayerIntegerMap;
    }

    private Context mContext;

    private LocalDataSource(@NonNull Context context) {
        mContext = context;
        initData();
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    private void initData() {
        groups = MyApplication.resourcesManager.getOtmsFolder(BussUtil.getConfigXml(mContext, "yzl"));
        childs = MyApplication.resourcesManager.getChildeData(mContext, groups);
        geodatabaseList = MyApplication.resourcesManager.getChildGdb(mContext,groups);
    }

    /**
     * @param type     1:基础地图 2:影像地图
     * @param callback
     */
    @Override
    public void getLoacalLayers(Integer type, getLayersCallback callback) {
        switch (type) {
            case 1:
                try {
                    File basefile = new File(MyApplication.resourcesManager.getBaseLayerPath());
                    File[] baselayers = basefile.listFiles(new TitanFileFilter.BaseTpkFileFilter());
                    List<TitanLayer> resultlayers = new ArrayList<>();
                    for (File file : baselayers) {
                        TitanLayer layer = new TitanLayer();
                        layer.setUrl(file.getAbsolutePath());
                        layer.setName(FileUtils.pathGetNoSuffixFileName(file.getPath()));
                        resultlayers.add(layer);
                    }
                    //MyApplication.sharedPreferences.edit().
                    callback.onSuccess(resultlayers);
                } catch (Exception e) {
                    callback.onFailure("获取图层异常:" + e.getMessage());
                }
                break;
            case 2:
                try {
                    File basefile = new File(MyApplication.resourcesManager.getBaseLayerPath());
                    File[] baselayers = basefile.listFiles(new TitanFileFilter.ImageTpkFileFilter());
                    List<TitanLayer> resultlayers = new ArrayList<>();
                    for (File file : baselayers) {
                        TitanLayer layer = new TitanLayer();
                        layer.setUrl(file.getAbsolutePath());
                        layer.setName(FileUtils.pathGetNoSuffixFileName(file.getPath()));
                        resultlayers.add(layer);
                    }
                    //MyApplication.sharedPreferences.edit().
                    callback.onSuccess(resultlayers);

                } catch (Exception e) {
                    callback.onFailure("获取图层异常:" + e.getMessage());
                }
                break;
            case 3:
                try {
                    int i = 0;
                    //一级目录
                    List<TitanLayer> fList = new ArrayList<>();
                    TitanLayer gLayer = null;
                    for (Map<String, List<File>> map : childs) {
                        for (String key : map.keySet()) {
                            File file = groups.get(i);
                            gLayer = setTitanLayer(file.getName(), file.getAbsolutePath(), 4, 4, true);
                            //二级目录
                            List<TitanLayer> sList = new ArrayList<>();
                            for (File f : map.get(key)) {
                                try {
                                    Geodatabase gdb = new Geodatabase(f.getAbsolutePath());
                                    List<GeodatabaseFeatureTable> gdbTable = gdb.getGeodatabaseTables();
                                    TitanLayer titanLayer = null;
                                    //三级目录
                                    List<TitanLayer> tList = new ArrayList<>();
                                    for (GeodatabaseFeatureTable table : gdbTable) {
                                        if (gdbTable.size() > 1) {
                                            titanLayer = setTitanLayer(f.getName(), f.getAbsolutePath(), 3, 1, true);
                                        } else if (gdbTable.size() == 1) {
                                            titanLayer = setTitanLayer(f.getName(), f.getAbsolutePath(), 3, 1, false);
                                        }
                                        if (titanLayer != null) {
                                            titanLayer.setSublayers(tList);
                                        }
                                        tList.add(setTitanLayer(table.getTableName(), gdb.getPath(), 3, 1, false));
                                    }
                                    sList.add(titanLayer);
                                    gLayer.setSublayers(sList);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        fList.add(gLayer);
                        i++;
                    }
                    callback.onSuccess(fList);
                } catch (Exception e) {
                    callback.onFailure("获取图层异常:" + e.getMessage());
                }
        }
    }

    /**
     * 初始化图层信息
     * @param name 文件名
     * @param path 地址
     * @param flag 文件标识
     * @param type 文件类型
     * @param hasSubLayer 是否有子级
     * @return
     */
    private TitanLayer setTitanLayer(String name, String path, int flag, int type, boolean hasSubLayer) {
        TitanLayer titanLayer = new TitanLayer();
        titanLayer.setName(name);
        titanLayer.setUrl(path);
        titanLayer.setFlag(flag);
        titanLayer.setType(type);
        titanLayer.setHasSubLayer(hasSubLayer);
        return titanLayer;
    }
}
