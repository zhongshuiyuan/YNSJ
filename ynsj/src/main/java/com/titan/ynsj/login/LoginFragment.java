package com.titan.ynsj.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.util.AppUtil;
import com.titan.util.ToastUtil;
import com.titan.ynsj.R;
import com.titan.ynsj.databinding.FragLoginBinding;


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
        fragLoginBinding = FragLoginBinding.inflate(inflater,container,false);
        fragLoginBinding.tvAppversion.setText(getString(R.string.app_version) + AppUtil.getAppVersion(mContext));
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
       /* Intent intent = new Intent(mContext, FunctionChoiceActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void showProgress() {
        //ProgressDialogUtil.startProgressDialog(mContext);
    }

    @Override
    public void stopProgress() {
        //ProgressDialogUtil.stopProgressDialog(mContext);
    }

    @Override
    public void showToast(String info) {
        ToastUtil.setToast(getActivity(),info);
    }
}
