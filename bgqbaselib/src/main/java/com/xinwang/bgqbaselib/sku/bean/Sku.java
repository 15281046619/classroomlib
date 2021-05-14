package com.xinwang.bgqbaselib.sku.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Date:2021/4/8
 * Time;15:19
 * author:baiguiqiang
 */
public class Sku implements Parcelable {

    private String id;

    private String mainImage;
    private int maxBugSum;//个人最大购买数目

    public int getMaxBugSum() {
        return maxBugSum==0?99999:maxBugSum;//最大数目三位数
    }

    public void setMaxBugSum(int maxBugSum) {
        this.maxBugSum = maxBugSum;
    }

    private int stockQuantity;

    private boolean inStock;

    private int originPrice;

    private int sellingPrice;

    private List<SkuAttribute> attributes;
    private String showPrice;
    private long totalStock;

    public long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(long totalStock) {
        this.totalStock = totalStock;
    }
    private int allow_coupon;//1允许(默认) 0不允许

    public int getAllow_coupon() {
        return allow_coupon;
    }

    public void setAllow_coupon(int allow_coupon) {
        this.allow_coupon = allow_coupon;
    }

    private int goodId;//商品id

    private String goodTitle;


    private boolean isCheck;

    private int addSum;



    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }



    public String getGoodTitle() {
        return goodTitle;
    }

    public void setGoodTitle(String goodTitle) {
        this.goodTitle = goodTitle;
    }


    public String getShowPrice() {
        return showPrice;
    }
    public int getAddSum() {
        if (addSum==0)
            setAddSum(1);
        return addSum;
    }

    public void setAddSum(int addSum) {
        this.addSum = addSum;
    }
    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }

    public String getId() {

        return id;

    }



    public void setId(String id) {

        this.id = id;

    }



    public String getMainImage() {

        return mainImage;

    }



    public void setMainImage(String mainImage) {

        this.mainImage = mainImage;

    }



    public int getStockQuantity() {

        return stockQuantity;

    }



    public void setStockQuantity(int stockQuantity) {

        this.stockQuantity = stockQuantity;

    }



    public boolean isInStock() {

        return inStock;

    }



    public void setInStock(boolean inStock) {

        this.inStock = inStock;

    }



    public int getOriginPrice() {

        return originPrice;

    }



    public void setOriginPrice(int originPrice) {

        this.originPrice = originPrice;

    }



    public int getSellingPrice() {

        return sellingPrice;

    }



    public void setSellingPrice(int sellingPrice) {

        this.sellingPrice = sellingPrice;

    }



    public List<SkuAttribute> getAttributes() {

        return attributes;

    }



    public void setAttributes(List<SkuAttribute> attributes) {

        this.attributes = attributes;

    }



    @Override

    public String toString() {

        return "Sku{" +

                "id='" + id + '\'' +

                ", mainImage='" + mainImage + '\'' +

                ", stockQuantity=" + stockQuantity +

                ", inStock=" + inStock +

                ", originPrice=" + originPrice +

                ", sellingPrice=" + sellingPrice +

                ", attributes=" + attributes +

                '}';

    }



    public Sku() {

    }



    @Override

    public int describeContents() {

        return 0;

    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.id);

        dest.writeString(this.mainImage);

        dest.writeInt(this.stockQuantity);

        dest.writeByte(this.inStock ? (byte) 1 : (byte) 0);

        dest.writeInt(this.originPrice);

        dest.writeInt(this.sellingPrice);

        dest.writeTypedList(this.attributes);
        dest.writeString(this.goodTitle);
        dest.writeInt(this.addSum);
        dest.writeInt(this.goodId);
        dest.writeString(this.showPrice);
        dest.writeLong(this.totalStock);
        dest.writeInt(this.maxBugSum);
        dest.writeInt(this.allow_coupon);
    }



    protected Sku(Parcel in) {

        this.id = in.readString();

        this.mainImage = in.readString();

        this.stockQuantity = in.readInt();

        this.inStock = in.readByte() != 0;

        this.originPrice = in.readInt();

        this.sellingPrice = in.readInt();

        this.attributes = in.createTypedArrayList(SkuAttribute.CREATOR);
        this.goodTitle =in.readString();
        this.addSum = in.readInt();
        this.goodId =in.readInt();
        this.showPrice =in.readString();
        this.totalStock =in.readLong();
        this.maxBugSum =in.readInt();
        this.allow_coupon =in.readInt();
    }



    public static final Creator<Sku> CREATOR = new Creator<Sku>() {

        @Override

        public Sku createFromParcel(Parcel source) {

            return new Sku(source);

        }



        @Override

        public Sku[] newArray(int size) {

            return new Sku[size];

        }

    };

}