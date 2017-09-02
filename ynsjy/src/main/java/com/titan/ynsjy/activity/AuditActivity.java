package com.titan.ynsjy.activity;

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
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
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
    @BindView(R.id.audit_pic_browse)
    TextView auditPicBrowse;
    @BindView(R.id.audit_take_pic)
    TextView auditTakePic;
    @BindView(R.id.audit_sure)
    TextView auditSure;
    @BindView(R.id.audit_cancel)
    TextView auditCancel;
    @BindView(R.id.audit_title)
    TextView auditTitle;
    @BindView(R.id.audit_add_list)
    ListView auditAddList;

    private Context mContext;
    private Feature feature;//小班
    private FeatureTable featureTable;
    private MyLayer myLayer;
    private long id;//原始数据小班id
    private String picPath;//图片文件夹地址
    private long newId;//新增小班id
    private String currentxbh = "";//小班唯一号
    private String imagePath = "";//图片地址
    private boolean auditType = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_audit);
        ButterKnife.bind(this);
        getData();
        this.mContext = AuditActivity.this;
        AuditAdapter adapter = new AuditAdapter(this,feature.getAttributes());
        auditAddList.setAdapter(adapter);
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel})
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
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getLongExtra("id", 0);
            picPath = intent.getStringExtra("picPath");
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
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void upLayerData() {
        Log.e("tag", "1asde1");
    }
}
