package com.xingwang.circle.utils;

import com.blankj.utilcode.util.FileUtils;
import com.xingwang.swip.utils.Constants;

public class CacheUtils {
    public static void clearCache(){
        FileUtils.deleteDir(Constants.VIDEO_PATH);
    }
}
