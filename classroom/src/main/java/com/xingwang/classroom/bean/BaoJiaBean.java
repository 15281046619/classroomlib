package com.xingwang.classroom.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2021/9/15
 * Time;16:05
 * author:baiguiqiang
 */
public class BaoJiaBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5739
         * date : 2021-09-15
         * provice : 四川
         * type : 玉米
         * price : 1212.00
         * user_id : 22
         * create_time : 1631670171
         * content : 定好时间
         * user : {"id":22,"phone":"152****6619","avatar":"http://oss.xw518app.xw518.com/2020-11-30/e7d3ee51f2884a568885eba8e84cdf3722zy.png","nickname":"哈哈","jifen":1269,"profile":"u乌苏三四死亡ii啊计算机试试看开始看时时刻刻上课困死咔咔咔可是可是上课","badge":"","create_time":"2019-05-22 10:44:06","state":1,"wxid":"o1D6Xw0Nn9TPHAmnrykpkfZUImTM","unionid":"","inviter_id":0,"inviter_time":0,"is_official":0,"reg_time":0,"launch_time":1631598967,"vip":0,"sex":1,"is_erp":2}
         */

        private int id;
        private String date;
        private String provice;
        private String type;
        private String price;
        private int user_id;
        private int create_time;
        private String content;
        private UserBean user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getProvice() {
            return provice;
        }

        public void setProvice(String provice) {
            this.provice = provice;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 22
             * phone : 152****6619
             * avatar : http://oss.xw518app.xw518.com/2020-11-30/e7d3ee51f2884a568885eba8e84cdf3722zy.png
             * nickname : 哈哈
             * jifen : 1269
             * profile : u乌苏三四死亡ii啊计算机试试看开始看时时刻刻上课困死咔咔咔可是可是上课
             * badge :
             * create_time : 2019-05-22 10:44:06
             * state : 1
             * wxid : o1D6Xw0Nn9TPHAmnrykpkfZUImTM
             * unionid :
             * inviter_id : 0
             * inviter_time : 0
             * is_official : 0
             * reg_time : 0
             * launch_time : 1631598967
             * vip : 0
             * sex : 1
             * is_erp : 2
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
    }
}
