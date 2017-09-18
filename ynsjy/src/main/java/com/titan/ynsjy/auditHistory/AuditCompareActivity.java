package com.titan.ynsjy.auditHistory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.titan.util.ActivityUtils;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ViewModelHolder;

/**
 * Created by hanyw on 2017/9/14/014.
 * 审计比较页面
 */

public class AuditCompareActivity extends AppCompatActivity {
    private Context mContext;

    public static final String COMPARE_VIEWMODEL_TAG = "COMPARE_VIEWMODEL_TAG";

    private AuditCompareFragment compareFragment;
    private AuditViewModel auditViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_copmare);
        compareFragment = findOrCreateCompareFragment();
        auditViewModel = findOrCreateAuditViewModel();
        compareFragment.setViewModel(auditViewModel);
    }

    private AuditCompareFragment findOrCreateCompareFragment() {
        AuditCompareFragment fragment = (AuditCompareFragment)
                getSupportFragmentManager().findFragmentById(R.id.audit_compare);
        if (fragment == null) {
            fragment = AuditCompareFragment.newIntance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.audit_compare);
        }
        return fragment;
    }

    public AuditViewModel findOrCreateAuditViewModel() {
        @SuppressWarnings("unchecked")
        ViewModelHolder<AuditViewModel> viewModel =
                (ViewModelHolder<AuditViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(COMPARE_VIEWMODEL_TAG);
        if (viewModel != null && viewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return viewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            AuditViewModel auditViewModel = new AuditViewModel(compareFragment);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(auditViewModel),
                    COMPARE_VIEWMODEL_TAG);
            return auditViewModel;
        }
    }

}
