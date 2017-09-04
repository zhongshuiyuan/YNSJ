package com.titan.ynsjy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.AuditAdapter;
import com.titan.ynsjy.dialog.EditPhoto;
import com.titan.ynsjy.edite.activity.ImageActivity;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.mview.IUpLayerData;
import com.titan.ynsjy.util.BaseUtil;
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

import static com.titan.ynsjy.edite.activity.BaseEditActivity.TAKE_PICTURE;
import static com.titan.ynsjy.edite.activity.XbEditActivity.getPicName;

/**
 * Created by hanyw on 2017/9/2/002.
 * 审计
 */

public class AuditActivity extends AppCompatActivity implements IUpLayerData {
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
    private long id;//原始数据小班id
    private String picPath;//图片文件夹地址
    private long newId;//新增小班id
    private String currentxbh = "";//小班唯一号
    private String imagePath = "";//图片地址
    private boolean auditType = false;
    private List<Feature> featureList;
    private List<String> historyList;
    private List<Feature> selectList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.dialog_audit, null);
        setContentView(view);
        ButterKnife.bind(this);
        initView();
        getData();
        setMyVisibility(auditType);
        this.mContext = AuditActivity.this;
    }

    private void setMyVisibility(boolean auditType) {
        if (auditType) {
            compareView.setVisibility(View.VISIBLE);
            auditPicBrowse.setVisibility(View.GONE);
            auditTakePic.setVisibility(View.GONE);
            auditSure.setVisibility(View.GONE);
        }
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
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel, R.id.audit_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_pic_browse:
                lookpictures(this);
                break;
            case R.id.audit_take_pic:
                photograph();
                break;
            case R.id.audit_sure:
                saveData();
                break;
            case R.id.audit_cancel:
                delAuditData();
                break;
            case R.id.audit_history:
                queryAuditHistory();
                //showAuditHistoryDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            EditPhoto dialog = new EditPhoto(mContext, imagePath, null, id);
            dialog.setUpLayerDataListener(this);
            dialog.show();
        }
    }

    public void showAuditHistoryDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.audit_history_choice);
        dialog.setCanceledOnTouchOutside(true);
        ListView choiceView = (ListView) dialog.findViewById(R.id.audit_choice_list);
        TextView btn_sure = (TextView) dialog.findViewById(R.id.audit_choice_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = selectList.get(0).getAttributes();
                Map<String, Object> map2 = selectList.get(1).getAttributes();
                auditReason.setText(map.get("MODIFYINFO").toString());
                auditInfo.setText(map.get("INFO").toString());
                auditEditBefore.setText(map.get("BEFOREINFO").toString());
                auditEditAfter.setText(map.get("AFTERINFO").toString());
                auditMark.setText(map.get("REMARK").toString());
                auditReason.setEnabled(false);
                auditInfo.setEnabled(false);
                auditEditBefore.setEnabled(false);
                auditEditAfter.setEnabled(false);
                auditMark.setEnabled(false);
                auditReason2.setText(map2.get("MODIFYINFO").toString());
                auditInfo2.setText(map2.get("INFO").toString());
                auditEditBefore2.setText(map2.get("BEFOREINFO").toString());
                auditEditAfter2.setText(map2.get("AFTERINFO").toString());
                auditMark2.setText(map2.get("REMARK").toString());
                auditReason2.setEnabled(false);
                auditInfo2.setEnabled(false);
                auditEditBefore2.setEnabled(false);
                auditEditAfter2.setEnabled(false);
                auditMark2.setEnabled(false);
                dialog.dismiss();
            }
        });

        if (historyList.size()<=0){
            ToastUtil.setToast(mContext,"没有历史记录");
            return;
        }
        AuditAdapter adapter = new AuditAdapter(mContext, historyList);
        choiceView.setAdapter(adapter);

        choiceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectList.size() < 2) {
                    selectList.add(featureList.get(position));
                } else {
                    selectList.remove(0);
                    selectList.add(featureList.get(position));
                }

            }
        });
        dialog.show();
    }

    /**
     * 图片浏览
     */
    public void lookpictures(Activity activity) {
        List<File> lst = MyApplication.resourcesManager.getImages(picPath);
        if (lst.size() == 0) {
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("xbh", currentxbh);
        intent.putExtra("picPath", picPath);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    private void photograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = picPath + "/" + getPicName(String.valueOf(id));
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /**
     * 获取修改表
     */
    private void getData() {
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
        featureTable = myLayer.getTable();
        BaseActivity.selGeoFeature.getAttributeValue("MODIFYTIME");
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getLongExtra("id", 0);
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
            Log.e("tag",featureTable.getFeature(newId).toString());
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void upLayerData() {
        Log.e("tag", "1asde1");
    }

    public void queryAuditHistory() {
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(featureTable.getExtent());
        queryParams.setReturnGeometry(true);
        queryParams.setWhere("1=1");
        featureTable.queryFeatures(queryParams, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {
                if (objects.featureCount() <= 0) {
                    ToastUtil.setToast(mContext, "没有查询到数据");
                    return;
                }
                historyList = new ArrayList<>();
                featureList = new ArrayList<>();
                for (Object object : objects) {
                    Feature feature = (Feature) object;
                    featureList.add(feature);
                    historyList.add(feature.getAttributeValue("MODIFYTIME").toString());
                }
                Log.e("tag","list:"+historyList.toString());
                showAuditHistoryDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.setToast(mContext, "没有查询到数据");
            }
        });
    }
}
