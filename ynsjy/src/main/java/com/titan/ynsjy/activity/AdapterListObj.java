package com.titan.ynsjy.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.model.TitanField;
import com.titan.ynsjy.R;
import com.titan.ynsjy.databinding.ItemFieldBinding;

import java.util.List;

/**
 * Created by whs on 2017/10/9
 *
 */

public class AdapterListObj<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> listobj;

    //private  AlarmInfoListInterface mAlarmInfoItemNav;

    private Context mContext;

    public AdapterListObj(List<T> listobj, Context mContext) {
        this.listobj = listobj;
        this.mContext = mContext;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemFieldBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
        public ItemFieldBinding getBinding() {
            return binding;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_field,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        TitanField field= (TitanField) listobj.get(position);
        ViewHolder holder= (ViewHolder) viewHolder;
        holder.getBinding().setTitanfield(field);

    }

    @Override
    public int getItemCount() {
        return listobj==null?0:listobj.size();
    }
    private void setList(List<T> listobj) {
        this.listobj = listobj;
        notifyDataSetChanged();
    }

    public void replaceData(List<T> listobj) {
        setList(listobj);

    }
}
