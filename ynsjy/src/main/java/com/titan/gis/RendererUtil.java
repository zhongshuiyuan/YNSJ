package com.titan.gis;

import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.Symbol;

/**
 * Created by whs on 2017/9/11
 * 渲染器工具类
 * 渲染器基类 BaseRenderer
 * 分级渲染器 ClassBreaksRenderer
 * 字典渲染器 DictionaryRenderer
 * 简单渲染器 SimpleRenderer
 * 唯一值渲染器 UniqueValueRenderer
 */

public class RendererUtil {
    /**
     * 简单渲染
     * @param symbol
     * @return
     */
    public static SimpleRenderer getSimpleRenderer(Symbol symbol){
        return new SimpleRenderer(symbol);

    }
}
