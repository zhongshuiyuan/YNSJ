package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.titan.baselibrary.listener.CancleListener;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ToastUtil;

/**
 * Created by li on 2017/7/6.
 * 紧急信息上报
 */

public class JjxxsbDialog extends Dialog {

    private Context mContext;

    public JjxxsbDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jjxxsb);
        setCanceledOnTouchOutside(false);

        initview();

    }
    /*初始化控件*/
    private void initview(){
        ImageView close =(ImageView) findViewById(R.id.xxsb_close);
        close.setOnClickListener(new CancleListener(this));

        Spinner sjlx =(Spinner) findViewById(R.id.sjlx_spinner);
        sjlx.setSelection(0,false);
        sjlx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        EditText sjms =(EditText) findViewById(R.id.txt_sjms);
        EditText fsdd =(EditText) findViewById(R.id.txt_fsdd);
        EditText tel =(EditText) findViewById(R.id.txt_tel);
        EditText beizhu =(EditText) findViewById(R.id.txt_beizhu);

        TextView sure =(TextView) findViewById(R.id.xxsb_save);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.getInstance().netWorkTip()){
                    //网络连接后上报成功
                    ToastUtil.setToast(mContext,"上报成功");
                    dismiss();
                }
            }
        });

    }

    /*发送数据*/
    private void senInofToServer(){

    }

}
