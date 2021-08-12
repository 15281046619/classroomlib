package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2021/7/27
 * Time;10:38
 * author:baiguiqiang
 */
public class CasesListBean extends CommonEntity {


    /**
     * data : {"total":123,"cases":[]}
     */

    private List<CaseBean> data;

    public List<CaseBean> getData() {
        return data;
    }

    public void setData(List<CaseBean> data) {
        this.data = data;
    }


    public static class CaseBean implements Serializable{
        private String id;
        private String title;
        private String cover;
        private String body;
        private String goods_ids;
        private int click;

        public CaseBean(String id, String title, String cover, String body, String goods_ids, int click) {
            this.id = id;
            this.title = title;
            this.cover = cover;
            this.body = body;
            this.goods_ids = goods_ids;
            this.click = click;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getGoods_ids() {
            return goods_ids;
        }

        public void setGoods_ids(String goods_ids) {
            this.goods_ids = goods_ids;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }
    }
}
