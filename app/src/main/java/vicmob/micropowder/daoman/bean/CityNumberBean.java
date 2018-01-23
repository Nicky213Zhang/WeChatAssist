package vicmob.micropowder.daoman.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eren on 2017/7/14.
 * <p/>
 * 城市运营商号段实例
 */
public class CityNumberBean {
    private List<CityNumberBean.AutoCityBean> auto_city;

    public List<CityNumberBean.AutoCityBean> getcityNumberBeen() {
        return auto_city;
    }

    public void set_Auto_city(List<CityNumberBean.AutoCityBean> auto_city) {
        this.auto_city = auto_city;
    }
    /**
     * 0 : 2
     * id : 2
     * 1 : 北京
     * city : 北京
     * 2 : 联通
     * operator : 联通
     */
    /**
     * id : 83
     * city : 安顺
     * operator : 联通
     * devices : 7YRBBDB690708669
     */
    public static class AutoCityBean {
        private String id;
        private String city;
        private String operator;
        private String devices;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getDevices() {
            return devices;
        }

        public void setDevices(String devices) {
            this.devices = devices;
        }
    }
}
