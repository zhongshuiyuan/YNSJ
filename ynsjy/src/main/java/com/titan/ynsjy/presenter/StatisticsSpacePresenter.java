package com.titan.ynsjy.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.mview.ILayerView;
import com.titan.ynsjy.statistics.EDAreaSum;
import com.titan.ynsjy.statistics.EDStatisticsActivity;
import com.titan.ynsjy.statistics.GYLAreaSum;
import com.titan.ynsjy.statistics.GYLStatisticsActivity;
import com.titan.ynsjy.statistics.LDAreaSum;
import com.titan.ynsjy.statistics.LDStatisticsActivity;
import com.titan.ynsjy.statistics.LGAreaSum;
import com.titan.ynsjy.statistics.LGStatisticaActivity;
import com.titan.ynsjy.statistics.StatisticsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/6/6.
 * 空间统计presenter
 */

public class StatisticsSpacePresenter {

    private Context mContext;
    private ILayerView iLayerView;

    public StatisticsSpacePresenter(Context context,ILayerView layerView){
        this.mContext = context;
        this.iLayerView = layerView;
    }


    /**
     * 林地落界统计表
     *
     * @param list
     */
    public void showLDData(MyLayer myLayer,List<GeodatabaseFeature> list, String tjdwStr) {
        if (list == null) {
            return;
        }
        ArrayList<LDAreaSum> data = StatisticsUtil.getLDLJData(list, tjdwStr);
        data.addAll(StatisticsUtil.showLDData_Guoyou(list, "1", tjdwStr));
        data.addAll(StatisticsUtil.showLDData_Guoyou(list, "2", tjdwStr));
        data.addAll(StatisticsUtil.showLDData_Guoyou(list, "3", tjdwStr));
        Intent intent = new Intent(mContext, LDStatisticsActivity.class);
        Bundle bunddle = new Bundle();
        bunddle.putSerializable("data", data);
        intent.putExtras(bunddle);
        mContext.startActivity(intent);
        // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        //overridePendingTransition(R.anim.activity_translate_in,R.anim.activity_translate_out);
        ProgressDialogUtil.stopProgressDialog(mContext);
        iLayerView.getGraphicLayer().removeAll();
        if (myLayer != null && myLayer.getLayer().isInitialized()) {
            myLayer.getLayer().clearSelection();
        }
        if (iLayerView.getDrawTool() != null) {
            iLayerView.getDrawTool().activate(0);
        }
    }

    /**
     * 二调统计表
     *
     * @param list
     */
    public void showEDData(MyLayer myLayer,List<GeodatabaseFeature> list,String tjdwStr) {
        if (list == null) {
            return;
        }
        ArrayList<EDAreaSum> data = StatisticsUtil.getEDData(list, tjdwStr);

        data.addAll(StatisticsUtil.showEDData_Guoyou(list, "1", tjdwStr));
        data.addAll(StatisticsUtil.showEDData_Guoyou(list, "2", tjdwStr));
        data.addAll(StatisticsUtil.showEDData_Guoyou(list, "3", tjdwStr));
        data.addAll(StatisticsUtil.showEDData_Guoyou(list, "4", tjdwStr));
        data.addAll(StatisticsUtil.showEDData_Guoyou(list, "5", tjdwStr));
        Intent intent = new Intent(mContext, EDStatisticsActivity.class);
        Bundle bunddle = new Bundle();
        bunddle.putSerializable("data", data);
        intent.putExtras(bunddle);
        mContext.startActivity(intent);
        // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        //overridePendingTransition(R.anim.activity_translate_in,R.anim.activity_translate_out);
        ProgressDialogUtil.stopProgressDialog(mContext);
        iLayerView.getGraphicLayer().removeAll();
        if (myLayer != null && myLayer.getLayer().isInitialized()) {
            myLayer.getLayer().clearSelection();
        }
        if (iLayerView.getDrawTool() != null) {
            iLayerView.getDrawTool().activate(0);
        }
    }

    /**
     * 林权宗地统计表
     *
     * @param list
     */
    public void showLGData(MyLayer myLayer,List<GeodatabaseFeature> list,String tjdwStr) {
        if (list == null) {
            return;
        }
        ArrayList<LGAreaSum> data = StatisticsUtil.getLQZDData(list, tjdwStr);

        Intent intent = new Intent(mContext, LGStatisticaActivity.class);
        Bundle bunddle = new Bundle();
        bunddle.putSerializable("data", data);
        intent.putExtras(bunddle);
        mContext.startActivity(intent);
        //overridePendingTransition(R.anim.activity_translate_in,R.anim.activity_translate_out);
        ProgressDialogUtil.stopProgressDialog(mContext);
        iLayerView.getGraphicLayer().removeAll();
        if (myLayer != null && myLayer.getLayer().isInitialized()) {
            myLayer.getLayer().clearSelection();
        }
        if (iLayerView.getDrawTool() != null) {
            iLayerView.getDrawTool().activate(0);
        }
    }

    /**
     * 公益林统计表
     *
     * @param list
     */
    public void showGYLData(MyLayer myLayer,List<GeodatabaseFeature> list,String tjdwStr) {
        if (list == null) {
            return;
        }
        ArrayList<GYLAreaSum> data = StatisticsUtil.getGYLData(list, tjdwStr);

        Intent intent = new Intent(mContext, GYLStatisticsActivity.class);
        Bundle bunddle = new Bundle();
        bunddle.putSerializable("data", data);
        intent.putExtras(bunddle);
        mContext.startActivity(intent);
        // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        //overridePendingTransition(R.anim.activity_translate_in,R.anim.activity_translate_out);
        ProgressDialogUtil.stopProgressDialog(mContext);
        iLayerView.getGraphicLayer().removeAll();
        if (myLayer != null && myLayer.getLayer().isInitialized()) {
            myLayer.getLayer().clearSelection();
        }
        if (iLayerView.getDrawTool() != null) {
            iLayerView.getDrawTool().activate(0);
        }
    }




}
