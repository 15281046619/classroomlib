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
        return maxBugSum==0?999:maxBugSum;//最大数目三位数
    }

    public void setMaxBugSum(int maxBugSum) {
        this.maxBugSum = maxBugSum;
    }

    private int stockQuantity;

    private boolean inStock;

    private String originPrice;

    private String sellingPrice;

    private List<SkuAttribute> attributes;
    private String showPrice;
    private long totalStock;

    public long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(long totalStock) {
        this.totalStock = totalStock;
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



    public String getOriginPrice() {

        return originPrice;

    }



    public void setOriginPrice(String originPrice) {

        this.originPrice = originPrice;

    }



    public String getSellingPrice() {

        return sellingPrice;

    }



    public void setSellingPrice(String sellingPrice) {

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

        dest.writeString(this.originPrice);

        dest.writeString(this.sellingPrice);

        dest.writeTypedList(this.attributes);
        dest.writeString(this.goodTitle);
        dest.writeInt(this.addSum);
        dest.writeInt(this.goodId);
        dest.writeString(this.showPrice);
        dest.writeLong(this.totalStock);
        dest.writeInt(this.maxBugSum);
    }



    protected Sku(Parcel in) {

        this.id = in.readString();

        this.mainImage = in.readString();

        this.stockQuantity = in.readInt();

        this.inStock = in.readByte() != 0;

        this.originPrice = in.readString();

        this.sellingPrice = in.readString();

        this.attributes = in.createTypedArrayList(SkuAttribute.CREATOR);
        this.goodTitle =in.readString();
        this.addSum = in.readInt();
        this.goodId =in.readInt();
        this.showPrice =in.readString();
        this.totalStock =in.readLong();
        this.maxBugSum =in.readInt();
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