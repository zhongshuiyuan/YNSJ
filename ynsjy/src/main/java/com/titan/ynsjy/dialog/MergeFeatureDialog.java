package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.mview.IBaseView;
import com.titan.ynsjy.presenter.BasePresenter;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.titan.ynsjy.BaseActivity.seflayerName;

/**
 * Created by li on 2017/6/2.
 * 小班合并
 */

public class MergeFeatureDialog extends Dialog{

    private Context mContext;
    private IBaseView iBaseView;
    private int mergedex = -1;
    private GeodatabaseFeature mergeFeature;
    private BasePresenter basePresenter;
    private MyLayer myLayer;
    private List<GeodatabaseFeature> list = new ArrayList<>();

    public MergeFeatureDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public MergeFeatureDialog(@NonNull Context context, @StyleRes int themeResId, List<GeodatabaseFeature> list, IBaseView baseView) {
        super(context, themeResId);
        this.mContext = context;
        this.list = list;
        this.iBaseView = baseView;
        basePresenter = new BasePresenter((BaseActivity) mContext,baseView);
    }

    protected MergeFeatureDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hebing);
        setCanceledOnTouchOutside(true);
        List<Long> id = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            id.add(list.get(i).getId());
        }
        ListView listview = (ListView) findViewById(R.id.cut_polygon);
        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(mContext, R.layout.item_hb, R.id.hb_tv1, id);
        listview.setAdapter(adapter);
        final Geometry[] geometries = new Geometry[list.size()];

        for (int i = 0; i < list.size(); i++) {
            geometries[i] = list.get(i).getGeometry();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int position, long arg3) {
//                ((Activity)mContext).runOnUiThread(new Runnable() {
//                    public void run() {
//
//                    }
//                });
                mergedex = position;
                mergeFeature = list.get(position);
                myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer(seflayerName, BaseActivity.layerNameList);
                iBaseView.getGraphicLayer().removeAll();
                Graphic graphic = new Graphic(mergeFeature.getGeometry(), new SimpleFillSymbol(Color.BLUE));
                iBaseView.getGraphicLayer().addGraphic(graphic);
                Envelope envelope = new Envelope();
                mergeFeature.getGeometry().queryEnvelope(envelope);
                iBaseView.getMapView().setExtent(envelope);
            }
        });

        TextView hb_btn = (TextView) findViewById(R.id.hb_btn);
        hb_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mergedex == -1) {
                    ToastUtil.setToast(mContext, "请选择保留小班");
                    return;
                }
                Geometry geometry = GeometryEngine.union(geometries, BaseActivity.spatialReference);
                basePresenter.updateFeature(geometry, mergeFeature, myLayer);

                for (int i = 0; i < list.size(); i++) {
                    if (i == mergedex)
                        continue;
                    basePresenter.delFeature(list.get(i));
                }
                ((BaseActivity)mContext).mapRemove(new View(mContext));
                dismiss();
                BaseActivity.selGeoFeaturesList.clear();
            }
        });

    }
}
