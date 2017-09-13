package com.titan.ynsjy.AuditHistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.model.AuditInfo;
import com.titan.ynsjy.databinding.AuditHistoryInfoBinding;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.UtilTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细内容展示页面
 */

public class AuditHistoryInfoFragment extends Fragment {
   /* @BindView(R.id.audit_people)
    EditText auditPeople;//审计人员
    @BindView(R.id.audit_time)
    EditText auditTime;//审计时间
    @BindView(R.id.audit_latlon)
    EditText auditLatlon;//审计地点
    @BindView(R.id.audit_reason)
    EditText auditReason;//审计原因
    @BindView(R.id.audit_info)
    EditText auditInfo;//描述信息
    @BindView(R.id.audit_edit_before)
    EditText auditEditBefore;//修改前状况
    @BindView(R.id.audit_edit_after)
    EditText auditEditAfter;//修改后状况
    @BindView(R.id.audit_mark)
    EditText auditMark;//备注
    Unbinder unbinder;*/
    private View view;
    private long id;//审计记录的OBJECTID

    private AuditHistoryInfoBinding binding;

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
        binding= AuditHistoryInfoBinding.inflate(inflater,container,false);
        //unbinder = ButterKnife.bind(this, view);
        return binding.getRoot();
        //view = inflater.inflate(R.layout.audit_history_info, container, false);
        //unbinder = ButterKnife.bind(this, view);
        //return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * @param map 审计记录属性集合
     */
    public void refresh(Map<String, Object> map) {
        try {
            String id=map.get("OBJECTID").toString();
            String time=map.get("MODIFYTIME").toString();
            String reason=map.get("MODIFYINFO").toString();
            String info=map.get("INFO").toString();
            String beforeinfo=map.get("BEFOREINFO").toString();
            String afterinfo=map.get("AFTERINFO").toString();
            String remark=map.get("REMARK").toString();
            AuditInfo auditInfo=new AuditInfo();
            auditInfo.setObjectid(id);
            auditInfo.setAddress("地址");
            auditInfo.setTime(time);
            auditInfo.setReason(reason);
            auditInfo.setInfo(info);
            auditInfo.setBeforinfo(beforeinfo);
            auditInfo.setAfterinfo(afterinfo);
            auditInfo.setReason(remark);
            binding.setAuditInfo(auditInfo);
            /*id = Long.valueOf(map.get("OBJECTID").toString());
            //auditPeople.setText(map.get("").toString());
            auditTime.setText(map.get("MODIFYTIME").toString());
            //auditLatlon.setText(map.get("").toString());
            auditReason.setText(map.get("MODIFYINFO").toString());
            auditInfo.setText(map.get("INFO").toString());
            auditEditBefore.setText(map.get("BEFOREINFO").toString());
            auditEditAfter.setText(map.get("AFTERINFO").toString());
            auditMark.setText(map.get("REMARK").toString());*/

        }catch (Exception e){
            ToastUtil.setToast(getActivity(),"获取数据异常"+e);
        }

    }

    /**
     * @param type 设置是否为编辑模式，true为编辑模式，false为默认模式
     */
    public void editMode(boolean type){
            //auditPeople.setEnabled(type);
            //auditTime.setEnabled(type);
            //auditLatlon.setEnabled(type);
            binding.auditReason.setEnabled(type);
        binding.auditInfo.setEnabled(type);
            binding.auditEditBefore.setEnabled(type);
            binding.auditEditAfter.setEnabled(type);
            binding.auditMark.setEnabled(type);
    }

    /**
     * 编辑之后保存数据
     * @param table 编辑表
     */
    public void save(FeatureTable table){
        Map<String,Object> map = new HashMap<>();
        //map.put("",auditPeople.getText().toString());
        map.put("MODIFYTIME", UtilTime.getSystemtime2());
        //map.put("",auditLatlon.getText().toString());
        map.put("MODIFYINFO",binding.auditReason.getText().toString());
        map.put("INFO",binding.auditInfo.getText().toString());
        map.put("BEFOREINFO",binding.auditEditBefore.getText().toString());
        map.put("AFTERINFO",binding.auditEditAfter.getText().toString());
        map.put("REMARK",binding.auditMark.getText().toString());
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
