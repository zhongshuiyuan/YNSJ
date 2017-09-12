package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/2/002.
 * 点击小班图片展示的图片展示大图
 */
public class ShowPicActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    Context mContext;
    ViewPager viewPager;//展示图片的ViewPager
    View menu;//菜单栏
    View view;
    String isPath;//当前图片地址
    MyAdapter adapter;
    List<String> pathList = new ArrayList<>();//所有图片的地址列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ShowPicActivity.this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(mContext).inflate(R.layout.pic_samp_show, null);
        setContentView(view);
        initView();
        getPicPath();
        adapter = new MyAdapter();
        viewPager.setAdapter(adapter);
        setView();
    }

    /*设定跳转页面*/
    private void setView() {
        for (int i = 0; i < pathList.size(); i++) {
            if (isPath.equals(pathList.get(i))) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    /*布局初始化*/
    private void initView() {
        LinearLayout imb_close = (LinearLayout) view.findViewById(R.id.pic_close);
        imb_close.setOnClickListener(this);
        LinearLayout imb_delet = (LinearLayout) view.findViewById(R.id.pic_delet);
        imb_delet.setOnClickListener(this);
        menu = view.findViewById(R.id.pic_menu);
        viewPager = (ViewPager) view.findViewById(R.id.show_pic_pager);
        viewPager.addOnPageChangeListener(this);
    }

    /*显示图片*/
    private void getPicPath() {
        Intent intent = getIntent();
        isPath = intent.getStringExtra("path");
        long fid = intent.getLongExtra("fid",0);
        String picPath = intent.getStringExtra("picPath");
        pathList = ResourcesManager.getImagesFiles(picPath,String.valueOf(fid));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_close://返回
                this.finish();
                break;
            case R.id.pic_delet:
                //当删除一页之后没有滑动时isPath不会改变，这时就需要获取当前页图片准确地址
                deletDialog(pathList.get(viewPager.getCurrentItem()));
                break;
        }
    }

    /*是否确认删除*/
    private void deletDialog(final String filePath) {
        final AlertDialog.Builder delDialog = new AlertDialog.Builder(mContext);
        delDialog.setMessage("确定删除吗？");
        delDialog.setTitle("提示");
        delDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        delDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletFile(filePath);
                adapter.notifyDataSetChanged();
            }
        });
        delDialog.create().show();
    }

    /*删除图片*/
    private void deletFile(String filePath) {
        if (filePath == null || pathList == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            ToastUtil.setToast(mContext, "文件不存在");
            return;
        }
        file.delete();
        pathList.remove(filePath);
        //删除文件后刷新手机媒体库
        mContext.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA + "='" + filePath + "'", null
        );
        ToastUtil.setToast(mContext, "文件已删除");
        if (pathList != null && pathList.size() <= 0) {
            //如果图片不存在就结束当前活动
            ShowPicActivity.this.finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //确定图片地址是当前页的图片地址
        isPath = pathList.get(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*适配器*/
    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //销毁已删除的图片所在item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }

        //加载需要展示的item
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            //图片压缩处理
            /*bitmap = new PicSampActivity().decodeSampledBitmap(pathList.get(position), 640, 400);
            imageView.setImageBitmap(bitmap);
            container.addView(imageView);
            BitmapFactory.decodeFile(null);*/
            //采用glide框架加载大图
            Glide.with(mContext)
                    .load(pathList.get(position))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
            container.addView(imageView);
            //点击图片可以控制菜单栏的显示、隐藏
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menu.getVisibility() == View.VISIBLE) {
                        menu.setVisibility(View.GONE);
                    } else {
                        menu.setVisibility(View.VISIBLE);
                    }
                }
            });
            return imageView;
        }

        //返回POSITION_NONE可以让item删除后viewpager成功刷新
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
