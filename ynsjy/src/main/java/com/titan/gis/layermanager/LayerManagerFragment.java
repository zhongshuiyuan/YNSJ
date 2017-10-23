package com.titan.gis.layermanager;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.renderer.Renderer;
import com.titan.gis.RendererUtil;
import com.titan.model.TitanLayer;
import com.titan.ynsjy.R;
import com.titan.ynsjy.databinding.FragLayermanagerBinding;
import com.titan.ynsjy.mview.LayerControlView;
import com.titan.ynsjy.util.ToastUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by WHS on 2017/1/4
 * 图层管理
 */

public class LayerManagerFragment extends DialogFragment implements LayerManager, LayersAdapter.LayerAddOrLocation {


    private FragLayermanagerBinding binding;

    private LayerManagerViewModel mViewModel;

    private static LayerManagerFragment Singleton;
    //图层
    private static List<TitanLayer> mLayerList = null;
    //
    private LayersAdapter mAadpter;

    private static MapView mMapView;

    private static LayerControlView mControlView;

    /**
     * Create a new instance of
     */
    public static LayerManagerFragment newInstance() {
        return new LayerManagerFragment();
    }

    public static LayerManagerFragment getInstance(MapView mapView, LayerControlView controlView) {

        if (Singleton == null) {
            mMapView = mapView;
            mControlView = controlView;
            Singleton = new LayerManagerFragment();
        }
        return Singleton;
    }

    public void setViewModel(LayerManagerViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //此处可以设置Dialog的style等等
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_layermanager, container, true);
        binding.setViewmodel(mViewModel);

        mLayerList = mViewModel.mLayerList.get();
        Log.e("layers", "图层" + mLayerList.size() + "子图层" + mLayerList.get(0).getSublayers().size());
        if (mAadpter == null) {
            mAadpter = new LayersAdapter(getActivity(), mLayerList, this);
        }
        binding.elvBaselayers.setAdapter(mAadpter);
        //默认展开
        for (int i = 0; i < mLayerList.size(); i++) {
            binding.elvBaselayers.expandGroup(i);
        }
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        //参数在onCreate中设置无效果
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.RIGHT | Gravity.TOP);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置dialog背景透明
            }
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.25), ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mViewModel.start();
    }

    @Override
    public void close() {
        this.dismiss();
    }

    @Override
    public void adapterRefresh() {
        if (mAadpter != null) {
            mAadpter.notifyDataSetChanged();
        }
    }

    /**
     * 图层添加
     *
     * @param isAdd
     * @param geotable
     */
    @Override
    public void addLayer(boolean isAdd, GeodatabaseFeatureTable geotable) {
        FeatureLayer featureLayer = new FeatureLayer(geotable);
        Map<FeatureLayer, Integer> layerindexmap = mViewModel.layerindexmap.get();
        if (isAdd) {
            //添加
            Renderer renderer = RendererUtil.getHisSymbol(mControlView, featureLayer);
            featureLayer.setRenderer(renderer);
            int layerindex = mMapView.addLayer(featureLayer);
            layerindexmap.put(featureLayer, layerindex);
            Log.e("tag", featureLayer.getName() + "," + layerindex);
            mMapView.removeLayer(mControlView.getGraphicLayer());
            mMapView.addLayer(mControlView.getGraphicLayer());
//            mMapView.setExtent(featureLayer.getFullExtent());
        } else {
            //移除
            try {
                for (FeatureLayer key : layerindexmap.keySet()) {
                    if ((key.getUrl()+key.getName()).equals(featureLayer.getUrl()+featureLayer.getName())) {
                        mMapView.removeLayer(key);
                        layerindexmap.remove(key);
                    }
                }
            } catch (Exception e) {
                ToastUtil.showShort(getActivity(), "图层移除异常" + e);
                Log.e("tag","图层移除异常:"+e);
            }
        }
    }

    /**
     * 图层定位
     *
     * @param geotable
     */
    @Override
    public void location(GeodatabaseFeatureTable geotable) {
        FeatureLayer layer = new FeatureLayer(geotable);
        mMapView.setExtent(layer.getFullExtent());
    }

    /**
     * 添加影像图
     *
     * @param path 文件地址
     * @param isAdd 是否是添加
     * @param type 文件类型，1、基础图，2、影像图
     */
    @Override
    public void addTiledLayer(boolean isAdd, String path,int type) {
        if (!new File(path).exists()) {
            ToastUtil.setToast(getActivity(), "图层文件不存在");
            return;
        }
        ArcGISLocalTiledLayer tiledLayer = new ArcGISLocalTiledLayer(path);
        Map<String, ArcGISLocalTiledLayer> tiledLayerIntegerMap = mViewModel.tiledLayerIntegerMap.get();
        Map<String, ArcGISLocalTiledLayer> imageLayerIntegerMap = mViewModel.imageLayerIntegerMap.get();
        if (isAdd) {
            //添加
            mMapView.addLayer(tiledLayer);
            if (type==1){
                tiledLayerIntegerMap.put(path, tiledLayer);
            }else if (type==2){
                imageLayerIntegerMap.put(path, tiledLayer);
            }
            mMapView.setExtent(tiledLayer.getFullExtent());
        } else {
            //移除
            try {
                if (type==1){
                    mMapView.removeLayer(tiledLayerIntegerMap.get(path));
                    tiledLayerIntegerMap.remove(path);
                }else if (type==2){
                    mMapView.removeLayer(imageLayerIntegerMap.get(path));
                    imageLayerIntegerMap.remove(path);
                }
            } catch (Exception e) {
                ToastUtil.setToast(getActivity(), "图层移除异常" + e);
                Log.e("tag", "layerError:" + e);
            }
        }
    }

    /**
     * 基础图、影像图定位
     *
     * @param path 文件地址
     */
    @Override
    public void locationTiledLater(String path) {
        ArcGISLocalTiledLayer layer = new ArcGISLocalTiledLayer(path);
        mMapView.setExtent(layer.getFullExtent());
    }


}
