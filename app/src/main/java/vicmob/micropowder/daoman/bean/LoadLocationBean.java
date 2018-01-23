package vicmob.micropowder.daoman.bean;

import java.util.List;

/**
 * Created by Eren on 2017/7/13.
 * <p/>
 * 网络获取地点实例
 */
public class LoadLocationBean {


    private List<AutoDataBean> auto_data;

    public List<AutoDataBean> getAuto_data() {
        return auto_data;
    }

    public void setAuto_data(List<AutoDataBean> auto_data) {
        this.auto_data = auto_data;
    }

    public static class AutoDataBean {
        /**
         * dataId : 123
         * address : 江苏省盐城市建湖县汇文西路91号
         * hello : 你好
         * longitude : 119.79710651921982
         * latitude : 33.47312115832263
         * devices : 7YRBBDB712012865
         */

        private int dataId;
        private String address;
        private String hello;
        private String longitude;
        private String latitude;
        private String devices;
        private int mnc;
        private int lac;
        private int cid;

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getDevices() {
            return devices;
        }

        public void setDevices(String devices) {
            this.devices = devices;
        }

        public int getMnc() {
            return mnc;
        }

        public void setMnc(int mnc) {
            this.mnc = mnc;
        }

        public int getLac() {
            return lac;
        }

        public void setLac(int lac) {
            this.lac = lac;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }
    }
}
