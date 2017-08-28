package com.titan.ynsjy.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.MyApplication;
import com.titan.ynsjy.R;
import com.titan.ynsjy.color.ColorPickerView;
import com.titan.ynsjy.color.ColorPickerView.OnColorChangedListener;
import com.titan.ynsjy.color.SansumColorSelecter;
import com.titan.ynsjy.entity.ScreenTool.Screen;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SlzylxqcQianMingDialog extends Dialog {
	static final int BACKGROUND_COLOR = Color.WHITE;
	LayoutParams p;
	static final int BRUSH_COLOR = Color.BLACK;
	Context context;
	PaintView mView;
	Screen screen;
	String ydh;
	String imagepath;
	ImageView qmimage;
	Bitmap bitmap;
	Bitmap startbitmap;
	/**��������*/
	int hblx = 0;
	int flag=0;
	Dialog dialog=null;
	/**���״̬*/
	boolean tcstate=false;
	/**typeΪ1��Ϊǩ��Ϊ2��Ϊ����λ��ͼ3Ϊ����λ��ͼ*/
	String type;
	LinearLayout.LayoutParams Params;
	View view=null;
	/**��������*/
	TextView hbzl;
	/**������������*/
	String [] hbary;
	TextView clstate;
	
	public SharedPreferences sharedPreferences;
	BaseActivity activity;
	
	public SlzylxqcQianMingDialog(Context context,String ydh,
			ImageView qmimage, Bitmap qmbitmap,String type) {
		super(context,R.style.Dialog);
		this.context = context;
		this.ydh = ydh;
		this.qmimage = qmimage;
		this.startbitmap = qmbitmap;
		this.type=type;
		this.activity=(BaseActivity) context;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if("1".equals(type)){
			setContentView(R.layout.slzylxqc_qianming);
		}else if("2".equals(type)||"3".equals(type)){
			setContentView(R.layout.slzylxqc_huitu);
			view=findViewById(R.id.linear);
		}
		
		sharedPreferences =context.getSharedPreferences("lxqc", Context.MODE_PRIVATE);
		screen = MyApplication.screen;
		/**��ȡ�Ի���ǰ�Ĳ���ֵ*/
		p = getWindow().getAttributes(); 
		if("1".equals(type)){
			p.height = (int) (screen.getHeightPixels() * 0.2);
			p.width = (int) (screen.getWidthPixels() * 1);
			Params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						(int) (screen.getHeightPixels() * 0.2));
			Params.setMargins(3, 3, 3, 3);
		}else if("2".equals(type)||"3".equals(type)){
			p.height = (int) (screen.getHeightPixels() *0.6);
			p.width = (int) (screen.getWidthPixels() *0.6);
			Params = new LinearLayout.LayoutParams(
					(int) (screen.getWidthPixels() *0.6),
					(int) (screen.getHeightPixels() *0.6));
		}

		mView = new PaintView(context);
		LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
		linear.addView(mView, Params);
		mView.requestFocus();
		/**������ʷǩ��*/
		if (startbitmap != null) {
			mView.start();
		}
		/**��ɫѡ��*/
		View colorchoose=findViewById(R.id.colorchoose);
		clstate=(TextView) findViewById(R.id.clstate);
		colorchoose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				colorChooseDialog(clstate);
			}
		});

		TextView ydhtv=(TextView) findViewById(R.id.ydh);
		if(ydh!=null&&ydhtv!=null){
			ydhtv.setText(ydh);
		}
		/**���*/
		View tianchong=findViewById(R.id.tianchong);
		final TextView tcstatetv=(TextView) findViewById(R.id.tcstate);
		tcstatetv.setText(sharedPreferences.getString("hbtc", "��"));
		if("��".equals(tcstatetv.getText().toString())){
			tcstate=false;
		}else{
			tcstate=true;
		}
		mView.setTianChong();
		tianchong.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mView.setTianChong();
				if(tcstate){
					sharedPreferences.edit().putString("hbtc", "��").commit();
					tcstatetv.setText("��");
				}else{
					sharedPreferences.edit().putString("hbtc", "��").commit();
					tcstatetv.setText("��");
				}
			}
		});
		/**���*/
		View kuangdu=findViewById(R.id.kuangdu);
		final TextView kdstate=(TextView) findViewById(R.id.kdstate);
		kdstate.setText(sharedPreferences.getInt("hbkd", 0)+"");
		mView.setHuaBiKd(Integer.parseInt(kdstate.getText().toString().trim()));
		kuangdu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showKdDialog(kdstate);
			}
		});
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		
		int color=sharedPreferences.getInt("color", Color.BLACK);
		int alpha=255-sharedPreferences.getInt("tmd", 0);
		mView.setColor(color, alpha);
		clstate.setBackgroundColor(color);
		
		/**����ѡ��*/
		View huabi = findViewById(R.id.huabi);
		hbzl=(TextView) findViewById(R.id.hbzl);
		hbary=new String[] { "���", "��Ƥ" };
		hblx=sharedPreferences.getInt("hblx", 0);
		mView.setHuaBi(hblx);
		huabi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
			dialog=new AlertDialog.Builder(context).setTitle("ѡ����Ҫ���Ƶ�ͼ��")
						.setSingleChoiceItems(hbary, hblx, new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										hblx = arg1;
										sharedPreferences.edit().putInt("hblx", arg1).commit();
										mView.setHuaBi(arg1);
										dialog.dismiss();
									}
								})
								.setNegativeButton("ȡ��", null)
								.show();
			}
		});
		
		/**����*/
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				ByteArrayOutputStream baos = null;
				try {
					File file = createImageFile();
					bitmap = mView.getCachebBitmap();
					baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] photoBytes = baos.toByteArray();
					if (photoBytes != null) {
						new FileOutputStream(file).write(photoBytes);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (baos != null)
							baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if("1".equals(type)){
				SlzylxqcDialog.setBitmap(bitmap);
				}
				ToastUtil.setToast(context, "����ɹ�");
				if (bitmap != null&&qmimage!=null&&"1".equals(type)) {
					qmimage.setImageBitmap(bitmap);
				}else if(bitmap != null&&qmimage!=null&&("2".equals(type)||"3".equals(type))){
				SlzylxqcYdwztDialog.setBitmap(bitmap);
				Bitmap wztbitmap=Bitmap.createScaledBitmap(bitmap, (int) (screen.getWidthPixels() *0.49), (int) (screen.getHeightPixels() *0.49), true);
				qmimage.setImageBitmap(wztbitmap);
				}
				dismiss();
			}
		});
		/**���*/
		Button clear = (Button) findViewById(R.id.clear);
		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(flag==0&&startbitmap != null){
					mView.start();
				}
				if(flag!=0){
					mView.clear();
				}
				int color=sharedPreferences.getInt("color", Color.BLACK);
				int alpha=255-sharedPreferences.getInt("tmd", 0);
				mView.setColor(color, alpha);
			}
		});
		/**�½�*/
		Button newbuild = (Button) findViewById(R.id.newbuild);
		newbuild.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(context).
						setTitle("�½�����").
						setMessage("�½����ܻ�����������������ݣ��Ƿ������").
						setNegativeButton("����", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								mView.clear();
								int color=sharedPreferences.getInt("color", Color.BLACK);
								int alpha=255-sharedPreferences.getInt("tmd", 0);
								mView.setColor(color, alpha);
								hblx=sharedPreferences.getInt("hblx", 0);
								mView.setHuaBi(hblx);
								flag=flag+1;
							}
						}).
						setPositiveButton("ȡ��", null).
						create().show();
			}
		});
	}

	/**��ɫѡ��diaolg
	 * @param clstate */
	protected void colorChooseDialog(final TextView clstate) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		try
		{
			/**չʾͼ��͸���Ⱥ�ͼ����ɫ���õ�dialog*/
			dialog.setContentView(R.layout.dialog_color_show);
			dialog.setCanceledOnTouchOutside(true);
			final TextView txtView = (TextView) dialog.findViewById(R.id.tianchongse);
			txtView.setBackgroundColor(sharedPreferences.getInt("color",Color.GREEN));
			ColorPickerView colorPickerView = (ColorPickerView) dialog
					.findViewById(R.id.color_picker);
			colorPickerView
					.setOnColorChangeListenrer(new OnColorChangedListener()
					{

						@Override
						public void colorChanged(int color)
						{
							sharedPreferences.edit().putInt("color", color).commit();
							txtView.setBackgroundColor(color);
						}
					});
			SansumColorSelecter mColorSelecter = (SansumColorSelecter) dialog.findViewById(R.id.color_seleter);
			mColorSelecter.setColorSelecterLinstener(new SansumColorSelecter.ColorSelecterLinstener()
					{
						@Override
						public void onColorSeleter(int color)
						{
							sharedPreferences.edit().putInt("color", color).commit();
							txtView.setBackgroundColor(color);
						}
					});
		} catch (Exception e){
			e.printStackTrace();
		}
		final SeekBar seekBar = (SeekBar) dialog
				.findViewById(R.id.symbol_seekbar);
		seekBar.setMax(255);
		seekBar.setProgress(255 - sharedPreferences.getInt("tmd", 0));

		final TextView textView = (TextView) dialog.findViewById(R.id.toumingdu);
		textView.setText((255 - sharedPreferences.getInt("tmd", 0)) + "");

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
			{
				sharedPreferences.edit().putInt("tmd", 255 - arg0.getProgress()).commit();
				textView.setText(arg0.getProgress() + "");
			}
		});

		Button button = (Button) dialog.findViewById(R.id.symbo_reset);
		button.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				seekBar.setProgress(255);
				sharedPreferences.edit().putInt("tmd", 255 - seekBar.getProgress()).commit();
				sharedPreferences.edit().putInt("color",Color.BLACK).commit();
				int color=sharedPreferences.getInt("color", Color.BLACK);
				int alpha=255-sharedPreferences.getInt("tmd", 0);
				mView.setColor(color, alpha);
				clstate.setBackgroundColor(color);
				dialog.dismiss();
			}
		});

		RadioButton radioSure = (RadioButton) dialog
				.findViewById(R.id.layer_render_btn_sure);
		radioSure.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				int color=sharedPreferences.getInt("color", Color.BLACK);
				int alpha=255-sharedPreferences.getInt("tmd", 0);
				mView.setColor(color, alpha);
				clstate.setBackgroundColor(color);
				dialog.dismiss();
			}
		});

		RadioButton radioCancle = (RadioButton) dialog
				.findViewById(R.id.layer_render_btn_cancle);
		radioCancle.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context, dialog, 0.65, 0.8);

	}

	/**��ʾ���ʿ��*/
	protected void showKdDialog(final TextView kd) {
		String bstr=kd.getText().toString();
	    LayoutInflater flater = LayoutInflater.from(context);  
	    final View view = flater.inflate(R.layout.dialog_progress, null);  
	    SeekBar seekBar1=(SeekBar) view.findViewById(R.id.seekBar1);
	    final TextView textview=(TextView) view.findViewById(R.id.textView1);
	    if("".equals(bstr)){
	    	textview.setText("0");
	    }else{
	    	textview.setText(bstr);
	    	seekBar1.setProgress(Integer.parseInt(bstr));
	    }
	    final AlertDialog.Builder builder = new AlertDialog.Builder(context);  
	    builder.setTitle("���");
	    builder.setView(view);
	    builder.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String astr=textview.getText().toString();
				kd.setText(astr);
				mView.setHuaBiKd(Integer.parseInt(astr));
				sharedPreferences.edit().putInt("hbkd", Integer.parseInt(astr)).commit();
			}

		});
	    builder.create().show();  
	    seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				textview.setText(arg1+"");
			}
		});
	}

	/** ����ļ�λ�� */
	private File createImageFile() throws IOException {
		File image = null;
		String fileName="";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if("1".equals(type)){
				fileName=MyApplication.resourcesManager.getLxqcImagePath("1");
				MyApplication.resourcesManager.deleteImage(fileName, ydh);//ɾ��֮ǰ��ͼƬ
			}else if("2".equals(type)){
				fileName=MyApplication.resourcesManager.getLxqcImagePath("2");
				MyApplication.resourcesManager.deleteImage(fileName, ydh);//ɾ��֮ǰ��ͼƬ
			}else if("3".equals(type)){
				fileName=MyApplication.resourcesManager.getLxqcImagePath("3");
				MyApplication.resourcesManager.deleteImage(fileName, ydh);//ɾ��֮ǰ��ͼƬ
			}
			
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			image = new File(fileName + "/" + ydh + "_"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg");
			imagepath = image.getAbsolutePath();
		} else {
			ToastUtil.setToast(context, "û��SD��");
		}
		return image;
	}

	class PaintView extends View {
		public Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width,p.height,Config.ARGB_8888);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheCanvas.drawColor(Color.WHITE);
		}

		/**���ǩ��*/
		public void clear() {
			if (cacheCanvas != null) {
				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);
				invalidate();
			}
		}
		/**�������״̬*/
		public void setTianChong() {
			if(!tcstate){
				paint.setStyle(Paint.Style.FILL);
				tcstate=true;
			}else{
				paint.setStyle(Paint.Style.STROKE);
				tcstate=false;
			}
		}
		/**����ʱ������ʷǩ��*/
		public void start() {
			clear();
			if("2".equals(type)||"3".equals(type)){
					Bitmap dragimg=Bitmap.createScaledBitmap(startbitmap, p.width, p.height, true);
					cacheCanvas.drawBitmap(dragimg, 0, 0, paint);
			}else if("1".equals(type)){
				cacheCanvas.drawBitmap(startbitmap, 0, 0, paint);
			}
			invalidate();
		}
		/**������ɫ*/
		public void setColor(int color,int alpha ){
			paint.setColor(color);
			paint.setAlpha(alpha);
		};
		/**���û���*/
		public void setHuaBi(int arg1) {
		hbzl.setText(hbary[arg1]);
		int color=sharedPreferences.getInt("color", Color.BLACK);
		int alpha=255-sharedPreferences.getInt("tmd", 0);
		setHuaBiKd(sharedPreferences.getInt("hbkd", 0));
		setColor(color, alpha);
			if(arg1==0){
				
			}else if(arg1==1){
				paint.setColor(Color.WHITE);
				setHuaBiKd(20);
			}
		}
		/**���û��ʿ��*/
		public void setHuaBiKd(int a) {
			paint.setStrokeWidth(a);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(cachebBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;
				if (hblx == 0) {
					path.moveTo(cur_x, cur_y);
				}else if(hblx==1){
					path.moveTo(cur_x, cur_y);
				}
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				if (hblx == 0) {
					path.quadTo(cur_x, cur_y, x, y);
					cur_x = x;
					cur_y = y;
				}else if(hblx==1){
					path.quadTo(cur_x, cur_y, x, y);
					cur_x = x;
					cur_y = y;
				}
				break;
			}

			case MotionEvent.ACTION_UP: {
				if (hblx == 0) {          
					cacheCanvas.drawPath(path, paint);
					path.reset();
				}else if(hblx==1){
					cacheCanvas.drawPath(path, paint);    
					path.reset();
				}
				
				break;
			}
			}

			invalidate();

			return true;
		}
	}
}
