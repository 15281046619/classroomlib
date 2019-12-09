package com.xingwang.classroom.bean;

import android.text.TextUtils;

import com.xingwang.classroom.http.CommonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/9/5
 * Time;14:23
 * author:baiguiqiang
 */
public class CommentBean extends CommonEntity {

    /**
     * data : {"comments":[{"id":195,"lecture_id":14,"bid":0,"body":"didgeridoo","pics":"","state":1,"customer_id":8,"to_customer_id":0,"publish_time":"2019-09-16 10:26:44","sub_comments":[],"user":{"id":8,"nickname":null,"avatar":null,"team_id":null,"fullname":null,"remark":null,"create_time":"2019-05-14 09:17:48","product_line":null,"phone":"15281046619","area":null}}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<CommentsBean> comments;

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * id : 195
             * lecture_id : 14
             * bid : 0
             * body : didgeridoo
             * pics :
             * state : 1
             * customer_id : 8
             * to_customer_id : 0
             * publish_time : 2019-09-16 10:26:44
             * sub_comments : []
             * user : {"id":8,"nickname":null,"avatar":null,"team_id":null,"fullname":null,"remark":null,"create_time":"2019-05-14 09:17:48","product_line":null,"phone":"15281046619","area":null}
             */
            public CommentsBean(int id,String body,String pics,int user_id,String publish_time,UserBean user,UserBean to_user){
                this.body =body;
                this.id = id;
                this.pics = pics;
                this.user_id = user_id;
                this.publish_time = publish_time;
                this.user = user;
                this.to_user = to_user;
            }
            private int id;
            private int lecture_id;
            private int bid;
            private int pid;

            public boolean isCurPosition() {
                return isCurPosition;
            }

            public void setCurPosition(boolean curPosition) {
                isCurPosition = curPosition;
            }

            private boolean isCurPosition=false;//是不是选中当前item 默认false

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            private String body;
            private String pics;
            private int state;
            private int user_id;
            private int to_user_id;
            private String publish_time;
            private UserBean user;
            private UserBean to_user;

            public UserBean getTo_user() {
                return to_user;
            }

            public void setTo_user(UserBean to_user) {
                this.to_user = to_user;
            }

            private List<CommentsBean> sub_comments;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLecture_id() {
                return lecture_id;
            }

            public void setLecture_id(int lecture_id) {
                this.lecture_id = lecture_id;
            }

            public int getBid() {
                return bid;
            }

            public void setBid(int bid) {
                this.bid = bid;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getPics() {
                return pics;
            }

            public void setPics(String pics) {
                this.pics = pics;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getTo_user_id() {
                return to_user_id;
            }

            public void setTo_user_id(int to_user_id) {
                this.to_user_id = to_user_id;
            }

            public String getPublish_time() {
                return publish_time;
            }

            public void setPublish_time(String publish_time) {
                this.publish_time = publish_time;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public List<CommentsBean> getSub_comments() {
                if (sub_comments==null){
                    sub_comments = new ArrayList<>();
                }
                return sub_comments;
            }

            public void setSub_comments(List<CommentsBean> sub_comments) {
                this.sub_comments = sub_comments;
            }

            public static class UserBean {
                /**
                 * id : 8
                 * nickname : null
                 * avatar : null
                 * team_id : null
                 * fullname : null
                 * remark : null
                 * create_time : 2019-05-14 09:17:48
                 * product_line : null
                 * phone : 15281046619
                 * area : null
                 */
                public UserBean(int id,String nickname,String phone,String avatar,String badge){
                    this.nickname =nickname;
                    this.phone = phone;
                    this.id = id;

                    this.avatar =avatar;
                    this. badge =badge;
                }

                private int id;
                private String nickname;
                private String avatar;
                private String phone;

                private String badge;//多个徽章用,分隔，例如gov,vip

                public String getBadge() {
                    return badge;
                }

                public void setBadge(String badge) {
                    this.badge = badge;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }


                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }


                public String getshowName() {
                    if (TextUtils.isEmpty(nickname)){
                        return phone;
                    }else {
                        return nickname;
                    }
                }
            }
        }
    }
}