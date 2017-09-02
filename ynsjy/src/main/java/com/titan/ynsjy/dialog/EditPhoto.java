package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.entity.ScreenTool;
import com.titan.ynsjy.mview.IUpLayerData;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.UtilTime;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanyw on 2017/7/14/014.
 * 图片编辑
 */

public class EditPhoto extends Dialog implements View.OnClickListener {
    private View view;
    private Context mContext;
    private String photoPath;
    private ImageView img_photo;
    private Bitmap photo;//照片
    private EditText ed_address;//地址
    private EditText ed_infor;//信息
    private EditText ed_mark;//备注
    private int labelTextSize;//标注文字大小
    private Long fk_Fxh_Uid;//对应数据ID
    private Long fk_Edit_Uid;//对应数据修改id
    private IUpLayerData listener;//图层更新回调监听

    public EditPhoto(Context context, String path, Long fk_Fxh_Uid,Long fk_Edit_Uid) {
        super(context);
        this.mContext = context;
        this.photoPath = path;
        this.fk_Fxh_Uid = fk_Fxh_Uid;
        this.fk_Edit_Uid = fk_Edit_Uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(mContext).inflate(R.layout.photo_edit, null);
        setContentView(view);
        initView();
        //setPhoto(photoPath);
        labelTextSize = convertDpToPixel(20, mContext);
        setTimeLabel(labelTextSize);
    }

    /*设置图片*/
    private void setPhoto(String photoPath) {
        photo = BitmapFactory.decodeFile(photoPath);
        img_photo.setImageBitmap(photo);
    }

    /*布局初始化*/
    private void initView() {
        img_photo = (ImageView) view.findViewById(R.id.showimage);//图片
        ed_address = (EditText) view.findViewById(R.id.photo_edit_address);//地址
        ed_infor = (EditText) view.findViewById(R.id.photo_edit_info);//描述信息--水印
        ed_mark = (EditText) view.findViewById(R.id.photo_edit_remark);//备注
        Button btn_time = (Button) view.findViewById(R.id.timebz);//添加时间--水印
        Button btn_sure = (Button) view.findViewById(R.id.sure);//确定
        Button img_close = (Button) view.findViewById(R.id.photo_edit_close);//关闭
        img_close.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        ed_address.setOnClickListener(this);
        ed_infor.setOnClickListener(this);
        ed_mark.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int textSize = convertDpToPixel(20, mContext);
        switch (v.getId()) {
            case R.id.sure://确定
                deletFile(photoPath, mContext);
                sure(ed_infor, photo.getHeight() - textSize, textSize);
                saveBitmap(photoPath, photo);
                savePhotoInfo();
                listener.upLayerData();
                dismiss();
                break;
            case R.id.photo_edit_close://返回
                deletFile(photoPath, mContext);
                dismiss();
                break;
        }
    }

    private void savePhotoInfo() {
        createFeature();
    }

    private void createFeature() {
        MyLayer myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("photo", BaseActivity.layerNameList);
        FeatureTable featureTable = myLayer.getTable();
        Graphic g = new Graphic(null, null,setPhotoInfo());
        try {
            long newId = featureTable.addFeature(g);
            featureTable.updateFeature(newId,g);
        } catch (TableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (view.getVisibility() == View.VISIBLE && keyCode == KeyEvent.KEYCODE_BACK) {
            deletFile(photoPath, mContext);
            dismiss();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /**
     * 添加数据
     */
    private Map<String, Object> setPhotoInfo() {
        Map<String, Object> map = new HashMap<>();
        //map.put("PK_UID", fk_Fxh_Uid);
        map.put("FK_FXH_UID", fk_Fxh_Uid);
        map.put("TIME", UtilTime.getSystemtime2());
        map.put("INFO", ed_infor.getText().toString());
        map.put("REMARK", ed_mark.getText().toString());
        map.put("ADDEESS", ed_address.getText().toString());
        map.put("URI", "123");//photoPath
        map.put("FK_EDITID",fk_Edit_Uid);
        map.put("ZPBH",getPicName(photoPath));
        return map;
    }

    private String getPicName(String path) {
        String picName = StringUtils.substringAfterLast(path,File.separator);
        return StringUtils.substringBefore(picName,".");
    }

    private void setTimeLabel(int textSize) {
        photo = BitmapFactory.decodeFile(photoPath);
        photo = myDrawText(UtilTime.getSystemtime2(), photo, photo.getHeight() - 3 * textSize, textSize);
        img_photo.setImageBitmap(photo);
    }

    /*删除图片*/
    private void deletFile(String filePath, Context context) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        file.delete();
        //删除文件后刷新手机媒体库
        context.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA + "='" + filePath + "'", null
        );
    }

    /*保存图片*/
    public void saveBitmap(String path, Bitmap bm) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*确定*/
    private void sure(EditText editText, int paddingTop, int size) {
        String infor = editText.getText().toString();
        photo = myDrawText(infor, photo, paddingTop, size);
        img_photo.setImageBitmap(photo);
    }

    public void setUpLayerDataListener(IUpLayerData listener) {
        this.listener = listener;
    }

    /*画笔设定*/
    private Bitmap myDrawText(String text, Bitmap bitmap, int paddingTop, int size) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(size);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(bitmap, paint, text, (bitmap.getWidth() - bounds.width()) / 2, paddingTop);
    }

    /*画文字*/
    private Bitmap drawTextToBitmap(Bitmap bitmap, Paint paint, String text, int paddingLeft, int paddingTop) {
        Bitmap.Config config = bitmap.getConfig();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(config, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /*将dp转为px*/
    private int convertDpToPixel(int dp, Context context) {
        ScreenTool.Screen screen = ScreenTool.getScreenPix(context);
        int width = screen.getWidthPixels();
        int height = screen.getHeightPixels();
        double ppi = Math.sqrt((width * width + height * height) / 9);
        return Double.valueOf(dp * ppi / 160).intValue();
    }
}
