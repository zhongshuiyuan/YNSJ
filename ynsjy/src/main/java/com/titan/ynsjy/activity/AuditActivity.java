package com.titan.ynsjy.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.query.Order;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.AuditAdapter;
import com.titan.ynsjy.dialog.EditPhoto;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.ArcGISQueryUtils;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.UtilTime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static android.R.attr.id;
import static com.titan.ynsjy.edite.activity.BaseEditActivity.TAKE_PICTURE;
import static com.titan.ynsjy.edite.activity.XbEditActivity.getPicName;

/**
 * Created by hanyw on 2017/9/2/002.
 * 审计
 */
@RuntimePermissions
public class AuditActivity extends AppCompatActivity {
    /**新增审计*/
    EditText auditReason;
    EditText auditInfo;
    EditText auditEditBefore;
    EditText auditEditAfter;
    EditText auditMark;
    /**审计对比*/
    EditText auditReason2;
    EditText auditInfo2;
    EditText auditEditBefore2;
    EditText auditEditAfter2;
    EditText auditMark2;
    @BindView(R.id.audit_pic_browse)//图片浏览
    TextView auditPicBrowse;
    @BindView(R.id.audit_take_pic)//拍照
    TextView auditTakePic;
    @BindView(R.id.audit_sure)//确定
    TextView auditSure;
    @BindView(R.id.audit_cancel)//取消
    TextView auditCancel;
    @BindView(R.id.audit_title)//审计标题
    TextView auditTitle;
    @BindView(R.id.audit_history)//审计历史
    TextView auditHistory;
//    @BindView(R.id.audit_add_list)
//    ListView auditAddList;

    private Context mContext;
    private View view;
    private View addView;
    private View compareView;
    private Feature feature;//小班
    private FeatureTable featureTable;
    private MyLayer myLayer;
    private long fid;//原始数据小班id
    private String picPath;//图片文件夹地址
    private long newId;//新增小班id
    private String currentxbh = "";//小班唯一号
    private String imagePath = "";//图片地址
    private boolean auditType = false;
    //private List<Feature> featureList;
    private List<Feature> historyList;
    private List<Feature> selectList = new ArrayList<>();
    private Map<String,Boolean> auditCheckMap = new HashMap<>();
    private static final int QUERY_FINISH = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FINISH:
                    showAuditHistoryDialog();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = AuditActivity.this;
        view = LayoutInflater.from(this).inflate(R.layout.dialog_audit, null);
        setContentView(view);
        ButterKnife.bind(this);
        getData();
        initView();
        setMyVisibility(auditType);
        if (auditType){
            queryAuditHistory();
        }
    }

    private void setMyVisibility(boolean auditType) {
        if (auditType) {
            compareView.setVisibility(View.VISIBLE);
            auditPicBrowse.setVisibility(View.GONE);
            auditTakePic.setVisibility(View.GONE);
            auditSure.setVisibility(View.GONE);
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AuditActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    private void initView() {
        addView = view.findViewById(R.id.audit_add);
        compareView = view.findViewById(R.id.audit_compare);
        auditReason = (EditText) addView.findViewById(R.id.audit_reason);
        auditInfo = (EditText) addView.findViewById(R.id.audit_info);
        auditEditBefore = (EditText) addView.findViewById(R.id.audit_edit_before);
        auditEditAfter = (EditText) addView.findViewById(R.id.audit_edit_after);
        auditMark = (EditText) addView.findViewById(R.id.audit_mark);
        auditReason2 = (EditText) compareView.findViewById(R.id.audit_reason);
        auditInfo2 = (EditText) compareView.findViewById(R.id.audit_info);
        auditEditBefore2 = (EditText) compareView.findViewById(R.id.audit_edit_before);
        auditEditAfter2 = (EditText) compareView.findViewById(R.id.audit_edit_after);
        auditMark2 = (EditText) compareView.findViewById(R.id.audit_mark);

        if (auditType){
            auditReason.setEnabled(false);
            auditInfo.setEnabled(false);
            auditEditBefore.setEnabled(false);
            auditEditAfter.setEnabled(false);
            auditMark.setEnabled(false);

            auditReason2.setEnabled(false);
            auditInfo2.setEnabled(false);
            auditEditBefore2.setEnabled(false);
            auditEditAfter2.setEnabled(false);
            auditMark2.setEnabled(false);
        }
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel, R.id.audit_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_pic_browse:
                lookpictures(this);
                break;
            case R.id.audit_take_pic:
                //photograph();
                AuditActivityPermissionsDispatcher.photographWithCheck(this);
                break;
            case R.id.audit_sure:
                saveData();
                break;
            case R.id.audit_cancel:
                this.finish();
//                if (auditType){
//
//                }else {
//                    delAuditData();
//                }
                break;
            case R.id.audit_history:
                queryAuditHistory();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            EditPhoto dialog = new EditPhoto(mContext, imagePath, null, fid);
            dialog.show();
        }
    }

    public void showAuditHistoryDialog() {
        try {
            final Dialog dialog = new Dialog(mContext, R.style.Dialog);
            dialog.setContentView(R.layout.audit_history_choice);
            ListView choiceView = (ListView) dialog.findViewById(R.id.audit_choice_list);
            TextView btn_sure = (TextView) dialog.findViewById(R.id.audit_choice_sure);
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> map = selectList.get(0).getAttributes();
                    auditReason.setText(getAttrValue(map,"MODIFYINFO"));
                    auditInfo.setText(getAttrValue(map,"INFO"));
                    auditEditBefore.setText(getAttrValue(map,"BEFOREINFO"));
                    auditEditAfter.setText(getAttrValue(map,"AFTERINFO"));
                    auditMark.setText(getAttrValue(map,"REMARK"));
                    Log.e("tag",":"+map);
                    if (selectList.size()==2){
                        Map<String, Object> map2 = selectList.get(1).getAttributes();
                        auditReason2.setText(getAttrValue(map2,"MODIFYINFO"));
                        auditInfo2.setText(getAttrValue(map2,"INFO"));
                        auditEditBefore2.setText(getAttrValue(map2,"BEFOREINFO"));
                        auditEditAfter2.setText(getAttrValue(map2,"AFTERINFO"));
                        auditMark2.setText(getAttrValue(map2,"REMARK"));
                    }
                    dialog.dismiss();
                }
            });
            if (historyList.size()<=0){
                Toast.makeText(mContext, "没有历史记录",Toast.LENGTH_SHORT).show();
                return;
            }
            final AuditAdapter adapter = new AuditAdapter(mContext, historyList,auditCheckMap);
            choiceView.setAdapter(adapter);

            selectList.clear();
            choiceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String featureId = String.valueOf(historyList.get(position).getId());
                    if (auditCheckMap.get(featureId)){
                        auditCheckMap.put(featureId,false);
                        selectList.remove((historyList.get(position)));
                    }else if (selectList.size() < 2) {
                        selectList.add(historyList.get(position));
                        auditCheckMap.put(featureId,true);
                    } else {
                        Feature s = selectList.get(0);
                        selectList.remove(s);
                        auditCheckMap.put(String.valueOf(s.getId()),false);
                        selectList.add(historyList.get(position));
                        auditCheckMap.put(featureId,true);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            dialog.show();
        }catch (Exception e){
            ToastUtil.setToast(mContext,"数据获取失败");
        }
    }

    private String getAttrValue(Map<String, Object> map,String attr){
        String value = map.get(attr).toString();
        if (value.isEmpty()||value.equals("")){
            value = "空";
        }
        return value;
    }
    /**
     * 图片浏览
     */
    public void lookpictures(Activity activity) {
        List<String> lst = ResourcesManager.getImagesFiles(picPath,"id"+fid+"_"); //getImages(picPath);
        if(lst == null||lst.size()==0){
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, PicSampActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("picPath", picPath);
        startActivity(intent);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    void photograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = picPath + "/" + getPicName(String.valueOf(id));
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void showRecordDenied(){
        ToastUtil.setToast(mContext,"拒绝后将无法da打开相机，您可以在手机中手动授予权限");
    }

    /**
     * 获取修改表
     */
    private void getData() {
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
        featureTable = myLayer.getTable();
        Intent intent = getIntent();
        if (intent != null) {
            fid = intent.getLongExtra("fid", 0);
            picPath = intent.getStringExtra("picPath");
            auditType = intent.getBooleanExtra("auditType", false);
        }
    }

    /**
     * 删除修改记录
     */
    private void delAuditData() {
        try {
            featureTable.deleteFeature(newId);
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存数据
     */
    private void saveData() {
        createFeature();
        setData();
        upEditLayerData();
    }

    /**
     * 新建数据
     */
    private void createFeature() {
        Graphic g = new Graphic(null, null);
        try {
            newId = featureTable.addFeature(g);
            feature = featureTable.getFeature(newId);
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 修改的数据集
     */
    private Map<String, Object> setData() {
        Map<String, Object> map = new HashMap<>();
        map.put("FK_EDIT_UID", id);
        map.put("MODIFYINFO", auditReason.getText().toString());
        map.put("MODIFYTIME", UtilTime.getSystemtime2());
        map.put("BEFOREINFO", auditEditBefore.getText().toString());
        map.put("AFTERINFO", auditEditAfter.getText().toString());
        map.put("REMARK", auditMark.getText().toString());
        map.put("INFO", auditInfo.getText().toString());
        return map;
    }

    /**
     * 更新数据
     */
    private void upEditLayerData() {
        Graphic graphic = new Graphic(null, null, setData());
        try {
            featureTable.updateFeature(newId, graphic);
            ToastUtil.setToast(mContext,"数据保存成功");
            this.finish();
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(mContext,"数据保存失败");
        }
    }

    public void queryAuditHistory(){
        ArcGISQueryUtils.getQueryFeatures(featureTable,"MODIFYTIME",Order.ASC,new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                if (objects.featureCount() <= 0) {
                    ToastUtil.setToast(mContext, "没有查询到数据");
                    return;
                }
                historyList = new ArrayList<>();
                for (Object object : objects) {
                    Feature feature = (Feature) object;
                    historyList.add(feature);
                    auditCheckMap.put(String.valueOf(feature.getId()),false);
                }
                Message message = new Message();
                message.what = QUERY_FINISH;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.setToast(mContext, "没有查询到数据");
            }
        });
    }
}
