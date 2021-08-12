package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2021/8/6
 * Time;11:39
 * author:baiguiqiang
 */
public class LateLyOrderBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1482
         * user_id : 4833
         * order_id : 1024
         * goods_id : 65
         * sku_id : 143
         * num : 8
         * price : 1600
         * admin_tips :
         * waybill_id : 0
         * state : 2
         * pay_state : 1
         * waybill_state : 1
         * cancel_state : 2
         * refund_state : 1
         * review_state : 1
         * user : {"id":4833,"phone":"19180732819","avatar":"http://oss.xw518app.xw518.com/2021-05-14/ff7a5fd3ee1a46ffae7e844154b58fd34833zy.png","nickname":"兴旺药业邱老师","jifen":450,"profile":"","badge":"技术服务老师","create_time":"2020-11-25 13:28:37","state":1,"wxid":"","unionid":"","inviter_id":0,"inviter_time":0,"is_official":1,"reg_time":1606282117,"launch_time":1628214031,"vip":0,"sex":0,"is_erp":0}
         * goods : {"id":65,"category_id":39,"title":"热痛宁（卡巴匹林钙可溶性粉）","cover":"http://oss.xw518app.xw518.com/default/2021/08/05/164723_热痛宁.jpg","pics":"[]","sku":"规格","state":1,"video":"","min_price":1600,"buy_verify":0,"rank":1,"click":17,"review_count":0,"attr0":"[]","attr1":"[]","attr2":"","attr3":"","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}
         * sku : {"id":143,"goods_id":65,"price":1600,"stock":999999,"allow_coupon":1,"state":1,"sku0":"100g/袋","sku1":"","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":"","cover":""}
         * order : {"id":1024,"order_no":"20210805164055000012e100000400","user_id":4833,"tel":"191****2819","area_code":310101,"address":"上海市 市辖区 黄浦区 jjdj\n","nickname":"jdjdk","tips":"","create_time":1628152855,"price":14300,"item_price":12800,"coupon_price":0,"total_price":14300,"reduction_price":0,"post_price":1500,"pay_state":1,"admin_tips":"","cancel_state":2,"state":2,"pay_type":null,"pay_time":0}
         */

        private int id;
        private int user_id;
        private int order_id;
        private int goods_id;
        private int sku_id;
        private int num;
        private int price;
        private String admin_tips;
        private int waybill_id;
        private int state;
        private int pay_state;
        private int waybill_state;
        private int cancel_state;
        private int refund_state;
        private int review_state;
        private UserBean user;
        private GoodsBean goods;
        private SkuBean sku;
        private OrderBean order;

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

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
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

        public int getWaybill_state() {
            return waybill_state;
        }

        public void setWaybill_state(int waybill_state) {
            this.waybill_state = waybill_state;
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public SkuBean getSku() {
            return sku;
        }

        public void setSku(SkuBean sku) {
            this.sku = sku;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public static class UserBean {
            /**
             * id : 4833
             * phone : 19180732819
             * avatar : http://oss.xw518app.xw518.com/2021-05-14/ff7a5fd3ee1a46ffae7e844154b58fd34833zy.png
             * nickname : 兴旺药业邱老师
             * jifen : 450
             * profile :
             * badge : 技术服务老师
             * create_time : 2020-11-25 13:28:37
             * state : 1
             * wxid :
             * unionid :
             * inviter_id : 0
             * inviter_time : 0
             * is_official : 1
             * reg_time : 1606282117
             * launch_time : 1628214031
             * vip : 0
             * sex : 0
             * is_erp : 0
             */

            private int id;
            private String phone;
            private String avatar;
            private String nickname;
            private int jifen;
            private String profile;
            private String badge;
            private String create_time;
            private int state;
            private String wxid;
            private String unionid;
            private int inviter_id;
            private int inviter_time;
            private int is_official;
            private int reg_time;
            private int launch_time;
            private int vip;
            private int sex;
            private int is_erp;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getJifen() {
                return jifen;
            }

            public void setJifen(int jifen) {
                this.jifen = jifen;
            }

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getWxid() {
                return wxid;
            }

            public void setWxid(String wxid) {
                this.wxid = wxid;
            }

            public String getUnionid() {
                return unionid;
            }

            public void setUnionid(String unionid) {
                this.unionid = unionid;
            }

            public int getInviter_id() {
                return inviter_id;
            }

            public void setInviter_id(int inviter_id) {
                this.inviter_id = inviter_id;
            }

            public int getInviter_time() {
                return inviter_time;
            }

            public void setInviter_time(int inviter_time) {
                this.inviter_time = inviter_time;
            }

            public int getIs_official() {
                return is_official;
            }

            public void setIs_official(int is_official) {
                this.is_official = is_official;
            }

            public int getReg_time() {
                return reg_time;
            }

            public void setReg_time(int reg_time) {
                this.reg_time = reg_time;
            }

            public int getLaunch_time() {
                return launch_time;
            }

            public void setLaunch_time(int launch_time) {
                this.launch_time = launch_time;
            }

            public int getVip() {
                return vip;
            }

            public void setVip(int vip) {
                this.vip = vip;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getIs_erp() {
                return is_erp;
            }

            public void setIs_erp(int is_erp) {
                this.is_erp = is_erp;
            }
        }

        public static class GoodsBean {
            /**
             * id : 65
             * category_id : 39
             * title : 热痛宁（卡巴匹林钙可溶性粉）
             * cover : http://oss.xw518app.xw518.com/default/2021/08/05/164723_热痛宁.jpg
             * pics : []
             * sku : 规格
             * state : 1
             * video :
             * min_price : 1600
             * buy_verify : 0
             * rank : 1
             * click : 17
             * review_count : 0
             * attr0 : []
             * attr1 : []
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
            private String pics;
            private String sku;
            private int state;
            private String video;
            private int min_price;
            private int buy_verify;
            private int rank;
            private int click;
            private int review_count;
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

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public int getMin_price() {
                return min_price;
            }

            public void setMin_price(int min_price) {
                this.min_price = min_price;
            }

            public int getBuy_verify() {
                return buy_verify;
            }

            public void setBuy_verify(int buy_verify) {
                this.buy_verify = buy_verify;
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

            public int getReview_count() {
                return review_count;
            }

            public void setReview_count(int review_count) {
                this.review_count = review_count;
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

        public static class SkuBean {
            /**
             * id : 143
             * goods_id : 65
             * price : 1600
             * stock : 999999
             * allow_coupon : 1
             * state : 1
             * sku0 : 100g/袋
             * sku1 :
             * sku2 :
             * sku3 :
             * sku4 :
             * sku5 :
             * sku6 :
             * sku7 :
             * sku8 :
             * sku9 :
             * cover :
             */

            private int id;
            private int goods_id;
            private int price;
            private int stock;
            private int allow_coupon;
            private int state;
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

            public int getAllow_coupon() {
                return allow_coupon;
            }

            public void setAllow_coupon(int allow_coupon) {
                this.allow_coupon = allow_coupon;
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

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }
        }

        public static class OrderBean {
            /**
             * id : 1024
             * order_no : 20210805164055000012e100000400
             * user_id : 4833
             * tel : 191****2819
             * area_code : 310101
             * address : 上海市 市辖区 黄浦区 jjdj
             * nickname : jdjdk
             * tips :
             * create_time : 1628152855
             * price : 14300
             * item_price : 12800
             * coupon_price : 0
             * total_price : 14300
             * reduction_price : 0
             * post_price : 1500
             * pay_state : 1
             * admin_tips :
             * cancel_state : 2
             * state : 2
             * pay_type : null
             * pay_time : 0
             */

            private int id;
            private String order_no;
            private int user_id;
            private String tel;
            private int area_code;
            private String address;
            private String nickname;
            private String tips;
            private int create_time;
            private int price;
            private int item_price;
            private int coupon_price;
            private int total_price;
            private int reduction_price;
            private int post_price;
            private int pay_state;
            private String admin_tips;
            private int cancel_state;
            private int state;
            private Object pay_type;
            private int pay_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
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

            public int getItem_price() {
                return item_price;
            }

            public void setItem_price(int item_price) {
                this.item_price = item_price;
            }

            public int getCoupon_price() {
                return coupon_price;
            }

            public void setCoupon_price(int coupon_price) {
                this.coupon_price = coupon_price;
            }

            public int getTotal_price() {
                return total_price;
            }

            public void setTotal_price(int total_price) {
                this.total_price = total_price;
            }

            public int getReduction_price() {
                return reduction_price;
            }

            public void setReduction_price(int reduction_price) {
                this.reduction_price = reduction_price;
            }

            public int getPost_price() {
                return post_price;
            }

            public void setPost_price(int post_price) {
                this.post_price = post_price;
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

            public Object getPay_type() {
                return pay_type;
            }

            public void setPay_type(Object pay_type) {
                this.pay_type = pay_type;
            }

            public int getPay_time() {
                return pay_time;
            }

            public void setPay_time(int pay_time) {
                this.pay_time = pay_time;
            }
        }
    }
}
