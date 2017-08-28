package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.core.internal.tasks.ags.v;
import com.titan.baselibrary.util.DialogParamsUtil;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.mview.IBaseView;
import com.titan.ynsjy.presenter.GpsCollectPresenter;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.versionupdata.VersionUpdata;

/**
 * Created by li on 2017/5/31.
 * 系统设置dialog
 */

public class SettingDialog extends Dialog {

    private BaseActivity mContext;
    private View gpsCaijiInclude;
    private IBaseView iBaseView;


    public SettingDialog(@NonNull BaseActivity context, @StyleRes int themeResId,IBaseView baseView) {
        super(context, themeResId);
        this.mContext = context;
        this.iBaseView = baseView;
        this.gpsCaijiInclude = iBaseView.getGpsCaijiInclude();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        setCanceledOnTouchOutside(false);

        CheckBox xsxjlx = (CheckBox) findViewById(R.id.xsxjlx);
        xsxjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean check) {
                MyApplication.sharedPreferences.edit().putBoolean("zongji", check).apply();
            }
        });

        TextView gpsset = (TextView) findViewById(R.id.gpsset);
        gpsset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GpsSetDialog setDialog = new GpsSetDialog(mContext,R.style.Dialog);
                BussUtil.setDialogParams(mContext, setDialog, 0.5, 0.5);
            }
        });

        TextView version = (TextView) findViewById(R.id.version_check);
        double vv = new VersionUpdata((BaseActivity)mContext).getCurentVersion();
        version.setText("版本更新   "+vv);
        version.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(MyApplication.getInstance().netWorkTip()){
//                            // 获取当前版本号 是否是最新版本
//                            String updataurl = mContext.getResources().getString(R.string.apk_updata);
//                            boolean flag = new VersionUpdata((BaseActivity)mContext).checkVersion(updataurl);
//                            if (!flag) {
//                                ToastUtil.setToast(mContext, "已是最新版本");
//                            }
//                        }
//                    }
//                }).start();

                if(MyApplication.getInstance().hasNetWork()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String updateurl = mContext.getResources().getString(R.string.apk_updata);
                            boolean flag = new VersionUpdata(mContext).checkVersion(updateurl);
                            if(!flag){
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.setToast(mContext, "已是最新版本");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        final CheckBox gpsCjlx = (CheckBox) findViewById(R.id.lxcj);
        if(gpsCaijiInclude.getVisibility() == View.VISIBLE){
            gpsCjlx.setChecked(true);
        }
        final GpsCollectPresenter gpsCollectPresenter = new GpsCollectPresenter(mContext,iBaseView);
        gpsCjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(arg1){
                    gpsCollectPresenter.showCollectionType();
                    gpsCaijiInclude.setVisibility(View.VISIBLE);
                }else{
                    gpsCaijiInclude.setVisibility(View.GONE);
                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.settings_close);
        close.setOnClickListener(new CancleListener(this));

        TextView addOnlineLayers =(TextView) findViewById(R.id.add_onlinelayer);
        addOnlineLayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.addOnlineLayers();
                SettingDialog.this.dismiss();
            }
        });

        TextView removeOnlineLayers =(TextView) findViewById(R.id.remove_onlinelayer);
        removeOnlineLayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.removeOnlineLayers();
                SettingDialog.this.dismiss();
            }
        });

        TextView picup =(TextView) findViewById(R.id.uptopic);
        picup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicUpDialog picUpDialog = new PicUpDialog(mContext,R.style.Dialog);
                DialogParamsUtil.setDialogParamsCenter(mContext,picUpDialog,0.6,0.8);
            }
        });
    }
}
