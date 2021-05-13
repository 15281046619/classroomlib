package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/4/15
 * Time;10:29
 * author:baiguiqiang
 */
public class CouponBean extends CommonEntity {

    /**
     * data : {"coupons":[{"id":3,"user_id":42,"name":"测试代金券","instructions":"测试","fee":1,"expire_at":1599898749,"conditions":"<p>测试<\/p>","is_verification":2,"verification_time":0,"verification_context":null,"create_time":0,"create_from":""},{"id":2,"user_id":42,"name":"10元代金券","instructions":"满200可用","fee":1000,"expire_at":19999999,"conditions":null,"is_verification":2,"verification_time":0,"verification_context":null,"create_time":0,"create_from":""}],"total":2}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * coupons : [{"id":3,"user_id":42,"name":"测试代金券","instructions":"测试","fee":1,"expire_at":1599898749,"conditions":"<p>测试<\/p>","is_verification":2,"verification_time":0,"verification_context":null,"create_time":0,"create_from":""},{"id":2,"user_id":42,"name":"10元代金券","instructions":"满200可用","fee":1000,"expire_at":19999999,"conditions":null,"is_verification":2,"verification_time":0,"verification_context":null,"create_time":0,"create_from":""}]
         * total : 2
         */

        private int total;
        private List<CouponsBean> coupons;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<CouponsBean> getCoupons() {
            return coupons==null?new ArrayList<>():coupons;
        }

        public void setCoupons(List<CouponsBean> coupons) {
            this.coupons = coupons;
        }

        public static class CouponsBean implements Serializable {
            /**
             * id : 3
             * user_id : 42
             * name : 测试代金券
             * instructions : 测试
             * fee : 1
             * expire_at : 1599898749
             * conditions : <p>测试</p>
             * is_verification : 2
             * verification_time : 0
             * verification_context : null
             * create_time : 0
             * create_from :
             */

            private int id;
            private int user_id;
            private String name;
            private String instructions;
            private int fee;
            private long expire_at;
            private String conditions;
            private int is_verification;
            private int verification_time;
            private Object verification_context;
            private long create_time;
            private String create_from;
            private int min_money;
            private long expire_from;//开始时间
            private String sku_ids; //规格限制
            private String goods_ids;//商品限制
            private String without_sku_ids;//禁止某些规格使用  不计算这些规格的商品
            private String noUseCause;//不能使用愿意。 空代表可以使用

            public String getWithout_sku_ids() {
                return without_sku_ids;
            }

            public void setWithout_sku_ids(String without_sku_ids) {
                this.without_sku_ids = without_sku_ids;
            }

            public String getNoUseCause() {
                return noUseCause;
            }

            public void setNoUseCause(String noUseCause) {
                this.noUseCause = noUseCause;
            }

            public String getSku_ids() {
                return sku_ids;
            }

            public void setSku_ids(String sku_ids) {
                this.sku_ids = sku_ids;
            }

            public String getGoods_ids() {
                return goods_ids;
            }

            public void setGoods_ids(String goods_ids) {
                this.goods_ids = goods_ids;
            }

            public long getExpire_from() {
                return expire_from;
            }

            public void setExpire_from(long expire_from) {
                this.expire_from = expire_from;
            }

            public int getMin_money() {
                return min_money;
            }

            public void setMin_money(int min_money) {
                this.min_money = min_money;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getInstructions() {
                return instructions;
            }

            public void setInstructions(String instructions) {
                this.instructions = instructions;
            }

            public int getFee() {
                return fee;
            }

            public void setFee(int fee) {
                this.fee = fee;
            }

            public long getExpire_at() {
                return expire_at;
            }

            public void setExpire_at(long expire_at) {
                this.expire_at = expire_at;
            }

            public String getConditions() {
                return conditions;
            }

            public void setConditions(String conditions) {
                this.conditions = conditions;
            }

            public int getIs_verification() {
                return is_verification;
            }

            public void setIs_verification(int is_verification) {
                this.is_verification = is_verification;
            }

            public int getVerification_time() {
                return verification_time;
            }

            public void setVerification_time(int verification_time) {
                this.verification_time = verification_time;
            }

            public Object getVerification_context() {
                return verification_context;
            }

            public void setVerification_context(Object verification_context) {
                this.verification_context = verification_context;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getCreate_from() {
                return create_from;
            }

            public void setCreate_from(String create_from) {
                this.create_from = create_from;
            }
        }
    }
}
