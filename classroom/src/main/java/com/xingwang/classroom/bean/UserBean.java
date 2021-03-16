package com.xingwang.classroom.bean;


import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2019/8/19
 * Time;14:30
 * author:baiguiqiang
 */
public class UserBean extends CommonEntity {
    /**
     * data : {"authstr":"BgAJU10KAlFTV11TBQdVAlpVUFYMA1VTUwpQAVEBAwQNWlIHAlwHAlJOAQlTVg4OCAgOCxYAUA=="}
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
         * authstr : BgAJU10KAlFTV11TBQdVAlpVUFYMA1VTUwpQAVEBAwQNWlIHAlwHAlJOAQlTVg4OCAgOCxYAUA==
         */

        private String authstr;

        public String getAuthstr() {
            return authstr;
        }

        public void setAuthstr(String authstr) {
            this.authstr = authstr;
        }
    }
}

