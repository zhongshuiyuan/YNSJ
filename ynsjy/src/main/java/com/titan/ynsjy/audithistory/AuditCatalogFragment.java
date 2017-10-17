package com.titan.ynsjy.audithistory;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.titan.model.AuditInfo;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.AuditHistoryExpandAdapter;
import com.titan.ynsjy.databinding.AuditHistoryCatalogBinding;
import com.titan.ynsjy.util.ArcGISQueryUtils;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by hanyw on 2017/9/7/007
 * 审计历史记录目录界面
 */

public class AuditCatalogFragment extends Fragment implements AuditHistory {
    private AuditHistoryCatalogBinding binding;

    private AuditHistoryViewModel auditViewModel;
    private ExpandableListView exListView;
    private AuditHistoryExpandAdapter adapter;
    //private MyLayer myLayer;
    private int type;//模式设定 0：单选，1：多选

    public List<Map<String, Object>> getSelectList() {
        return selectList;
    }

    private List<Map<String, Object>> selectList = new ArrayList<>();//已选择的历史记录
    private Map<String, Object> attrMap;//选择的审计记录属性集合
    private boolean isTwoPane;//是否双页
    private List<Feature> featureList;//审计记录列表
    private List<List<Feature>> childList;//编辑id对应图斑集合
    private List<String> fk_uidList;//编辑id列表
    private static final int QUERY_FINISH = 1;//查询完成
    private static final int QUERY_NODATA = 2;//没有查询到数据
    private Map<String, Boolean> cbMap;//checkbox状态
    AuditCatalogFragment catalogFragment;//所有审计历史记录显示页面
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FINISH:
                    exRefresh(getActivity(), fk_uidList, childList, cbMap, 0);
                    break;
                case QUERY_NODATA:
                    ToastUtil.setToast(getActivity(), "没有查询到数据");
                    break;
                default:
                    break;
            }
        }
    };
    //存储选择的审计信息
    private List<AuditInfo> auditInfos;

    public static AuditCatalogFragment singleton;

    private static GeodatabaseFeatureTable audittable;


    public static AuditCatalogFragment newInstance(GeodatabaseFeatureTable featureTable) {

        if (singleton == null) {
            audittable=featureTable;
            singleton = new AuditCatalogFragment();
        }
        return singleton;
    }

    public interface onRefreshDetial {
        //刷新
        void onRefreshDetial(Map<String, Object> map, boolean flag, Feature feature);

        //比较
        void onShowCompare(List<Map<String, Object>> selectList, Map<String, Object> map);
    }


    private onRefreshDetial onRefresh;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = AuditHistoryCatalogBinding.inflate(inflater,container,false);
        binding.setViewmodel(auditViewModel);
        init();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onRefresh = (onRefreshDetial) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void setViewModel(AuditHistoryViewModel viewModel) {
        auditViewModel = viewModel;
    }

    private void init() {
        try{
            //getData();
            queryData();
        }catch(Exception e) {
            ToastUtil.setToast(getActivity(),"初始化数据异常"+e);
        }


    }


    /**
     * 查询审计记录
     */
    public void queryData() {

        ArcGISQueryUtils.getQueryFeaturesAll(audittable, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                Message message = new Message();
                long size = objects.featureCount();
                if (size <= 0) {
                    message.what = QUERY_NODATA;
                    handler.sendMessage(message);
                    return;
                }
                featureList = new ArrayList<>();
                cbMap = new HashMap<>();
                for (Object object : objects) {
                    Feature feature = (Feature) object;
                    featureList.add(feature);
                    cbMap.put(String.valueOf(feature.getId()), false);
                }
                childList = featureSort();
                message.what = QUERY_FINISH;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.setToast(getActivity(), "数据查询出错");
            }
        });
    }

    /**
     * @return 按照编辑id对应分类好的map集合
     */
    private List<List<Feature>> featureSort() {
        try{

            List<List<Feature>> list = new ArrayList<>();
            List<Feature> cList;
            fk_uidList = new ArrayList<>();
            //按原始数据ID分组
            for (Feature f : featureList) {
                if(f.getAttributeValue("FK_EDIT_UID")==null||f.getAttributeValue("FK_EDIT_UID").equals(""))
                    continue;
                String fk_uid = f.getAttributeValue("FK_EDIT_UID").toString();
                if (fk_uidList.contains(fk_uid)) {
                    continue;
                }
                fk_uidList.add(fk_uid);
            }

            for (String fk_uid : fk_uidList) {
                cList = new ArrayList<>();
                List<Feature> tempList = new ArrayList<>();
                for (Feature f : featureList) {
                    if(f.getAttributeValue("FK_EDIT_UID")==null||f.getAttributeValue("FK_EDIT_UID").equals(""))
                        continue;
                    if (f.getAttributeValue("FK_EDIT_UID").toString().equals(fk_uid)) {
                        cList.add(f);
                        tempList.add(f);
                    }
                }
                featureList.removeAll(tempList);
                list.add(cList);
            }
            return list;
        }catch(Exception e) {
            ToastUtil.setToast(getActivity(),"解析数据异常"+e);
            return  null;
        }


    }

    /**
     * 多选模式
     */
    public void multiSelect(int mode){
        selectList.clear();
        for (String key:cbMap.keySet()) {
            cbMap.put(key,false);
        }
        modeChoice(mode);
    }

    /**
     * 全选
     */
    public void allSelect(){
        //modeChoice(1);

        for (String key:cbMap.keySet()) {
            cbMap.put(key,true);
        }
        if(fk_uidList!=null&& fk_uidList.size()>0){
            exRefresh(getActivity(), fk_uidList, childList, cbMap, 1);
        }

    }

    /**
     * @param mode 模式选择，true为比较模式，false为默认模式
     */
    public void modeChoice(int mode) {
        type = mode;
        exRefresh(getActivity(), fk_uidList, childList, cbMap, mode);
    }

    /**
     * @param context 上下文
     * @param list    编辑id
     * @param cbMap   checkbox状态
     * @param type    页面显示状态，1:全选，true为比较模式
     */
    public void exRefresh(Context context, final List<String> list, final List<List<Feature>> listfeature,
                          final Map<String, Boolean> cbMap, final int type) {
        exListView = binding.auditHistoryExlist;
        adapter = new AuditHistoryExpandAdapter(context, list, type, listfeature, cbMap);

        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                LinearLayout layout = (LinearLayout) v;
                CheckBox checkBox = (CheckBox) layout.findViewById(R.id.audit_item_check);
                checkBox.toggle();

                Feature feature;
                try {
                    feature = audittable.getFeature(listfeature.get(groupPosition).get(childPosition).getId());
                    attrMap = feature.getAttributes();
                } catch (Exception e) {
                    ToastUtil.setToast(getActivity(),"数据读取出错："+e);
                    //e.printStackTrace();
                    return false;
                }
                //选择布局模式
//                if (type == 0) {
                    //setLayout(attrMap);
//                } else {
                    //审计历史记录比较时双选
                    String featureId = String.valueOf(feature.getId());
                    Feature selFeature = listfeature.get(groupPosition).get(childPosition);

                    if (cbMap.get(featureId)) {
                        cbMap.put(featureId, false);
                        selectList.remove(selFeature.getAttributes());
                    } else {
                        //多选模式
                        selectList.add(selFeature.getAttributes());
                        cbMap.put(featureId, true);
                    }
                    adapter.notifyDataSetChanged();
//                }
                if (selectList.size()>=1){
                    setLayout(selectList.get(selectList.size()-1),true,selFeature);
                }else {
                    setLayout(null,false,selFeature);
                }
                return false;
            }
        });

        exListView.setAdapter(adapter);
        if (type==1){
            //全选
            selectList.clear();
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                exListView.expandGroup(i,true);
            }
            for(Feature feature:featureList) {
                selectList.add(feature.getAttributes());
            }
        }
    }



    /**
     * @param map 选择的审计记录
     */
    public void setLayout(Map<String, Object> map,boolean flag,Feature feature) {
        if (isTwoPane) {
            onRefresh.onRefreshDetial(map,flag,feature);
            //fragment.editMode(false);
        } /*else {
            AuditInfoActivity.actionStart(getActivity(), map);
        }*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTwoPane = getActivity().findViewById(R.id.audit_detail_frame) != null;
    }

    /**
     * 确定比较，选择小班
     */
    @OnClick({R.id.audit_catalog_sure, R.id.audit_multi_select, R.id.audit_all_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_catalog_sure:
                if (type == 1) {
                    if (selectList.size() == 2) {
                        onRefresh.onShowCompare(selectList, attrMap);
                        //showCompare(attrMap);
                        return;
                    }
                    ToastUtil.setToast(getActivity(), "请选择两个记录");
                } /*else if (type == 2) {
                    //AuditHistoryActivity historyActivity = (AuditHistoryActivity) getActivity();
                    //historyActivity.exportFile();
                }*/
                break;
            case R.id.audit_multi_select:
                break;
            case R.id.audit_all_select:

                break;
            default:
                break;
        }

    }
}
