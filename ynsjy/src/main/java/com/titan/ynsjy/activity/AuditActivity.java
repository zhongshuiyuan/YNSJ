package com.titan.ynsjy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.titan.ynsjy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hanyw on 2017/9/2/002.
 * 审计
 */

public class AuditActivity extends AppCompatActivity {
    @BindView(R.id.audit_reason)
    EditText auditReason;
    @BindView(R.id.audit_info)
    EditText auditInfo;
    @BindView(R.id.audit_edit_before)
    EditText auditEditBefore;
    @BindView(R.id.audit_edit_after)
    EditText auditEditAfter;
    @BindView(R.id.audit_mark)
    EditText auditMark;
    @BindView(R.id.audit_pic_browse)
    TextView auditPicBrowse;
    @BindView(R.id.audit_take_pic)
    TextView auditTakePic;
    @BindView(R.id.audit_sure)
    TextView auditSure;
    @BindView(R.id.audit_cancel)
    TextView auditCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_audit);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.audit_pic_browse, R.id.audit_take_pic, R.id.audit_sure, R.id.audit_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.audit_pic_browse:
                break;
            case R.id.audit_take_pic:
                break;
            case R.id.audit_sure:
                break;
            case R.id.audit_cancel:
                break;
        }
    }
}
