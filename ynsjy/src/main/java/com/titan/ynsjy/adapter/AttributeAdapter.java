package com.titan.ynsjy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	List<Field> fieldList = null;
	Map<String, Object> selectAttribute;
	private String currentLayerName = "当前图层";
	List<Map<String, String>> attrList;
	private Context context;

	private Map<String, String> xiangMap = new HashMap<String, String>();
	private Map<String, String> xianMap = new HashMap<String, String>();
	private Map<String, String> cunMap = new HashMap<String, String>();

	public AttributeAdapter(List<Field> fieldList,
							Map<String, Object> selectAttribute,
							List<Map<String, String>> list, Context context, String layer) {
		this.context = context;
		this.attrList = list;
		inflater = LayoutInflater.from(context);
		this.fieldList = fieldList;
		this.selectAttribute = selectAttribute;
		this.currentLayerName = layer;

		xianMap = Util.getXianValue(context);
		xiangMap = Util.getXiangValue(context);
		cunMap = Util.getCunValue(context);
	}

	@Override
	public int getCount() {
		return fieldList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_attributeinfo, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv_key);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String strValue = "";// 对应 县 乡 村
		String xianD = "", xiangD = "";

		Field f = fieldList.get(position);
		Object obj = selectAttribute.get(f.getName());
		CodedValueDomain domain = (CodedValueDomain) f.getDomain();

		if (f.getAlias().contains("县")) {
			if (obj != null) {
				xianD = obj.toString();
				if (domain != null) {
					strValue = domain.getCodedValues().get(obj);
				} else {
					strValue = Util.getXXCValue(context, xianD, xianD, xianMap);
				}
			}
		} else if (f.getAlias().contains("乡")) {
			if (obj != null) {
				xiangD = obj.toString();
				if (domain != null) {
					strValue = domain.getCodedValues().get(obj);
				} else {
					strValue = Util.getXXCValue(context, xiangD, xianD,
							xiangMap);
				}
			}
		} else if (f.getAlias().contains("村")) {
			if (obj != null) {
				if (domain != null) {
					strValue = domain.getCodedValues().get(obj);
				} else {
					if (xiangD.contains(xianD)) {
						strValue = Util.getXXCValue(context, obj.toString(),
								xiangD, cunMap);
					} else {
						strValue = Util.getXXCValue(context, obj.toString(),
								xianD + xiangD, cunMap);
					}
				}
			}
		}

		holder.tv1.setText(fieldList.get(position).getAlias()+":");
		holder.tv2.setText(strValue);

		return convertView;
	}

	final class ViewHolder {
		TextView tv1;
		TextView tv2;
	}

}