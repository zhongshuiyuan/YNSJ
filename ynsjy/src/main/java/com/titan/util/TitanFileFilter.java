package com.titan.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by whs on 2017/9/22
 */

public class TitanFileFilter {


    /**
     * mp3
     */
    public class MP3FileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if(file.isDirectory())
                return true;
            else
            {
                String name = file.getName();
                if(name.endsWith(".mp3") || name.endsWith(".mp4"))
                    return true;
                else
                    return false;
            }

        }

    }

    /**
     * 地理数据库过滤器
     */
    public static class GeodatabaseFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {


            if(file.isDirectory())
                return true;
            else
            {
                String name = file.getName();
                if(name.endsWith(".geodatabase") || name.endsWith(".otms"))
                    return true;
                else
                    return false;
            }

        }

    }
}
