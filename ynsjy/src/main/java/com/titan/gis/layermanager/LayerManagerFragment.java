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
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;

import com.esri.android.map.MapView;
import com.titan.model.TitanLayer;
import com.titan.ynsjy.R;
import com.titan.ynsjy.databinding.FragLayermanagerBinding;

import java.util.List;

/**
 * Created by WHS on 2017/1/4
 * 图层管理
 */

public class LayerManagerFragment extends DialogFragment implements LayerManager {



    private FragLayermanagerBinding binding;

    private LayerManagerViewModel mViewModel;

    private  static LayerManagerFragment Singleton;
    //图层
    private static  List<TitanLayer> mLayerList=null;
    //
    private LayersAdapter mAadpter;

    private static MapView mMapView;



    /**
     * Create a new instance of
     */
    public static LayerManagerFragment newInstance() {
        return new LayerManagerFragment();
    }

    public static LayerManagerFragment getInstance(MapView mapView){


        if(Singleton==null){
            mMapView=mapView;
            Singleton=new LayerManagerFragment();
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
        //
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(inflater, R.layout.frag_layermanager,container,true);
        binding.setViewmodel(mViewModel);

        mLayerList=mViewModel.mLayerList.get();
        Log.e("layers","图层"+mLayerList.size()+"子图层"+mLayerList.get(0).getSublayers().size());
        mAadpter=new LayersAdapter(getActivity(),mLayerList);
        binding.elvBaselayers.setAdapter(mAadpter);
        binding.elvBaselayers.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition);
                //if()parent.isGroupExpanded(groupPosition)
                return false;
            }
        });
        binding.elvBaselayers.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CheckedTextView ctv= (CheckedTextView) v;
                if(ctv!=null){
                    ctv.toggle();
                }
                return false;
            }
        });
        //默认展开
        for (int i = 0; i <mLayerList.size() ; i++) {
            binding.elvBaselayers.expandGroup(i);
            i++;
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
                window.setGravity(Gravity.RIGHT|Gravity.TOP);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置dialog背景透明
            }
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width= (int) (dm.widthPixels * 0.25);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.25), ViewGroup.LayoutParams.WRAP_CONTENT);
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
        if(mAadpter!=null){
            mAadpter.notifyDataSetChanged();
        }

    }
}
