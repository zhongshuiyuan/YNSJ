package com.titan.ynsjy.adapter;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.titan.util.FileUtils;
import com.titan.ynsjy.R;

/**
 * Created by whs on 2017/9/28
 */

public class GeodatabaseExpAdapter extends BaseExpandableListAdapter implements AdapterView.OnItemClickListener{

    private Context mContext;
    private Geodatabase geodatabase=null;


    private OnCheckListener mOnCheckedListener;
    //private OnChildClickListener mOnChildCheckListener;

    public GeodatabaseExpAdapter(Context mContext, Geodatabase geodatabaseList) {
        this.mContext = mContext;
        this.geodatabase = geodatabaseList;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    /*public void updateListView(Geodatabase geodatabase) {
        this.geodatabase = geodatabase;
        notifyDataSetChanged();
    }*/



    @Override
    public int getGroupCount() {
        return geodatabase==null?0:1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return geodatabase == null ? 0 : (geodatabase.getGeodatabaseTables() == null ? 0 : geodatabase.getGeodatabaseTables().size());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return geodatabase;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return geodatabase.getGeodatabaseTables().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder =new GroupViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_featuretable,null);
            viewHolder.ctv= (CheckedTextView) convertView.findViewById(R.id.ctv);
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (GroupViewHolder) convertView.getTag();
        }
        String filename=FileUtils.pathGetNoSuffixFileName(geodatabase.getPath());
        Log.e("gdbname",filename+"-"+geodatabase.getGeodatabaseTables().size());
        viewHolder.ctv.setText(FileUtils.pathGetNoSuffixFileName(geodatabase.getPath()));
        viewHolder.ctv.setTag(groupPosition);
        viewHolder.ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCheckedListener!=null){
                    mOnCheckedListener.onGroupChecked(v, (Integer) v.getTag());
                }
            }
        });
        //判断是否已经打开列表
        if(isExpanded){
            //Drawable d=ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_keyboard_arrow_down_black_24dp,null);
            viewHolder.iv.setImageDrawable(VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_arrow_down, null));
            //holder.arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }else{
            //Drawable e=ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_keyboard_arrow_right_black_24dp,null);
            viewHolder.iv.setImageDrawable(VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_arrow_right, null));
            //holder.arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
        }
        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder =new ChildViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_featuretable,null);
            viewHolder.ctv= (CheckedTextView) convertView.findViewById(R.id.ctv);
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.iv_arrow);
            viewHolder.iv.setVisibility(View.GONE);
            //convertView.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
            //convertView.setTag(viewHolder);
            String tablename=geodatabase.getGeodatabaseTables().get(childPosition).getTableName();
            final GeodatabaseFeatureTable geodatabaseFeatureTable=geodatabase.getGeodatabaseTables().get(childPosition);
            Log.e("tablename",tablename);
            viewHolder.ctv.setText(geodatabase.getGeodatabaseTables().get(childPosition).getTableName());
            viewHolder.ctv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnCheckedListener!=null){
                        mOnCheckedListener.onChildChecked(v,geodatabaseFeatureTable);
                    }
                }
            });
        }
        /*else {
            viewHolder= (ChildViewHolder) convertView.getTag();
        }*/

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
    }

  /*  @Override
    public void onClick(View v) {
        if (mOnCheckedListener != null){
            mOnCheckedListener.onItemClick(v, (Integer) v.getTag());
        }
    }*/

    private class GroupViewHolder {
        private  CheckedTextView ctv;
        private ImageView iv;
    }
    private class ChildViewHolder {
        private  CheckedTextView ctv;
        private ImageView iv;
    }

    public interface OnCheckListener {
        void onGroupChecked(View view,int position);
        void onChildChecked(View view,GeodatabaseFeatureTable geodatabaseFeatureTable);
    }
    /*public static interface OnChildCheckListener {
        void onItemClick(View view,int position);
    }*/
    public void setOnGroupCheckListener(OnCheckListener listener) {
        this.mOnCheckedListener = listener;
    }
}
