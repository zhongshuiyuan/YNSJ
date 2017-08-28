package com.titan.ynsjy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.titan.ynsjy.R;
import com.titan.ynsjy.adapter.LxqcTrgxdcAdapter;
import com.titan.ynsjy.db.DataBaseHelper;
import com.titan.ynsjy.util.ToastUtil;

import java.util.HashMap;
import java.util.List;

public class SlzylxqcTrgxqkDialog extends Dialog {

	Context context;
	List<HashMap<String, String>> trgxlist;
	LxqcTrgxdcAdapter adapter;
	String ydhselect;
	public SlzylxqcTrgxqkDialog(Context context,List<HashMap<String, String>> list,String ydhselect) {
		super(context, R.style.Dialog);
		this.context = context;
		this.trgxlist = list;
		this.ydhselect=ydhselect;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_trgxqkdc_view);
		
		TextView ydh=(TextView) findViewById(R.id.ydh);
		if(ydhselect!=null){
			ydh.setText(ydhselect);
		}
		
		ListView listview=(ListView) findViewById(R.id.listview);
		adapter=new LxqcTrgxdcAdapter(context, trgxlist);
		listview.setAdapter(adapter);
		TextView addmore=(TextView) findViewById(R.id.addmore);
		addmore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				HashMap<String, String>map=new HashMap<String, String>();
				map.put("YDH", ydhselect);
				map.put("SZ", "");
				map.put("GAOZS1", "");
				map.put("GAOZS2", "");
				map.put("GAOZS3", "");
				map.put("JKZK", "");
				map.put("PHQK", "");
				trgxlist.add(map);
				adapter.notifyDataSetChanged();
			}
		});
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DataBaseHelper.deleteLxqcTrgxqkdcAllData(context, ydhselect);
				final String[] zd = context.getResources().getStringArray(R.array.lxqctrgxqkzd);
				for(int i=0;i<trgxlist.size();i++){
					DataBaseHelper.addLxqcTrgxdcbData(context, zd, trgxlist.get(i));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
			}
		});
		Button cancle=(Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
}
