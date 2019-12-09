package com.xingwang.classroom.bean;

/**
 * Date:2019/10/10
 * Time;16:02
 * author:baiguiqiang
 * image
 * "ad":"[{\"time\":\"10\",\"type\":\"image\",\"body\":{\"img_src\":\"http://res.xw518.com/2019/09-29/15/5d905e6849bb9_timg (20).jpg\",\"link\":\"123456\"}}]"
 */
public class ADBean {
    private String time;
    private String type;
    private BodyBean body;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }


    public static class BodyBean{
        private String img_src;
        private String link;
        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
        public String getImg_src() {
            return img_src;
        }

        public void setImg_src(String img_src) {
            this.img_src = img_src;
        }
    }
}

