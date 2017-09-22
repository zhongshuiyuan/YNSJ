package com.titan.ynsj;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.crashreport.CrashReport;
import com.titan.util.DeviceUtil;
import com.titan.util.GpsUtil;
import com.titan.util.ResourcesManager;

public class MyApplication extends MultiDexApplication {

	public static MyApplication mApplication;
	public static ResourcesManager resourcesManager;
	public static SharedPreferences sharedPreferences;
	public static DeviceUtil.Screen screen;
	/** 移动设备唯一号 */
	public static String macAddress;
	/** 移动设备序列号 */
	public static String mobileXlh;
	/** 移动设备型号 */
	public static String mobileType;
	/** 注册网络 */
	//private ConnectionChangeReceiver mNetworkStateReceiver;

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
		sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		/*try {
			resourcesManager = ResourcesManager.getInstance(this);
			*//** 创建系统存放数据的默认文件夹 *//*
			//resourcesManager.createFolder();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		/* 打开GPS */
		/*if(!GpsUtil.isOpen(this)){
			GpsUtil.openGps(this);
		}*/
        GpsUtil.openGps(this);
		/** 获取设备信息 */
		//getMbInfo();
		//屏幕尺寸
		//screen = DeviceUtil.getScreenPix(this);

		String appPath= Environment.getExternalStorageDirectory().getAbsolutePath();
        String appPath1= Environment.getExternalStorageDirectory().getPath();

        String download=Environment.getDownloadCacheDirectory().getAbsolutePath();

		//new MyAsyncTask().execute("copyDatabase");
		/*异常本地记录*/
		/*CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);*/
	}




	/** 异步类 */
	/*class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(final String... params) {
			if (params[0].equals("copyDatabase")) {
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
	}*/







	/** 复制默认数据到本地 */
	/*private void copyDatabase(String fileDir,String assetPath, String filename) {
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
	}*/
	/**解压文件并删除zip文件*/
	/*public void UnZipTFolder(String fileDir,String filename){
		try {
			//ZIPUtil.UnZipFolder(fileDir+"/"+filename, fileDir+"/");
			ZIPUtil.unzip(fileDir+"/"+filename, fileDir+"/");
		} catch (Exception e) {//java.io.FileNotFoundException: /storage/emulated/0/maps.zip: open failed: ENOENT (No such file or directory)
			e.printStackTrace();
		}
		//删除本地的zip压缩文件
		new File(fileDir+"/"+filename).delete();
	}*/

}
