package com.titan.baselibrary.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.titan.baselibrary.R;
import com.titan.baselibrary.customview.CustomProgressDialog;


/**
 * Created by li on 2016/5/31.
 * 自定义progressDialog工具类
 */
public class ProgressDialogUtil {

	private static CustomProgressDialog progressDialog;

	@SuppressLint("ShowToast")
	public static Toast makeText(Context context, CharSequence text,
								 int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context, R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}

	/**
	 * 显示信息提示框
	 * @param context
	 * @param text
	 */
	public static void setToast(final Activity context, final String text) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ProgressDialogUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**显示数据加载框*/
	public static void startProgressDialog(final Context context) {
		((Activity)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog == null) {
					progressDialog = CustomProgressDialog.createDialog(context);
					progressDialog.setMessage("加载中...");
				}
				progressDialog.show();
			}
		});
	}
	/**关闭数据加载框*/
	public static void stopProgressDialog(Context context) {
		((Activity)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
		});
	}

}
