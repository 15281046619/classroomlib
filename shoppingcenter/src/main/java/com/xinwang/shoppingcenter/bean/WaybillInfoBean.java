package com.xinwang.shoppingcenter.bean;

import com.google.gson.annotations.SerializedName;
import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2021/5/6
 * Time;11:14
 * author:baiguiqiang
 */
public class WaybillInfoBean extends CommonEntity {

    /**
     * data : {"queryTimes":3,"upgrade_info":"","fee_num":1,"status":4,"expSpellName":"shentong","msg":"查询成功","updateStr":"2021-05-06 10:25:20","possibleExpList":[],"flag":true,"tel":"400-889-5543","ret_code":0,"logo":"http://static.showapi.com/app2/img/expImg/shentong.jpg","expTextName":"申通快递","data":[{"context":"已签收，签收人凭取货码签收。","time":"2021-04-28 18:52:30"},{"context":"快件已由成都中铁城锦南汇646号店代收，请及时取件，如有疑问请联系17345053511","time":"2021-04-28 11:50:27"},{"context":"【四川成都中和公司】的派件员【新怡万涛】正在为您派件，如有疑问请联系派件员，联系电话【15708132318】","time":"2021-04-28 09:21:39"},{"context":"快件已到达【四川成都中和公司】扫描员是【张龙】","time":"2021-04-28 08:05:56"},{"context":"快件由【四川成都转运中心】发往【四川成都中和公司】","time":"2021-04-27 16:31:05"},{"context":"快件已到达【四川成都转运中心】扫描员是【康奋威】","time":"2021-04-27 16:21:07"},{"context":"快件由【福建泉州转运中心】发往【四川成都转运中心】","time":"2021-04-26 04:38:38"},{"context":"快件已到达【福建泉州转运中心】扫描员是【国勇代理】","time":"2021-04-26 04:24:52"},{"context":"快件由【福建安海公司】发往【福建泉州转运中心】","time":"2021-04-26 02:04:55"},{"context":"快件由【福建安海公司】发往【福建泉州转运中心】","time":"2021-04-26 01:20:28"},{"context":"【福建安海公司】的收件员【安海自动分拣】已收件","time":"2021-04-26 01:19:35"},{"context":"【福建安海公司】已收件，扫描员【詹栋军】","time":"2021-04-25 23:26:55"}],"mailNo":"773093909188061","dataSize":12,"update":1620267920777}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * queryTimes : 3
         * upgrade_info :
         * fee_num : 1
         * status : 4
         * expSpellName : shentong
         * msg : 查询成功
         * updateStr : 2021-05-06 10:25:20
         * possibleExpList : []
         * flag : true
         * tel : 400-889-5543
         * ret_code : 0
         * logo : http://static.showapi.com/app2/img/expImg/shentong.jpg
         * expTextName : 申通快递
         * data : [{"context":"已签收，签收人凭取货码签收。","time":"2021-04-28 18:52:30"},{"context":"快件已由成都中铁城锦南汇646号店代收，请及时取件，如有疑问请联系17345053511","time":"2021-04-28 11:50:27"},{"context":"【四川成都中和公司】的派件员【新怡万涛】正在为您派件，如有疑问请联系派件员，联系电话【15708132318】","time":"2021-04-28 09:21:39"},{"context":"快件已到达【四川成都中和公司】扫描员是【张龙】","time":"2021-04-28 08:05:56"},{"context":"快件由【四川成都转运中心】发往【四川成都中和公司】","time":"2021-04-27 16:31:05"},{"context":"快件已到达【四川成都转运中心】扫描员是【康奋威】","time":"2021-04-27 16:21:07"},{"context":"快件由【福建泉州转运中心】发往【四川成都转运中心】","time":"2021-04-26 04:38:38"},{"context":"快件已到达【福建泉州转运中心】扫描员是【国勇代理】","time":"2021-04-26 04:24:52"},{"context":"快件由【福建安海公司】发往【福建泉州转运中心】","time":"2021-04-26 02:04:55"},{"context":"快件由【福建安海公司】发往【福建泉州转运中心】","time":"2021-04-26 01:20:28"},{"context":"【福建安海公司】的收件员【安海自动分拣】已收件","time":"2021-04-26 01:19:35"},{"context":"【福建安海公司】已收件，扫描员【詹栋军】","time":"2021-04-25 23:26:55"}]
         * mailNo : 773093909188061
         * dataSize : 12
         * update : 1620267920777
         */

        private int queryTimes;
        private String upgrade_info;
        private int fee_num;
        @SerializedName("status")
        private int statusX;
        private String expSpellName;
        private String msg;
        private String updateStr;
        private boolean flag;
        private String tel;
        private int ret_code;
        private String logo;
        private String expTextName;
        private String mailNo;
        private int dataSize;
        private long update;

        private List<DataBean> data;

        public int getQueryTimes() {
            return queryTimes;
        }

        public void setQueryTimes(int queryTimes) {
            this.queryTimes = queryTimes;
        }

        public String getUpgrade_info() {
            return upgrade_info;
        }

        public void setUpgrade_info(String upgrade_info) {
            this.upgrade_info = upgrade_info;
        }

        public int getFee_num() {
            return fee_num;
        }

        public void setFee_num(int fee_num) {
            this.fee_num = fee_num;
        }

        public int getStatusX() {
            return statusX;
        }

        public void setStatusX(int statusX) {
            this.statusX = statusX;
        }

        public String getExpSpellName() {
            return expSpellName;
        }

        public void setExpSpellName(String expSpellName) {
            this.expSpellName = expSpellName;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUpdateStr() {
            return updateStr;
        }

        public void setUpdateStr(String updateStr) {
            this.updateStr = updateStr;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getExpTextName() {
            return expTextName;
        }

        public void setExpTextName(String expTextName) {
            this.expTextName = expTextName;
        }

        public String getMailNo() {
            return mailNo;
        }

        public void setMailNo(String mailNo) {
            this.mailNo = mailNo;
        }

        public int getDataSize() {
            return dataSize;
        }

        public void setDataSize(int dataSize) {
            this.dataSize = dataSize;
        }

        public long getUpdate() {
            return update;
        }

        public void setUpdate(long update) {
            this.update = update;
        }



        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * context : 已签收，签收人凭取货码签收。
             * time : 2021-04-28 18:52:30
             */

            private String context;
            private String time;

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
