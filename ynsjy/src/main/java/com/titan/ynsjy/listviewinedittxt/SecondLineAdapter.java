package com.titan.ynsjy.listviewinedittxt;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.titan.ynsjy.R;
import com.titan.ynsjy.edite.activity.XbEditActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyw on 2017/8/31/031.
 * 属性页面列表适配器
 */

public class SecondLineAdapter extends BaseAdapter {
    private XbEditActivity mContext;
    private DecimalFormat format = new DecimalFormat("0.000000");
    DecimalFormat df = new DecimalFormat("0.00");
    private double zlchl = 0;//平均造林成活率
    boolean flag;//是否可修改

    @SuppressLint("SimpleDateFormat")
    public static DateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private String pname;
    /*   */
    private FeatureLayer featureLayer;
    private GeodatabaseFeature selGeoFeature;
    private Map<String, Object> attribute;
    private Map<String,List<String>> map;//数据集
    private List<String> nameList = new ArrayList<>();
    private List<String> valueList = new ArrayList<>();

    public SecondLineAdapter(XbEditActivity context, boolean flag, Map<String,List<String>> map){
        this.mContext = context;
        this.flag = flag;
        this.map = map;
        setData();
    }

    private void setData(){
        nameList = map.get("name");
        valueList = map.get("value");
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_listviewinedittxt_line, parent, false);
            holder.etLine = (TextView) convertView.findViewById(R.id.etLine);
            holder.tvLine = (TextView) convertView.findViewById(R.id.tvline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.etLine.getTag() instanceof TextWatcher) {
            holder.etLine.removeTextChangedListener((TextWatcher) (holder.etLine.getTag()));
        }

        holder.tvLine.setText(nameList.get(position));

        holder.etLine.setText(valueList.get(position));
        holder.etLine.setEnabled(false);

        if (flag) {
            //holder.tvLine.setBackgroundColor(Color.GRAY);
            //holder.etLine.setBackgroundColor(Color.GRAY);
            holder.etLine.setClickable(false);
        } else {
            holder.tvLine.setBackgroundColor(Color.WHITE);
            holder.etLine.setBackgroundColor(Color.WHITE);
        }

        holder.etLine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            }
        });

        final TextWatcher watcher = new SimpeTextWather() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {

                } else {

                }
            }
        };
        holder.etLine.addTextChangedListener(watcher);
        holder.etLine.setTag(watcher);

        return convertView;
    }

    static class ViewHolder {
        TextView tvLine;
        TextView etLine;
    }

    private void showChangeValueDialog(TextView tv_name,TextView tv_value ){
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.attr_value_edit);
        dialog.setCanceledOnTouchOutside(false);
        TextView keyText = (TextView) dialog.findViewById(R.id.attr_key);
        EditText editText = (EditText) dialog.findViewById(R.id.attr_value);
        Button exit = (Button) dialog.findViewById(R.id.attr_exit);
        Button sure = (Button) dialog.findViewById(R.id.attr_sure);
        keyText.setText(tv_name.getText());
        editText.setText(tv_value.getText());
        setEditTextCursorLocation(editText);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                dialog.dismiss();
            }
        });
    }

    /** 把edittext的光标放置在最后 */
    public void setEditTextCursorLocation(EditText et) {
        CharSequence txt = et.getText();
        if (txt instanceof Spannable) {
            Spannable spanText = (Spannable) txt;
            Selection.setSelection(spanText, txt.length());
        }
    }
}
