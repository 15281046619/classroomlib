package com.xingwang.classroom.view.gift;

/**
 * Date:2020/9/12
 * Time;11:03
 * author:baiguiqiang
 */
public class GiftBean {
    private String avatar;
    private String name;
    private String userId;
    private String giftName;
    private String giftId;
    private String giftImg;
    private int sum;

    public GiftBean(String avatar, String name, String userId, String giftName, String giftId, String giftImg, int sum) {
        this.avatar = avatar;
        this.name = name;
        this.userId = userId;
        this.giftName = giftName;
        this.giftId = giftId;
        this.giftImg = giftImg;
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }
}
