package com.titan.gis.callout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Field;
import com.titan.ynsjy.R;
import com.titan.ynsjy.util.ReUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by whs on 2017/9/13
 * Callout工具类
 */

public class CalloutUtil {
    /**
     * 创建数据信息
     * @param context
     * @param fields
     * @param feature
     * @param onClickListener
     * @return
     */
    public static View createCallView(Context context, List<Field> fields, GeodatabaseFeature feature, View.OnClickListener onClickListener){

        View view = LayoutInflater.from(context).inflate(R.layout.callout_attr, null);
        TextView tv_attr = (TextView) view.findViewById(R.id.tv_content);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        StringBuffer calloutcontent = new StringBuffer();
        calloutcontent.append("");
        Map<String,Object> attrmap=feature.getAttributes();
        try {
            //final Set<String> keys = attrmap.keySet();
            for (Field field : fields) {
                String alias = field.getAlias();
                //获取包含中文别名的字段
                if(!ReUtil.hasChinese(alias)){
                  continue;
                }else{
                    Object object = attrmap.get(field.getName());
                    String value;
                    if (object!=null){
                        value = attrmap.get(field.getName()).toString();
                    }else {
                        value = "";
                    }
                    calloutcontent.append(alias + " | " + value + "\n");
                }
            }


        } catch (Exception e) {
            Toast.makeText(context, "获取属性信息异常" + e, Toast.LENGTH_LONG).show();

        }
        tv_attr.setText(calloutcontent);
        iv_close.setOnClickListener(onClickListener);
        return view;
    }
}
