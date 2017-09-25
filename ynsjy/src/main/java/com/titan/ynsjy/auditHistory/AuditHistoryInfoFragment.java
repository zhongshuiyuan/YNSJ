package com.titan.ynsjy.auditHistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.AuditHistoryActivity;
import com.titan.ynsjy.adapter.AuditAdapter;
import com.titan.ynsjy.databinding.AuditHistoryInfoBinding;
import com.titan.ynsjy.dialog.AuditInfoEditDialog;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 * 审计详细内容展示页面
 */

public class AuditHistoryInfoFragment extends Fragment implements AuditInfo {
    private View view;
    private long id;//审计记录的OBJECTID
    private ListView listView;
    private TextView tvplaceholder;
    private AuditHistoryInfoBinding binding;
    private AuditViewModel auditViewModel;
    private AuditInfoEditDialog dialog;

    private static AuditHistoryInfoFragment singleton;

    public static AuditHistoryInfoFragment newInstance() {
        if (singleton == null) {
            singleton = new AuditHistoryInfoFragment();
        }
        return singleton;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.audit_history_info,container,false);
//        listView = (ListView) view.findViewById(R.id.audit_info_list);
//        tvplaceholder = (TextView) view.findViewById(R.id.audit_placeholder);
        binding = AuditHistoryInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setViewModel(AuditViewModel viewModel) {
        auditViewModel = viewModel;
    }

    public void setMyVisibility(boolean flag) {
        if (flag) {
            binding.auditInfoList.setVisibility(View.VISIBLE);
            binding.auditPlaceholder.setVisibility(View.GONE);
        } else {
            binding.auditInfoList.setVisibility(View.GONE);
            binding.auditPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param feature 审计记录
     */
    public void refresh(Feature feature) {
        try {
            if (feature!=null){
                id = feature.getId();
                AuditAdapter adapter = new AuditAdapter(getActivity(), feature.getAttributes(), auditViewModel);
                binding.auditInfoList.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e("tag", "activity:" + getContext() + e);
            //ToastUtil.setToast(getContext(),"获取数据异常"+e);
        }

    }

    public void showEditDialog(String alias, String value) {
        dialog = AuditInfoEditDialog.newInstance(alias, value);
        dialog.show(getFragmentManager(), "edit_value_dialog");
        dialog.setViewModel(auditViewModel);
    }

    @Override
    public void closeDialog() {
        dialog.dismiss();
    }

    @Override
    public void sure(String name, String value) {
        MyLayer myLayer = AuditHistoryActivity.myLayer;
        save(myLayer.getTable(), name, value);
        dialog.dismiss();
    }

    /**
     * 编辑之后保存数据
     *
     * @param table 编辑表
     */
    public void save(FeatureTable table, String name, String value) {
        Map<String, Object> map = null;
        try {
            map = table.getFeature(id).getAttributes();
            List<Field> list = table.getFields();
            for (int i = 0; i < list.size(); i++) {
                if (name.equals(list.get(i).getName())) {
                    map.put(list.get(i).getName(), value);
                }
            }
        } catch (TableException e) {
            e.printStackTrace();
        }
        Graphic graphic = new Graphic(null, null, map);
        try {
            table.updateFeature(id, graphic);
            refresh(table.getFeature(id));
            ToastUtil.setToast(getActivity(), "数据保存成功");
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(getActivity(), "数据保存失败");
        }
    }
}
