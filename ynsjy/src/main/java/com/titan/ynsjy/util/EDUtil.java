package com.titan.ynsjy.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;

import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.service.PullParseXml;

import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EDUtil {

	private static EDUtil util;
	private Context context;

	public synchronized static EDUtil getInstance(Context context) {
		if (util == null) {
			try {
				util = new EDUtil(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return util;
	}

	public static void resetUtil() {
		util = null;
	}

	private EDUtil(Context context) throws Exception {
		this.context = context;
	}

	/**
	 * @param attributeName
	 * @param type
	 * @return
	 */
	public static List<Row> getEdAttributeList(Context ctx, String attributeName, String type) {
		AssetManager asset = ctx.getAssets();
		List<Row> list = null;
		try {
			InputStream input = asset.open("ED_" + type + ".xml");
			PullParseXml parseXml = new PullParseXml();
			list = parseXml.PullParseXML(input, attributeName);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @param attributeName
	 * @param type
	 * @return
	 */
	public static String getEdAttributetype(Context ctx, String attributeName,
			String type) {
		AssetManager asset = ctx.getAssets();
		String fieldtype = "";
		try {
			InputStream input = asset.open("ED_" + type + ".xml");
			PullParseXml parseXml = new PullParseXml();
			fieldtype = parseXml.PullParseXMLforFeildType(input, attributeName);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return fieldtype;
	}

	/** 閼惧嘲褰噚ml娑擃厾娈戞禒锝囩垳鐞涳拷 */
	public static List<Row> getAttributeList(Context ctx, String name,
			String xml) {
		AssetManager asset = ctx.getAssets();
		List<Row> list = null;
		try {
			InputStream input = asset.open(xml);
			PullParseXml parseXml = new PullParseXml();
			list = parseXml.PullParseXML(input, name);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}

	//
	@SuppressLint("SimpleDateFormat")
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	public static void isLockScreen(Activity activity) {
		KeyguardManager mKeyguardManager = (KeyguardManager) activity
				.getSystemService(Context.KEYGUARD_SERVICE);

		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
			setAlarm(activity);
		}
	}

	public static void setAlarm(Activity activity) {
		AlarmManager alarmManager = (AlarmManager) activity
				.getSystemService(Context.ALARM_SERVICE);
		int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		long timeOrLengthofWait = 1000;
		String ALARM_ACTION = "ALARM_ACTION";
		Intent intentToFire = new Intent(ALARM_ACTION);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(activity, 0,
				intentToFire, 0);
		// 鐠佸墽鐤咥larm Set the alarm
		alarmManager.set(alarmType, timeOrLengthofWait, alarmIntent);
	}

	public static boolean IsEmpty(Object obj) {
		if (obj != null) {
			return true;
		}
		return false;
	}

	/** 閺傚洣娆㈢憴锝呯槕 */
	public static void decript(String path) {
		try {
			byte[] buffer = File2byte(path);
			ArrayUtils.reverse(buffer);
			FileOutputStream outputStream = new FileOutputStream(new File(path));
			outputStream.write(buffer, 0, buffer.length); 
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static boolean checkEng(String str) {
		return str.matches("^[a-zA-Z]*");
	}

	public static void uninstallAPK(Context context) {
		Uri packageURI = Uri.parse("package:com.example.updateversion");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

	public static void installApk(Context context, String mSavePath,
			HashMap<String, String> mHashMap) {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static double getVersionCode(Context context) {
		double versionCode = 0;
		String versionName;
		try {
			// // 閼惧嘲褰噋ackagemanager閻ㄥ嫬鐤勬笟锟�
			// PackageManager packageManager = getPackageManager();
			// getPackageName()閺勵垯缍樿ぐ鎾冲缁崵娈戦崠鍛倳閿涳拷0娴狅綀銆冮弰顖濆箯閸欐牜澧楅張顑夸繆閹拷
			// PackageInfo packInfo =
			// packageManager.getPackageInfo(getPackageName(),0);
			// String version = packInfo.versionName;
			// 閼惧嘲褰囨潪顖欐閻楀牊婀伴崣鍑ょ礉鐎电懓绨睞ndroidManifest.xml娑撳獘ndroid:versionCode
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

	public static void toggleGPS(Context context) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
	
}
