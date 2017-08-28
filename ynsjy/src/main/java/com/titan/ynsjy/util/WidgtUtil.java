package com.titan.ynsjy.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.titan.ynsjy.R;

import java.util.ArrayList;
import java.util.List;

public class WidgtUtil {

	/** 创建添加古树名木界面 */
	@SuppressLint("NewApi")
	public static List<Object> initGsmmaddview(Context mContext, int number,
											   int textstartid, int edittextstartid, int buttonstartid,
											   List<Integer> inputnum, String headname) {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setBackground(mContext.getResources().getDrawable(
				R.drawable.background_view_rounded));

		LinearLayout layout1 = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams1.setMargins(0, 0, 0, 5);
		layout1.setGravity(Gravity.CENTER);
		layout1.setOrientation(LinearLayout.HORIZONTAL);
		layout1.setBackground(mContext.getResources().getDrawable(
				R.drawable.background_view_rounded_blue));

		TextView text1 = new TextView(mContext);
		text1.setText(headname);
		text1.setPadding(0, 10, 0, 10);
		text1.setTextSize(mContext.getResources().getDimension(
				R.dimen.larger_txtsize22));
		text1.setTextColor(mContext.getResources().getColor(R.color.white));
		LinearLayout.LayoutParams text1Params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		layout1.addView(text1, text1Params);
		layout.addView(layout1, layoutParams1);

		ScrollView sView = new ScrollView(mContext);
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 0, 3f);

		LinearLayout layout2 = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams2.setMargins(0, 5, 0, 5);
		layout2.setOrientation(LinearLayout.VERTICAL);
		layoutParams2.gravity = Gravity.CENTER_HORIZONTAL;

		int a;
		if (number % 2 == 1) {
			a = (number / 2) + 1;
		} else {
			a = (number / 2);
		}
		int textid = textstartid;
		int edittextid = edittextstartid;
		for (int i = 0; i < a; i++) {
			LinearLayout layout3 = new LinearLayout(mContext);
			LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				layoutParams3.setMargins(0, 0, 0, 0);
			} else {
				layoutParams3.setMargins(0, 10, 0, 0);
			}

			layout3.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < 2; j++) {
				LinearLayout layout4 = new LinearLayout(mContext);
				LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
				if (j == 0) {
					layoutParams4.setMargins(10, 0, 0, 0);
				} else if (j == 1) {
					layoutParams4.setMargins(10, 0, 40, 0);
				}
				layout4.setOrientation(LinearLayout.HORIZONTAL);
				layout4.setGravity(Gravity.RIGHT);

				TextView text2 = new TextView(mContext);
				LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
				text2.setGravity(Gravity.RIGHT);
				text2.setPadding(5, 5, 5, 5);
				text2.setTextColor(mContext.getResources().getColor(
						R.color.blue));
				text2.setTextSize(mContext.getResources().getDimension(
						R.dimen.larger_txtsize18));
				text2.setId(textid);
				textid = textid + 1;

				EditText editText1 = new EditText(mContext);
				editText1.setId(edittextid);
				edittextid = edittextid + 1;

				LinearLayout.LayoutParams editParams1 = new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f);
				editParams1.setMargins(5, 0, 0, 0);
				editText1.setPadding(5, 5, 5, 5);
				editText1.setSingleLine(true);
				editText1.setBackground(mContext.getResources().getDrawable(
						R.drawable.background_edittxt));
				if (inputnum != null) {
					if (inputnum.contains(i * 2 + j + 1)) {
						editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
					}
				}
				layout4.addView(text2, textParams2);
				layout4.addView(editText1, editParams1);
				layout3.addView(layout4, layoutParams4);
				if (i == (a - 1) && (number % 2) != 0 && j == 1) {
					layout4.setVisibility(View.INVISIBLE);
				}

			}
			layout2.addView(layout3, layoutParams3);
		}
		LinearLayout layout5 = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layout5.setOrientation(LinearLayout.HORIZONTAL);
		layoutParams5.setMargins(0, 5, 0, 0);
		layout5.setBackground(mContext.getResources().getDrawable(
				R.drawable.background_view_rounded_blue));
		layout5.setGravity(Gravity.CENTER);
		for (int i = 0; i < 2; i++) {
			Button button1 = new Button(mContext);
			LinearLayout.LayoutParams bttonParams1 = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				bttonParams1.setMargins(0, 5, 15, 5);
				button1.setText(mContext.getResources().getString(R.string.bc));
			} else if (i == 1) {
				bttonParams1.setMargins(15, 5, 0, 5);
				button1.setText(mContext.getResources().getString(
						R.string.cancle));
			}
			button1.setBackground(mContext.getResources().getDrawable(
					R.drawable.background_view_button));
			button1.setPadding(10, 10, 10, 10);
			button1.setId(buttonstartid + i);
			button1.setTextColor(mContext.getResources()
					.getColor(R.color.white));
			button1.setTextSize(mContext.getResources().getDimension(
					R.dimen.larger_txtsize18));
			layout5.addView(button1, bttonParams1);
		}
		sView.addView(layout2, layoutParams2);
		// layout6.addView(sView, scrollParams);
		layout.addView(sView, scrollParams);
		layout.addView(layout5, layoutParams5);
		List<Object> list = new ArrayList<Object>();
		list.add(layout);
		list.add(layoutParams);
		return list;
	}

	/**
	 * 连续清查动态布局界面
	 *
	 * @param mContext
	 * @param number
	 *            字段个数
	 * @param textstartid
	 *            字段说明TextView初始id
	 * @param edittextstartid
	 *            字段值EditText初始id
	 * @param buttonstartid
	 *            按钮初始id
	 * @param inputnum
	 *            输入类型为数字的EditText
	 * @param spinnernum
	 *            为Spinner的项
	 * @param buttonnum
	 *            为Button的项
	 * @param headname
	 *            表头名称
	 * @param editnum
	 *            不能编辑的edittext项
	 * @return
	 */
	@SuppressLint("NewApi")
	public static List<Object> initLxqcaddview(Context mContext, int number,
											   int textstartid, int edittextstartid, int buttonstartid,
											   List<Integer> inputnum, List<Integer> spinnernum, List<Integer> buttonnum,String headname,
											   List<Integer> editnum) {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setBackground(mContext.getResources().getDrawable(
				R.drawable.background_view_rounded));

		LinearLayout layout1 = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams1.setMargins(0, 0, 0, 5);
		layout1.setGravity(Gravity.CENTER);
		layout1.setOrientation(LinearLayout.HORIZONTAL);
		layout1.setBackground(mContext.getResources().getDrawable(
				R.drawable.background_view_rounded_blue));

		TextView text1 = new TextView(mContext);
		text1.setText(headname);
		text1.setPadding(0, 10, 0, 10);
		text1.setTextSize(mContext.getResources().getDimension(
				R.dimen.larger_txtsize22));
		text1.setTextColor(mContext.getResources().getColor(R.color.white));
		LinearLayout.LayoutParams text1Params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		layout1.addView(text1, text1Params);
		layout.addView(layout1, layoutParams1);

		ScrollView sView = new ScrollView(mContext);
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 0, 3f);

		LinearLayout layout2 = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams2.setMargins(0, 5, 0, 5);
		layout2.setOrientation(LinearLayout.VERTICAL);
		layoutParams2.gravity = Gravity.CENTER_HORIZONTAL;

		int a;
		if (number % 2 == 1) {
			a = (number / 2) + 1;
		} else {
			a = (number / 2);
		}
		int textid = textstartid;
		int edittextid = edittextstartid;
		for (int i = 0; i < a; i++) {
			LinearLayout layout3 = new LinearLayout(mContext);
			LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				layoutParams3.setMargins(0, 0, 0, 0);
			} else {
				layoutParams3.setMargins(0, 10, 0, 0);
			}

			layout3.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < 2; j++) {
				LinearLayout layout4 = new LinearLayout(mContext);
				LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
				if (j == 0) {
					layoutParams4.setMargins(10, 0, 0, 0);
				} else if (j == 1) {
					layoutParams4.setMargins(10, 0, 40, 0);
				}
				layout4.setOrientation(LinearLayout.HORIZONTAL);
				layout4.setGravity(Gravity.RIGHT);

				TextView text2 = new TextView(mContext);
				LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
				text2.setGravity(Gravity.RIGHT);
				text2.setPadding(5, 5, 5, 5);
				text2.setTextColor(mContext.getResources().getColor(
						R.color.blue));
				text2.setTextSize(mContext.getResources().getDimension(
						R.dimen.larger_txtsize18));
				text2.setId(textid);
				textid = textid + 1;

				Spinner spinner = null;
				EditText editText1 = null;
				Button button=null;
				if (spinnernum != null && spinnernum.contains((i * 2) + j + 1)) {
					spinner = new Spinner(mContext);
					spinner.setId(edittextid);
					edittextid = edittextid + 1;

					LinearLayout.LayoutParams spinnerParams1 = new LinearLayout.LayoutParams(
							0, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f);
					spinner.setBackground(mContext.getResources().getDrawable(
							R.drawable.background_edittxt));
					spinnerParams1.setMargins(5, 0, 0, 0);
					spinner.setPadding(0, 0, 0, 0);
					layout4.addView(text2, textParams2);
					layout4.addView(spinner, spinnerParams1);
					layout3.addView(layout4, layoutParams4);
				} else if(buttonnum!=null && buttonnum.contains((i * 2) + j + 1)){
					button=new Button(mContext) ;
					button.setId(edittextid);
					edittextid = edittextid + 1;

					LinearLayout.LayoutParams buttonParams1 = new LinearLayout.LayoutParams(
							0, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f);
					buttonParams1.setMargins(5, 0, 0, 0);
					button.setPadding(5, 5, 5, 5);
					button.setGravity(Gravity.LEFT);
					button.setTextColor(Color.parseColor("#000000"));
					button.setTextSize(18);
					button.setBackground(mContext.getResources()
							.getDrawable(R.drawable.background_edittxt));
					layout4.addView(text2, textParams2);
					layout4.addView(button, buttonParams1);
					layout3.addView(layout4, layoutParams4);

				}else {
					editText1 = new EditText(mContext);
					editText1.setId(edittextid);
					edittextid = edittextid + 1;

					LinearLayout.LayoutParams editParams1 = new LinearLayout.LayoutParams(
							0, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f);
					editParams1.setMargins(5, 0, 0, 0);
					editText1.setPadding(5, 5, 5, 5);
					editText1.setSingleLine(true);
					if (inputnum != null && inputnum.contains(i * 2 + j + 1)) {
						editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					}
					if(editnum!=null && editnum.contains(i * 2 + j + 1)){
						editText1.setFocusable(false);
						editText1.setFocusableInTouchMode(false);
						editText1.setBackgroundColor(Color.parseColor("#DCDCDC"));
					}else{
						editText1.setBackground(mContext.getResources()
								.getDrawable(R.drawable.background_edittxt));
					}
					layout4.addView(text2, textParams2);
					layout4.addView(editText1, editParams1);
					layout3.addView(layout4, layoutParams4);

				}

				if (i == (a - 1) && (number % 2) != 0 && j == 1) {
					layout4.setVisibility(View.INVISIBLE);
				}

			}
			layout2.addView(layout3, layoutParams3);
		}
		LinearLayout layout5 = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layout5.setOrientation(LinearLayout.HORIZONTAL);
		layoutParams5.setMargins(0, 5, 0, 0);
		layout5.setBackground(mContext.getResources().getDrawable(
				R.drawable.background_view_rounded_blue));
		layout5.setGravity(Gravity.CENTER);
		for (int i = 0; i < 2; i++) {
			Button button1 = new Button(mContext);
			LinearLayout.LayoutParams bttonParams1 = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				bttonParams1.setMargins(0, 5, 15, 5);
				button1.setText(mContext.getResources().getString(R.string.bc));
			} else if (i == 1) {
				bttonParams1.setMargins(15, 5, 0, 5);
				button1.setText(mContext.getResources().getString(
						R.string.cancle));
			}
			button1.setBackground(mContext.getResources().getDrawable(
					R.drawable.background_view_button));
			button1.setPadding(10, 10, 10, 10);
			button1.setId(buttonstartid + i);
			button1.setTextColor(mContext.getResources()
					.getColor(R.color.white));
			button1.setTextSize(mContext.getResources().getDimension(
					R.dimen.larger_txtsize18));
			layout5.addView(button1, bttonParams1);
		}
		sView.addView(layout2, layoutParams2);
		// layout6.addView(sView, scrollParams);
		layout.addView(sView, scrollParams);
		layout.addView(layout5, layoutParams5);
		List<Object> list = new ArrayList<Object>();
		list.add(layout);
		list.add(layoutParams);
		return list;
	}

}
