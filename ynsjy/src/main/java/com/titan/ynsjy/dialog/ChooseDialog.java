package com.titan.ynsjy.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.NamePhone;
import com.titan.ynsjy.service.RetrofitHelper;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 32 on 2017/6/21.
 * 用户信息
 */

public class ChooseDialog extends DialogFragment implements View.OnClickListener {

    private View mInflate;
    private Context mContext;

    private Spinner mSp_name;
    private TextView mTv_phone;
    private TextView mTv_shebei;
    private TextView mTv_xulie;
    private TextView mTv_sbname;

    private ArrayList<NamePhone> npList = new ArrayList<>();//姓名、手机信息列表
    private ArrayList<String> nameList = new ArrayList<>();//姓名列表
    private String mName = "";

    private String shebei;//设备号
    private String xulie;//序列号
    private String sbname;//设备名称
    private String phone;//手机号

    private static int TYPE;//信息更新类型

    /**
     * SP临时文件
     */
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences kmsp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.dialog_choose, container, false);

        mContext = getDialog().getContext();

        kmsp = mContext.getSharedPreferences(PREFS_NAME, 0);

        // 设置dialog宽、高
        BussUtil.setDialogParams2(mContext,getDialog(),0.4,0.5);

        initView();

        ishaveInfo();

        return mInflate;
    }

    private void initView() {
        mSp_name = (Spinner) mInflate.findViewById(R.id.sp_name); // 姓名
        mTv_phone = (TextView) mInflate.findViewById(R.id.tv_phone); // 手机号
        mTv_shebei = (TextView) mInflate.findViewById(R.id.tv_shebei); // 设备号
        mTv_xulie = (TextView) mInflate.findViewById(R.id.tv_xulie); // 序列号
        mTv_sbname = (TextView) mInflate.findViewById(R.id.tv_sbname); // 设备名称
        Button mBtn_sure = (Button) mInflate.findViewById(R.id.btn_sure);

        getName();

        // 设备号
        mTv_shebei.setText(MyApplication.macAddress);

        // 序列号
        mTv_xulie.setText(MyApplication.mobileXlh);

        // 设备名称
        mTv_sbname.setText(MyApplication.mobileType);

        mBtn_sure.setOnClickListener(this);
    }

    /**
     * 网络请求（获取姓名、手机号）
     */
    private void getName() {
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().getUserNumber();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext, "网络错误，请检查网络连接");
                    }

                    @Override
                    public void onNext(String s) {
                        if (!s.equals("0")) {
                            ArrayList<NamePhone> json = new Gson().fromJson(s, new TypeToken<ArrayList<NamePhone>>() {
                            }.getType());
                            npList.clear();
                            npList.addAll(json);

                            nameList.clear();
                            for (int i = 0; i < npList.size(); i++) {
                                nameList.add(npList.get(i).getNAME()); // 姓名列表
                            }

                            setInfo(npList, nameList);
                        } else if (s.equals("0")) {
                            ToastUtil.setToast(mContext, "没有数据");
                        }
                    }
                });
    }

    /**
     * 根据姓名获取手机号
     *
     * @param npList   姓名、手机信息列表
     * @param nameList 姓名列表
     */
    private void setInfo(final ArrayList<NamePhone> npList, ArrayList<String> nameList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_name.setAdapter(adapter);

        // 选择姓名
        mSp_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mName = adapterView.getItemAtPosition(i).toString();

                // 获取手机号
                for (int j = 0; j < npList.size(); j++) {
                    if (npList.get(j).getNAME().equals(mName)) {
                        mTv_phone.setText(npList.get(j).getPHONE());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                if (TYPE == 0)
                    sendInfo();
                else if (TYPE == 1)
                    updateSBinfo();
                kmsp.edit().putString("isFirst", "1").apply();
                dismiss();
                break;
        }
    }

    /*获取数据*/
    private void getData() {
        shebei = mTv_shebei.getText().toString();
        xulie = mTv_xulie.getText().toString();
        sbname = mTv_sbname.getText().toString();
        phone = mTv_phone.getText().toString();
    }

    /*检查设备是否录入服务器数据库*/
    private void ishaveInfo() {
        Observable<String> observable = RetrofitHelper.getInstance(mContext)
                .getServer().isHaveSBH(MyApplication.macAddress);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ChooseDialog.this.dismiss();
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("设备未录入")) {
                            TYPE = 0;
                        } else if (s.equals("设备已录入，用户名未录入") || s.equals("设备号、用户名均已录入")) {
                            TYPE = 1;
                        }
                    }
                });
    }

    /* 上传设备信息*/
    private void sendInfo() {
        getData();
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().sendSbInfo(shebei, xulie, sbname, mName, phone, "", "");
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ChooseDialog.this.dismiss();
                ToastUtil.setToast(mContext, "网络错误，请检查网络连接");
            }

            @Override
            public void onNext(String s) {
                switch (s) {
                    case "录入成功":
                        ToastUtil.setToast(mContext, "录入成功");
                        break;
                    case "设备已录入,使用者姓名未录入":
                        ToastUtil.setToast(mContext, "设备已录入,使用者姓名未录入");
                        break;
                    case "设备已录入,使用者姓名也录入":
                        ToastUtil.setToast(mContext, "设备已录入,使用者姓名也录入");
                        break;
                    default:
                        ToastUtil.setToast(mContext, "录入失败");
                        break;
                }
            }
        });
    }

    /*修改设备信息*/
    private void updateSBinfo() {
        getData();
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().updateSbinfo(mName, shebei, phone, "");
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext, "网络错误，请检查网络连接");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("1,修改成功") || s.equals("1,设备号不存在，已重新录入")) {
                            ToastUtil.setToast(mContext, "修改成功");
                        }
                    }
                });
    }
}
