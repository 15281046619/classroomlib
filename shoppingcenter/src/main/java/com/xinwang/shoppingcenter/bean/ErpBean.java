package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2021/4/12
 * Time;11:26
 * author:baiguiqiang
 */
public class ErpBean extends CommonEntity {

    /**
     * data : {"id":42,"phone":"18202839759","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/UQiaRf7n9bXQeJ44tzSvwTPFkghGibmgX0fwCUC8vUKDibrAHM4W4Mpj8yHpqRITJtAjdxC3VJB5fxnlcuQiaacWdw/132","nickname":"小谢","jifen":327,"profile":"你好哦","badge":"","create_time":"2019-06-19 11:44:20","state":1,"wxid":"o1D6XwwqG4iUshrTGiB4SSwSRCK0","unionid":"","inviter_id":0,"is_official":0,"launch_time":1618185757,"vip":1,"sex":3}
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
         * id : 42
         * phone : 18202839759
         * avatar : https://thirdwx.qlogo.cn/mmopen/vi_32/UQiaRf7n9bXQeJ44tzSvwTPFkghGibmgX0fwCUC8vUKDibrAHM4W4Mpj8yHpqRITJtAjdxC3VJB5fxnlcuQiaacWdw/132
         * nickname : 小谢
         * jifen : 327
         * profile : 你好哦
         * badge :
         * create_time : 2019-06-19 11:44:20
         * state : 1
         * wxid : o1D6XwwqG4iUshrTGiB4SSwSRCK0
         * unionid :
         * inviter_id : 0
         * is_official : 0
         * launch_time : 1618185757
         * vip : 1
         * sex : 3
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
        private int launch_time;
        private int vip;
        private int sex;

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
    }
}
