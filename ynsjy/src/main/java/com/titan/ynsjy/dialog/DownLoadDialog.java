package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by li on 2017/6/8.
 * 离线数据下载dialog
 */

public class DownLoadDialog extends Dialog {

    private Context mContext;


    public DownLoadDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public DownLoadDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected DownLoadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
