package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/4/22
 * Time;13:55
 * author:baiguiqiang
 */
public class AddressBean extends CommonEntity {
    /**
     * data : {"deliveryaddrs":"{\"defaultIndex\":-1,\"deliveryaddrs\":[{\"accurateAddress\":\"四川省成都市武侯区南沈路\",\"city\":\"上海\",\"consignee\":\"过过瘾\",\"phone\":\"1552255\"}]}"}
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
         * deliveryaddrs : {"defaultIndex":-1,"deliveryaddrs":[{"accurateAddress":"四川省成都市武侯区南沈路","city":"上海","consignee":"过过瘾","phone":"1552255"}]}
         */

        private String deliveryaddrs;

        public String getDeliveryaddrs() {
            return deliveryaddrs;
        }

        public void setDeliveryaddrs(String deliveryaddrs) {
            this.deliveryaddrs = deliveryaddrs;
        }
    }



    public static class  DataBean2{

        private String defaultId;
        private List<DeliveryaddrsBean> deliveryaddrs;

        public String getDefaultId() {
            return defaultId==null?"":defaultId;
        }

        public void setDefaultId(String defaultId) {
            this.defaultId = defaultId;
        }

        public List<DeliveryaddrsBean> getDeliveryaddrs() {
            return deliveryaddrs==null?new ArrayList<>():deliveryaddrs;
        }

        public void setDeliveryaddrs(List<DeliveryaddrsBean> deliveryaddrs) {
            this.deliveryaddrs = deliveryaddrs;
        }

        public static class DeliveryaddrsBean {

            private String id;
            private String accurateAddress;
            private String city;
            private String phone;
            private String consignee;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getConsignee() {
                return consignee;
            }

            public void setConsignee(String consignee) {
                this.consignee = consignee;
            }

            public String getAccurateAddress() {
                return accurateAddress;
            }

            public void setAccurateAddress(String accurateAddress) {
                this.accurateAddress = accurateAddress;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}
