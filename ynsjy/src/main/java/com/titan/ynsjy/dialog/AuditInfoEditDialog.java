package com.titan.ynsjy.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.titan.ynsjy.R;
import com.titan.ynsjy.auditHistory.AuditViewModel;
import com.titan.ynsjy.databinding.DialogEdittxtInputBinding;
import com.titan.ynsjy.util.ToastUtil;

/**
 * Created by hanyw on 2017/9/22/022.
 * 修改审计历史信息
 */

public class AuditInfoEditDialog extends DialogFragment {
    private Context mContext;
    private DialogEdittxtInputBinding binding;
    public static AuditInfoEditDialog dialog;
    private AuditViewModel auditViewModel;

    public static AuditInfoEditDialog newInstance(String alias, String value) {
        dialog = new AuditInfoEditDialog();
        Bundle bundle = new Bundle();
        bundle.putString("alias", alias);
        bundle.putString("value", value);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DialogEdittxtInputBinding.inflate(inflater, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = getDialog().getContext();
        binding.setViewmodel(auditViewModel);
        return binding.getRoot();
    }

    public void setViewModel(AuditViewModel viewModel) {
        auditViewModel = viewModel;
    }
}
