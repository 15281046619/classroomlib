package com.xinwang.shoppingcenter.bean;

/**
 * Date:2021/6/25
 * Time;10:33
 * author:baiguiqiang
 */
public class CharBodyBean {
    private String title;
    private String items;

    public CharBodyBean(String title, String items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
