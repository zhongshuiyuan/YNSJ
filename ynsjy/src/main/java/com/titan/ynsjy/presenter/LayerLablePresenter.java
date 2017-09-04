package com.titan.ynsjy.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Envelope;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.query.QueryParameters;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.LayerLableAdapter;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.mview.ILayerView;
import com.titan.ynsjy.util.BaseUtil;

import java.util.HashMap;
import java.util.List;


/**
 * Created by li on 2017/6/5.
 * 图层标注presenter
 */

public class LayerLablePresenter {

    private Context mContext;
    public ILayerView iLayerView;
    public MyLayer myLayer;
    private long[] arrays;
    private HashMap<Field, Boolean> checkboxMap = new HashMap<>();
    private String[] filterField = new String[]{"OBJECTID_1", "OBJECTID_12", "OBJECTID", "Shape_Leng", "Shape_Le_1"};

    public LayerLablePresenter(Context context, ILayerView layerView) {
        this.mContext = context;
        this.iLayerView = layerView;
    }

    /**
     * 显示选择图层的对应字段
     */
    public void showLayerAials(final View lableView, MyLayer myLayer) {
        this.myLayer = myLayer;
        ImageView imageView = (ImageView) lableView.findViewById(R.id.attr_field_exit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lableView.setVisibility(View.GONE);
            }
        });
        ListView listView = (ListView) lableView.findViewById(R.id.field_list);
        List<Field> fields = myLayer.getLayer().getFeatureTable().getFields();
        boolean flag = false;
        for (Field f : fields) {
            flag = false;
            for (String aFilterField : filterField) {
                if (f.getName().equals(aFilterField)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            checkboxMap.put(f, false);
        }
        LayerLableAdapter lableAdapter = new LayerLableAdapter(mContext, this, checkboxMap);
        listView.setAdapter(lableAdapter);
        BaseUtil.setHeight(lableAdapter, listView);

    }

    /**
     * 查询当前区域内的对应图层的小班数据
     */
    public void queryFeatures(final MyLayer myLayer, final boolean isChecked, final List<Field> fields, final int position) {
        QueryParameters q = new QueryParameters();
        q.setWhere("1=1");
        q.setInSpatialReference(iLayerView.getBaseTitleLayer().getSpatialReference());
        q.setReturnGeometry(true);
        q.setGeometry(iLayerView.getCurrentEnvelope());
        //myLayer.getLayer().setSelectionColor(0);
        myLayer.getTable().queryIds(q, new CallbackListener<long[]>() {
            @Override
            public void onCallback(long[] longs) {
                if (longs.length > 0) {
                    arrays = longs;
                    addLableToLayer(isChecked, fields, position);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    /**
     * 添加lable
     */
    public void addLableToLayer(boolean isChecked, List<Field> fields, int position) {
        if (isChecked) {
            iLayerView.getGraphicLayer().removeAll();
            return;
        }
        if (arrays == null) {
            return;
        }
        iLayerView.getGraphicLayer().removeAll();
        for (long id : arrays) {
            GeodatabaseFeature feature = (GeodatabaseFeature) myLayer.getLayer().getFeature(id);
            Object obj = feature.getAttributeValue(fields.get(position));
            String text;
            Log.e("tag","obj"+obj);
            if (obj != null&&!obj.equals("")) {
                text = obj.toString();
            } else {
                text = "空";
            }
            TextSymbol textSymbol = new TextSymbol(20, text, Color.BLUE);
            //解决中文乱码
            textSymbol.setFontFamily("DroidSansFallback.ttf");
            Envelope env = new Envelope();
            feature.getGeometry().queryEnvelope(env);
            Graphic graphic = new Graphic(env.getCenter(), textSymbol);
            iLayerView.getGraphicLayer().addGraphic(graphic);
        }
    }

}
