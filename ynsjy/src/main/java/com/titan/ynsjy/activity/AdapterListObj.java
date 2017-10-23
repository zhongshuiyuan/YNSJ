package com.titan.ynsjy.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.titan.model.TitanField;
import com.titan.ynsjy.R;
import com.titan.ynsjy.databinding.ItemFieldBinding;

import java.util.List;

/**
 * Created by whs on 2017/10/9
 *
 */

public class AdapterListObj<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<T> listobj;

    //private  AlarmInfoListInterface mAlarmInfoItemNav;

    private Context mContext;

    public   void setmListener(ItemClickListener mListener) {
        AdapterListObj.mListener = mListener;
    }

    public static ItemClickListener mListener;

    @Override
    public void onClick(View v) {
        mListener.onItemClick(v);
    }

    interface ItemClickListener{
        void onItemClick(View v);
    }



    public AdapterListObj(List<T> listobj, Context mContext) {
        this.listobj = listobj;
        this.mContext = mContext;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener {

        private ItemFieldBinding binding;
        //private ItemClickListener mListener;
        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            switch (viewType){
                case 1:
                    //时间类型
                    EditText et_value=(EditText)itemView.findViewById(R.id.et_value);
                    et_value.setOnFocusChangeListener(this);
                    break;
            }
            binding= DataBindingUtil.bind(itemView);
        }
        public ItemFieldBinding getBinding() {
            return binding;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                mListener.onItemClick(v);

            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_field,parent,false);
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        TitanField field= (TitanField) listobj.get(position);
        ViewHolder holder= (ViewHolder) viewHolder;
        holder.getBinding().setTitanfield(field);


    }

    @Override
    public int getItemViewType(int position) {
        TitanField field= (TitanField) listobj.get(position);
        if(field.getAlias().contains("时间")||field.getName().contains("time")){
            ((TitanField) listobj.get(position)).setFieldtype(1);
            return 1;
        }else {
            return 0;
        }
        //return super.getItemViewType(field.getFieldtype());
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
