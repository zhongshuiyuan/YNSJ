package com.titan.ynsjy.audithistory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.gis.GisUtil;
import com.titan.gis.SymbolUtil;
import com.titan.model.TitanField;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.AuditAdapter;
import com.titan.ynsjy.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细内容展示页面
 */

public class AuditHistoryInfoFragment extends Fragment {
    private View view;
    private long id;//审计记录的OBJECTID
    private ListView listView;
    private TextView tvplaceholder;
    private MapView mapView;
    private GraphicsLayer mgraphicslayer;
    private FeatureTable featureTable;

    private static AuditHistoryInfoFragment singleton;

    public static AuditHistoryInfoFragment newInstance(){
        if(singleton==null){
            singleton=new AuditHistoryInfoFragment();
        }
        return singleton;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audit_history_info,container,false);
        listView = (ListView) view.findViewById(R.id.audit_info_list);
        tvplaceholder = (TextView) view.findViewById(R.id.audit_placeholder);
        mapView= (MapView) view.findViewById(R.id.mapview_audithistory);
        mgraphicslayer=new GraphicsLayer();


        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (STATUS.LAYER_LOADED == status) {
                    //spatialReference=mapView.getSpatialReference();
                    ToastUtil.setToast(getActivity(), "添加图层成功");
                } else {
                    //ProgressDialogUtil.stopProgressDialog(mContext);
                    ToastUtil.setToast(getActivity(), "添加图层失败");
                }
            }
        });
        /* 基础底图 */
        String titlePath = MyApplication.resourcesManager.getTitlePath();
        ArcGISLocalTiledLayer titlelayer=new ArcGISLocalTiledLayer(titlePath);
         /* 影像底图 */
        String imagePath = MyApplication.resourcesManager.getImagePath();
        ArcGISLocalTiledLayer imagelayer=new ArcGISLocalTiledLayer(imagePath);
        mapView.addLayer(titlelayer);
        mapView.addLayer(imagelayer);
        mapView.addLayer(mgraphicslayer);
        mapView.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 设置空布局
     * @param flag
     */
    public void setMyVisibility(boolean flag){
        if (flag){
            mapView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            tvplaceholder.setVisibility(View.GONE);
        }else {
            mapView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            tvplaceholder.setVisibility(View.VISIBLE);
        }
    }
    /**
     * @param map 审计记录属性集合
     */
    public void refresh(Map<String, Object> map, GeodatabaseFeatureTable audittable,Feature feature) {
        try {
            if (map!=null){
                mgraphicslayer.removeAll();
                List<TitanField> fieldList=GisUtil.getFields(map,audittable.getFields());
                AuditAdapter adapter = new AuditAdapter(getActivity(),fieldList);
                listView.setAdapter(adapter);
                mgraphicslayer.addGraphic(new Graphic(feature.getGeometry(), SymbolUtil.fillSymbol));
                mapView.addLayer(mgraphicslayer);
                mapView.setExtent(feature.getGeometry());

            }

        }catch (Exception e){
            //Log.e("tag","activity:"+getContext()+e);
            ToastUtil.setToast(getActivity(),"更新数据异常"+e);
        }

    }

    /**
     * @param type 设置是否为编辑模式，true为编辑模式，false为默认模式
     */
    public void editMode(boolean type){
//        binding.auditPeople.setEnabled(type);
//        //binding.auditTime.setEnabled(type);
//        binding.auditLatlon.setEnabled(type);
//        binding.auditReason.setEnabled(type);
//        binding.auditInfo.setEnabled(type);
//        binding.auditEditBefore.setEnabled(type);
//        binding.auditEditAfter.setEnabled(type);
//        binding.auditMark.setEnabled(type);
    }

    /**
     * 编辑之后保存数据
     * @param table 编辑表
     */
    public void save(FeatureTable table){
        Map<String,Object> map = new HashMap<>();
        Graphic graphic = new Graphic(null,null,map);
        try {
            table.updateFeature(id,graphic);
            ToastUtil.setToast(getActivity(),"数据保存成功");
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(getActivity(),"数据保存失败");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }


}
