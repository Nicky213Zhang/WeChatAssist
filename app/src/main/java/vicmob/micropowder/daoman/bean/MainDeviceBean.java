package vicmob.micropowder.daoman.bean;

import java.util.List;

/**
 * Created by sunjing on 2017/10/28.
 */

public class MainDeviceBean {

    private List<AutoControlBean> auto_control;

    public List<AutoControlBean> getAuto_control() {
        return auto_control;
    }

    public void setAuto_control(List<AutoControlBean> auto_control) {
        this.auto_control = auto_control;
    }

    public static class AutoControlBean {
        /**
         * id : null
         * isNewRecord : true
         * remarks : null
         * createDate : null
         * updateDate : null
         * controlId : 1
         * maindevices : 7YRBBDB690708669
         * partdevices : 8f9d977b7d14
         * isNewRecordCustomId : false
         */

        private Object id;
        private boolean isNewRecord;
        private Object remarks;
        private Object createDate;
        private Object updateDate;
        private int controlId;
        private String maindevices;
        private String partdevices;
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

        public int getControlId() {
            return controlId;
        }

        public void setControlId(int controlId) {
            this.controlId = controlId;
        }

        public String getMaindevices() {
            return maindevices;
        }

        public void setMaindevices(String maindevices) {
            this.maindevices = maindevices;
        }

        public String getPartdevices() {
            return partdevices;
        }

        public void setPartdevices(String partdevices) {
            this.partdevices = partdevices;
        }

        public boolean isIsNewRecordCustomId() {
            return isNewRecordCustomId;
        }

        public void setIsNewRecordCustomId(boolean isNewRecordCustomId) {
            this.isNewRecordCustomId = isNewRecordCustomId;
        }
    }
}
