package com.liuzhaobin.example.zxing.util;

import com.google.zxing.common.StringUtils;

import java.io.File;

public class FileUtil {

    /**
     * user: Rex
     * date: 2016年12月29日  上午12:25:04
     * @param dir
     * void
     * TODO 判断路径是否存在，如果不存在则创建
     */
    public static void mkdirs(String dir){

        File file = new File(dir);
        if(file.isDirectory()){
            return;
        } else {
            file.mkdirs();
        }
    }

}
