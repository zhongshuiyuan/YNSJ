package com.titan.baselibrary.util;

import android.content.Context;
import android.os.storage.StorageManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by li on 2016/5/31.
 * 获取设备内存地址工具类
 */
public class ResourcesManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private Context mContext;
	private static ResourcesManager instance;

	public static synchronized ResourcesManager getInstance(Context context) {
		if (instance == null) {
			instance = new ResourcesManager(context);
		}
		return instance;
	}

	private ResourcesManager(Context context){
		this.mContext = context;
	}

	/** 获取手机内部存储地址和外部SD卡存储地址 */
	public String[] getStoragePath() {

		StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
		String[] paths = null;
		try {
			//paths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class[0]).invoke(sm,new Object[]{});
			paths = (String[])sm.getClass().getMethod("getVolumePaths").invoke(sm);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return paths;
	}

}
