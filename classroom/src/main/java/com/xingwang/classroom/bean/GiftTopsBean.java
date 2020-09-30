package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 * Date:2020/9/30
 * Time;11:35
 * author:baiguiqiang
 */
public class GiftTopsBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user : {"id":42,"phone":"184****4035","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/UQiaRf7n9bXQeJ44tzSvwTPFkghGibmgX0fwCUC8vUKDibrAHM4W4Mpj8yHpqRITJtAGTuMXJSiaPNOy7w6MEk8Dvg/132","nickname":"谢👣蕾","jifen":346,"profile":"你好","badge":"官方人员","create_time":"2019-06-19 11:44:20","state":1,"wxid":"","unionid":"","inviter_id":0,"is_official":0}
         * jifen : 0
         * gifts : {"航母":0,"油轮":0,"火箭":0,"飞机":0,"跑车":0,"小轿车":0,"自行车":0,"爱心":0,"feiji":1,"huojian":1}
         */

        private UserBean user;
        private int jifen;
        private Object gifts;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public int getJifen() {
            return jifen;
        }

        public void setJifen(int jifen) {
            this.jifen = jifen;
        }

        public Object getGifts() {
            return gifts;
        }

        public void setGifts(Object gifts) {
            this.gifts = gifts;
        }

        public static class UserBean {
            /**
             * id : 42
             * phone : 184****4035
             * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/UQiaRf7n9bXQeJ44tzSvwTPFkghGibmgX0fwCUC8vUKDibrAHM4W4Mpj8yHpqRITJtAGTuMXJSiaPNOy7w6MEk8Dvg/132
             * nickname : 谢👣蕾
             * jifen : 346
             * profile : 你好
             * badge : 官方人员
             * create_time : 2019-06-19 11:44:20
             * state : 1
             * wxid :
             * unionid :
             * inviter_id : 0
             * is_official : 0
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
            private int is_official;

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

            public int getIs_official() {
                return is_official;
            }

            public void setIs_official(int is_official) {
                this.is_official = is_official;
            }
        }


    }
}
