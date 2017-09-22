package com.titan.ynsjy.audithistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.gis.GisUtil;
import com.titan.model.TitanField;
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
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setMyVisibility(boolean flag){
        if (flag){
            listView.setVisibility(View.VISIBLE);
            tvplaceholder.setVisibility(View.GONE);
        }else {
            listView.setVisibility(View.GONE);
            tvplaceholder.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param map 审计记录属性集合
     */
    public void refresh(Map<String, Object> map, GeodatabaseFeatureTable audittable) {
        try {
            if (map!=null){
                List<TitanField> fieldList=GisUtil.getFields(map,audittable.getFields());
                AuditAdapter adapter = new AuditAdapter(getActivity(),fieldList);
                listView.setAdapter(adapter);
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
//        map.put("AUDIT_PEOPLE",binding.auditPeople.getText().toString());
//        map.put("MODIFYTIME", UtilTime.getSystemtime2());
//        Log.e("tag","time:"+UtilTime.getSystemtime2());
//        map.put("AUDIT_COORDINATE",binding.auditLatlon.getText().toString());
//        map.put("MODIFYINFO",binding.auditReason.getText().toString());
//        map.put("INFO",binding.auditInfo.getText().toString());
//        map.put("BEFOREINFO",binding.auditEditBefore.getText().toString());
//        map.put("AFTERINFO",binding.auditEditAfter.getText().toString());
//        map.put("REMARK",binding.auditMark.getText().toString());
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
