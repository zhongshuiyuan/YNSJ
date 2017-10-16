package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableAdapter extends BaseExpandableListAdapter {

	List<File> groups;
	BaseActivity activity;
	private Context mContext;
	List<Map<String, List<File>>> childs;
	List<Geodatabase> geodatabaseList;
	HashMap<String, Boolean> childCheckBox = new HashMap<String, Boolean>();



    private OnItemClickListener mItemClickListener;

    public  interface  OnItemClickListener{
        void  onGroupChecked();
        void  onChildChecked(boolean isadd,GeodatabaseFeatureTable geotable);

    }
    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

	

	public ExpandableAdapter(Context context, List<File> groups,
                             List<Map<String, List<File>>> childs, HashMap<String, Boolean> childCheckBox) {
		this.mContext = context;
		this.groups = groups;
		this.childs = childs;
		this.childCheckBox = childCheckBox;
        //mItemClickListener=onItemClickListener;
		//this.geodatabaseList=geodatabases;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition)
				.get(groups.get(groupPosition).getName()).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String gdbpath=childs.get(groupPosition).get(groups.get(groupPosition).getName())
				.get(childPosition).getAbsolutePath();
		GeodatabaseExpAdapter geodatabaseExpAdapter = null;
        final CustExpListview SecondLevelexplv = new CustExpListview(mContext);
        try {
            final Geodatabase geodatabase=new Geodatabase(gdbpath);
			geodatabaseExpAdapter = new GeodatabaseExpAdapter(mContext,
                    geodatabase);
            geodatabaseExpAdapter.setOnGroupCheckListener(new GeodatabaseExpAdapter.OnCheckListener() {
                @Override
                public void onGroupChecked(View view, int position) {
                    CheckedTextView group= (CheckedTextView) view;
                    group.toggle();
                    boolean ischeck=group.isChecked();
                    //geodatabaseExpAdapter.geo.get(position).setChecked(groupIsChecked);
                    //SecondLevelexplv.collapseGroup(position);
                    SecondLevelexplv.expandGroup(position);
                }

                @Override
                public void onChildChecked(View view, GeodatabaseFeatureTable geodatabaseFeatureTable) {
                    CheckedTextView group= (CheckedTextView) view;
                    group.toggle();
                    boolean ischeck=group.isChecked();
                    mItemClickListener.onChildChecked(ischeck,geodatabaseFeatureTable);

                }
            });
			SecondLevelexplv.setAdapter(geodatabaseExpAdapter);
			SecondLevelexplv.setGroupIndicator(null);
            SecondLevelexplv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            SecondLevelexplv.setLayoutParams(lp);
            SecondLevelexplv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {

                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                            (geodatabase.getGeodatabaseTables().size() + 1)* (int) mContext.getResources().getDimension(R.dimen.parent_expandable_list_height));
                    SecondLevelexplv.setLayoutParams(lp);

                }
            });
            SecondLevelexplv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) mContext
                            .getResources().getDimension(
                                    R.dimen.parent_expandable_list_height));
                    SecondLevelexplv.setLayoutParams(lp);
                }
            });
			return SecondLevelexplv;

		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return null;
		}

		/*final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_expandable_child, null);
			holder = new ViewHolder();
			holder.cText = (TextView) convertView.findViewById(R.id.id_child_txt);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_child);
			holder.checkBox.setTag(groupPosition+"="+childPosition);
			holder.imageview_2 = (ImageView) convertView.findViewById(R.id.featurelayer_extent);
			holder.lv= (ListView) convertView.findViewById(R.id.lv_layers);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (childs.get(groupPosition).get(groups.get(groupPosition).getName())
				.get(childPosition).getName().endsWith(".otms")) {
			holder.cText.setText(childs.get(groupPosition)
					.get(groups.get(groupPosition).getName())
					.get(childPosition).getName().replace(".otms", ""));
            File gdbfile= childs.get(groupPosition).get(groups.get(groupPosition).getName())
                    .get(childPosition);
			holder.lv.setVisibility(View.VISIBLE);
			getTableList(gdbfile,holder);


		}
		if (childs.get(groupPosition).get(groups.get(groupPosition).getName())
				.get(childPosition).getName().endsWith(".geodatabase")) {
			holder.cText.setText(childs.get(groupPosition)
					.get(groups.get(groupPosition).getName())
					.get(childPosition).getName().replace(".geodatabase", ""));
            holder.lv.setVisibility(View.VISIBLE);
            File gdbfile= childs.get(groupPosition).get(groups.get(groupPosition).getName())
                    .get(childPosition);
            getTableList(gdbfile,holder);

            //holder.lv.setAdapter(new LineAdapter());

		}
//		if (childs.get(groupPosition).get(groups.get(groupPosition).getName())
//				.get(childPosition).getName().endsWith(".shp")){
//			holder.cText.setText(childs.get(groupPosition)
//					.get(groups.get(groupPosition).getName())
//					.get(childPosition).getName().replace(".shp", ""));
//		}
		
		String path = childs.get(groupPosition).get(groups.get(groupPosition).getName()).get(childPosition).getPath();
		boolean flag = childCheckBox.get(path);
		holder.checkBox.setChecked(flag);
        //holder.checkBox.setOnCheckedChangeListener(new MyCbCheckedChangeListener());

		//图层位置
		holder.imageview_2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String gname = groups.get(groupPosition).getName();
				String cname = childs.get(groupPosition).get(gname).get(childPosition).getName();
				for (int i = 0; i < BaseActivity.layerNameList.size(); i++) {
					String name = BaseActivity.layerNameList.get(i).getPname();
					String name1 = BaseActivity.layerNameList.get(i).getCname();
					if (name.equals(gname) && cname.contains(name1)) {
						final Geometry geometry = BaseActivity.layerNameList.get(i).getLayer().getFullExtent();
						activity.ZoomToGeom(geometry);
						break;
					}
				}
			}
		});


		return convertView;*/
	}


    @Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition)
				.get(groups.get(groupPosition).getName()).size();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expandable_group, null);
			groupHolder = new GroupViewHolder();

			//groupHolder.imageview_1 = (ImageView) convertView.findViewById(R.id.id_group_img);
			groupHolder.cText = (TextView) convertView.findViewById(R.id.id_group_txt);
			groupHolder.img_simple = (Button) convertView.findViewById(R.id.layer_render_simple);
			groupHolder.img_unique = (Button) convertView.findViewById(R.id.layer_render_unique);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupViewHolder) convertView.getTag();
		}

		groupHolder.cText.setText(groups.get(groupPosition).getName());
		groupHolder.img_simple.setVisibility(View.GONE);
		groupHolder.img_unique.setVisibility(View.GONE);
		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


    static class ViewHolder {
		TextView cText;
		CheckBox checkBox;
		ImageView imageview_2;
		ListView lv;
	}

	static class GroupViewHolder {
		TextView cText;
		Button img_simple;
		Button img_unique;
	}

	/*private class MyCbCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton v, boolean arg1) {
			switch (v.getId()) {
			case R.id.cb_child:
				Object obj = v.getTag();
				String[] str = obj.toString().split("=");
				int gpositon = Integer.parseInt(str[0]);
				int cpositon = Integer.parseInt(str[1]);
				activity.initOtmsChild(groups, childs, gpositon, cpositon);
				v.toggle();
				notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}*/

	/**
	 * 根据字符串生成布局，，因为我没有写layout.xml 所以用java 代码生成
	 * 实际中可以通过Inflate加载自己的自定义布局文件，设置数据之后并返回
	 * @return
	 */
	/*private TextView getChildView(String string) {
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		CheckBox cb=new CheckBox(activity);
		TextView textView = new TextView(activity);
		textView.setLayoutParams(layoutParams);

		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

		textView.setPadding(40, 20, 0, 20);
		textView.setText(string);
		textView.setTextColor(Color.BLACK);
		return textView;
	}*/
	private class CustExpListview extends ExpandableListView {

		public CustExpListview(Context context) {
			super(context);
		}

		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
					MeasureSpec.AT_MOST);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
