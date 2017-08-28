package com.titan.ynsjy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.titan.ynsjy.R;

import java.util.List;

/**
 * Created by li on 2017/3/30
 *
 * 选择图片Adapter
 */

public class RecyclerViewAdapter_logpic extends RecyclerView.Adapter<RecyclerViewAdapter_logpic.MyViewHolder> implements View.OnClickListener{
    private List<String> pathList;
    private Context mContext;
    private int mColumnWidth=100;
    private String keyType = "log";
    private PicOnclick picOnclick;

    public RecyclerViewAdapter_logpic(Context ctx, List<String> list, int width, String type){
        this.mContext = ctx;
        this.pathList = list;
        this.mColumnWidth = width;
        this.keyType = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_log_image, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String title = mContext.getResources().getString(R.string.serverhost);
        String path = pathList.get(position);
        Bitmap bitmap = null;
        if(path.contains(title)){
            Picasso.with(mContext).load(path).into(holder.img_pic);
        }else{
            bitmap = BitmapFactory.decodeFile(path);
            holder.img_pic.setImageBitmap(bitmap);
        }
        holder.img_pic.setOnClickListener(this);
        holder.img_pic.setId(position);
        holder.itemView.setId(position);

        if(keyType.equals("log")){
            holder.img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }else{
            holder.img_close.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    @Override
    public void onClick(View v) {
        if(picOnclick != null){
            int id = v.getId();
            picOnclick.setPicOnclick(v, id);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_pic;
        ImageView img_close;

        public MyViewHolder(View v) {
            super(v);
            img_pic = (ImageView) v.findViewById(R.id.imageView_pic);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mColumnWidth,mColumnWidth);
            img_pic.setLayoutParams(params);
            params.setMargins(10,10,10,10);
            img_close = (ImageView) v.findViewById(R.id.imageView_close);
        }
    }

    /**
     * 图片点击回调接口
     */
    public interface PicOnclick{
        void setPicOnclick(View item, int position);
    }

    /**
     * 调用此方法，实现点击
     * @param onclick
     */
    public void setPicOnclick(PicOnclick onclick){
        this.picOnclick = onclick;
    }


}
