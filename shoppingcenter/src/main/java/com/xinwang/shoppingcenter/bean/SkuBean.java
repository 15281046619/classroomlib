package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2021/4/8
 * Time;10:01
 * author:baiguiqiang
 */
public class SkuBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 4
         * goods_id : 3
         * price : 100.00
         * stock : 100
         * state : 1
         * sku0 : 12
         * sku1 : 12
         * sku2 : 12
         * sku3 :
         * sku4 :
         * sku5 :
         * sku6 :
         * sku7 :
         * sku8 :
         * sku9 :
         */


        private int id;
        private int goods_id;
        private int price;//分
        private int stock;
        private int state;
        private int allow_coupon;
        private String sku0;
        private String sku1;
        private String sku2;
        private String sku3;
        private String sku4;
        private String sku5;
        private String sku6;
        private String sku7;
        private String sku8;
        private String sku9;
        private String cover;

        public int getAllow_coupon() {
            return allow_coupon;
        }

        public void setAllow_coupon(int allow_coupon) {
            this.allow_coupon = allow_coupon;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getSku0() {
            return sku0;
        }

        public void setSku0(String sku0) {
            this.sku0 = sku0;
        }

        public String getSku1() {
            return sku1;
        }

        public void setSku1(String sku1) {
            this.sku1 = sku1;
        }

        public String getSku2() {
            return sku2;
        }

        public void setSku2(String sku2) {
            this.sku2 = sku2;
        }

        public String getSku3() {
            return sku3;
        }

        public void setSku3(String sku3) {
            this.sku3 = sku3;
        }

        public String getSku4() {
            return sku4;
        }

        public void setSku4(String sku4) {
            this.sku4 = sku4;
        }

        public String getSku5() {
            return sku5;
        }

        public void setSku5(String sku5) {
            this.sku5 = sku5;
        }

        public String getSku6() {
            return sku6;
        }

        public void setSku6(String sku6) {
            this.sku6 = sku6;
        }

        public String getSku7() {
            return sku7;
        }

        public void setSku7(String sku7) {
            this.sku7 = sku7;
        }

        public String getSku8() {
            return sku8;
        }

        public void setSku8(String sku8) {
            this.sku8 = sku8;
        }

        public String getSku9() {
            return sku9;
        }

        public void setSku9(String sku9) {
            this.sku9 = sku9;
        }
    }
}
