package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.RecyclerViewAdapter_logpic;
import com.titan.ynsjy.entity.Image;
import com.titan.ynsjy.entity.PicUp;
import com.titan.ynsjy.service.RetrofitHelper;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.Util;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2017/7/15.
 * 图片上传
 */

public class PicUpDialog extends Dialog {


    private BaseActivity activity;
    private TextView viewLon,viewLat,viewBtn;
    private EditText viewMiaoshu,viewBz;
    private static RecyclerView recyclerView;
    private View picselect;

    private static ArrayList<String> picList;
    public static final int PICK_PHOTO = 3;

    public PicUpDialog(@NonNull BaseActivity context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.activity = context;
        picList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picup);
        setCanceledOnTouchOutside(true);

        initView();

    }

    /*控件初始化*/
    private void initView(){
        viewLon =(TextView) findViewById(R.id.uppic_lon);
        viewLon.setText(activity.currentLon+"");
        viewLat =(TextView) findViewById(R.id.uppic_lat);
        viewLat.setText(activity.currentLat+"");
        viewMiaoshu =(EditText) findViewById(R.id.uppic_miaoshu);
        viewBz =(EditText) findViewById(R.id.uppic_beizhu);
        picselect =(View) findViewById(R.id.picselect_view);
        viewBtn =(TextView) findViewById(R.id.uppic_btn);

        recyclerView =(RecyclerView) findViewById(R.id.picup_recyc);

        ImageView close =(ImageView) findViewById(R.id.picup_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        picselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectPic();
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){

        if(picList.size() ==0){
            ToastUtil.setToast(activity,"请至少选择一张图片");
            return;
        }

        String lon = viewLon.getText().toString().trim();
        String lat = viewLat.getText().toString().trim();
        String miaoshu = viewMiaoshu.getText().toString().trim();
        String bz = viewBz.getText().toString().trim();

        Gson gson = new Gson();
        ArrayList<Image> images = new ArrayList<>();
        for(String path : picList){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            String base = Util.Bitmap2StrByBase64(bitmap);
            images.add(new Image(path,base));

        }
        String pic = gson.toJson(images);

        PicUp picUp = new PicUp();
        picUp.setJD(lon);
        picUp.setWD(lat);
        picUp.setMSXX(miaoshu);
        picUp.setREMARK(bz);
        picUp.setZPDZ(pic);
        picUp.setSCSBBH(MyApplication.macAddress);
        String jsonvalue = gson.toJson(picUp);

        Observable<String> observable = RetrofitHelper.getInstance(activity).getServer().uPPatrolEvent(jsonvalue);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(activity,"网络异常");
                    }

                    @Override
                    public void onNext(String s) {
                        if(s.equals("true")){
                            dismiss();
                            ToastUtil.setToast(activity,"上传成功");
                        }else{
                            ToastUtil.setToast(activity,"上传失败");
                        }
                    }
                });


    }

    /**跳转到选择图片界面*/
    private void toSelectPic() {
        if (picList.size() == 9) {
            ToastUtil.setToast(activity, activity.getResources().getString(R.string.log_pic_select_tip));
            return;
        }
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
        activity.startActivityForResult(intent, PICK_PHOTO);
    }

    /**/
    public static void initSelectPics(Context context,Intent data){
        //图片选择成功
        picList.clear();
        ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
        picList.addAll(list);
        loadPhoto(context,picList, 100);
    }

    /**选择图片后加载图片*/
    private static void loadPhoto(Context context,List<String> list, int mColumnWidth){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if(recyclerView != null){
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        RecyclerViewAdapter_logpic adapterImg = new RecyclerViewAdapter_logpic(context,list,mColumnWidth,"log");
        if(adapterImg != null && recyclerView != null){
            recyclerView.setAdapter(adapterImg);
        }
    }
}
