package com.xinwang.shoppingcenter.bean;

import android.text.TextUtils;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.utils.GsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/3/25
 * Time;10:34
 * author:baiguiqiang
 */
public class GoodsBean  extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data==null?new ArrayList<>():data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 3
         * category_id : 1
         * title : 测试2
         * cover : http://oss.xw518app.xw518.com/default/2021/03/24/151422_1.jpg
         * pics : [{"url":"http://oss.xw518app.xw518.com/default/2021/03/24/151425_16b173becda5c7cc51a5be2b6fb4c07b.jpg","uid":1616570065754,"status":"success"},{"url":"http://oss.xw518app.xw518.com/default/2021/03/24/151430_705793b8gy1gbkmuauorgj20hs0hs0y6.jpg","uid":1616570070451,"status":"success"}]
         * body : <p>2</p>
         * sku : 2
         * state : 1
         * rank : 2
         * click : 0
         * attr0 :
         * attr1 :
         * attr2 :
         * attr3 :
         * attr4 :
         * attr5 :
         * attr6 :
         * attr7 :
         * attr8 :
         * attr9 :
         */

        private int id;
        private int category_id;
        private String title;
        private String cover;

        public String getCoverRate() {
            if (TextUtils.isEmpty(coverRate)){
                setCoverRate(getRoundRate());
            }
            return coverRate;
        }

        /**
         * 0.5-2倍数之间
         * @return
         */
        private String getRoundRate(){
            return "1";
            //   return 0.5+Math.random ()*1.5+"";
        }
        public void setCoverRate(String coverRate) {
            this.coverRate = coverRate;
        }

        private String coverRate;//宽高比率
        private String pics;
        private String body;
        private String sku;
        private String[] skus;

        public String[] getSkus() {
            if (skus==null){
                setSkus(sku.split(" "));
            }
            if (skus==null)
                setSkus(new String[]{});
            return skus;
        }

        public void setSkus(String[] skus) {
            this.skus = skus;
        }

        private int state;
        private int rank;
        private int click;
        private String attr0;
        private String attr1;
        private String attr2;
        private String attr3;
        private String attr4;
        private String attr5;
        private String attr6;
        private String attr7;
        private String attr8;
        private String attr9;




        private List<String> picBeans;

        public List<String> getPicBeans() {
            try {
                if (picBeans==null){
                    picBeans= GsonUtils.changeGsonToSafeList(getPics(),String.class);
                    setPicBeans(picBeans);
                }
            }catch (Exception e){
                picBeans =new ArrayList<>();
            }

            return picBeans;
        }

        public void setPicBeans(List<String> picBeans) {
            this.picBeans = picBeans;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public String getAttr0() {
            return attr0;
        }

        public void setAttr0(String attr0) {
            this.attr0 = attr0;
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public String getAttr2() {
            return attr2;
        }

        public void setAttr2(String attr2) {
            this.attr2 = attr2;
        }

        public String getAttr3() {
            return attr3;
        }

        public void setAttr3(String attr3) {
            this.attr3 = attr3;
        }

        public String getAttr4() {
            return attr4;
        }

        public void setAttr4(String attr4) {
            this.attr4 = attr4;
        }

        public String getAttr5() {
            return attr5;
        }

        public void setAttr5(String attr5) {
            this.attr5 = attr5;
        }

        public String getAttr6() {
            return attr6;
        }

        public void setAttr6(String attr6) {
            this.attr6 = attr6;
        }

        public String getAttr7() {
            return attr7;
        }

        public void setAttr7(String attr7) {
            this.attr7 = attr7;
        }

        public String getAttr8() {
            return attr8;
        }

        public void setAttr8(String attr8) {
            this.attr8 = attr8;
        }

        public String getAttr9() {
            return attr9;
        }

        public void setAttr9(String attr9) {
            this.attr9 = attr9;
        }
    }
}
