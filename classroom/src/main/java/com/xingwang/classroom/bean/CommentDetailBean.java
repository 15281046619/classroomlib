package com.xingwang.classroom.bean;


import com.xingwang.classroom.http.CommonEntity;



/**
 * Date:2019/9/5
 * Time;14:23
 * author:baiguiqiang
 */
public class CommentDetailBean extends CommonEntity {

    /**
     * data : {"comments":[{"id":195,"lecture_id":14,"bid":0,"body":"didgeridoo","pics":"","state":1,"customer_id":8,"to_customer_id":0,"publish_time":"2019-09-16 10:26:44","sub_comments":[],"customer":{"id":8,"nickname":null,"avatar":null,"team_id":null,"fullname":null,"remark":null,"create_time":"2019-05-14 09:17:48","product_line":null,"phone":"15281046619","area":null}}]}
     */

    private CommentBean.DataBean.CommentsBean data;

    public CommentBean.DataBean.CommentsBean getData() {
        return data;
    }

    public void setData(CommentBean.DataBean.CommentsBean data) {
        this.data = data;
    }

}