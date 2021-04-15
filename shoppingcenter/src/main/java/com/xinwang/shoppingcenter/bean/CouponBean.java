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
            private double fee;
            private int expire_at;
            private String conditions;
            private int is_verification;
            private int verification_time;
            private Object verification_context;
            private int create_time;
            private String create_from;

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

            public double getFee() {
                return fee;
            }

            public void setFee(double fee) {
                this.fee = fee;
            }

            public int getExpire_at() {
                return expire_at;
            }

            public void setExpire_at(int expire_at) {
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

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
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
