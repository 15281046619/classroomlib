package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2021/4/29
 * Time;10:31
 * author:baiguiqiang
 */
public class OrderListBean extends CommonEntity {

    /**
     * status : 1
     * message : 获取成功！
     * data : {"total":2,"orders":[{"id":2,"user_id":42,"tel":"13123","area_code":0,"address":"121","nickname":"123","tips":"12312","create_time":1618987742,"price":"395.00","pay_state":1,"admin_tips":"","cancel_state":1,"state":1,"items":[{"id":14,"user_id":42,"order_id":2,"sku_id":4,"num":2,"price":"100.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":4,"goods_id":3,"price":"100.00","stock":96,"cover":"","state":1,"sku0":"12","sku1":"12","sku2":"12","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":3,"category_id":1,"title":"测试2","cover":"http://oss.xw518app.xw518.com/default/2021/03/24/151422_1.jpg","video":"","pics":"[{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151425_16b173becda5c7cc51a5be2b6fb4c07b.jpg\",\"uid\":1616570065754,\"status\":\"success\"},{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151430_705793b8gy1gbkmuauorgj20hs0hs0y6.jpg\",\"uid\":1616570070451,\"status\":\"success\"}]","body":"<p>2<\/p>","sku":"颜色 内存大小 硬盘容量","state":1,"min_price":"0.00","buy_verify":0,"rank":2,"click":43,"attr0":"","attr1":"","attr2":"","attr3":"","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}},{"id":15,"user_id":42,"order_id":2,"sku_id":5,"num":1,"price":"15.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":5,"goods_id":10,"price":"15.00","stock":23,"cover":"","state":1,"sku0":"","sku1":"","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":"1.00","buy_verify":0,"rank":324234,"click":370,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}},{"id":16,"user_id":42,"order_id":2,"sku_id":6,"num":12,"price":"15.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":6,"goods_id":10,"price":"15.00","stock":0,"cover":"","state":1,"sku0":"红色","sku1":"大","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":"1.00","buy_verify":0,"rank":324234,"click":370,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}}]},{"id":1,"user_id":42,"tel":"1212312323","area_code":0,"address":"132213","nickname":"132321","tips":"","create_time":1618971368,"price":"395.00","pay_state":1,"admin_tips":"","cancel_state":2,"state":2,"items":[]}]}
     */


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * total : 2
         * orders : [{"id":2,"user_id":42,"tel":"13123","area_code":0,"address":"121","nickname":"123","tips":"12312","create_time":1618987742,"price":"395.00","pay_state":1,"admin_tips":"","cancel_state":1,"state":1,"items":[{"id":14,"user_id":42,"order_id":2,"sku_id":4,"num":2,"price":"100.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":4,"goods_id":3,"price":"100.00","stock":96,"cover":"","state":1,"sku0":"12","sku1":"12","sku2":"12","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":3,"category_id":1,"title":"测试2","cover":"http://oss.xw518app.xw518.com/default/2021/03/24/151422_1.jpg","video":"","pics":"[{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151425_16b173becda5c7cc51a5be2b6fb4c07b.jpg\",\"uid\":1616570065754,\"status\":\"success\"},{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151430_705793b8gy1gbkmuauorgj20hs0hs0y6.jpg\",\"uid\":1616570070451,\"status\":\"success\"}]","body":"<p>2<\/p>","sku":"颜色 内存大小 硬盘容量","state":1,"min_price":"0.00","buy_verify":0,"rank":2,"click":43,"attr0":"","attr1":"","attr2":"","attr3":"","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}},{"id":15,"user_id":42,"order_id":2,"sku_id":5,"num":1,"price":"15.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":5,"goods_id":10,"price":"15.00","stock":23,"cover":"","state":1,"sku0":"","sku1":"","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":"1.00","buy_verify":0,"rank":324234,"click":370,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}},{"id":16,"user_id":42,"order_id":2,"sku_id":6,"num":12,"price":"15.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":6,"goods_id":10,"price":"15.00","stock":0,"cover":"","state":1,"sku0":"红色","sku1":"大","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":"1.00","buy_verify":0,"rank":324234,"click":370,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}}]},{"id":1,"user_id":42,"tel":"1212312323","area_code":0,"address":"132213","nickname":"132321","tips":"","create_time":1618971368,"price":"395.00","pay_state":1,"admin_tips":"","cancel_state":2,"state":2,"items":[]}]
         */

        private int total;
        private List<OrdersBean> orders;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean implements Serializable {
            /**
             * id : 2
             * user_id : 42
             * tel : 13123
             * area_code : 0
             * address : 121
             * nickname : 123
             * tips : 12312
             * create_time : 1618987742
             * price : 395.00
             * pay_state : 1
             * admin_tips :
             * cancel_state : 1
             * state : 1
             * items : [{"id":14,"user_id":42,"order_id":2,"sku_id":4,"num":2,"price":"100.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":4,"goods_id":3,"price":"100.00","stock":96,"cover":"","state":1,"sku0":"12","sku1":"12","sku2":"12","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":3,"category_id":1,"title":"测试2","cover":"http://oss.xw518app.xw518.com/default/2021/03/24/151422_1.jpg","video":"","pics":"[{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151425_16b173becda5c7cc51a5be2b6fb4c07b.jpg\",\"uid\":1616570065754,\"status\":\"success\"},{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151430_705793b8gy1gbkmuauorgj20hs0hs0y6.jpg\",\"uid\":1616570070451,\"status\":\"success\"}]","body":"<p>2<\/p>","sku":"颜色 内存大小 硬盘容量","state":1,"min_price":"0.00","buy_verify":0,"rank":2,"click":43,"attr0":"","attr1":"","attr2":"","attr3":"","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}},{"id":15,"user_id":42,"order_id":2,"sku_id":5,"num":1,"price":"15.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":5,"goods_id":10,"price":"15.00","stock":23,"cover":"","state":1,"sku0":"","sku1":"","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":"1.00","buy_verify":0,"rank":324234,"click":370,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}},{"id":16,"user_id":42,"order_id":2,"sku_id":6,"num":12,"price":"15.00","admin_tips":"","waybill_id":0,"state":1,"pay_state":1,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":6,"goods_id":10,"price":"15.00","stock":0,"cover":"","state":1,"sku0":"红色","sku1":"大","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":"1.00","buy_verify":0,"rank":324234,"click":370,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}}]
             */

            private int id;
            private int user_id;
            private String tel;
            private int area_code;
            private String address;
            private String nickname;
            private String tips;
            private int create_time;
            private int price;
            private int post_price;
            private int pay_state;
            private String admin_tips;
            private int cancel_state;
            private int state;
            private List<ItemsBean> items;
            private List<CouponBean.DataBean.CouponsBean> coupons;

            public List<CouponBean.DataBean.CouponsBean> getCoupons() {
                return coupons;
            }

            public void setCoupons(List<CouponBean.DataBean.CouponsBean> coupons) {
                this.coupons = coupons;
            }

            public int getPost_price() {
                return post_price;
            }

            public void setPost_price(int post_price) {
                this.post_price = post_price;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public int getArea_code() {
                return area_code;
            }

            public void setArea_code(int area_code) {
                this.area_code = area_code;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getPay_state() {
                return pay_state;
            }

            public void setPay_state(int pay_state) {
                this.pay_state = pay_state;
            }

            public String getAdmin_tips() {
                return admin_tips;
            }

            public void setAdmin_tips(String admin_tips) {
                this.admin_tips = admin_tips;
            }

            public int getCancel_state() {
                return cancel_state;
            }

            public void setCancel_state(int cancel_state) {
                this.cancel_state = cancel_state;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean implements Serializable {
                /**
                 * id : 14
                 * user_id : 42
                 * order_id : 2
                 * sku_id : 4
                 * num : 2
                 * price : 100.00
                 * admin_tips :
                 * waybill_id : 0
                 * state : 1
                 * pay_state : 1
                 * cancel_state : 1
                 * refund_state : 1
                 * review_state : 1
                 * sku : {"id":4,"goods_id":3,"price":"100.00","stock":96,"cover":"","state":1,"sku0":"12","sku1":"12","sku2":"12","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""}
                 * goods : {"id":3,"category_id":1,"title":"测试2","cover":"http://oss.xw518app.xw518.com/default/2021/03/24/151422_1.jpg","video":"","pics":"[{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151425_16b173becda5c7cc51a5be2b6fb4c07b.jpg\",\"uid\":1616570065754,\"status\":\"success\"},{\"url\":\"http://oss.xw518app.xw518.com/default/2021/03/24/151430_705793b8gy1gbkmuauorgj20hs0hs0y6.jpg\",\"uid\":1616570070451,\"status\":\"success\"}]","body":"<p>2<\/p>","sku":"颜色 内存大小 硬盘容量","state":1,"min_price":"0.00","buy_verify":0,"rank":2,"click":43,"attr0":"","attr1":"","attr2":"","attr3":"","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}
                 */

                private int id;
                private int user_id;
                private int order_id;
                private int sku_id;
                private int num;
                private String price;
                private String admin_tips;
                private int waybill_id;
                private int state;
                private int pay_state;
                private int cancel_state;
                private int refund_state;
                private int review_state;
                private SkuBean.DataBean sku;
                private GoodsBean.DataBean goods;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getUser_id() {
                    return user_id;
                }

                public void setUser_id(int user_id) {
                    this.user_id = user_id;
                }

                public int getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(int order_id) {
                    this.order_id = order_id;
                }

                public int getSku_id() {
                    return sku_id;
                }

                public void setSku_id(int sku_id) {
                    this.sku_id = sku_id;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getAdmin_tips() {
                    return admin_tips;
                }

                public void setAdmin_tips(String admin_tips) {
                    this.admin_tips = admin_tips;
                }

                public int getWaybill_id() {
                    return waybill_id;
                }

                public void setWaybill_id(int waybill_id) {
                    this.waybill_id = waybill_id;
                }

                public int getState() {
                    return state;
                }

                public void setState(int state) {
                    this.state = state;
                }

                public int getPay_state() {
                    return pay_state;
                }

                public void setPay_state(int pay_state) {
                    this.pay_state = pay_state;
                }

                public int getCancel_state() {
                    return cancel_state;
                }

                public void setCancel_state(int cancel_state) {
                    this.cancel_state = cancel_state;
                }

                public int getRefund_state() {
                    return refund_state;
                }

                public void setRefund_state(int refund_state) {
                    this.refund_state = refund_state;
                }

                public int getReview_state() {
                    return review_state;
                }

                public void setReview_state(int review_state) {
                    this.review_state = review_state;
                }

                public SkuBean.DataBean getSku() {
                    return sku;
                }

                public void setSku(SkuBean.DataBean sku) {
                    this.sku = sku;
                }

                public GoodsBean.DataBean getGoods() {
                    return goods;
                }

                public void setGoods(GoodsBean.DataBean goods) {
                    this.goods = goods;
                }



            }
        }
    }
}
