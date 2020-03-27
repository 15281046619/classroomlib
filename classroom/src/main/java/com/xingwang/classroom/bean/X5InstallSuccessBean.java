package com.xingwang.classroom.bean;

/**
 * Date:2020/3/27
 * Time;10:35
 * author:baiguiqiang
 */
public class X5InstallSuccessBean {
    private int type;//0 下载 1 安装
    private int progress;//下载进度

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public X5InstallSuccessBean(int type, int progress) {
        this.type = type;
        this.progress = progress;
    }
}
