package com.titan.ynsjy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.color.ColorPickView;
import com.titan.ynsjy.color.SansumColorSelecter;
import com.titan.ynsjy.entity.MyLayer;

/**
 * Created by li on 2017/6/1.
 * 颜色选择器
 */

public class ColorDialog extends Dialog {

    private Context mContext;
    private int type;
    private TextView view;
    private SeekBar seekBar;
    private FeatureLayer myLayer;
    private String field;//字段名

    public ColorDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ColorDialog(@NonNull Context context, @StyleRes int themeResId, final int type, final TextView view,
                       final SeekBar seekBar, final FeatureLayer myLayer, String field) {
        super(context, themeResId);
        this.mContext = context;
        this.type = type;
        this.view = view;
        this.seekBar = seekBar;
        this.myLayer = myLayer;
        this.field = field;
    }

    protected ColorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //展示图层透明度和图层颜色设置的dialog
        setContentView(R.layout.base_color);
        setCanceledOnTouchOutside(true);

        TextView nocolor = (TextView) findViewById(R.id.nocolor);
        nocolor.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                int color = mContext.getResources().getColor(R.color.transparent);
                if (type == 0) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "tianchongse", color).apply();
                } else if (type == 1) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "bianjiese", color).apply();
                }else if (type==2){
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName()+ field + "tianchongse", color).apply();
                }
                view.setBackground(mContext.getResources().getDrawable(R.drawable.touming));
                seekBar.setProgress(100);
                MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "tmd", 100).apply();
                dismiss();
            }
        });

        ColorPickView colorPickerView = (ColorPickView) findViewById(R.id.fill_colro_pick);
        colorPickerView.setOnColorChangedListener(new ColorPickView.OnColorChangedListener() {
            @Override
            public void onColorChange(int color) {
                if (type == 0) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "tianchongse", color).apply();
                } else if (type == 1) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "bianjiese", color).apply();
                }else if (type==2){
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName()+ field + "tianchongse", color).apply();
                }
                view.setBackgroundColor(color);
            }
        });

        SansumColorSelecter select = (SansumColorSelecter) findViewById(R.id.color_seleter);
        select.setColorSelecterLinstener(new SansumColorSelecter.ColorSelecterLinstener() {

            @Override
            public void onColorSeleter(int color) {
                if (type == 0) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "tianchongse", color).apply();
                } else if (type == 1) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName() + "bianjiese", color).apply();
                }else if (type==2){
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getUrl()+myLayer.getName()+ field + "tianchongse", color).apply();
                }
                view.setBackgroundColor(color);
                dismiss();
            }
        });

        Button button = (Button) findViewById(R.id.render_btn_sure);
        button.setOnClickListener(new CancleListener(this));

    }
}
