package com.titan.ynsjy.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.util.Camera.CameraActivity;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.dialog.EditPhoto;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.UtilTime;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by hanyw on 2017/9/2/002.
 * 新增审计
 */
@RuntimePermissions
public class AuditActivity extends AppCompatActivity {
    /**
     * 新增审计
     */
    @BindView(R.id.audit_pic_browse)//图片浏览
            TextView auditPicBrowse;
    @BindView(R.id.audit_take_pic)//拍照
            TextView auditTakePic;
    @BindView(R.id.audit_sure)//确定
            TextView auditSure;
    @BindView(R.id.audit_cancel)//取消
            TextView auditCancel;
    @BindView(R.id.audit_people)
    EditText auditPeople;
    @BindView(R.id.audit_reason)
    EditText auditReason;
    @BindView(R.id.audit_info)
    EditText auditInfo;
    @BindView(R.id.audit_edit_before)
    EditText auditEditBefore;
    @BindView(R.id.audit_edit_after)
    EditText auditEditAfter;
    @BindView(R.id.audit_mark)
    EditText auditMark;
    @BindView(R.id.fragment_videotape)
    TextView fragmentVideotape;

    private Context mContext;
    private View compareView;
    private Feature feature;//小班
    private FeatureTable featureTable;
    private MyLayer myLayer;
    private long fid;//原始数据小班id
    private String picPath;//图片文件夹地址
    private long newId;//新增小班id
    private String imagePath = "";//图片地址
    private boolean auditType = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = AuditActivity.this;
        setContentView(R.layout.dialog_audit);
        ButterKnife.bind(this);
        getData();
        setMyVisibility(auditType);
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
        AuditActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel,R.id.fragment_videotape})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_pic_browse:
                lookpictures(this);
                break;
            case R.id.audit_take_pic:
                AuditActivityPermissionsDispatcher.photographWithCheck(this);
                break;
            case R.id.audit_sure:
                saveData();
                break;
            case R.id.audit_cancel:
                this.finish();
                break;
            case R.id.fragment_videotape:
                videotape();
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

    /**
     * 录像
     */
    private void videotape() {
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(ResourcesManager.getImagePath(picPath) + "/" + ResourcesManager.getVideoName(String.valueOf(fid)));
        Log.e("tag", "path" + ResourcesManager.getImagePath(picPath) + "/" + ResourcesManager.getVideoName(String.valueOf(fid)));
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivity(intent);
    }

    /**
     * 图片浏览
     */
    public void lookpictures(Activity activity) {
        List<String> lst = ResourcesManager.getImagesFiles(picPath, String.valueOf(fid)); //getImages(picPath);
        if (lst == null || lst.size() == 0) {
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, PicSampActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("picPath", picPath);
        startActivity(intent);
    }

    /**
     * 拍照
     */
    @NeedsPermission({Manifest.permission.CAMERA})
    void photograph() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = picPath + "/" + ResourcesManager.getPicName(String.valueOf(fid));
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PICTURE);*/
        Intent intent=new Intent(AuditActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void showRecordDenied() {
        ToastUtil.setToast(mContext, "拒绝后将无法da打开相机，您可以在手机中手动授予权限");
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
        map.put("FK_EDIT_UID", fid);
        map.put("AUDIT_PEOPLE", auditPeople.getText().toString());
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
            ToastUtil.setToast(mContext, "数据保存成功");
            this.finish();
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(mContext, "数据保存失败");
        }
    }

}
