package com.titan.ynsj.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.titan.ynsj.MyApplication;


/**
 * Created by hanyw on 2017/9/13/013.
 * 用户登录
 */

public class LoginViewModel extends BaseObservable {
    private Context mContext;
    private Login login;
    private SharedPreferences sharedPreferences = MyApplication.sharedPreferences;

    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableBoolean isremember = new ObservableBoolean();

    public LoginViewModel(Context context, Login login) {
        this.mContext = context;
        this.login = login;
    }

    /**
     * 初始化数据
     */
    public void initData(){
        isremember.set(sharedPreferences.getBoolean("isremember",false));
        if (isremember.get()){
            username.set(sharedPreferences.getString("ic_user_name",""));
            password.set(sharedPreferences.getString("ic_password",""));
        }
    }
    /**
     * 登录
     */
    public void onLogin() {
        if (checkEmpty()){
            login.showToast("请输入用户名和密码");
            return;
        }
        login.showProgress();
        if (!username.get().trim().equals("admin")) {
            login.stopProgress();
            login.showToast("用户名错误");
            return;
        }
        if (!password.get().trim().equals("123456")) {
            login.stopProgress();
            login.showToast("密码错误");
            return;
        }
        login.stopProgress();
        login.onNext();
    }

    /**
     * 记住用户名
     */
    public void onCheckRemember(){
        if (checkEmpty()){
            isremember.set(!isremember.get());
            login.showToast("请输入用户名或密码");
            return;
        }
        if (isremember.get()){
            isremember.set(true);
        }else {
            login.showToast("已取消记住用户名");
            isremember.set(false);
        }
        SharedPreferences.Editor editor = MyApplication.sharedPreferences.edit();
        editor.putBoolean("isremember",isremember.get());
        editor.putString("ic_user_name",username.get().trim());
        editor.putString("ic_password",password.get().trim());
        editor.apply();
    }

    /**
     * 检查用户名或密码是否为空
     * @return 任意一个为空返回true，都不为空返回false
     */
    private boolean checkEmpty(){
        return TextUtils.isEmpty(username.get())||TextUtils.isEmpty(password.get());
    }
}
