package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2021/5/6
 * Time;11:07
 * author:baiguiqiang
 */
public class WaybillDetailBean extends CommonEntity {

    /**
     * data : {"id":1,"user_id":22,"type":"shunfeng","waybill_no":"535962308717","state":1,"items":[{"id":83,"user_id":22,"order_id":59,"sku_id":14,"num":1,"price":1,"admin_tips":"","waybill_id":1,"state":1,"pay_state":2,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":14,"goods_id":10,"price":1,"stock":11097,"cover":"http://oss.xw518app.xw518.com/default/2021/04/21/100850_效果图 (1).jpg","state":1,"sku0":"1","sku1":"1","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":1,"buy_verify":0,"rank":324234,"click":436,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}}]}
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
         * id : 1
         * user_id : 22
         * type : shunfeng
         * waybill_no : 535962308717
         * state : 1
         * items : [{"id":83,"user_id":22,"order_id":59,"sku_id":14,"num":1,"price":1,"admin_tips":"","waybill_id":1,"state":1,"pay_state":2,"cancel_state":1,"refund_state":1,"review_state":1,"sku":{"id":14,"goods_id":10,"price":1,"stock":11097,"cover":"http://oss.xw518app.xw518.com/default/2021/04/21/100850_效果图 (1).jpg","state":1,"sku0":"1","sku1":"1","sku2":"","sku3":"","sku4":"","sku5":"","sku6":"","sku7":"","sku8":"","sku9":""},"goods":{"id":10,"category_id":3,"title":"康恩贝B族维生素片复合vb多种维生素b男女性b1 b6 b2 b12 维生素b","cover":"http://oss.xw518app.xw518.com/default/2021/03/31/090407_229875808.jpg","video":"","pics":"[\"http://oss.xw518app.xw518.com/default/2021/04/07/113028_沙盘图1.jpg\"]","body":"<video class=\"ql-video\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4\"><\/video><p><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090458_tb_image_share_1617152450934.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090506_tb_image_share_1617152455851.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090512_tb_image_share_1617152460889.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090525_-218692286.jpg\"><img src=\"http://oss.xw518app.xw518.com/default/2021/03/31/090529_tb_image_share_1617152466237.jpg\"><\/p><p class=\"ql-align-center\"><span class=\"ql-size-small\">这是一个什么鬼东西 哈哈哈哈哈哈<\/span><\/p>","sku":"颜色 尺码","state":1,"min_price":1,"buy_verify":0,"rank":324234,"click":436,"attr0":"[\"21金维他\"]","attr1":"[]","attr2":"[]","attr3":"[]","attr4":"","attr5":"","attr6":"","attr7":"","attr8":"","attr9":""}}]
         */

        private int id;
        private int user_id;
        private String type;
        private String waybill_no;
        private int state;
        private List<OrderListBean.DataBean.OrdersBean.ItemsBean> items;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWaybill_no() {
            return waybill_no;
        }

        public void setWaybill_no(String waybill_no) {
            this.waybill_no = waybill_no;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public List<OrderListBean.DataBean.OrdersBean.ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<OrderListBean.DataBean.OrdersBean.ItemsBean> items) {
            this.items = items;
        }



    }
}
