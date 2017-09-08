package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.esri.core.map.Feature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.fragment.AuditCatalogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/9/7/007.
 */

public class AuditCatalogFragmentActivity extends AppCompatActivity {
    private Context mContext;
    private View view;
    private Map<String ,List<Feature>> map;
    private List<String> list;
    public static void actionStart(Context context, HashMap<String ,Object> map,ArrayList<String> list) {
        Intent intent = new Intent(context, AuditCatalogFragment.class);
        intent.putExtra("map", map);
        intent.putExtra("list",list);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        view = LayoutInflater.from(this).inflate(R.layout.audit_history_catalog,null);
        setContentView(view);
        this.map = (HashMap<String ,List<Feature>>)getIntent().getExtras().getSerializable("map");
        this.list = (List<String>)getIntent().getExtras().getSerializable("list");
        setMyAdapter();
    }

    private void setMyAdapter(){
        AuditCatalogFragment fragment = (AuditCatalogFragment) getFragmentManager().findFragmentById(R.id.audit_history_exlist);
        fragment.exRefresh(mContext,list,map);
    }
}
