/*
package com.titan.ynsjy.util;

*/
/* Copyright 2014 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the sample code usage restrictions document for further information.
 *
 *//*


import java.io.File;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.ags.FeatureServiceInfo;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTableEditErrors;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geodatabase.GenerateGeodatabaseParameters;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusCallback;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusInfo;
import com.esri.core.tasks.geodatabase.GeodatabaseSyncTask;
import com.esri.core.tasks.geodatabase.SyncGeodatabaseParameters;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.ynsjy.BaseActivity;

public class GDBUtil {

	//static final String DEFAULT_FEATURE_SERVICE_URL = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/Sync/WildfireSync/FeatureServer";

	static final String DEFAULT_FEATURE_SERVICE_URL= "http://101.201.54.143:6080/arcgis/rest/services/Sync/SyncService/FeatureServer";

	//static final String DEFAULT_BASEMAP_SERVICE_URL = "http://services.arcgisonline.com/ArcGIS/rest/services/ESRI_StreetMap_World_2D/MapServer";
	static final String DEFAULT_BASEMAP_SERVICE_URL = "http://gis.gyforest.com:6080/arcgis/rest/services/GYLY_PUBCDATA/GYS_JCDL/MapServer";

	static final String DEFAULT_GDB_PATH = "/otms/同步数据/";

	static final int[] FEATURE_SERVICE_LAYER_IDS = { 0 };

	protected static final String TAG = "GDBUtil";

	private static GeodatabaseSyncTask gdbTask;

	private static String gdbFileName = "/offlinedata.geodatabase";

	*/
/**
	 * Checks whether the device is connected to a network
	 *//*

	public static boolean hasInternet(Activity a) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
		return hasConnectedWifi || hasConnectedMobile;
	}

	*/
/**
	 * Download data into a geodatabase
	 *
	 * @param activity
	 *//*

	public static void downloadData(final BaseActivity activity) {
		Log.i(TAG, "downloadData");
		String path = ResourcesManager.getInstance(activity).getFolderPath("/otms/同步数据");
		if(!new File(path).exists()){
			new File(path).mkdirs();
		}
		String gdbFilePath = path + gdbFileName;
		showProgress(activity, true);
		final MapView mapView = activity.getMapView();
		ProgressDialogUtil.startProgressDialog(activity);
		downloadGeodatabase(activity, mapView,gdbFilePath);
	}

	// Fetches the geodatabase and loads onto the mapview either locally or
	// downloading from the server
	private static void downloadGeodatabase(final BaseActivity activity, final MapView mapView,final String gdbFilePath) {

		// request and download geodatabase from the server
		if (!isGeoDatabaseLocal(gdbFilePath)) {
			gdbTask = new GeodatabaseSyncTask(DEFAULT_FEATURE_SERVICE_URL, null);

			gdbTask.fetchFeatureServiceInfo(new CallbackListener<FeatureServiceInfo>() {

				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "", e);
					showMessage(activity, e.getMessage());
					showProgress(activity, false);
				}

				@Override
				public void onCallback(FeatureServiceInfo fsInfo) {

					if (fsInfo.isSyncEnabled()) {
						requestGdbFromServer(gdbTask, activity, mapView, fsInfo,gdbFilePath);
					}
				}
			});
		}
		else {
			// load the geodatabase from the device
			// add local layers from the geodatabase
			showMessage(activity, "数据已存在...");
			showProgress(activity, false);
		}

	}

	*/
/**
	 * Download the geodatabase from the server.
	 *//*

	private static void requestGdbFromServer(GeodatabaseSyncTask geodatabaseSyncTask,Geometry extent,
											 final MapView mapView, FeatureServiceInfo fsInfo,String gdbFilePath) {

		//Polygon extent = activity.syncfeatureLayer.getExtent();
		//ex extent=new Exten
		GenerateGeodatabaseParameters params = new GenerateGeodatabaseParameters(fsInfo, extent,mapView.getSpatialReference(), null, true);
		params.setOutSpatialRef(mapView.getSpatialReference());

		// gdb complete callback
		CallbackListener<String> gdbResponseCallback = new CallbackListener<String>() {

			@Override
			public void onCallback(String path) {

				// add local layers from the geodatabase
				showMessage(activity, "下载成功!");
				showProgress(activity, false);
				ProgressDialogUtil.stopProgressDialog(activity);
				activity.getMapView().removeLayer(activity.syncfeatureLayer);
			}

			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "", e);
				showMessage(activity, e.getMessage());
				showProgress(activity, false);
			}

		};

		GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

			@Override
			public void statusUpdated(GeodatabaseStatusInfo status) {
				if (!status.isDownloading()) {
					showMessage(activity, status.getStatus().toString());
				}

			}
		};

		// single method does it all!
		geodatabaseSyncTask.generateGeodatabase(params, gdbFilePath, false, statusCallback, gdbResponseCallback);
		showMessage(activity, "Submitting gdb job...");
	}

	// upload and synchronize local geodatabase to the server
	//数据同步
	public static void synchronize(final BaseActivity activity) {
		ProgressDialogUtil.startProgressDialog(activity);
		showProgress(activity, true);

		gdbTask = new GeodatabaseSyncTask(DEFAULT_FEATURE_SERVICE_URL, null);
		gdbTask.fetchFeatureServiceInfo(new CallbackListener<FeatureServiceInfo>() {

			@Override
			public void onError(Throwable e) {

				Log.e(TAG, "", e);
				showMessage(activity, e.getMessage());
				showProgress(activity, false);
			}

			@Override
			public void onCallback(FeatureServiceInfo objs) {
				if (objs.isSyncEnabled()) {
					doSyncAllInOne(activity);
				}
			}
		});
	}

	*/
/**
	 * Synchronizing the edits to the Map working on both online/offline mode
	 *
	 * @throws Exception
	 *//*

	private static void doSyncAllInOne(final BaseActivity activity) {

		try {
			// create local geodatabase
			String path = ResourcesManager.getInstance(activity).getFolderPath("/otms/同步数据");
			String gdbFilePath = path + gdbFileName;
			if(!new File(gdbFilePath).exists()){
				showMessage(activity, "同步数据不存在");
				ProgressDialogUtil.stopProgressDialog(activity);
				return;
			}
			Geodatabase gdb = new Geodatabase(gdbFilePath);

			// get sync parameters from geodatabase
			final SyncGeodatabaseParameters syncParams = gdb.getSyncParameters();

			CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>> syncResponseCallback = new CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>>() {

				@Override
				public void onCallback(Map<Integer, GeodatabaseFeatureTableEditErrors> objs) {
					showProgress(activity, false);
					if (objs != null) {
						if (objs.size() > 0) {
							showMessage(activity, "Sync Completed With Errors");
						} else {
							showMessage(activity, "Sync Completed Without Errors");
						}

					} else {
						ProgressDialogUtil.stopProgressDialog(activity);
						showMessage(activity, "同步结束");
					}
				}

				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "", e);
					showMessage(activity, e.getMessage());
					showProgress(activity, false);
					ProgressDialogUtil.stopProgressDialog(activity);
				}

			};

			GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

				@Override
				public void statusUpdated(GeodatabaseStatusInfo status) {

					showMessage(activity, status.getStatus().toString());
				}
			};

			// Performs Synchronization
			gdbTask.syncGeodatabase(syncParams, gdb, statusCallback, syncResponseCallback);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*/
/**
	 * Checks whether the geodatabase is available locally
	 *//*

	public static boolean isGeoDatabaseLocal(String gdbFilePath) {
		File file = new File(gdbFilePath);
		if(file.exists()){
			File[] files = new File(file.getParent()).listFiles();
			for(File ff : files){
				ff.delete();
			}
		}
		return file.exists();
	}

	static void showProgress(final BaseActivity activity, final boolean b) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				activity.setProgressBarIndeterminateVisibility(b);
			}
		});
	}

	static void showMessage(final BaseActivity activity, final String message) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
*/
