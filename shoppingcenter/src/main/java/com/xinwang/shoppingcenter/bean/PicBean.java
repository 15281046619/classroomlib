package com.xinwang.shoppingcenter.bean;

import java.io.Serializable;

/**
 * Date:2021/3/29
 * Time;15:25
 * author:baiguiqiang
 */
public class PicBean implements Serializable {
    private String url;
    private String status;

    public PicBean(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
