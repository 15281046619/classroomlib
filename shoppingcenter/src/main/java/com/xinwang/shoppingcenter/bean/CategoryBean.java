package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Date:2021/3/24
 * Time;13:19
 * author:baiguiqiang
 */
public class CategoryBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * pid : 0
         * title : 测试栏目
         * state : 1
         * rank : 1
         * attr : [{"id":2,"category_id":1,"title":"测试属性0","field":"attr0","value":"规格","rank":1}]
         */

        private int id;
        private int pid;
        private String title;
        private int state;
        private int rank;
        private List<AttrBean> attr;
        private HashMap<String,Object> selectAttr=new HashMap<>();//储存选择得属性

        public HashMap<String, Object> getSelectAttr() {
            if (selectAttr==null){
                setSelectAttr(new HashMap<>());
            }
            return selectAttr;
        }

        public void setSelectAttr(HashMap<String, Object> selectAttr) {
            this.selectAttr = selectAttr;
        }

        public DataBean(int id, String title, List<AttrBean> attr) {
            this.id = id;
            this.title = title;
            this.attr = attr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public List<AttrBean> getAttr() {
            return attr==null?new ArrayList<>():attr;
        }

        public void setAttr(List<AttrBean> attr) {
            this.attr = attr;
        }

        public static class AttrBean implements Serializable{
            /**
             * id : 2
             * category_id : 1
             * title : 测试属性0
             * field : attr0
             * value : 规格
             * rank : 1
             */

            private int id;
            private int category_id;
            private String title;
            private String field;
            private String value;
            private int rank;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }
        }

    }

}
