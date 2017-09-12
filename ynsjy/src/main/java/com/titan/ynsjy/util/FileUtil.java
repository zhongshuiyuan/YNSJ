package com.titan.ynsjy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by whs on 2017/9/12
 * 文件操作类
 */

public class FileUtil {

    public static void getFileName(){

    }

    /**
     * 复制单个文件
     * @param oldPathFile 准备复制的文件源
     * @param newPathFile 拷贝到新绝对路径带文件名
     * @return
     */
    public static void copyFile(String oldPathFile, String newPathFile) throws IOException {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPathFile);
        if (oldfile.exists()) { //文件存在时
            InputStream inStream = new FileInputStream(oldPathFile); //读入原文件
            FileOutputStream fs = new FileOutputStream(newPathFile);
            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }

    }



}
