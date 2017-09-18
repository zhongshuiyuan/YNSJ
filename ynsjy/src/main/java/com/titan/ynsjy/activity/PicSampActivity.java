package com.titan.ynsjy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.titan.ynsjy.BaseActivity;
import com.titan.ynsjy.R;
import com.titan.ynsjy.entity.MyLayer;
import com.titan.ynsjy.util.BaseUtil;
import com.titan.ynsjy.util.ResourcesManager;
import com.titan.ynsjy.util.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.titan.ynsjy.util.ResourcesManager.getVideoThumbnail;


/**
 * Created by hanyw on 2017/4/28/028.
 * 小班多媒体信息浏览
 */
public class PicSampActivity extends AppCompatActivity implements View.OnClickListener {
    TextView cancel;//取消全选
    TextView all;//全选
    TextView del;//删除
    String foutPath;
    LinearLayout menuLinear;//菜单栏
    List<String> picPathList = new ArrayList<>();//图片地址列表
    Map<String, Boolean> isSelectList = new HashMap<>();//选择的项
    List<String> checkPathList = new ArrayList<>();//选择项的地址
    private String picPath;
    private long fid;

    MyAdapter adapter;
    private Context mContext;
    private View view;
    private GridView pic_view;
    private MyLayer myLayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = PicSampActivity.this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(mContext).inflate(R.layout.pic_sampling, null);
        setContentView(view);
        initView();
        getData();
        initData();
        adapter = new MyAdapter(picPathList,fid,picPath);
        pic_view.setAdapter(adapter);
    }

    /*计算图片压缩比例*/
    private static int computSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获取图片的原始宽度和高度
        final int height = options.outHeight;
        final int width = options.outWidth;
        //先设图片比例为1
        int inSampleSize = 1;
        //判断原图和需要压缩的尺寸大小
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //通过三元运算符选择小的一个比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /*压缩图片*/
    public static Bitmap decodeSampledBitmap(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //设为true可以先获取图片属性，但不加载图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //计算压缩比例
        options.inSampleSize = computSampleSize(options, reqWidth, reqHeight);
        //重新设为false，根据上面计算的宽高重新加载图片，得到压缩后的图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    /*布局初始化*/
    private void initView() {
        Button btn_back = (Button) view.findViewById(R.id.pic_back);
        btn_back.setOnClickListener(this);
        Button btn_pic = (Button) view.findViewById(R.id.pic_new);
        btn_pic.setOnClickListener(this);
        pic_view = (GridView) view.findViewById(R.id.pic_view);
        menuLinear = (LinearLayout) view.findViewById(R.id.pic_samp_menu);
        cancel = (TextView) view.findViewById(R.id.pic_cancel);
        cancel.setOnClickListener(this);
        all = (TextView) view.findViewById(R.id.pic_all);
        all.setOnClickListener(this);
        del = (TextView) view.findViewById(R.id.pic_del);
        del.setOnClickListener(this);
    }

    /*读取小班对应的图片*/
    private void initData() {
        String path = picPath;
        picPathList = ResourcesManager.getImagesFiles(path,String.valueOf(fid));
        for (int i = 0; i < picPathList.size(); i++) {
            isSelectList.put(String.valueOf(i), false);
        }
    }

    private void getData() {
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer("edit", BaseActivity.layerNameList);
        Intent intent = getIntent();
        if (intent != null) {
            fid = intent.getLongExtra("fid", 0);
            picPath = intent.getStringExtra("picPath");
//            auditType = intent.getBooleanExtra("auditType", false);
        }
    }

    /*点击事件处理*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_back:
                this.finish();
                break;
            case R.id.pic_new://拍照采样
                //photograph();
                break;
            case R.id.pic_cancel://取消全选
                cancelAll();
                break;
            case R.id.pic_all://全选
                selectAll();
                break;
            case R.id.pic_del://删除
                delFile(checkPathList);
                break;
        }
    }

    /*调用相机拍照并存储到文件夹中*/
    private void photograph() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        foutPath = getFileURI() + getPicName();
//        makeRootDirectory(getFileURI());
//        Uri uri = Uri.fromFile(new File(foutPath));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(intent, 1);
    }

    /*删除文件*/
    void delFile(List<String> list) {
        if (list.size() <= 0)
            return;
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            if (!file.exists()) {
                continue;
            }
            file.delete();
            //删除文件后刷新手机媒体库
            PicSampActivity.this.getContentResolver().delete(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "='" + list.get(i) + "'", null
            );
        }
        ToastUtil.setToast(mContext, "文件已删除");
        list.clear();
        initData();
        adapter.notifyDataSetChanged();
    }

    /*取消全选*/
    private void cancelAll() {
        isSelectList.clear();
        checkPathList.clear();
        for (int i = 0; i < picPathList.size(); i++) {
            isSelectList.put(String.valueOf(i), false);
        }
        adapter.notifyDataSetChanged();
    }

    /*全选*/
    private void selectAll() {
        isSelectList.clear();
        for (int i = 0; i < picPathList.size(); i++) {
            isSelectList.put(String.valueOf(i), true);
            checkPathList.add(picPathList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    /*单选*/
    private void selectImage(int position, boolean flag) {
        for (int i = 0; i < picPathList.size(); i++) {
            if (i != position) {
                continue;
            }
            if (!flag) {
                isSelectList.put(String.valueOf(i), false);
                checkPathList.remove(picPathList.get(i));
            }else {
                isSelectList.put(String.valueOf(i), true);
                checkPathList.add(picPathList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * @param path 视频地址
     */
    private void showVideo(String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://"+path);
        Log.e("tag","file://"+path);
        intent.setDataAndType(uri,"video/mp4");
        startActivity(intent);
    }

    /*弹出图片放大显示*/
    private void showPic(String path, long fid,String picPath) {
        Intent intent = new Intent(PicSampActivity.this, ShowPicActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("fid", fid);
        intent.putExtra("picPath",picPath);
        startActivityForResult(intent, 2);
    }

    /*获取文件名*/
    private String getPicName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'img'_yyyyMMdd_HHmmss", Locale.CHINA);
        return sdf.format(date) + ".jpg";
    }

    /*创建文件夹*/
    private void makeRootDirectory(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    /*相机拍照完成后对返回的数据处理*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scanDir(this, foutPath);
                //ToastUtil.setToast(mContext,"已将相片保存到："+foutPath);
//                EditPhoto editPhoto = new EditPhoto(mContext,foutPath,);
//                editPhoto.show();
//                initData();
//                adapter.notifyDataSetChanged();
            }
        }

        if (requestCode == 2) {
            initData();
            adapter.notifyDataSetChanged();
        }
    }

    /*扫描文件*/
    public static void scanDir(final Context context, String uri) {
        //判断sdk版本是否大于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context, new String[]{uri}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                    + Environment.getExternalStorageDirectory())));
        }
    }

    /*图片展示的适配器*/
    class MyAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

        private LruCache<String, Bitmap> mMemoryCache;
        List<String> pathList;//图片地址列表
        private long fid;
        private String picPath;
        private int mFirstVisibleItem;//屏幕第一张图片的下标
        private int mVisibleItemCount;//屏幕范围内有多少张图片可见

        public MyAdapter(List<String> picPathList,long fid,String picPath) {
            this.pathList = picPathList;
            this.fid = fid;
            this.picPath = picPath;
            //计算出应用的最大可用内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            //取十分之一的内存作为图片缓存
            int cacheSize = maxMemory / 10;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
            //GridView的滚动监听
            pic_view.setOnScrollListener(MyAdapter.this);
        }

        @Override
        public int getCount() {
            return pathList.size();
        }

        @Override
        public Object getItem(int position) {
            return pathList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final String path = (String) getItem(position);
            viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.pic_sampling_item, parent, false);
                viewHolder.picView = (ImageView) convertView.findViewById(R.id.pic_samp_view);
                viewHolder.picBox = (CheckBox) convertView.findViewById(R.id.pic_checkBox);
                viewHolder.picVideo = (ImageView) convertView.findViewById(R.id.pic_video);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //设置图片
            setImageView(viewHolder.picView, path);
            //viewHolder.picView.setImageBitmap(getVideoThumbnail(path));
            //设置图片显示模式
            viewHolder.picView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.picBox.setChecked(isSelectList.get(String.valueOf(position)));
            viewHolder.picView.setTag(path);
            if (path.endsWith(".jpg")){
                viewHolder.picVideo.setVisibility(View.GONE);
            }
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.picBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean flag = finalViewHolder.picBox.isChecked();
                    //选择图片
                    selectImage(position, flag);
                }
            });
            viewHolder.picView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path.endsWith(".jpg")){
                        //打开大图
                        showPic(pathList.get(position), fid,picPath);
                    }else {
                        //打开视频
                        showVideo(path);
                    }
                }
            });
            return convertView;
        }


        /*给ImageView设置图片*/
        private void setImageView(ImageView imageView, String path) {
            Bitmap bitmap = getVideoThumbnail(path); //getBitmap(path);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                addImageView(imageView, path);
            }
        }

        /*将一张图片设给ImageView并且加入到LruCache中*/
        private void addImageView(ImageView imageView, String path) {
            Bitmap bmp = decodeSampledBitmap(path, 150, 100);
            imageView.setImageBitmap(bmp);
            if (mMemoryCache.get(path) == null && bmp != null) {
                mMemoryCache.put(path, bmp);
            }
        }

        /*从LruCache中返回一张图片，如果不存在就返回null*/
        private Bitmap getBitmap(String path) {
            return mMemoryCache.get(path);
        }

        /*屏幕滚动时加载图片*/
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
            }
        }

        /*内存卡中有数据时进行加载*/
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mFirstVisibleItem = firstVisibleItem;
            mVisibleItemCount = visibleItemCount;
            if (visibleItemCount > 0) {
                loadBitmaps(firstVisibleItem, visibleItemCount);
            }
        }

        /*加载屏幕范围内的图片*/
        private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                String path = pathList.get(i);
                ImageView imageView = (ImageView) pic_view.findViewWithTag(path);
                setImageView(imageView, path);
            }
        }

        /*item的ViewHolder类*/
        class ViewHolder {
            ImageView picView;
            ImageView picVideo;
            CheckBox picBox;
        }
    }
}
