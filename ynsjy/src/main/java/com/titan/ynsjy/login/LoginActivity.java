package com.titan.ynsjy.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.titan.util.ActivityUtils;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewModelHolder;

/**
 * Created by hanyw on 2017/9/13/013.
 * 登录
 */

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_VIEWMODEL_TAG = "LOGIN_VIEWMODEL_TAG";

    private LoginViewModel mViewModel;
    private LoginFragment mFragment;
    private Context mContext;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_login);
        mFragment = findOrCreateViewFragment();
        mViewModel = findOrCreateViewModel();
        mFragment.setViewModel(mViewModel);

        //判断是否是pad，是则使用横屏
//        if(PadUtil.isPad(mContext)){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    public LoginFragment findOrCreateViewFragment(){
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment==null){
            fragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.content_frame);
        }
        return fragment;
    }

    public LoginViewModel findOrCreateViewModel(){
        @SuppressWarnings("unchecked")
        ViewModelHolder<LoginViewModel> holder =
                (ViewModelHolder<LoginViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(LOGIN_VIEWMODEL_TAG);
        if (holder==null||holder.getViewmodel()==null){
            LoginViewModel viewModel = new LoginViewModel(mContext,mFragment);
            ActivityUtils.addFragmentToActivity
                    (getSupportFragmentManager(),ViewModelHolder.createContainer(viewModel),LOGIN_VIEWMODEL_TAG);
            return viewModel;
        }
        return holder.getViewmodel();
    }
}
