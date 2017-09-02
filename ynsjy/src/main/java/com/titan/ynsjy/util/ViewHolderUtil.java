package com.titan.ynsjy.util;


import android.util.SparseArray;
import android.view.View;

/**
 * Created by hanyw on 2017/7/12/012.
 * 适配器ViewHolder工具类
 */

public class ViewHolderUtil {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View chlidView = viewHolder.get(id);
        if (chlidView == null) {
            chlidView = view.findViewById(id);
            viewHolder.put(id, chlidView);
        }
        return (T) chlidView;
    }
}
