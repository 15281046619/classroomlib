package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 * Date:2019/10/16
 * Time;10:22
 * author:baiguiqiang
 */
public class ADGroupBean extends CommonEntity {


    /**
     * status : 1
     * message : 获取成功！
     * data : [{"id":4,"group":"课程推荐","title":"测试1","desc":"vrsf","pic":"http://res.xw518.com/2019/10-16/09/5da67480be9a6_timg (7).jpg","uri":"www.baidu.com","weight":9}]
     */



    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4
         * group : 课程推荐
         * title : 测试1
         * desc : vrsf
         * pic : http://res.xw518.com/2019/10-16/09/5da67480be9a6_timg (7).jpg
         * uri : www.baidu.com
         * weight : 9
         */

        private int id;
        private String group;
        private String title;
        private String desc;
        private String pic;
        private String uri;
        private int weight;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
