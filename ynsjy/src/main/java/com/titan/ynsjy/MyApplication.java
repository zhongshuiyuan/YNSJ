package com.titan.ynsjy;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.crashreport.CrashReport;
import com.titan.baselibrary.crash.CrashHandler;
import com.titan.ynsjy.entity.ScreenTool;
import com.titan.ynsjy.service.Webservice;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ConnectionChangeReceiver;
import com.titan.ynsjy.util.NetUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;
import com.titan.ynsjy.util.ZIPUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyApplication extends MultiDexApplication {

	public static MyApplication mApplication;
	public static ResourcesManager resourcesManager;
	public static SharedPreferences sharedPreferences;
	public static ScreenTool.Screen screen;
	/** 移动设备唯一号 */
	public static String macAddress;
	/** 移动设备序列号 */
	public static String mobileXlh;
	/** 移动设备型号 */
	public static String mobileType;
	/** 注册网络 */
	private ConnectionChangeReceiver mNetworkStateReceiver;

	private static MyApplication instance = null;

	public static MyApplication getInstance(){
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/** Bugly SDK初始化
		 * 参数1：上下文对象
		 * 参数2：APPID，平台注册时得到,注意替换成你的appId
		 * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
		 * 发布新版本时需要修改以及bugly isbug需要改成false等部分
		 * 腾讯bugly 在android 4.4版本上有bug 启动报错
		 */
		CrashReport.initCrashReport(getApplicationContext(), "8885f21b4f", true);
		instance = this;
		/** 注册网络监听 */
		//initNetworkReceiver();

		mApplication = this;
		sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		try {
			resourcesManager = ResourcesManager.getInstance(this);
			/** 创建系统存放数据的默认文件夹 */
			//resourcesManager.createFolder();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* 打开GPS */
		//SytemUtil.openGps(this);
		/** 获取设备信息 */
		getMbInfo();
		//new MyAsyncTask().execute("getMbInfo");

		/* 自动注册设备信息到数据库 */
		/*if (netWorkTip()) {
			new MyAsyncTask().execute("addMacAddress");
		}*/


		new MyAsyncTask().execute("copyDatabase");

		/*异常本地记录*/
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	/*@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		//MultiDex.install(this);
	}*/

	/** 获取设备信息 */
	public void getMbInfo() {
		/* 获取mac地址 作为设备唯一号 */
		String mac = BussUtil.getWifiMacAddress(this);
		if (mac != null && !mac.equals("")) {
			macAddress = mac;
			sharedPreferences.edit().putString("mac", macAddress).apply();
		}
		mobileXlh = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
		mobileType = android.os.Build.MANUFACTURER + "——"
				+ android.os.Build.MODEL;// SM-P601 型号
		// android.os.Build.MANUFACTURER;// samsung 厂商
		/* 获取屏幕分辨率 */
		screen = ScreenTool.getScreenPix(this);
	}

	/** 添加设备信息到后台数据库 */
	public void addMacAddress() {
		Webservice webservice = new Webservice(this);
		String result = webservice.addMacAddress(macAddress, mobileXlh,mobileType);

		if (result.equals(Webservice.netException)) {
			// 网络异常
			sharedPreferences.edit().putBoolean(macAddress, false).apply();
		} else if (result.equals("已录入")) {
			// 设备信息已经录入
			sharedPreferences.edit().putBoolean(macAddress, true).apply();
		} else if (result.equals("录入成功")) {
			// 设备信息录入成功
			sharedPreferences.edit().putBoolean(macAddress, true).apply();
		} else if (result.equals("录入失败")) {
			// 设备信息录入失败
			sharedPreferences.edit().putBoolean(macAddress, false).apply();
		}

	}

	/** 异步类 */
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(final String... params) {
			if (params[0].equals("addMacAddress")) {
				addMacAddress();
			} else if (params[0].equals("getMbInfo")) {
				getMbInfo();
			} else if (params[0].equals("copyDatabase")) {
				String path;
				try {
					path = MyApplication.resourcesManager.getTootPath();
					copyDatabase(path, "maps.zip", "maps.zip");
				} catch (jsqlite.Exception e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//CopyAssets("app", path);
				//File file = new File(path+"/maps");
			}
			return null;
		}
	}

	private byte[] InputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/** 注册网络监听 实时监控网络状态 */
//	public void initNetworkReceiver() {
//		ConnectionChangeReceiver.mListeners.add(instance);
//		mNetworkStateReceiver = new ConnectionChangeReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		registerReceiver(mNetworkStateReceiver, filter);
//	}

	/**检查网络连接是否正常*/
    public boolean netWorkTip(){
        if (NetUtil.getNetworkState(instance) == NetUtil.NETWORN_NONE) {
            //Toast.makeText(instance,"网络未连接",Toast.LENGTH_SHORT).show();
			ToastUtil.setToast(instance,"网络未连接");
            return false;
        } else {
            return true;
        }
    }
    /**检查网络连接是否正常*/
    public boolean hasNetWork(){
        if (NetUtil.getNetworkState(instance) == NetUtil.NETWORN_NONE) {
            return false;
        } else {
            return true;
        }
    }

	/**
	 * 复制asset文件到指定目录
	 * @param oldPath asset下的路径
	 * @param newPath SD卡下保存路径
	 */
	private void CopyAssets(String oldPath,String newPath) {
		try {
			String fileNames[] = mApplication.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
			for(String name : fileNames){
				if(name.contains(".")){
					copyDatabase(newPath, oldPath+"/"+name,name);
				}else{
					File file = new File(newPath+"/"+name);
					file.mkdirs();
					CopyAssets(oldPath+"/"+name, newPath+"/"+name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**复制文件到平板中*/
	private void copyFile(String fileDir,String assetPath, String filename){
		File file = new File(fileDir + "/" + filename);
		if (file.exists() && file.isFile()) {
			UnZipTFolder(fileDir, filename);
			return;
		}
		try {
			InputStream db = getResources().getAssets().open(assetPath);
			FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
			byte[] buffer = new byte[8129];
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**复制文件夹到平板中*/
	public void copyFolder(String fileDir,String assetPath, String filename){
		File file = new File(fileDir + "/" + filename);
		if (file.exists() && file.isFile()) {
			UnZipTFolder(fileDir, filename);
			return;
		}
		try {
			InputStream db = getResources().getAssets().open(assetPath);
			FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
			byte[] buffer = new byte[8129];
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 复制默认数据到本地 */
	private void copyDatabase(String fileDir,String assetPath, String filename) {
		File file = new File(fileDir + "/" + filename);
		if (file.exists() && file.isFile()) {
			UnZipTFolder(fileDir, filename);
			return;
		}
		try {
			InputStream db = getResources().getAssets().open(assetPath);
			if(db == null){
				return;
			}
			FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
			byte[] buffer = new byte[1024];//8129
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		UnZipTFolder(fileDir, filename);
	}
	/**解压文件并删除zip文件*/
	public void UnZipTFolder(String fileDir,String filename){
		try {
			//ZIPUtil.UnZipFolder(fileDir+"/"+filename, fileDir+"/");
			ZIPUtil.unzip(fileDir+"/"+filename, fileDir+"/");
		} catch (Exception e) {//java.io.FileNotFoundException: /storage/emulated/0/maps.zip: open failed: ENOENT (No such file or directory)
			e.printStackTrace();
		}
		//删除本地的zip压缩文件
		new File(fileDir+"/"+filename).delete();
	}

}
