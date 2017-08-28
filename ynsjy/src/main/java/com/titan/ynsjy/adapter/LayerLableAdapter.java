package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.esri.core.map.Field;
import com.titan.ynsjy.R;
import com.titan.ynsjy.presenter.LayerLablePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/6/5.
 * 图层标注adapter
 */

public class LayerLableAdapter extends BaseAdapter {

    private Context mContext;
    private List<Field> fields = new ArrayList<>();
    private LayerLablePresenter lablePresenter;
    private HashMap<Field,Boolean> checkboxs = new HashMap<>();

    public LayerLableAdapter(Context context, List<Field> fields, LayerLablePresenter lablePresenter,HashMap<Field,Boolean> checkboxs){
        this.mContext = context;
        this.fields = fields;
        this.lablePresenter = lablePresenter;
        this.checkboxs = checkboxs;
    }

    @Override
    public int getCount() {
        return fields.size();
    }

    @Override
    public Object getItem(int position) {
        return fields.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attr_field_chose_item, null);
            viewHolder.attrField = (CheckBox) convertView.findViewById(R.id.field_name);
            viewHolder.queryField = (ImageView) convertView.findViewById(R.id.field_query);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.attrField.setChecked(checkboxs.get(fields.get(position)));
        viewHolder.attrField.setText(fields.get(position).getAlias());
        viewHolder.attrField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = checkboxs.get(fields.get(position));
                for (Field f : fields) {
                    checkboxs.put(f, false);
                }
                checkboxs.put(fields.get(position), !isChecked);
                notifyDataSetChanged();
                if(!isChecked){
                    lablePresenter.queryFeatures(lablePresenter.myLayer,isChecked,fields,position);
                }else{
                    lablePresenter.iLayerView.getGraphicLayer().removeAll();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        CheckBox attrField;
        ImageView queryField;
    }
}
