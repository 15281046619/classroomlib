package com.xingwang.classroom.bean;


import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2020/8/19
 * Time;16:44
 * author:baiguiqiang
 */
public class UserInfoBean extends CommonEntity {

    /**
     * data : {"id":22,"phone":"15281046619","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/SZLuTsYHfoibEl2flT1CWic3Bbj3cFPicVZX64jgTZNbqfH4FgS7VXsZglicgOvu0nRghaRNXXianEdBX0Ump9d9ZVg/132","nickname":".","jifen":52,"profile":"放过","badge":"vip","create_time":"2019-05-22 10:44:06","state":1,"wxid":"o1D6Xw0Nn9TPHAmnrykpkfZUImTM","unionid":"","inviter_id":0,"is_official":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 22
         * phone : 15281046619
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/SZLuTsYHfoibEl2flT1CWic3Bbj3cFPicVZX64jgTZNbqfH4FgS7VXsZglicgOvu0nRghaRNXXianEdBX0Ump9d9ZVg/132
         * nickname : .
         * jifen : 52
         * profile : 放过
         * badge : vip
         * create_time : 2019-05-22 10:44:06
         * state : 1
         * wxid : o1D6Xw0Nn9TPHAmnrykpkfZUImTM
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
