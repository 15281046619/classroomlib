package com.xingwang.circle.bean;

/**
 * 用户类型
 */
public interface CardFileType {
    //图片
    String IMG="image";
    //视频
    String VIDEO="shortvideo";
    //视频封面
    String COVER="cover";
    //无文件
    String NONE="text";

    //点赞
    int DIGG=0;
    //取消点赞
    int UNDIGG=1;
    //收藏
    int COLLECT=2;
    //取消收藏
    int UNCOLLECT=3;
}
