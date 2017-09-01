package com.titan.ynsjy.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.titan.ynsjy.R;

/**
 * Created by hanyw on 2017/9/1/001.
 * 审计
 */

public class AuditDialog extends DialogFragment implements View.OnClickListener {
    private View view;
    private EditText ed_reason;//修改原因
    private EditText ed_before;//修改之前
    private EditText ed_after ;//修改之后
    private EditText ed_mark  ;//备注
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_audit,container,false);
        init();
        return view;
    }

    private void init() {
        ed_reason = (EditText) view.findViewById(R.id.audit_reason);
        ed_before= (EditText) view.findViewById(R.id.audit_edit_before);
        ed_after= (EditText) view.findViewById(R.id.audit_edit_after);
        ed_mark= (EditText) view.findViewById(R.id.audit_mark);
        TextView btn_sure = (TextView) view.findViewById(R.id.audit_sure);//确定
        btn_sure.setOnClickListener(this);
        TextView btn_cancel = (TextView) view.findViewById(R.id.audit_cancel);//取消
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.audit_sure:
                break;
            case R.id.audit_cancel:
                dismiss();
                break;
        }
    }

}
