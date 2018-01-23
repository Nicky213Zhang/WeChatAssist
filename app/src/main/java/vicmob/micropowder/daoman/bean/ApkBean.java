package vicmob.micropowder.daoman.bean;

/**
 * Created by Eren on 2017/6/19.
 */
public class ApkBean {

    /**
     * wxversion : {"versionCode":"25","versionName":"1.4","versionSpace":"34451400","versiondetail":"赚赚有新的更新"}
     */

    private WxversionBean wxversion;

    public WxversionBean getWxversion() {
        return wxversion;
    }

    public void setWxversion(WxversionBean wxversion) {
        this.wxversion = wxversion;
    }

    public static class WxversionBean {
        /**
         * versionCode
         * versionName
         * versionSpace
         * versiondetail
         */

        private int versionCode;
        private String versionName;
        private int versionSpace;
        private String versiondetail;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionSpace() {
            return versionSpace;
        }

        public void setVersionSpace(int versionSpace) {
            this.versionSpace = versionSpace;
        }

        public String getVersiondetail() {
            return versiondetail;
        }

        public void setVersiondetail(String versiondetail) {
            this.versiondetail = versiondetail;
        }
    }
}
