package com.titan.versionupdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by li on 2017/5/26.
 * 版本更新帮助类
 */

public class VersionUpdata {

    Activity activity;
    private Dialog downApkDialog;
    /**
     * 更新进度条
     */
    private ProgressBar mProgress;
    /**
     * 是否取消更新
     */
    private boolean cancelUpdate = false;
    /**
     * 记录进度条数量
     */
    private int progress;

    /**构造*/
    public VersionUpdata(Activity activity){
        this.activity = activity;
    }

    /**检查是否有更新
     * updateurl 远程配置xml地址
     * 有更新返回true并弹出更新框，无更新返回false
     * */
    public boolean checkVersion(String updateurl){
        boolean ff = false;
        final HashMap<String, String> mHashMap = isUpdata(activity,updateurl);
        String flag = mHashMap.get("flag").toString().trim();
        if (flag.equals("true")) {
            ff = true;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showUpdateDialog(activity,mHashMap);
                }
            });
        }
        return ff;
    }

    public double getCurentVersion(){
        double versionCode = getVersionCode(activity);
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showUpdateDialog(Context context, final HashMap<String, String> mHashMap) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件更新");
        builder.setMessage("有新版本,建议更新!");
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downApkDialog.dismiss();
                // 显示下载对话框
                showDownloadDialog(mHashMap);
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downApkDialog.dismiss();
            }
        });
        downApkDialog = builder.create();
        downApkDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog(HashMap<String, String> mHashMap) {
        final Dialog downApkDialog = new Dialog(activity, R.style.Dialog);
        downApkDialog.setContentView(R.layout.dialog_download_apk);
        downApkDialog.setCanceledOnTouchOutside(false);
        mProgress = (ProgressBar) downApkDialog.findViewById(R.id.update_progress);
        Button downCancleBtn = (Button) downApkDialog.findViewById(R.id.down_cancle_btn);
        downCancleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                downApkDialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        setDialogParams(activity, downApkDialog, 1, 1);
        // 下载文件
        downloadApk(mHashMap);
    }

    /**
     * 下载apk文件
     */
    private void downloadApk(HashMap<String, String> mHashMap) {
        // 启动新线程下载软件
        new downloadApkThread(mHashMap).start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        HashMap<String, String> mHashMap;
        public downloadApkThread(HashMap<String, String> mHashMap){
            this.mHashMap = mHashMap;
        }

        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String mSavePath = Environment.getExternalStorageDirectory() + "/download";

                    String strUrl = mHashMap.get("url").trim();
                    URL url = new URL(strUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("name").toString().trim());
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.setProgress(progress);
                            }
                        });
                        //handler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHashMap.put("path",mSavePath);
                            installApk(activity, mHashMap);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                    cancelUpdate = false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            downApkDialog.dismiss();
        }
    }

    /**
     * dialog 宽度和高度设置
     */
    private static void setDialogParams(Context context, Dialog dialog, double pwidth, double mwidth) {
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        params.width = (int) (dm.widthPixels * mwidth);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 判断apk是否需要更新
     */
    private static HashMap<String, String> isUpdata(Context context,String updateurl) {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        double versionCode = getVersionCode(context);
        //通过服务器获得版本号
        try {
            map = HttpHelper.sendHttpRequest(context,updateurl);
            double serviceCode = Double.valueOf(map.get("version"));
            if (serviceCode > versionCode) {
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        map.put("flag",flag+"");
        return map;
    }

    private static double getVersionCode(Context context) {
        double versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = Double.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.trim());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /** 安装APK文件 */
    private static void installApk(Context context,HashMap<String, String> mHashMap) {
        String mSavePath = mHashMap.get("path");
        File apkfile = new File(mSavePath, mHashMap.get("name").toString().trim());
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),"application/vnd.android.package-archive");
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
    }
}
