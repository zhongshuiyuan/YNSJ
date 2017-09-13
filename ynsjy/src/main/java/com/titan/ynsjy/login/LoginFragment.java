package com.titan.ynsjy.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lling.photopicker.utils.OtherUtils;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.ynsjy.R;
import com.titan.ynsjy.activity.FunctionChoiceActivity;
import com.titan.ynsjy.databinding.FragLoginBinding;
import com.titan.ynsjy.util.ToastUtil;


/**
 * Created by hanyw on 2017/9/13/013.
 * 用户登录
 */

public class LoginFragment extends Fragment implements Login {
    private Context mContext;
    private FragLoginBinding fragLoginBinding;
    private LoginViewModel loginViewModel;
    private static LoginFragment singleton;

    public static LoginFragment newInstance(){
        if (singleton==null){
            singleton = new LoginFragment();
        }
        return singleton;
    }

    public void setViewModel(LoginViewModel viewModel){
        loginViewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        fragLoginBinding = DataBindingUtil.inflate(inflater, R.layout.frag_login,container,false);
        fragLoginBinding.tvAppversion.setText(getString(R.string.app_version) + OtherUtils.getAppVersion(mContext));
        fragLoginBinding.setViewmodel(loginViewModel);
        return fragLoginBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loginViewModel.initData();
    }

    @Override
    public void onNext() {
        Intent intent = new Intent(mContext, FunctionChoiceActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress() {
        ProgressDialogUtil.startProgressDialog(mContext);
    }

    @Override
    public void stopProgress() {
        ProgressDialogUtil.stopProgressDialog(mContext);
    }

    @Override
    public void showToast(String info) {
        ToastUtil.setToast(getActivity(),info);
    }
}
