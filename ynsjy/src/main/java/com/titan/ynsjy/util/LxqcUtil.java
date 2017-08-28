package com.titan.ynsjy.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

import com.titan.ynsjy.adapter.LxqcYdyzAdapter;
import com.titan.ynsjy.dialog.HzbjDialog;
import com.titan.ynsjy.dialog.ShuziDialog;
import com.titan.ynsjy.dialog.XzzyDialog;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.listviewinedittxt.Line;
import com.titan.ynsjy.service.PullParseXml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

public class LxqcUtil {
	//static String LXQC="/连续清查";
	static String FILENAME="/config.xml";
	/**
	 * 获取资源type值
	 * @param ctx
	 * @param attributeName资源tablename
	 * @return
	 */
	public static String getAttributetype(Context ctx, String attributeName,String path)
	{
		//String str = ResourcesManager.otms + LXQC+FILENAME;
		//String path =ResourcesManager.getFilePath(str);
		path = new File(path).getParent()+FILENAME;
		String type = "";
		try
		{
			File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();
			type = parseXml.PullParseXMLforFeildType(inputStream, attributeName);
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return type;
	}
	/**
	 * 获取资源qzlx值 1为汉字输入 2为数字输入 3为选项输入
	 * @return
	 */
	public static String getAttributeQztype(Context ctx, String attributeName,String path)
	{
		//String str = ResourcesManager.otms + LXQC+FILENAME;
		//String path =ResourcesManager.getFilePath(str);
		path = new File(path).getParent()+FILENAME;
		String type = "";
		try
		{
			File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();
			type = parseXml.PullParseXMLforQzlxType(inputStream, attributeName);
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return type;
	}

	/**
	 * 获取资源xiaoshu值 控制数字输入小数点后几位
	 * @return
	 */
	public static String getAttributeXstype(Context ctx, String attributeName,String path)
	{
		//String str = ResourcesManager.otms + LXQC+FILENAME;
		//String path =ResourcesManager.getFilePath(str);
		path = new File(path).getParent()+FILENAME;
		String type = "";
		try
		{
			File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();
			type = parseXml.PullParseXMLforXslxType(inputStream, attributeName);
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return type;
	}
	/**
	 * 获取资源列表
	 * @param ctx
	 * @param attributeName资源tablename
	 * @return
	 */
	public static List<Row> getAttributeList(Context ctx, String attributeName,String path)
	{
		//String str = ResourcesManager.otms + LXQC+FILENAME;
		//String path =ResourcesManager.getFilePath(str);
		path = new File(path).getParent()+FILENAME;
		List<Row> list = null;
		try
		{
			File file = new File(path);
			FileInputStream inputStream = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();
			list = parseXml.PullParseXML(inputStream, attributeName);
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 获取资源小班号字段
	 * @param ctx
	 * @param attributeName资源tablename
	 * @return
	 */
	public static String getAttributeXbh(Context ctx, String tableName,String idname,String dbpath)
	{
		//String str = ResourcesManager.otms + LXQC+FILENAME;
		//String path =ResourcesManager.getFilePath(str);
		dbpath = new File(dbpath).getParent()+FILENAME;
		String xbh = "";
		try
		{
			File file = new File(dbpath);
			FileInputStream inputStream = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();
			xbh = parseXml.PullParseXbhXML(inputStream, tableName, idname);
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return xbh;
	}
	/**
	 * 设置强制填写的提示框
	 * @param context 上下文
	 * @param theme 主题
	 * @param tv 要赋值的textview
	 * @param map
	 * @param zd 字段名
	 * @param sp 偏好
	 * @param type 为1为文字输入 2时为数字输入
	 * @param inputtype数字输入类型 为0为小数1为整数
	 * @param xiaoshu 1为小数点后1为2为小数点后两位
	 */
	public static void showAlertDialog(final Context context,final String theme,final TextView tv,final HashMap<String, String> map,final String zd,final String inputtype,final String xiaoshu,final String type){
		final Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("强制填写").setMessage("当前字段已设置为不可填写，是否强制填写？")
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if("1".equals(type)){
							HzbjDialog hzdialog = new HzbjDialog(context,theme, tv, map,zd);
							BussUtil.setDialogParams(context, hzdialog, 0.5,0.5);
						}else if("2".equals(type)){
							ShuziDialog shuzidialog=new ShuziDialog(context, theme, tv, map, zd, null, null, inputtype, xiaoshu, "");
							BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
						}
					}
				}).setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).create();
		dialog.show();
	}
	/**
	 * 连续清查样地因子调查不可修改项
	 * @param context
	 * @param theme
	 * @param tv
	 * @param position
	 * @param lines
	 * @param adapter
	 * @param input_history
	 * @param type 1为汉字输入 2为数字输入 3为选项输入
	 */
	public static void showTcAlertDialog(final Context context,final TextView tv,final Line line,final LxqcYdyzAdapter adapter,final String name,final String type,final String path){
		final Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("强制填写").setMessage("当前字段已设置为不可填写，是否强制填写？")
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if("1".equals(type)){
							HzbjDialog wbdialog = new HzbjDialog(context, tv,line, adapter);
							BussUtil.setDialogParams(context, wbdialog, 0.5, 0.5);
						}else if("2".equals(type)){
//					String numtemp = LxqcUtil.getAttributeXstype(context, "slzylxqc.xml", name);
							String numtemp = LxqcUtil.getAttributeXstype(context, name,path);
							if(numtemp.equals("0")){
								ShuziDialog szdialog = new ShuziDialog(context, tv, line,"1","", adapter);
								BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
							}else if(numtemp.equals("1")){
								ShuziDialog szdialog = new ShuziDialog(context, tv, line,"0","1", adapter);
								BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
							}else if(numtemp.equals("2")){
								ShuziDialog szdialog = new ShuziDialog(context, tv, line,"0","2", adapter);
								BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
							}else if(numtemp.equals("-1")){
								ShuziDialog szdialog = new ShuziDialog(context, tv, line,"","", adapter);
								BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
							}
						}else if("3".equals(type)){
							List<Row> list = LxqcUtil.getAttributeList(context,name,path);
							XzzyDialog xzdialog = new XzzyDialog(context,list, tv,line,adapter);
							BussUtil.setDialogParams(context, xzdialog, 0.5, 0.5);
						}
					}
				}).setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).create();
		dialog.show();
	}
}
