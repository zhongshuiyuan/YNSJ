package com.titan.gis.layermanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.model.TitanLayer;
import com.titan.ynsjy.R;

import java.util.List;

public class LayersAdapter extends BaseExpandableListAdapter {

	private List<TitanLayer> groups;
	private Context mContext;
	private TitanLayer childs;



    /*private OnItemClickListener mItemClickListener;

    public  interface  OnItemClickListener{
        void  onGroupChecked();
        void  onChildChecked(boolean isadd, GeodatabaseFeatureTable geotable);

    }
    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
*/


	public LayersAdapter(Context context, List<TitanLayer> groups) {
		this.mContext = context;
		this.groups = groups;
	}



    @Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupHolder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_group, null);
			groupHolder = new GroupViewHolder();

			groupHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_layersname);
			groupHolder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupViewHolder) convertView.getTag();
		}

		groupHolder.tv_name.setText(groups.get(groupPosition).getName());
		return convertView;

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getSublayers().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layer_child, null);
            viewHolder = new ChildViewHolder();
            Log.e("child","id:"+groupPosition+"id:"+childPosition);
            viewHolder.ctv_layername = (CheckedTextView) convertView.findViewById(R.id.ctv);
            viewHolder.iv_local = (ImageView) convertView.findViewById(R.id.iv_local);
            viewHolder.ctv_layername.setText(groups.get(groupPosition).getSublayers().get(childPosition).getName());
            //convertView.setTag(viewHolder);

        }
        return convertView;

	}


	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getSublayers().size();
	}


	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


    private class ChildViewHolder {
		CheckedTextView ctv_layername;
        ImageView iv_local;
	}

	private  class GroupViewHolder {
		TextView tv_name;
		ImageView iv_add;
	}


}
