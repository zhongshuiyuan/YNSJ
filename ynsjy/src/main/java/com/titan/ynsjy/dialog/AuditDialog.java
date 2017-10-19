/*
package com.titan.ynsjy.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.edite.activity.ImageActivity;
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
import butterknife.Unbinder;

import static com.titan.ynsjy.edite.activity.BaseEditActivity.TAKE_PICTURE;

*/
/**
 * Created by hanyw on 2017/9/1/001.
 * 新增审计页面
 *//*


public class AuditDialog extends DialogFragment{
    @BindView(R.id.audit_reason)
    EditText auditReason;//修改原因
    @BindView(R.id.audit_info)
    EditText auditInfo;//描述信息
    @BindView(R.id.audit_edit_before)
    EditText auditEditBefore;//修改之前
    @BindView(R.id.audit_edit_after)
    EditText auditEditAfter;//修改之后
    @BindView(R.id.audit_mark)
    EditText auditMark;//备注
    @BindView(R.id.audit_pic_browse)
    TextView auditPicBrowse;//图片浏览
    @BindView(R.id.audit_take_pic)
    TextView auditTakePic;//拍照
    @BindView(R.id.audit_sure)
    TextView auditSure;//确定
    @BindView(R.id.audit_cancel)
    TextView auditCancel;//取消
    Unbinder unbinder;
    private View view;
    private Context mContext;
    private Feature feature;//小班
    private FeatureTable featureTable;
    private MyLayer myLayer;
    private long id;//原始数据小班id
    private String picPath;//图片文件夹地址
    private long newId;//新增小班id
    private String currentxbh = "";//小班唯一号
    private String imagePath = "";//图片地址

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_audit, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.mContext = getActivity();
        getData();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_pic_browse:
                lookpictures(getActivity());
                break;
            case R.id.audit_take_pic:
                photograph();
                break;
            case R.id.audit_sure:
                saveData();
                break;
            case R.id.audit_cancel:
                delAuditData();
                dismiss();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK){
            EditPhoto dialog = new EditPhoto(mContext,imagePath,null,id);
            dialog.show();
        }
    }

    */
/**
     * 拍照
     *//*

    private void photograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = picPath+"/"+ ResourcesManager.getPicName(String.valueOf(id));
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    */
/** 图片浏览*//*

    public void lookpictures(Activity activity){
        List<File> lst = MyApplication.resourcesManager.getImages(picPath);
        if(lst.size() == 0){
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("xbh", currentxbh);
        intent.putExtra("picPath", picPath);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    */
/**
     * 删除修改记录
     *//*

    private void delAuditData() {
        try {
            featureTable.deleteFeature(newId);
        } catch (TableException e) {
            e.printStackTrace();
        }
    }


    */
/**
     * 获取修改表
     *//*

    private void getData() {
        myLayer = BaseUtil.getIntance(getActivity()).getFeatureInLayer("edit", BaseActivity.layerNameList);
        featureTable = myLayer.getTable();
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getLong("id");
            picPath = bundle.getString("picPath");
            currentxbh = bundle.getString("currentxbh");
        }
    }

    */
/**
     * 保存数据
     *//*

    private void saveData() {
        createFeature();
        setData();
        upEditLayerData();
    }

    */
/**
     * 新建数据
     *//*

    private void createFeature() {
        Graphic g = new Graphic(null, null);
        try {
            newId = featureTable.addFeature(g);
            feature = featureTable.getFeature(newId);
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * @return 修改的数据集
     *//*

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

    */
/**
     * 更新数据
     *//*

    private void upEditLayerData() {
        Graphic graphic = new Graphic(null, null, setData());
        try {
            featureTable.updateFeature(newId, graphic);
        } catch (TableException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
*/
