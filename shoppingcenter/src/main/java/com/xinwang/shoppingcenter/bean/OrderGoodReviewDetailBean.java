package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2021/6/8
 * Time;14:46
 * author:baiguiqiang
 */
public class OrderGoodReviewDetailBean extends CommonEntity {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private ReviewListBean.DataBean review;

        public ReviewListBean.DataBean getReview() {
            return review;
        }

        public void setReview(ReviewListBean.DataBean review) {
            this.review = review;
        }
    }
}
