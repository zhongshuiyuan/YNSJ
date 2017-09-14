package com.titan.ynsjy.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.esri.android.map.CalloutPopupWindow;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.Bsuserbase;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.entity.ScreenTool;
import com.titan.ynsjy.entity.ScreenTool.Screen;
import com.titan.ynsjy.service.PullParseXml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务帮助类
 * */
public class BussUtil {

	/** 获取工程中xml配置文件 */
	public static List<Row> getConfigXml(Context ctx, String projectname,String name) {
		List<Row> list = new ArrayList<Row>();
		try {
			String path = MyApplication.resourcesManager.getFolderPath(ResourcesManager.otms + "/" + projectname);
			File file = new File(path, "config.xml");
			InputStream input = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();

			list = parseXml.PullParseXML(input, name);
			if(list == null){
				list = new ArrayList<Row>();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/** 获取otms文件夹下config.xml图层数据配置信息 */
	public static List<Row> getConfigXml(Context ctx,String name) {
		List<Row> list = null;
		try {
			String path = MyApplication.resourcesManager.getFolderPath(ResourcesManager.otms + "/" );
			File file = new File(path, "config.xml");
			InputStream input = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();

			list = parseXml.PullParseXML(input, name);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 检测字符串中是否有特殊符号 */
	public static boolean stringCheck(String str) {
		String regEx = "[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*";
		if (str.replaceAll(regEx, "").length() == 0) {
			// 沒有特殊符号
			return true;
		} else {
			// 用户名中存在特殊符号
			return false;
		}
	}

	/** 检测手机号是否符合规范 */
	public static boolean checkTelNumber(String number) {
		// String regExp = "^[1]([3-9][0-9]{1}|59|58|88|89)[0-9]{8}$";
		String regExp = "^[1]([3-9][0-9])[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
		boolean flag = m.find();
		return flag;
	}

	/** 获取唯一识表示 mac地址 */
	public static String getWifiMacAddress(Context context) {
		String macAddress = "";
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiMgr.isWifiEnabled()) {
			// 如果wifi打开
			// wifiMgr.setWifiEnabled(false);
		} else {
			// 如果wifi关闭
			wifiMgr.setWifiEnabled(true);
		}
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			// 如果wifi关闭的情况下 可能获取不到
			macAddress = info.getMacAddress();
		}
		return macAddress;
	}

	/** 关闭calloutpopuwind */
	public static void closeCalloutPopu(final View view,
			final CalloutPopupWindow popupWindow) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ImageView imageView = (ImageView) view.findViewById(R.id.calloutpopuwindow_close);
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						popupWindow.hide();
					}
				});
			}
		}).start();
	}

	/** 获取软件版本号 */
	public static double getVersionCode(Context context) {
		double versionCode = 0;
		String versionName;
		try {
			// // 获取packagemanager的实例
			// PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			// PackageInfo packInfo =
			// packageManager.getPackageInfo(getPackageName(),0);
			// String version = packInfo.versionName;
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			// versionCode = context.getPackageManager().getPackageInfo(
			// getPackageName(), 0).versionCode;
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			versionCode = Double.parseDouble(versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/** 安装APK文件 */
	public static void installApk(Context context, String mSavePath,
			HashMap<String, String> mHashMap) {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
	}

	/** 解析登录成功后返回的用户信息 */
	public static Bsuserbase getUserInfo(String str, Bsuserbase bsuserbase) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject result = jsonObject.optJSONArray("ds").optJSONObject(0);
			bsuserbase = new Bsuserbase();
			if (isEmperty(result.getString("ID"))) {
				bsuserbase.setID(Integer.parseInt(result.getString("ID")));
			}
			if (isEmperty(result.getString("PASSWORD"))) {
				bsuserbase.setPASSWORD(result.getString("PASSWORD"));
			}
			if (isEmperty(result.getString("BZ"))) {
				bsuserbase.setBZ(result.getString("BZ"));
			}
			if (isEmperty(result.getString("DATASHARE"))) {
				bsuserbase.setDATASHARE(result.getString("DATASHARE"));
			}
			if (isEmperty(result.getString("ISACTIVE"))) {
				bsuserbase.setISACTIVE(Integer.parseInt(result
						.getString("ISACTIVE")));
			}
			if (isEmperty(result.getString("ISZJ"))) {
				bsuserbase.setISZJ(Integer.parseInt(result.getString("ISZJ")));
			}
			if (isEmperty(result.getString("LASTLOGIN"))) {
				bsuserbase.setLASTLOGIN(result.getString("LASTLOGIN"));
			}
			if (isEmperty(result.getString("LOGINTIMES"))) {
				bsuserbase.setLOGINTIMES(Integer.parseInt(result
						.getString("LOGINTIMES")));
			}
			if (isEmperty(result.getString("DATASHARE"))) {
				bsuserbase.setDATASHARE(result.getString("DATASHARE"));
			}
			if (isEmperty(result.getString("UNITNAME"))) {
				bsuserbase.setUNITNAME(result.getString("UNITNAME"));
			}
			if (isEmperty(result.getString("UNITID"))) {
				bsuserbase.setUNITID(Integer.parseInt(result
						.getString("UNITID")));
			}
			if (isEmperty(result.getString("PX"))) {
				bsuserbase.setPX(Integer.parseInt(result.getString("PX")));
			}
			if (isEmperty(result.getString("SKINNAME"))) {
				bsuserbase.setSKINNAME(result.getString("SKINNAME"));
			}
			if (isEmperty(result.getString("USER_IMAGE"))) {
				bsuserbase.setUSER_IMAGE(result.getString("USER_IMAGE"));
			}
			if (isEmperty(result.getString("SYSTEMTYPE"))) {
				bsuserbase.setSYSTEMTYPE(Integer.parseInt(result
						.getString("SYSTEMTYPE")));
			}
			if (isEmperty(result.getString("SYSTEMIDS"))) {
				bsuserbase.setSYSTEMIDS(result.getString("SYSTEMIDS"));
			}
			if (isEmperty(result.getString("USER_JD"))) {
				bsuserbase.setUSER_JD(result.getString("USER_JD"));
			}
			if (isEmperty(result.getString("USER_CITY"))) {
				bsuserbase.setUSER_CITY(result.getString("USER_CITY"));
			}
			if (isEmperty(result.getString("USERGRPJ"))) {
				bsuserbase.setUSERGRPJ(result.getString("USERGRPJ"));
			}
			if (isEmperty(result.getString("USERZZMM"))) {
				bsuserbase.setUSERZZMM(result.getString("USERZZMM"));
			}
			if (isEmperty(result.getString("USERHY"))) {
				bsuserbase.setUSERHY(result.getString("USERHY"));
			}
			if (isEmperty(result.getString("USERMZ"))) {
				bsuserbase.setUSERMZ(result.getString("USERMZ"));
			}
			if (isEmperty(result.getString("USER_S"))) {
				bsuserbase.setUSER_S(result.getString("USER_S"));
			}
			if (isEmperty(result.getString("USERBIRTH"))) {
				bsuserbase.setUSERBIRTH(result.getString("USERBIRTH"));
			}
			if (isEmperty(result.getString("USEROLD"))) {
				bsuserbase.setUSEROLD(Integer.parseInt(result
						.getString("USEROLD")));
			}
			if (isEmperty(result.getString("USERSEX"))) {
				bsuserbase.setUSERSEX(Integer.parseInt(result
						.getString("USERSEX")));
			}
			if (isEmperty(result.getString("USEREMAIL"))) {
				bsuserbase.setUSEREMAIL(result.getString("USEREMAIL"));
			}
			if (isEmperty(result.getString("MOBILEPHONENO"))) {
				bsuserbase.setMOBILEPHONENO(result.getString("MOBILEPHONENO"));
			}
			if (isEmperty(result.getString("TELNO"))) {
				bsuserbase.setTELNO(result.getString("TELNO"));
			}
			if (isEmperty(result.getString("REALNAME"))) {
				bsuserbase.setREALNAME(result.getString("REALNAME"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bsuserbase;
	}

	/** 解析webservice数据所属数据 数据 */
	public static List<HashMap<String, String>> getSjssData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
				}
				// DUCODE
				if (isEmperty(obj.getString("DUCODE"))) {
					map.put("DUCODE", obj.getString("DUCODE"));
				} else {
					map.put("DUCODE", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// DATATYPE
				if (isEmperty(obj.getString("DATATYPE"))) {
					map.put("DATATYPE", obj.getString("DATATYPE"));
				} else {
					map.put("DATATYPE", "");
				}
				// ISXZQH
				if (isEmperty(obj.getString("ISXZQH"))) {
					map.put("ISXZQH", obj.getString("ISXZQH"));
				} else {
					map.put("ISXZQH", "");
				}
				// DATACODE
				if (isEmperty(obj.getString("DATACODE"))) {
					map.put("DATACODE", obj.getString("DATACODE"));
				} else {
					map.put("DATACODE", "");
				}
				// PARENTCODE
				if (isEmperty(obj.getString("PARENTCODE"))) {
					map.put("PARENTCODE", obj.getString("PARENTCODE"));
				} else {
					map.put("PARENTCODE", "");
				}
				// DUNAME
				if (isEmperty(obj.getString("DUNAME"))) {
					map.put("DUNAME", obj.getString("DUNAME"));
				} else {
					map.put("DUNAME", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice数据 基地性质 数据 */
	public static List<HashMap<String, String>> getJdxzData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 是否系统级别字典，系统级别字典 手动加入数据库或导入 不能修改，删除。 系统：0 非系统 ：1
				if (isEmperty(obj.getString("SYSDIF"))) {
					map.put("SYSDIF", obj.getString("SYSDIF"));
				} else {
					map.put("SYSDIF", "");
				}
				// DICID
				if (isEmperty(obj.getString("DICID"))) {
					map.put("DICID", obj.getString("DICID"));
				} else {
					map.put("DICID", "");
				}
				// 暂时定为200 如果要存储比较长的字符串时再改吧
				if (isEmperty(obj.getString("DICVAL"))) {
					map.put("DICVAL", obj.getString("DICVAL"));
				} else {
					map.put("DICVAL", "");
				}
				// 存储字典分组 例：IMGTYPE 8长度纯英文 字符，区分大小写
				if (isEmperty(obj.getString("DICGROUP"))) {
					map.put("DICGROUP", obj.getString("DICGROUP"));
				} else {
					map.put("DICGROUP", "");
				}
				// 字典描述
				if (isEmperty(obj.getString("DICDES"))) {
					map.put("DICDES", obj.getString("DICDES"));
				} else {
					map.put("DICDES", "");
				}
				// 字典编码，唯一值
				if (isEmperty(obj.getString("DICCODE"))) {
					map.put("DICCODE", obj.getString("DICCODE"));
				} else {
					map.put("DICCODE", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性日常巡检数据 */
	public static List<HashMap<String, String>> getSwdyxRcxjJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 巡查人员
				if (isEmperty(obj.getString("INSPECTOR"))) {
					map.put("INSPECTOR", obj.getString("INSPECTOR"));
				} else {
					map.put("INSPECTOR", "");
				}
				// 巡查部门
				if (isEmperty(obj.getString("INSPECTDEPARTMENT"))) {
					map.put("INSPECTDEPARTMENT",
							obj.getString("INSPECTDEPARTMENT"));
				} else {
					map.put("INSPECTDEPARTMENT", "");
				}
				// 巡查时间
				if (isEmperty(obj.getString("INSPECTTIME"))) {
					map.put("INSPECTTIME", obj.getString("INSPECTTIME"));
				} else {
					map.put("INSPECTTIME", "");
				}
				// 出动车辆数
				if (isEmperty(obj.getString("VEHICLENUM"))) {
					map.put("VEHICLENUM", obj.getString("VEHICLENUM"));
				} else {
					map.put("VEHICLENUM", "");
				}
				if (isEmperty(obj.getString("DISPATCHNUM"))) {
					map.put("DISPATCHNUM", obj.getString("DISPATCHNUM"));
				} else {
					map.put("DISPATCHNUM", "");
				}
				if (isEmperty(obj.getString("INSPECTSITE"))) {
					map.put("INSPECTSITE", obj.getString("INSPECTSITE"));
				} else {
					map.put("INSPECTSITE", "");
				}
				// 被巡查单位
				if (isEmperty(obj.getString("INSPECTEDUNIT"))) {
					map.put("INSPECTEDUNIT", obj.getString("INSPECTEDUNIT"));
				} else {
					map.put("INSPECTEDUNIT", "");
				}
				// 被巡查人
				if (isEmperty(obj.getString("PERINSPECTED"))) {
					map.put("PERINSPECTED", obj.getString("PERINSPECTED"));
				} else {
					map.put("PERINSPECTED", "");
				}
				// 是否有违法行为
				if (isEmperty(obj.getString("ISILLEGAL"))) {
					map.put("ISILLEGAL", obj.getString("ISILLEGAL"));
				} else {
					map.put("ISILLEGAL", "");
				}
				// 巡查结果
				if (isEmperty(obj.getString("INSPECTRESULT"))) {
					map.put("INSPECTRESULT", obj.getString("INSPECTRESULT"));
				} else {
					map.put("INSPECTRESULT", "");
				}
				// 经度
				if (isEmperty(obj.getString("LONGITUDE"))) {
					map.put("LONGITUDE", obj.getString("LONGITUDE"));
				} else {
					map.put("LONGITUDE", "");
				}
				// 纬度
				if (isEmperty(obj.getString("LATITUDE"))) {
					map.put("LATITUDE", obj.getString("LATITUDE"));
				} else {
					map.put("LATITUDE", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// 数据所属代码
				if (isEmperty(obj.getString("UNITCODE"))) {
					map.put("UNITCODE", obj.getString("UNITCODE"));
				} else {
					map.put("UNITCODE", "");
				}

				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性动物扰民数据 */
	public static List<HashMap<String, String>> getSwdyxDwrmJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 扰民事件名称
				if (isEmperty(obj.getString("EVENTNAME"))) {
					map.put("EVENTNAME", obj.getString("EVENTNAME"));
				} else {
					map.put("EVENTNAME", "");
				}
				// 发生的时间
				if (isEmperty(obj.getString("HAPPENTIME"))) {
					map.put("HAPPENTIME", obj.getString("HAPPENTIME"));
				} else {
					map.put("HAPPENTIME", "");
				}
				// 办结时间
				if (isEmperty(obj.getString("FINISHTIME"))) {
					map.put("FINISHTIME", obj.getString("FINISHTIME"));
				} else {
					map.put("FINISHTIME", "");
				}
				// 主办人
				if (isEmperty(obj.getString("TRANSACTOR"))) {
					map.put("TRANSACTOR", obj.getString("TRANSACTOR"));
				} else {
					map.put("TRANSACTOR", "");
				}
				// 被扰人
				if (isEmperty(obj.getString("DISPERSON"))) {
					map.put("DISPERSON", obj.getString("DISPERSON"));
				} else {
					map.put("DISPERSON", "");
				}
				// 事件描述
				if (isEmperty(obj.getString("DESCRIPTION"))) {
					map.put("DESCRIPTION", obj.getString("DESCRIPTION"));
				} else {
					map.put("DESCRIPTION", "");
				}
				// 事发位置
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				// 扰民动物
				if (isEmperty(obj.getString("BIONAME"))) {
					map.put("BIONAME", obj.getString("BIONAME"));
				} else {
					map.put("BIONAME", "");
				}
				// 事件处理结果
				if (isEmperty(obj.getString("RESULTDESC"))) {
					map.put("RESULTDESC", obj.getString("RESULTDESC"));
				} else {
					map.put("RESULTDESC", "");
				}
				// 是否处理完
				if (isEmperty(obj.getString("ISFINISHED"))) {
					map.put("ISFINISHED", obj.getString("ISFINISHED"));
				} else {
					map.put("ISFINISHED", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// 数据所属代码
				if (isEmperty(obj.getString("UNITCODE"))) {
					map.put("UNITCODE", obj.getString("UNITCODE"));
				} else {
					map.put("UNITCODE", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 救护站台账管理 数据 */
	public static List<HashMap<String, String>> getSwdyxJhzJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 救助站名称
				if (isEmperty(obj.getString("AIDNAME"))) {
					map.put("AIDNAME", obj.getString("AIDNAME"));
				} else {
					map.put("AIDNAME", "");
				}
				// 负责人
				if (isEmperty(obj.getString("MANAGER"))) {
					map.put("MANAGER", obj.getString("MANAGER"));
				} else {
					map.put("MANAGER", "");
				}
				// 联系电话
				if (isEmperty(obj.getString("PHONE"))) {
					map.put("PHONE", obj.getString("PHONE"));
				} else {
					map.put("PHONE", "");
				}
				// 救护站位置——经度
				if (isEmperty(obj.getString("LAT"))) {
					map.put("LAT", obj.getString("LAT"));
				} else {
					map.put("LAT", "");
				}
				// 救护站位置——维度
				if (isEmperty(obj.getString("LNG"))) {
					map.put("LNG", obj.getString("LNG"));
				} else {
					map.put("LNG", "");
				}
				// 行政区域编码
				if (isEmperty(obj.getString("AREACODE"))) {
					map.put("AREACODE", obj.getString("AREACODE"));
				} else {
					map.put("AREACODE", "");
				}
				// 救护站详细地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				// 救护站救助条件
				if (isEmperty(obj.getString("CONDITION"))) {
					map.put("CONDITION", obj.getString("CONDITION"));
				} else {
					map.put("CONDITION", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// 模块管理单位
				if (isEmperty(obj.getString("UNITCODE"))) {
					map.put("UNITCODE", obj.getString("UNITCODE"));
				} else {
					map.put("UNITCODE", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 救护站 数据 */
	public static List<HashMap<String, String>> getSwdyxJhzsqbJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 救助站ID
				if (isEmperty(obj.getString("AIDID"))) {
					map.put("AIDID", obj.getString("AIDID"));
				} else {
					map.put("AIDID", "");
				}
				// 救助站名
				if (isEmperty(obj.getString("AIDNAME"))) {
					map.put("AIDNAME", obj.getString("AIDNAME"));
				} else {
					map.put("AIDNAME", "");
				}
				// 申请人
				if (isEmperty(obj.getString("APPLICANT"))) {
					map.put("APPLICANT", obj.getString("APPLICANT"));
				} else {
					map.put("APPLICANT", "");
				}
				// 申请原因
				if (isEmperty(obj.getString("REASON"))) {
					map.put("REASON", obj.getString("REASON"));
				} else {
					map.put("REASON", "");
				}
				// 申请时间
				if (isEmperty(obj.getString("APPTIME"))) {
					map.put("APPTIME", obj.getString("APPTIME"));
				} else {
					map.put("APPTIME", "");
				}
				// 申请人或救助站联系电话
				if (isEmperty(obj.getString("APPPHONE"))) {
					map.put("APPPHONE", obj.getString("APPPHONE"));
				} else {
					map.put("APPPHONE", "");
				}
				// 救护动物名称
				if (isEmperty(obj.getString("ANIMALNAME"))) {
					map.put("ANIMALNAME", obj.getString("ANIMALNAME"));
				} else {
					map.put("ANIMALNAME", "");
				}
				// 救助动物数量
				if (isEmperty(obj.getString("ANIMALNUM"))) {
					map.put("ANIMALNUM", obj.getString("ANIMALNUM"));
				} else {
					map.put("ANIMALNUM", "");
				}
				// 获得救助动物方式
				if (isEmperty(obj.getString("GETMETHOD"))) {
					map.put("GETMETHOD", obj.getString("GETMETHOD"));
				} else {
					map.put("GETMETHOD", "");
				}
				// 是否提交给野馆站审核
				if (isEmperty(obj.getString("ISSUBMIT"))) {
					map.put("ISSUBMIT", obj.getString("ISSUBMIT"));
				} else {
					map.put("ISSUBMIT", "");
				}
				// 批准救护--野馆站审核确定【将状态设为未审核】
				if (isEmperty(obj.getString("ISAIDBEGIN"))) {
					map.put("ISAIDBEGIN", obj.getString("ISAIDBEGIN"));
				} else {
					map.put("ISAIDBEGIN", "");
				}
				// 救助结束
				if (isEmperty(obj.getString("ISAIDEND"))) {
					map.put("ISAIDEND", obj.getString("ISAIDEND"));
				} else {
					map.put("ISAIDEND", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARKER"))) {
					map.put("REMARKER", obj.getString("REMARKER"));
				} else {
					map.put("REMARKER", "");
				}
				// UNITCODE
				if (isEmperty(obj.getString("UNITCODE"))) {
					map.put("UNITCODE", obj.getString("UNITCODE"));
				} else {
					map.put("UNITCODE", "");
				}
				// 未审核通过的原因
				if (isEmperty(obj.getString("REASON2"))) {
					map.put("REASON2", obj.getString("REASON2"));
				} else {
					map.put("REASON2", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 驯养繁殖基地 数据 */
	public static List<HashMap<String, String>> getSwdyxXyfzjdJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 基地名称
				if (isEmperty(obj.getString("BASENAME"))) {
					map.put("BASENAME", obj.getString("BASENAME"));
				} else {
					map.put("BASENAME", "");
				}
				// 基地负责人
				if (isEmperty(obj.getString("MANAGER"))) {
					map.put("MANAGER", obj.getString("MANAGER"));
				} else {
					map.put("MANAGER", "");
				}
				// 基地所在地区
				if (isEmperty(obj.getString("AREA"))) {
					map.put("AREA", obj.getString("AREA"));
				} else {
					map.put("AREA", "");
				}
				// 基地地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				// 基地性质代码
				if (isEmperty(obj.getString("PROPERTY"))) {
					map.put("PROPERTY", obj.getString("PROPERTY"));
				} else {
					map.put("PROPERTY", "");
				}
				// 登记日期
				if (isEmperty(obj.getString("REGDATE"))) {
					map.put("REGDATE", obj.getString("REGDATE"));
				} else {
					map.put("REGDATE", "");
				}
				// 联系电话
				if (isEmperty(obj.getString("PHONE"))) {
					map.put("PHONE", obj.getString("PHONE"));
				} else {
					map.put("PHONE", "");
				}
				// 经营范围
				if (isEmperty(obj.getString("BUSISCOPE"))) {
					map.put("BUSISCOPE", obj.getString("BUSISCOPE"));
				} else {
					map.put("BUSISCOPE", "");
				}
				// 销往地点
				if (isEmperty(obj.getString("SELLPLACE"))) {
					map.put("SELLPLACE", obj.getString("SELLPLACE"));
				} else {
					map.put("SELLPLACE", "");
				}
				// 经营用途
				if (isEmperty(obj.getString("BUSIUSE"))) {
					map.put("BUSIUSE", obj.getString("BUSIUSE"));
				} else {
					map.put("BUSIUSE", "");
				}
				// 经办人
				if (isEmperty(obj.getString("TRANSACTOR"))) {
					map.put("TRANSACTOR", obj.getString("TRANSACTOR"));
				} else {
					map.put("TRANSACTOR", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// 单位代码
				if (isEmperty(obj.getString("UNITCODE"))) {
					map.put("UNITCODE", obj.getString("UNITCODE"));
				} else {
					map.put("UNITCODE", "");
				}
				// 经度
				if (isEmperty(obj.getString("LONGITUDE"))) {
					map.put("LONGITUDE", obj.getString("LONGITUDE"));
				} else {
					map.put("LONGITUDE", "");
				}
				// 纬度
				if (isEmperty(obj.getString("LATITUDE"))) {
					map.put("LATITUDE", obj.getString("LATITUDE"));
				} else {
					map.put("LATITUDE", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 经营动物单位 数据 */
	public static List<HashMap<String, String>> getSwdyxJydwdwJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				// ID
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
				} else {
					map.put("ID", "");
				}
				// 经度
				if (isEmperty(obj.getString("X"))) {
					map.put("X", obj.getString("X"));
				} else {
					map.put("X", "");
				}
				// 纬度
				if (isEmperty(obj.getString("Y"))) {
					map.put("Y", obj.getString("Y"));
				} else {
					map.put("Y", "");
				}
				// 负责人
				if (isEmperty(obj.getString("FZR"))) {
					map.put("FZR", obj.getString("FZR"));
				} else {
					map.put("FZR", "");
				}
				// 联系电话
				if (isEmperty(obj.getString("PHONE"))) {
					map.put("PHONE", obj.getString("PHONE"));
				} else {
					map.put("PHONE", "");
				}
				// 单位地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 标本馆 数据 */
	public static List<HashMap<String, String>> getSwdyxBbgJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				// ID
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
				} else {
					map.put("ID", "");
				}
				// 经度
				if (isEmperty(obj.getString("X"))) {
					map.put("X", obj.getString("X"));
				} else {
					map.put("X", "");
				}
				// 纬度
				if (isEmperty(obj.getString("Y"))) {
					map.put("Y", obj.getString("Y"));
				} else {
					map.put("Y", "");
				}
				// 标本馆名称
				if (isEmperty(obj.getString("BBG"))) {
					map.put("BBG", obj.getString("BBG"));
				} else {
					map.put("BBG", "");
				}
				// 负责人
				if (isEmperty(obj.getString("FZR"))) {
					map.put("FZR", obj.getString("FZR"));
				} else {
					map.put("FZR", "");
				}
				// 联系电话
				if (isEmperty(obj.getString("PHONE"))) {
					map.put("PHONE", obj.getString("PHONE"));
				} else {
					map.put("PHONE", "");
				}
				// 标本馆地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 餐馆 数据 */
	public static List<HashMap<String, String>> getSwdyxCgJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				// ID
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
				} else {
					map.put("ID", "");
				}
				// 经度
				if (isEmperty(obj.getString("X"))) {
					map.put("X", obj.getString("X"));
				} else {
					map.put("X", "");
				}
				// 纬度
				if (isEmperty(obj.getString("Y"))) {
					map.put("Y", obj.getString("Y"));
				} else {
					map.put("Y", "");
				}
				// 餐馆名称
				if (isEmperty(obj.getString("NAME"))) {
					map.put("NAME", obj.getString("NAME"));
				} else {
					map.put("NAME", "");
				}
				// 餐馆地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				// 人均消费
				if (isEmperty(obj.getString("PAY"))) {
					map.put("PAY", obj.getString("PAY"));
				} else {
					map.put("PAY", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 疫源疫病监测站 数据 */
	public static List<HashMap<String, String>> getSwdyxYyybjczJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				// ID
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
				} else {
					map.put("ID", "");
				}
				// 经度
				if (isEmperty(obj.getString("X"))) {
					map.put("X", obj.getString("X"));
				} else {
					map.put("X", "");
				}
				// 纬度
				if (isEmperty(obj.getString("Y"))) {
					map.put("Y", obj.getString("Y"));
				} else {
					map.put("Y", "");
				}
				// 地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				// 监测站名称
				if (isEmperty(obj.getString("NAME"))) {
					map.put("NAME", obj.getString("NAME"));
				} else {
					map.put("NAME", "");
				}
				// 监测站负责人
				if (isEmperty(obj.getString("FZR"))) {
					map.put("FZR", obj.getString("FZR"));
				} else {
					map.put("FZR", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 交易市场 数据 */
	public static List<HashMap<String, String>> getSwdyxJyscJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				// ID
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
				} else {
					map.put("ID", "");
				}
				// 经度
				if (isEmperty(obj.getString("X"))) {
					map.put("X", obj.getString("X"));
				} else {
					map.put("X", "");
				}
				// 纬度
				if (isEmperty(obj.getString("Y"))) {
					map.put("Y", obj.getString("Y"));
				} else {
					map.put("Y", "");
				}
				// 地址
				if (isEmperty(obj.getString("ADDRESS"))) {
					map.put("ADDRESS", obj.getString("ADDRESS"));
				} else {
					map.put("ADDRESS", "");
				}
				// 交易市场名称
				if (isEmperty(obj.getString("NAME"))) {
					map.put("NAME", obj.getString("NAME"));
				} else {
					map.put("NAME", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的 有害生物踏查点 数据 */
	public static List<HashMap<String, String>> getYhswTcdJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 调查点编号
				if (isEmperty(obj.getString("DCDID"))) {
					map.put("DCDID", obj.getString("DCDID"));
				} else {
					map.put("DCDID", "");
				}
				// 调查路线编号
				if (isEmperty(obj.getString("DCLXID"))) {
					map.put("DCLXID", obj.getString("DCLXID"));
				} else {
					map.put("DCLXID", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCPERSON"))) {
					map.put("DCPERSON", obj.getString("DCPERSON"));
				} else {
					map.put("DCPERSON", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCTIME"))) {
					map.put("DCTIME", obj.getString("DCTIME"));
				} else {
					map.put("DCTIME", "");
				}
				// 调查部门
				if (isEmperty(obj.getString("DCDEPARTMENT"))) {
					map.put("DCDEPARTMENT", obj.getString("DCDEPARTMENT"));
				} else {
					map.put("DCDEPARTMENT", "");
				}
				// 经度
				if (isEmperty(obj.getString("LON"))) {
					map.put("LON", obj.getString("LON"));
				} else {
					map.put("LON", "");
				}
				// 纬度
				if (isEmperty(obj.getString("LAT"))) {
					map.put("LAT", obj.getString("LAT"));
				} else {
					map.put("LAT", "");
				}
				// 海拔
				if (isEmperty(obj.getString("ALTITUDE"))) {
					map.put("ALTITUDE", obj.getString("ALTITUDE"));
				} else {
					map.put("ALTITUDE", "");
				}
				// 小地名
				if (isEmperty(obj.getString("TOPONYMY"))) {
					map.put("TOPONYMY", obj.getString("TOPONYMY"));
				} else {
					map.put("TOPONYMY", "");
				}
				// 细斑号
				if (isEmperty(obj.getString("XBH"))) {
					map.put("XBH", obj.getString("XBH"));
				} else {
					map.put("XBH", "");
				}
				// 细斑面积
				if (isEmperty(obj.getString("XBAREA"))) {
					map.put("XBAREA", obj.getString("XBAREA"));
				} else {
					map.put("XBAREA", "");
				}
				// 寄主名称
				if (isEmperty(obj.getString("JZNAME"))) {
					map.put("JZNAME", obj.getString("JZNAME"));
				} else {
					map.put("JZNAME", "");
				}
				// 传播途径
				if (isEmperty(obj.getString("CBTJ"))) {
					map.put("CBTJ", obj.getString("CBTJ"));
				} else {
					map.put("CBTJ", "");
				}
				// 林分组成
				if (isEmperty(obj.getString("LFZC"))) {
					map.put("LFZC", obj.getString("LFZC"));
				} else {
					map.put("LFZC", "");
				}
				// 危害部位
				if (isEmperty(obj.getString("WHBW"))) {
					map.put("WHBW", obj.getString("WHBW"));
				} else {
					map.put("WHBW", "");
				}
				// 有害生物名称
				if (isEmperty(obj.getString("YHSWNAME"))) {
					map.put("YHSWNAME", obj.getString("YHSWNAME"));
				} else {
					map.put("YHSWNAME", "");
				}
				// 目测危害程度
				if (isEmperty(obj.getString("MCWHCD"))) {
					map.put("MCWHCD", obj.getString("MCWHCD"));
				} else {
					map.put("MCWHCD", "");
				}
				// 虫态
				if (isEmperty(obj.getString("CT"))) {
					map.put("CT", obj.getString("CT"));
				} else {
					map.put("CT", "");
				}
				// 目测危害面积
				if (isEmperty(obj.getString("MCWHMJ"))) {
					map.put("MCWHMJ", obj.getString("MCWHMJ"));
				} else {
					map.put("MCWHMJ", "");
				}
				// 来源
				if (isEmperty(obj.getString("SOURCE"))) {
					map.put("SOURCE", obj.getString("SOURCE"));
				} else {
					map.put("SOURCE", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("UPLOADSTATUS"))) {
					map.put("UPLOADSTATUS", obj.getString("UPLOADSTATUS"));
				} else {
					map.put("UPLOADSTATUS", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的 有害生物踏查路线 数据 */
	public static List<HashMap<String, String>> getYhswTclxJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 踏查路线编号
				if (isEmperty(obj.getString("TCLXID"))) {
					map.put("TCLXID", obj.getString("TCLXID"));
				} else {
					map.put("TCLXID", "");
				}
				// 踏查人
				if (isEmperty(obj.getString("TCPERSON"))) {
					map.put("TCPERSON", obj.getString("TCPERSON"));
				} else {
					map.put("TCPERSON", "");
				}
				// 踏查时间
				if (isEmperty(obj.getString("TCTIME"))) {
					map.put("TCTIME", obj.getString("TCTIME"));
				} else {
					map.put("TCTIME", "");
				}
				// 踏查单位
				if (isEmperty(obj.getString("TCDEPARTMENT"))) {
					map.put("TCDEPARTMENT", obj.getString("TCDEPARTMENT"));
				} else {
					map.put("TCDEPARTMENT", "");
				}
				// 踏查代表面积
				if (isEmperty(obj.getString("TCDBMJ"))) {
					map.put("TCDBMJ", obj.getString("TCDBMJ"));
				} else {
					map.put("TCDBMJ", "");
				}
				// 踏查起点经度
				if (isEmperty(obj.getString("TCQDLON"))) {
					map.put("TCQDLON", obj.getString("TCQDLON"));
				} else {
					map.put("TCQDLON", "");
				}
				// 踏查起点纬度
				if (isEmperty(obj.getString("TCQDLAT"))) {
					map.put("TCQDLAT", obj.getString("TCQDLAT"));
				} else {
					map.put("TCQDLAT", "");
				}
				// 踏查终点经度
				if (isEmperty(obj.getString("TCZDLON"))) {
					map.put("TCZDLON", obj.getString("TCZDLON"));
				} else {
					map.put("TCZDLON", "");
				}
				// 踏查终点纬度
				if (isEmperty(obj.getString("TCZDLAT"))) {
					map.put("TCZDLAT", obj.getString("TCZDLAT"));
				} else {
					map.put("TCZDLAT", "");
				}
				// 有害生物名称
				if (isEmperty(obj.getString("YHSWNAME"))) {
					map.put("YHSWNAME", obj.getString("YHSWNAME"));
				} else {
					map.put("YHSWNAME", "");
				}
				// 寄主名称
				if (isEmperty(obj.getString("JZMC"))) {
					map.put("JZMC", obj.getString("JZMC"));
				} else {
					map.put("JZMC", "");
				}
				// 林分组成
				if (isEmperty(obj.getString("LFZC"))) {
					map.put("LFZC", obj.getString("LFZC"));
				} else {
					map.put("LFZC", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 备注
				if (isEmperty(obj.getString("REMARK"))) {
					map.put("REMARK", obj.getString("REMARK"));
				} else {
					map.put("REMARK", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("UPLOADSTATUS"))) {
					map.put("UPLOADSTATUS", obj.getString("UPLOADSTATUS"));
				} else {
					map.put("UPLOADSTATUS", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地虫害调查 数据 */
	public static List<HashMap<String, String>> getYhswYdchdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 样地编号
				if (isEmperty(obj.getString("YDBH"))) {
					map.put("YDBH", obj.getString("YDBH"));
				} else {
					map.put("YDBH", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCR"))) {
					map.put("DCR", obj.getString("DCR"));
				} else {
					map.put("DCR", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCTIME"))) {
					map.put("DCTIME", obj.getString("DCTIME"));
				} else {
					map.put("DCTIME", "");
				}
				// 调查单位
				if (isEmperty(obj.getString("DCDW"))) {
					map.put("DCDW", obj.getString("DCDW"));
				} else {
					map.put("DCDW", "");
				}
				// 样地形状
				if (isEmperty(obj.getString("YDXZ"))) {
					map.put("YDXZ", obj.getString("YDXZ"));
				} else {
					map.put("YDXZ", "");
				}
				// 样地长
				if (isEmperty(obj.getString("YDC"))) {
					map.put("YDC", obj.getString("YDC"));
				} else {
					map.put("YDC", "");
				}
				// 样地宽
				if (isEmperty(obj.getString("YDK"))) {
					map.put("YDK", obj.getString("YDK"));
				} else {
					map.put("YDK", "");
				}
				// 样地面积
				if (isEmperty(obj.getString("YDMJ"))) {
					map.put("YDMJ", obj.getString("YDMJ"));
				} else {
					map.put("YDMJ", "");
				}
				// 调查类型
				if (isEmperty(obj.getString("DCLX"))) {
					map.put("DCLX", obj.getString("DCLX"));
				} else {
					map.put("DCLX", "");
				}
				// 样地代表面积
				if (isEmperty(obj.getString("YDDBMJ"))) {
					map.put("YDDBMJ", obj.getString("YDDBMJ"));
				} else {
					map.put("YDDBMJ", "");
				}
				// 灾害面积
				if (isEmperty(obj.getString("ZHMJ"))) {
					map.put("ZHMJ", obj.getString("ZHMJ"));
				} else {
					map.put("ZHMJ", "");
				}
				// 海拔
				if (isEmperty(obj.getString("HB"))) {
					map.put("HB", obj.getString("HB"));
				} else {
					map.put("HB", "");
				}
				// 右侧角经度
				if (isEmperty(obj.getString("YCJJD"))) {
					map.put("YCJJD", obj.getString("YCJJD"));
				} else {
					map.put("YCJJD", "");
				}
				// 右侧角纬度
				if (isEmperty(obj.getString("YCJWD"))) {
					map.put("YCJWD", obj.getString("YCJWD"));
				} else {
					map.put("YCJWD", "");
				}
				// 细斑号
				if (isEmperty(obj.getString("XBH"))) {
					map.put("XBH", obj.getString("XBH"));
				} else {
					map.put("XBH", "");
				}
				// 细斑面积
				if (isEmperty(obj.getString("XBMJ"))) {
					map.put("XBMJ", obj.getString("XBMJ"));
				} else {
					map.put("XBMJ", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 小地名
				if (isEmperty(obj.getString("XDM"))) {
					map.put("XDM", obj.getString("XDM"));
				} else {
					map.put("XDM", "");
				}
				// 寄主名称
				if (isEmperty(obj.getString("JZMC"))) {
					map.put("JZMC", obj.getString("JZMC"));
				} else {
					map.put("JZMC", "");
				}
				// 害虫名称
				if (isEmperty(obj.getString("HCMC"))) {
					map.put("HCMC", obj.getString("HCMC"));
				} else {
					map.put("HCMC", "");
				}
				// 虫态
				if (isEmperty(obj.getString("CT"))) {
					map.put("CT", obj.getString("CT"));
				} else {
					map.put("CT", "");
				}
				// 危害部位
				if (isEmperty(obj.getString("WHBW"))) {
					map.put("WHBW", obj.getString("WHBW"));
				} else {
					map.put("WHBW", "");
				}
				// 危害程度
				if (isEmperty(obj.getString("WHCD"))) {
					map.put("WHCD", obj.getString("WHCD"));
				} else {
					map.put("WHCD", "");
				}
				// 来源
				if (isEmperty(obj.getString("SOURCE"))) {
					map.put("SOURCE", obj.getString("SOURCE"));
				} else {
					map.put("SOURCE", "");
				}
				// 传播途径
				if (isEmperty(obj.getString("CBTJ"))) {
					map.put("CBTJ", obj.getString("CBTJ"));
				} else {
					map.put("CBTJ", "");
				}
				// 虫龄
				if (isEmperty(obj.getString("CAGE"))) {
					map.put("CAGE", obj.getString("CAGE"));
				} else {
					map.put("CAGE", "");
				}
				// 虫珠率
				if (isEmperty(obj.getString("CZL"))) {
					map.put("CZL", obj.getString("CZL"));
				} else {
					map.put("CZL", "");
				}
				// 虫口密度
				if (isEmperty(obj.getString("CKMD"))) {
					map.put("CKMD", obj.getString("CKMD"));
				} else {
					map.put("CKMD", "");
				}
				// 树龄
				if (isEmperty(obj.getString("SAGE"))) {
					map.put("SAGE", obj.getString("SAGE"));
				} else {
					map.put("SAGE", "");
				}
				// 郁闭度
				if (isEmperty(obj.getString("YBD"))) {
					map.put("YBD", obj.getString("YBD"));
				} else {
					map.put("YBD", "");
				}
				// 健康珠数
				if (isEmperty(obj.getString("JKZS"))) {
					map.put("JKZS", obj.getString("JKZS"));
				} else {
					map.put("JKZS", "");
				}
				// 死亡珠数
				if (isEmperty(obj.getString("SWZS"))) {
					map.put("SWZS", obj.getString("SWZS"));
				} else {
					map.put("SWZS", "");
				}
				// 样坑号
				if (isEmperty(obj.getString("YKH"))) {
					map.put("YKH", obj.getString("YKH"));
				} else {
					map.put("YKH", "");
				}
				// 样坑深度
				if (isEmperty(obj.getString("YKSD"))) {
					map.put("YKSD", obj.getString("YKSD"));
				} else {
					map.put("YKSD", "");
				}
				// 坡位
				if (isEmperty(obj.getString("PW"))) {
					map.put("PW", obj.getString("PW"));
				} else {
					map.put("PW", "");
				}
				// 坡向
				if (isEmperty(obj.getString("PX"))) {
					map.put("PX", obj.getString("PX"));
				} else {
					map.put("PX", "");
				}
				// 果实类型
				if (isEmperty(obj.getString("GSLX"))) {
					map.put("GSLX", obj.getString("GSLX"));
				} else {
					map.put("GSLX", "");
				}
				// 苗圃名称
				if (isEmperty(obj.getString("MPMC"))) {
					map.put("MPMC", obj.getString("MPMC"));
				} else {
					map.put("MPMC", "");
				}
				// 苗圃总面积
				if (isEmperty(obj.getString("MPZMJ"))) {
					map.put("MPZMJ", obj.getString("MPZMJ"));
				} else {
					map.put("MPZMJ", "");
				}
				// 产苗量
				if (isEmperty(obj.getString("CML"))) {
					map.put("CML", obj.getString("CML"));
				} else {
					map.put("CML", "");
				}
				// 主要苗木品种
				if (isEmperty(obj.getString("ZYMMPZ"))) {
					map.put("ZYMMPZ", obj.getString("ZYMMPZ"));
				} else {
					map.put("ZYMMPZ", "");
				}
				// 备注
				if (isEmperty(obj.getString("BZ"))) {
					map.put("BZ", obj.getString("BZ"));
				} else {
					map.put("BZ", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("SCZT"))) {
					map.put("SCZT", obj.getString("SCZT"));
				} else {
					map.put("SCZT", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地虫害详查 数据 */
	public static List<HashMap<String, String>> getYhswYdchxcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 样地编号
				if (isEmperty(obj.getString("YDBH"))) {
					map.put("YDBH", obj.getString("YDBH"));
				} else {
					map.put("YDBH", "");
				}
				// 样树编号
				if (isEmperty(obj.getString("YSBH"))) {
					map.put("YSBH", obj.getString("YSBH"));
				} else {
					map.put("YSBH", "");
				}
				// 卵
				if (isEmperty(obj.getString("LUAN"))) {
					map.put("LUAN", obj.getString("LUAN"));
				} else {
					map.put("LUAN", "");
				}
				// 幼虫
				if (isEmperty(obj.getString("YC"))) {
					map.put("YC", obj.getString("YC"));
				} else {
					map.put("YC", "");
				}
				// 蛹
				if (isEmperty(obj.getString("YONG"))) {
					map.put("YONG", obj.getString("YONG"));
				} else {
					map.put("YONG", "");
				}
				// 成虫
				if (isEmperty(obj.getString("CC"))) {
					map.put("CC", obj.getString("CC"));
				} else {
					map.put("CC", "");
				}
				// 虫道
				if (isEmperty(obj.getString("CD"))) {
					map.put("CD", obj.getString("CD"));
				} else {
					map.put("CD", "");
				}
				// 树高
				if (isEmperty(obj.getString("SG"))) {
					map.put("SG", obj.getString("SG"));
				} else {
					map.put("SG", "");
				}
				// 胸径
				if (isEmperty(obj.getString("XJ"))) {
					map.put("XJ", obj.getString("XJ"));
				} else {
					map.put("XJ", "");
				}
				// 冠幅
				if (isEmperty(obj.getString("GF"))) {
					map.put("GF", obj.getString("GF"));
				} else {
					map.put("GF", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地病害调查 数据 */
	public static List<HashMap<String, String>> getYhswYdbhdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 样地编号
				if (isEmperty(obj.getString("YDBH"))) {
					map.put("YDBH", obj.getString("YDBH"));
				} else {
					map.put("YDBH", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCR"))) {
					map.put("DCR", obj.getString("DCR"));
				} else {
					map.put("DCR", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCSJ"))) {
					map.put("DCSJ", obj.getString("DCSJ"));
				} else {
					map.put("DCSJ", "");
				}
				// 调查单位
				if (isEmperty(obj.getString("DCDW"))) {
					map.put("DCDW", obj.getString("DCDW"));
				} else {
					map.put("DCDW", "");
				}
				// 样地形状
				if (isEmperty(obj.getString("YDXZ"))) {
					map.put("YDXZ", obj.getString("YDXZ"));
				} else {
					map.put("YDXZ", "");
				}
				// 样地长
				if (isEmperty(obj.getString("YDC"))) {
					map.put("YDC", obj.getString("YDC"));
				} else {
					map.put("YDC", "");
				}
				// 样地宽
				if (isEmperty(obj.getString("YDK"))) {
					map.put("YDK", obj.getString("YDK"));
				} else {
					map.put("YDK", "");
				}
				// 样地面积
				if (isEmperty(obj.getString("YDMJ"))) {
					map.put("YDMJ", obj.getString("YDMJ"));
				} else {
					map.put("YDMJ", "");
				}
				// 调查类型
				if (isEmperty(obj.getString("DCLX"))) {
					map.put("DCLX", obj.getString("DCLX"));
				} else {
					map.put("DCLX", "");
				}
				// 样地代表面积
				if (isEmperty(obj.getString("YDDBMJ"))) {
					map.put("YDDBMJ", obj.getString("YDDBMJ"));
				} else {
					map.put("YDDBMJ", "");
				}
				// 灾害面积
				if (isEmperty(obj.getString("ZHMJ"))) {
					map.put("ZHMJ", obj.getString("ZHMJ"));
				} else {
					map.put("ZHMJ", "");
				}
				// 海拔
				if (isEmperty(obj.getString("HB"))) {
					map.put("HB", obj.getString("HB"));
				} else {
					map.put("HB", "");
				}
				// 右侧角经度
				if (isEmperty(obj.getString("YCJLON"))) {
					map.put("YCJLON", obj.getString("YCJLON"));
				} else {
					map.put("YCJLON", "");
				}
				// 右侧角纬度
				if (isEmperty(obj.getString("YCJLAT"))) {
					map.put("YCJLAT", obj.getString("YCJLAT"));
				} else {
					map.put("YCJLAT", "");
				}
				// 细斑号
				if (isEmperty(obj.getString("XBH"))) {
					map.put("XBH", obj.getString("XBH"));
				} else {
					map.put("XBH", "");
				}
				// 细斑面积
				if (isEmperty(obj.getString("XBMJ"))) {
					map.put("XBMJ", obj.getString("XBMJ"));
				} else {
					map.put("XBMJ", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 小地名
				if (isEmperty(obj.getString("XDM"))) {
					map.put("XDM", obj.getString("XDM"));
				} else {
					map.put("XDM", "");
				}
				// 寄主名称
				if (isEmperty(obj.getString("JZMC"))) {
					map.put("JZMC", obj.getString("JZMC"));
				} else {
					map.put("JZMC", "");
				}
				// 病害名称
				if (isEmperty(obj.getString("BHMC"))) {
					map.put("BHMC", obj.getString("BHMC"));
				} else {
					map.put("BHMC", "");
				}
				// 分布类型
				if (isEmperty(obj.getString("FBLX"))) {
					map.put("FBLX", obj.getString("FBLX"));
				} else {
					map.put("FBLX", "");
				}
				// 来源
				if (isEmperty(obj.getString("LY"))) {
					map.put("LY", obj.getString("LY"));
				} else {
					map.put("LY", "");
				}
				// 危害程度
				if (isEmperty(obj.getString("WHCD"))) {
					map.put("WHCD", obj.getString("WHCD"));
				} else {
					map.put("WHCD", "");
				}
				// 传播途径
				if (isEmperty(obj.getString("CBTJ"))) {
					map.put("CBTJ", obj.getString("CBTJ"));
				} else {
					map.put("CBTJ", "");
				}
				// 果实类型
				if (isEmperty(obj.getString("GSLX"))) {
					map.put("GSLX", obj.getString("GSLX"));
				} else {
					map.put("GSLX", "");
				}
				// 病感率
				if (isEmperty(obj.getString("BGL"))) {
					map.put("BGL", obj.getString("BGL"));
				} else {
					map.put("BGL", "");
				}
				// 病感指数
				if (isEmperty(obj.getString("BGZS"))) {
					map.put("BGZS", obj.getString("BGZS"));
				} else {
					map.put("BGZS", "");
				}
				// 平均胸径
				if (isEmperty(obj.getString("PJXJ"))) {
					map.put("PJXJ", obj.getString("PJXJ"));
				} else {
					map.put("PJXJ", "");
				}
				// 树龄
				if (isEmperty(obj.getString("SL"))) {
					map.put("SL", obj.getString("SL"));
				} else {
					map.put("SL", "");
				}
				// 郁闭度
				if (isEmperty(obj.getString("YBD"))) {
					map.put("YBD", obj.getString("YBD"));
				} else {
					map.put("YBD", "");
				}
				// 坡度
				if (isEmperty(obj.getString("PD"))) {
					map.put("PD", obj.getString("PD"));
				} else {
					map.put("PD", "");
				}
				// 地形
				if (isEmperty(obj.getString("DX"))) {
					map.put("DX", obj.getString("DX"));
				} else {
					map.put("DX", "");
				}
				// 样坑号
				if (isEmperty(obj.getString("YKH"))) {
					map.put("YKH", obj.getString("YKH"));
				} else {
					map.put("YKH", "");
				}
				// 样坑深度
				if (isEmperty(obj.getString("YKSD"))) {
					map.put("YKSD", obj.getString("YKSD"));
				} else {
					map.put("YKSD", "");
				}
				// 平均高
				if (isEmperty(obj.getString("PJG"))) {
					map.put("PJG", obj.getString("PJG"));
				} else {
					map.put("PJG", "");
				}
				// 有害植物盖度
				if (isEmperty(obj.getString("YHZWGD"))) {
					map.put("YHZWGD", obj.getString("YHZWGD"));
				} else {
					map.put("YHZWGD", "");
				}
				// 苗圃名称
				if (isEmperty(obj.getString("MPMC"))) {
					map.put("MPMC", obj.getString("MPMC"));
				} else {
					map.put("MPMC", "");
				}
				// 苗圃总面积
				if (isEmperty(obj.getString("MPZMJ"))) {
					map.put("MPZMJ", obj.getString("MPZMJ"));
				} else {
					map.put("MPZMJ", "");
				}
				// 产苗量
				if (isEmperty(obj.getString("CML"))) {
					map.put("CML", obj.getString("CML"));
				} else {
					map.put("CML", "");
				}
				// 主要苗木品种
				if (isEmperty(obj.getString("ZYMMPZ"))) {
					map.put("ZYMMPZ", obj.getString("ZYMMPZ"));
				} else {
					map.put("ZYMMPZ", "");
				}
				// 备注
				if (isEmperty(obj.getString("BZ"))) {
					map.put("BZ", obj.getString("BZ"));
				} else {
					map.put("BZ", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("SCZT"))) {
					map.put("SCZT", obj.getString("SCZT"));
				} else {
					map.put("SCZT", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地病害详查 数据 */
	public static List<HashMap<String, String>> getYhswYdbhxcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 样地编号
				if (isEmperty(obj.getString("YDBH"))) {
					map.put("YDBH", obj.getString("YDBH"));
				} else {
					map.put("YDBH", "");
				}
				// 样树编号
				if (isEmperty(obj.getString("YSBH"))) {
					map.put("YSBH", obj.getString("YSBH"));
				} else {
					map.put("YSBH", "");
				}
				// 病害分级
				if (isEmperty(obj.getString("BHFJ"))) {
					map.put("BHFJ", obj.getString("BHFJ"));
				} else {
					map.put("BHFJ", "");
				}
				// 树高
				if (isEmperty(obj.getString("SG"))) {
					map.put("SG", obj.getString("SG"));
				} else {
					map.put("SG", "");
				}
				// 胸径
				if (isEmperty(obj.getString("XJ"))) {
					map.put("XJ", obj.getString("XJ"));
				} else {
					map.put("XJ", "");
				}
				// 冠幅
				if (isEmperty(obj.getString("GF"))) {
					map.put("GF", obj.getString("GF"));
				} else {
					map.put("GF", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物有害植物调查 数据 */
	public static List<HashMap<String, String>> getYhswYhzwdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 样地编号
				if (isEmperty(obj.getString("YDBH"))) {
					map.put("YDBH", obj.getString("YDBH"));
				} else {
					map.put("YDBH", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCR"))) {
					map.put("DCR", obj.getString("DCR"));
				} else {
					map.put("DCR", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCSJ"))) {
					map.put("DCSJ", obj.getString("DCSJ"));
				} else {
					map.put("DCSJ", "");
				}
				// 调查单位
				if (isEmperty(obj.getString("DCDW"))) {
					map.put("DCDW", obj.getString("DCDW"));
				} else {
					map.put("DCDW", "");
				}
				// 样地形状
				if (isEmperty(obj.getString("YDXZ"))) {
					map.put("YDXZ", obj.getString("YDXZ"));
				} else {
					map.put("YDXZ", "");
				}
				// 样地长
				if (isEmperty(obj.getString("YDC"))) {
					map.put("YDC", obj.getString("YDC"));
				} else {
					map.put("YDC", "");
				}
				// 样地宽
				if (isEmperty(obj.getString("YDK"))) {
					map.put("YDK", obj.getString("YDK"));
				} else {
					map.put("YDK", "");
				}
				// 样地面积
				if (isEmperty(obj.getString("YDMJ"))) {
					map.put("YDMJ", obj.getString("YDMJ"));
				} else {
					map.put("YDMJ", "");
				}
				// 样地代表面积
				if (isEmperty(obj.getString("YDDBMJ"))) {
					map.put("YDDBMJ", obj.getString("YDDBMJ"));
				} else {
					map.put("YDDBMJ", "");
				}
				// 灾害面积
				if (isEmperty(obj.getString("ZHMJ"))) {
					map.put("ZHMJ", obj.getString("ZHMJ"));
				} else {
					map.put("ZHMJ", "");
				}
				// 海拔
				if (isEmperty(obj.getString("HB"))) {
					map.put("HB", obj.getString("HB"));
				} else {
					map.put("HB", "");
				}
				// 右侧角经度
				if (isEmperty(obj.getString("YCJLON"))) {
					map.put("YCJLON", obj.getString("YCJLON"));
				} else {
					map.put("YCJLON", "");
				}
				// 右侧角纬度
				if (isEmperty(obj.getString("YCJLAT"))) {
					map.put("YCJLAT", obj.getString("YCJLAT"));
				} else {
					map.put("YCJLAT", "");
				}
				// 细斑号
				if (isEmperty(obj.getString("XBH"))) {
					map.put("XBH", obj.getString("XBH"));
				} else {
					map.put("XBH", "");
				}
				// 细斑面积
				if (isEmperty(obj.getString("XBMJ"))) {
					map.put("XBMJ", obj.getString("XBMJ"));
				} else {
					map.put("XBMJ", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 寄主名称
				if (isEmperty(obj.getString("JZMC"))) {
					map.put("JZMC", obj.getString("JZMC"));
				} else {
					map.put("JZMC", "");
				}
				// 有害植物名称
				if (isEmperty(obj.getString("YHZWMC"))) {
					map.put("YHZWMC", obj.getString("YHZWMC"));
				} else {
					map.put("YHZWMC", "");
				}
				// 有害植物高度
				if (isEmperty(obj.getString("YHZWGD"))) {
					map.put("YHZWGD", obj.getString("YHZWGD"));
				} else {
					map.put("YHZWGD", "");
				}
				// 有害植物地径
				if (isEmperty(obj.getString("YHZWDJ"))) {
					map.put("YHZWDJ", obj.getString("YHZWDJ"));
				} else {
					map.put("YHZWDJ", "");
				}
				// 危害植物名称
				if (isEmperty(obj.getString("WHZWMC"))) {
					map.put("WHZWMC", obj.getString("WHZWMC"));
				} else {
					map.put("WHZWMC", "");
				}
				// 危害部位
				if (isEmperty(obj.getString("WHBW"))) {
					map.put("WHBW", obj.getString("WHBW"));
				} else {
					map.put("WHBW", "");
				}
				// 危害程度
				if (isEmperty(obj.getString("WHCD"))) {
					map.put("WHCD", obj.getString("WHCD"));
				} else {
					map.put("WHCD", "");
				}
				// 来源
				if (isEmperty(obj.getString("LY"))) {
					map.put("LY", obj.getString("LY"));
				} else {
					map.put("LY", "");
				}
				// 传播途径
				if (isEmperty(obj.getString("CBTJ"))) {
					map.put("CBTJ", obj.getString("CBTJ"));
				} else {
					map.put("CBTJ", "");
				}
				// 被害枝干数
				if (isEmperty(obj.getString("BHZGS"))) {
					map.put("BHZGS", obj.getString("BHZGS"));
				} else {
					map.put("BHZGS", "");
				}
				// 枝干总数
				if (isEmperty(obj.getString("ZGZS"))) {
					map.put("ZGZS", obj.getString("ZGZS"));
				} else {
					map.put("ZGZS", "");
				}
				// 危害率
				if (isEmperty(obj.getString("WHL"))) {
					map.put("WHL", obj.getString("WHL"));
				} else {
					map.put("WHL", "");
				}
				// 危害植物生长状况
				if (isEmperty(obj.getString("WHZWSZZK"))) {
					map.put("WHZWSZZK", obj.getString("WHZWSZZK"));
				} else {
					map.put("WHZWSZZK", "");
				}
				// 平均胸径
				if (isEmperty(obj.getString("PJXJ"))) {
					map.put("PJXJ", obj.getString("PJXJ"));
				} else {
					map.put("PJXJ", "");
				}
				// 平均高
				if (isEmperty(obj.getString("PJG"))) {
					map.put("PJG", obj.getString("PJG"));
				} else {
					map.put("PJG", "");
				}
				// 树龄
				if (isEmperty(obj.getString("SL"))) {
					map.put("SL", obj.getString("SL"));
				} else {
					map.put("SL", "");
				}
				// 郁闭度
				if (isEmperty(obj.getString("YBD"))) {
					map.put("YBD", obj.getString("YBD"));
				} else {
					map.put("YBD", "");
				}
				// 地形
				if (isEmperty(obj.getString("DX"))) {
					map.put("DX", obj.getString("DX"));
				} else {
					map.put("DX", "");
				}
				// 地径
				if (isEmperty(obj.getString("DJ"))) {
					map.put("DJ", obj.getString("DJ"));
				} else {
					map.put("DJ", "");
				}
				// 分布类型
				if (isEmperty(obj.getString("FBLX"))) {
					map.put("FBLX", obj.getString("FBLX"));
				} else {
					map.put("FBLX", "");
				}
				// 坡度
				if (isEmperty(obj.getString("PD"))) {
					map.put("PD", obj.getString("PD"));
				} else {
					map.put("PD", "");
				}
				// 样坑深度
				if (isEmperty(obj.getString("YKSD"))) {
					map.put("YKSD", obj.getString("YKSD"));
				} else {
					map.put("YKSD", "");
				}
				// 备注
				if (isEmperty(obj.getString("BZ"))) {
					map.put("BZ", obj.getString("BZ"));
				} else {
					map.put("BZ", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("SCZT"))) {
					map.put("SCZT", obj.getString("SCZT"));
				} else {
					map.put("SCZT", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物木材病虫害调查 数据 */
	public static List<HashMap<String, String>> getYhswMcbchdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 木材病虫害编号
				if (isEmperty(obj.getString("MCBCHBH"))) {
					map.put("MCBCHBH", obj.getString("MCBCHBH"));
				} else {
					map.put("MCBCHBH", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCR"))) {
					map.put("DCR", obj.getString("DCR"));
				} else {
					map.put("DCR", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCSJ"))) {
					map.put("DCSJ", obj.getString("DCSJ"));
				} else {
					map.put("DCSJ", "");
				}
				// 调查单位
				if (isEmperty(obj.getString("DCDW"))) {
					map.put("DCDW", obj.getString("DCDW"));
				} else {
					map.put("DCDW", "");
				}
				// 被调查单位
				if (isEmperty(obj.getString("BDCDW"))) {
					map.put("BDCDW", obj.getString("BDCDW"));
				} else {
					map.put("BDCDW", "");
				}
				// 调查点经度
				if (isEmperty(obj.getString("DCDJD"))) {
					map.put("DCDJD", obj.getString("DCDJD"));
				} else {
					map.put("DCDJD", "");
				}
				// 调查点纬度
				if (isEmperty(obj.getString("DCDWD"))) {
					map.put("DCDWD", obj.getString("DCDWD"));
				} else {
					map.put("DCDWD", "");
				}
				// 主要加工产品
				if (isEmperty(obj.getString("ZYJGCP"))) {
					map.put("ZYJGCP", obj.getString("ZYJGCP"));
				} else {
					map.put("ZYJGCP", "");
				}
				// 小地名
				if (isEmperty(obj.getString("XDM"))) {
					map.put("XDM", obj.getString("XDM"));
				} else {
					map.put("XDM", "");
				}
				// 现有库存木材品种
				if (isEmperty(obj.getString("XYKCMCPZ"))) {
					map.put("XYKCMCPZ", obj.getString("XYKCMCPZ"));
				} else {
					map.put("XYKCMCPZ", "");
				}
				// 主要加工经营树种
				if (isEmperty(obj.getString("ZYJGJYSZ"))) {
					map.put("ZYJGJYSZ", obj.getString("ZYJGJYSZ"));
				} else {
					map.put("ZYJGJYSZ", "");
				}
				// 抽查木材数量
				if (isEmperty(obj.getString("CCMCSL"))) {
					map.put("CCMCSL", obj.getString("CCMCSL"));
				} else {
					map.put("CCMCSL", "");
				}
				// 库存量
				if (isEmperty(obj.getString("CCL"))) {
					map.put("CCL", obj.getString("CCL"));
				} else {
					map.put("CCL", "");
				}
				// 抽查木材品种
				if (isEmperty(obj.getString("CCMCPZ"))) {
					map.put("CCMCPZ", obj.getString("CCMCPZ"));
				} else {
					map.put("CCMCPZ", "");
				}
				// 病虫名称
				if (isEmperty(obj.getString("BCMC"))) {
					map.put("BCMC", obj.getString("BCMC"));
				} else {
					map.put("BCMC", "");
				}
				// 危害程度
				if (isEmperty(obj.getString("WHCD"))) {
					map.put("WHCD", obj.getString("WHCD"));
				} else {
					map.put("WHCD", "");
				}
				// 危害部位
				if (isEmperty(obj.getString("WHBW"))) {
					map.put("WHBW", obj.getString("WHBW"));
				} else {
					map.put("WHBW", "");
				}
				// 虫态
				if (isEmperty(obj.getString("CT"))) {
					map.put("CT", obj.getString("CT"));
				} else {
					map.put("CT", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 备注
				if (isEmperty(obj.getString("BZ"))) {
					map.put("BZ", obj.getString("BZ"));
				} else {
					map.put("BZ", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("SCZT"))) {
					map.put("SCZT", obj.getString("SCZT"));
				} else {
					map.put("SCZT", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物诱虫灯调查 数据 */
	public static List<HashMap<String, String>> getYhswYcddcJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 诱虫灯编号
				if (isEmperty(obj.getString("YCDBH"))) {
					map.put("YCDBH", obj.getString("YCDBH"));
				} else {
					map.put("YCDBH", "");
				}
				// 诱虫灯名称
				if (isEmperty(obj.getString("YCDMC"))) {
					map.put("YCDMC", obj.getString("YCDMC"));
				} else {
					map.put("YCDMC", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCR"))) {
					map.put("DCR", obj.getString("DCR"));
				} else {
					map.put("DCR", "");
				}
				// 调查单位
				if (isEmperty(obj.getString("DCDW"))) {
					map.put("DCDW", obj.getString("DCDW"));
				} else {
					map.put("DCDW", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCSJ"))) {
					map.put("DCSJ", obj.getString("DCSJ"));
				} else {
					map.put("DCSJ", "");
				}
				// 调查点经度
				if (isEmperty(obj.getString("DCDJD"))) {
					map.put("DCDJD", obj.getString("DCDJD"));
				} else {
					map.put("DCDJD", "");
				}
				// 调查点纬度
				if (isEmperty(obj.getString("DCDWD"))) {
					map.put("DCDWD", obj.getString("DCDWD"));
				} else {
					map.put("DCDWD", "");
				}
				// 海拔
				if (isEmperty(obj.getString("HB"))) {
					map.put("HB", obj.getString("HB"));
				} else {
					map.put("HB", "");
				}
				// 小地名
				if (isEmperty(obj.getString("XDM"))) {
					map.put("XDM", obj.getString("XDM"));
				} else {
					map.put("XDM", "");
				}
				// 细斑号
				if (isEmperty(obj.getString("XBH"))) {
					map.put("XBH", obj.getString("XBH"));
				} else {
					map.put("XBH", "");
				}
				// 细斑面积
				if (isEmperty(obj.getString("XBMJ"))) {
					map.put("XBMJ", obj.getString("XBMJ"));
				} else {
					map.put("XBMJ", "");
				}
				// 主要害虫名称
				if (isEmperty(obj.getString("ZYHCMC"))) {
					map.put("ZYHCMC", obj.getString("ZYHCMC"));
				} else {
					map.put("ZYHCMC", "");
				}
				// 林分组成
				if (isEmperty(obj.getString("LFZC"))) {
					map.put("LFZC", obj.getString("LFZC"));
				} else {
					map.put("LFZC", "");
				}
				// 害虫数量
				if (isEmperty(obj.getString("HCSL"))) {
					map.put("HCSL", obj.getString("HCSL"));
				} else {
					map.put("HCSL", "");
				}
				// 郁闭度
				if (isEmperty(obj.getString("YBD"))) {
					map.put("YBD", obj.getString("YBD"));
				} else {
					map.put("YBD", "");
				}
				// 平均高
				if (isEmperty(obj.getString("PJG"))) {
					map.put("PJG", obj.getString("PJG"));
				} else {
					map.put("PJG", "");
				}
				// 平均胸径
				if (isEmperty(obj.getString("PJXJ"))) {
					map.put("PJXJ", obj.getString("PJXJ"));
				} else {
					map.put("PJXJ", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 备注
				if (isEmperty(obj.getString("BZ"))) {
					map.put("BZ", obj.getString("BZ"));
				} else {
					map.put("BZ", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("SCZT"))) {
					map.put("SCZT", obj.getString("SCZT"));
				} else {
					map.put("SCZT", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 松材线虫病普查 数据 */
	public static List<HashMap<String, String>> getYhswScxcbpcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				// 样地编号
				if (isEmperty(obj.getString("YDBH"))) {
					map.put("YDBH", obj.getString("YDBH"));
				} else {
					map.put("YDBH", "");
				}
				// 调查人
				if (isEmperty(obj.getString("DCR"))) {
					map.put("DCR", obj.getString("DCR"));
				} else {
					map.put("DCR", "");
				}
				// 调查单位
				if (isEmperty(obj.getString("DCDW"))) {
					map.put("DCDW", obj.getString("DCDW"));
				} else {
					map.put("DCDW", "");
				}
				// 调查时间
				if (isEmperty(obj.getString("DCSJ"))) {
					map.put("DCSJ", obj.getString("DCSJ"));
				} else {
					map.put("DCSJ", "");
				}
				// 枯死松树经度
				if (isEmperty(obj.getString("KSJD"))) {
					map.put("KSJD", obj.getString("KSJD"));
				} else {
					map.put("KSJD", "");
				}
				// 枯死松树纬度
				if (isEmperty(obj.getString("KSWD"))) {
					map.put("KSWD", obj.getString("KSWD"));
				} else {
					map.put("KSWD", "");
				}
				// 细斑号
				if (isEmperty(obj.getString("XBH"))) {
					map.put("XBH", obj.getString("XBH"));
				} else {
					map.put("XBH", "");
				}
				// 细斑面积
				if (isEmperty(obj.getString("XBMJ"))) {
					map.put("XBMJ", obj.getString("XBMJ"));
				} else {
					map.put("XBMJ", "");
				}
				// 危害程度
				if (isEmperty(obj.getString("WHCD"))) {
					map.put("WHCD", obj.getString("WHCD"));
				} else {
					map.put("WHCD", "");
				}
				// 枯死松树面积
				if (isEmperty(obj.getString("KSMJ"))) {
					map.put("KSMJ", obj.getString("KSMJ"));
				} else {
					map.put("KSMJ", "");
				}
				// 树种
				if (isEmperty(obj.getString("SZ"))) {
					map.put("SZ", obj.getString("SZ"));
				} else {
					map.put("SZ", "");
				}
				// 枯死松树数量
				if (isEmperty(obj.getString("KSSL"))) {
					map.put("KSSL", obj.getString("KSSL"));
				} else {
					map.put("KSSL", "");
				}
				// 寄主名称
				if (isEmperty(obj.getString("JZMC"))) {
					map.put("JZMC", obj.getString("JZMC"));
				} else {
					map.put("JZMC", "");
				}
				// 平均高
				if (isEmperty(obj.getString("PJG"))) {
					map.put("PJG", obj.getString("PJG"));
				} else {
					map.put("PJG", "");
				}
				// 平均胸径
				if (isEmperty(obj.getString("PJXJ"))) {
					map.put("PJXJ", obj.getString("PJXJ"));
				} else {
					map.put("PJXJ", "");
				}
				// 取样人
				if (isEmperty(obj.getString("QYR"))) {
					map.put("QYR", obj.getString("QYR"));
				} else {
					map.put("QYR", "");
				}
				// 取样部位
				if (isEmperty(obj.getString("QYBW"))) {
					map.put("QYBW", obj.getString("QYBW"));
				} else {
					map.put("QYBW", "");
				}
				// 取样数量
				if (isEmperty(obj.getString("QYSL"))) {
					map.put("QYSL", obj.getString("QYSL"));
				} else {
					map.put("QYSL", "");
				}
				// 镜检数量
				if (isEmperty(obj.getString("JJSL"))) {
					map.put("JJSL", obj.getString("JJSL"));
				} else {
					map.put("JJSL", "");
				}
				// 小地名
				if (isEmperty(obj.getString("XDM"))) {
					map.put("XDM", obj.getString("XDM"));
				} else {
					map.put("XDM", "");
				}
				// 鉴定人
				if (isEmperty(obj.getString("JDR"))) {
					map.put("JDR", obj.getString("JDR"));
				} else {
					map.put("JDR", "");
				}
				// 鉴定日期
				if (isEmperty(obj.getString("JDRQ"))) {
					map.put("JDRQ", obj.getString("JDRQ"));
				} else {
					map.put("JDRQ", "");
				}
				// 鉴定结果
				if (isEmperty(obj.getString("JDJG"))) {
					map.put("JDJG", obj.getString("JDJG"));
				} else {
					map.put("JDJG", "");
				}
				// 木材有无蓝变
				if (isEmperty(obj.getString("MCLBF"))) {
					map.put("MCLBF", obj.getString("MCLBF"));
				} else {
					map.put("MCLBF", "");
				}
				// 市
				if (isEmperty(obj.getString("CITY"))) {
					map.put("CITY", obj.getString("CITY"));
				} else {
					map.put("CITY", "");
				}
				// 县
				if (isEmperty(obj.getString("COUNTY"))) {
					map.put("COUNTY", obj.getString("COUNTY"));
				} else {
					map.put("COUNTY", "");
				}
				// 镇
				if (isEmperty(obj.getString("TOWN"))) {
					map.put("TOWN", obj.getString("TOWN"));
				} else {
					map.put("TOWN", "");
				}
				// 村
				if (isEmperty(obj.getString("VILLAGE"))) {
					map.put("VILLAGE", obj.getString("VILLAGE"));
				} else {
					map.put("VILLAGE", "");
				}
				// 备注
				if (isEmperty(obj.getString("BZ"))) {
					map.put("BZ", obj.getString("BZ"));
				} else {
					map.put("BZ", "");
				}
				// 上传状态
				if (isEmperty(obj.getString("SCZT"))) {
					map.put("SCZT", obj.getString("SCZT"));
				} else {
					map.put("SCZT", "");
				}
				// 上报人
				if (isEmperty(obj.getString("SBR"))) {
					map.put("SBR", obj.getString("SBR"));
				} else {
					map.put("SBR", "");
				}
				// 上报时间
				if (isEmperty(obj.getString("SBSJ"))) {
					map.put("SBSJ", obj.getString("SBSJ"));
				} else {
					map.put("SBSJ", "");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 判断string字符是否为null 或者 "" */
	public static boolean isEmperty(String str) {
		if (str.equals("") || str == null) {
			return false;
		} else {
			return true;
		}
	}

	/* 判断Object是否为null 或者 "" */
	public static boolean objEmperty(Object str) {
		if (str == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 计算两点之间的距离
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static Double distance(double lat1, double lng1, double lat2,double lng2) {

		Double R = 6370996.81;
		Double x = (lng2 - lng1) * Math.PI * R
				* Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
		Double y = (lat2 - lat1) * Math.PI * R / 180;
		Double distance = Math.hypot(x, y);
		return distance;
	}

	/** dialog 宽度和高度设置 */
	public static void setDialogParams2(Context context, Dialog dialog,double pwidth, double mwidth) {
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		Screen screen = ScreenTool.getScreenPix(context);
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
		}
		params.height = screen.getHeightPixels()/2;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
		dialog.show();
	}

	/** dialog 宽度和高度设置 */
	public static void setDialogParams(Context context, Dialog dialog,double pwidth, double mwidth) {
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		Screen screen = ScreenTool.getScreenPix(context);
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
		}
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.TOP;
		params.y = 150;
		dialog.getWindow().setAttributes(params);
	}
	
	/** dialog 宽度和高度设置 高度为屏幕的1/2 */
	public static void setDialogParam(Context context, Dialog dialog,
			double pwidth, double mwidth,double phight,double mhight) {
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		Screen screen = ScreenTool.getScreenPix(context);
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
			params.height = (int) (screen.getHeightPixels()*phight);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
			params.height = (int) (screen.getHeightPixels()*mhight);
		}
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}
	
	/** dialog 全屏宽度和高度设置 */
	public static void setDialogParamsFull(Context context, Dialog dialog) {
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(params);
	}

	/** 监测GPS是否打开 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}
}
