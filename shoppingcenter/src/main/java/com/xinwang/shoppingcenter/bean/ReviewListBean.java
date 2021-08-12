package com.xinwang.shoppingcenter.bean;

import android.text.TextUtils;

import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.utils.GsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/6/4
 * Time;13:45
 * author:baiguiqiang
 */
public class ReviewListBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        public DataBean(int id, int goods_id, int sku_id, int user_id, int order_item_id, int score, String media, String content, int anonymous_state, String reply, int reply_state, int state, UserBean user, long review_time) {
            this.id = id;
            this.goods_id = goods_id;
            this.sku_id = sku_id;
            this.user_id = user_id;
            this.order_item_id = order_item_id;
            this.score = score;
            this.media = media;
            this.content = content;
            this.anonymous_state = anonymous_state;
            this.reply = reply;
            this.reply_state = reply_state;
            this.state = state;
            this.user = user;
            this.review_time = review_time;
        }

        /**
         * id : 1
         * goods_id : 3
         * sku_id : 105
         * user_id : 42
         * order_item_id : 3
         * score : 5
         * media :
         * content : 好纠结
         * anonymous_state : 1
         * reply :
         * reply_state : 1
         * state : 1
         * user : {"id":42,"phone":"182****9759","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/UQiaRf7n9bXQeJ44tzSvwTPFkghGibmgX0fwCUC8vUKDibrAHM4W4Mpj8yHpqRITJtAjdxC3VJB5fxnlcuQiaacWdw/132","nickname":"小谢","jifen":344,"profile":"你好哦","badge":"","create_time":"2019-06-19 11:44:20","state":1,"wxid":"o1D6XwwqG4iUshrTGiB4SSwSRCK0","unionid":"","inviter_id":0,"is_official":0,"launch_time":1622777298,"vip":1,"sex":3}
         */

        private int id;
        private int goods_id;
        private int sku_id;
        private int user_id;
        private int order_item_id;
        private int score;
        private String media;
        private String content;
        private int anonymous_state;
        private String reply;
        private int reply_state;
        private int state;
        private UserBean user;
        private long review_time;

        public long getReview_time() {
            return review_time;
        }

        public void setReview_time(long review_time) {
            this.review_time = review_time;
        }

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

        public int getSku_id() {
            return sku_id;
        }

        public void setSku_id(int sku_id) {
            this.sku_id = sku_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getOrder_item_id() {
            return order_item_id;
        }

        public void setOrder_item_id(int order_item_id) {
            this.order_item_id = order_item_id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getMedia() {
            return media;
        }
        public List<GoodsDetailBean.MediaBean> getMediaList(){
            if (!TextUtils.isEmpty(media)){
                try {
                    return GsonUtils.changeGsonToSafeList(media, GoodsDetailBean.MediaBean.class);
                }catch (Exception e){
                    try {
                        List<String> mString =GsonUtils.changeGsonToSafeList(media,String.class);
                        List<GoodsDetailBean.MediaBean> mediaBeans =new ArrayList<>();
                        for (int i=0;i<mString.size();i++)
                            mediaBeans.add(new GoodsDetailBean.MediaBean(mString.get(i),0,mString.get(i)));
                        return mediaBeans;
                    }catch (Exception e1){
                        e1.printStackTrace();
                        return new ArrayList<>();
                    }

                }

            }
            return new ArrayList<>();
        }
        public void setMedia(String media) {
            this.media = media;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAnonymous_state() {
            return anonymous_state;
        }

        public void setAnonymous_state(int anonymous_state) {
            this.anonymous_state = anonymous_state;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public int getReply_state() {
            return reply_state;
        }

        public void setReply_state(int reply_state) {
            this.reply_state = reply_state;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements Serializable{
            public UserBean() {
            }

            public UserBean(int id, String avatar, String nickname) {
                this.id = id;
                this.avatar = avatar;
                this.nickname = nickname;
            }

            /**
             * id : 42
             * phone : 182****9759
             * avatar : https://thirdwx.qlogo.cn/mmopen/vi_32/UQiaRf7n9bXQeJ44tzSvwTPFkghGibmgX0fwCUC8vUKDibrAHM4W4Mpj8yHpqRITJtAjdxC3VJB5fxnlcuQiaacWdw/132
             * nickname : 小谢
             * jifen : 344
             * profile : 你好哦
             * badge :
             * create_time : 2019-06-19 11:44:20
             * state : 1
             * wxid : o1D6XwwqG4iUshrTGiB4SSwSRCK0
             * unionid :
             * inviter_id : 0
             * is_official : 0
             * launch_time : 1622777298
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
                return avatar==null?"":avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname==null?getPhone()+"":nickname;
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
                return badge==null?"":badge;
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
}
