package com.xingwang.classroom.bean;


import com.xinwang.bgqbaselib.http.CommonEntity;

public class LiveInfoBean extends CommonEntity {

    /**
     * data : {"live":{"id":33,"category_id":7,"title":"ces","body":"<p>1<\/p>","cover":"http://xw518app.oss-cn-beijing.aliyuncs.com//default/2020/08/17/112723_bg1.jpg","start_time":1597634846,"is_end":1,"speaker":"1","state":1,"click":144,"guest_count":144,"fixed_state":2,"fixed_str":"<p>撒打发斯蒂芬多示范法付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付1撒打发斯蒂芬的<img src=\"http://oss.xw518app.xw518.com/default/2020/08/19/162534_直播806.jpg\"><\/p>","alias":"1","subscribe_redirect_url":"https://www.baidu.com。","chat_count":16}}
     */

    private InfoBean data;

    public InfoBean getData() {
        return data;
    }

    public void setData(InfoBean data) {
        this.data = data;
    }

    public static class InfoBean {
        /**
         * live : {"id":33,"category_id":7,"title":"ces","body":"<p>1<\/p>","cover":"http://xw518app.oss-cn-beijing.aliyuncs.com//default/2020/08/17/112723_bg1.jpg","start_time":1597634846,"is_end":1,"speaker":"1","state":1,"click":144,"guest_count":144,"fixed_state":2,"fixed_str":"<p>撒打发斯蒂芬多示范法付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付1撒打发斯蒂芬的<img src=\"http://oss.xw518app.xw518.com/default/2020/08/19/162534_直播806.jpg\"><\/p>","alias":"1","subscribe_redirect_url":"https://www.baidu.com。","chat_count":16}
         */

        private LiveBean live;

        public LiveBean getLive() {
            return live;
        }

        public void setLive(LiveBean live) {
            this.live = live;
        }

        public static class LiveBean {
            /**
             * id : 33
             * category_id : 7
             * title : ces
             * body : <p>1</p>
             * cover : http://xw518app.oss-cn-beijing.aliyuncs.com//default/2020/08/17/112723_bg1.jpg
             * start_time : 1597634846
             * is_end : 1
             * speaker : 1
             * state : 1
             * click : 144
             * guest_count : 144
             * fixed_state : 2
             * fixed_str : <p>撒打发斯蒂芬多示范法付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付1撒打发斯蒂芬的<img src="http://oss.xw518app.xw518.com/default/2020/08/19/162534_直播806.jpg"></p>
             * alias : 1
             * subscribe_redirect_url : https://www.baidu.com。
             * chat_count : 16
             */

            private int id;
            private int category_id;
            private String title;
            private String body;
            private String cover;
            private int start_time;
            private int is_end;
            private String speaker;
            private int state;
            private int click;
            private int guest_count;
            private int fixed_state;
            private String fixed_str;
            private String alias;
            private String subscribe_redirect_url;
            private int chat_count;

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

            public String getBody() {
                return body==null?"":body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getIs_end() {
                return is_end;
            }

            public void setIs_end(int is_end) {
                this.is_end = is_end;
            }

            public String getSpeaker() {
                return speaker;
            }

            public void setSpeaker(String speaker) {
                this.speaker = speaker;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getClick() {
                return click;
            }

            public void setClick(int click) {
                this.click = click;
            }

            public int getGuest_count() {
                return guest_count;
            }

            public void setGuest_count(int guest_count) {
                this.guest_count = guest_count;
            }

            public int getFixed_state() {
                return fixed_state;
            }

            public void setFixed_state(int fixed_state) {
                this.fixed_state = fixed_state;
            }

            public String getFixed_str() {
                return fixed_str;
            }

            public void setFixed_str(String fixed_str) {
                this.fixed_str = fixed_str;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getSubscribe_redirect_url() {
                return subscribe_redirect_url;
            }

            public void setSubscribe_redirect_url(String subscribe_redirect_url) {
                this.subscribe_redirect_url = subscribe_redirect_url;
            }

            public int getChat_count() {
                return chat_count;
            }

            public void setChat_count(int chat_count) {
                this.chat_count = chat_count;
            }
        }
    }
}
