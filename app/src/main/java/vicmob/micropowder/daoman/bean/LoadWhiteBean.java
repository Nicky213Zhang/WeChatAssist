package vicmob.micropowder.daoman.bean;

import java.util.List;

/**
 * Created by sunjing on 2017/10/19.
 */

public class LoadWhiteBean {

    private List<AutoDevicesBean> auto_devices;

    public List<AutoDevicesBean> getAuto_devices() {
        return auto_devices;
    }

    public void setAuto_devices(List<AutoDevicesBean> auto_devices) {
        this.auto_devices = auto_devices;
    }

    public static class AutoDevicesBean {
        /**
         * id : null
         * isNewRecord : true
         * remarks : null
         * createDate : null
         * updateDate : null
         * devicesId : 1
         * file : 10.xls
         * devices : 52b99baf7d34
         * isNewRecordCustomId : false
         */

        private Object id;
        private boolean isNewRecord;
        private Object remarks;
        private Object createDate;
        private Object updateDate;
        private int devicesId;
        private String file;
        private String devices;
        private int status;
        private boolean isNewRecordCustomId;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public int getDevicesId() {
            return devicesId;
        }

        public void setDevicesId(int devicesId) {
            this.devicesId = devicesId;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getDevices() {
            return devices;
        }

        public void setDevices(String devices) {
            this.devices = devices;
        }

        public boolean isIsNewRecordCustomId() {
            return isNewRecordCustomId;
        }

        public void setIsNewRecordCustomId(boolean isNewRecordCustomId) {
            this.isNewRecordCustomId = isNewRecordCustomId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
