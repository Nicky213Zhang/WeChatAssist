package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eren on 2017/7/6.
 * <p/>
 * 创建地图搜索结果数据库表实例
 */

@DatabaseTable(tableName = "t_map_search")
public class MapSearchBean {

    public MapSearchBean() {
    }

    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false)       //地址
    private String searchAddresses;

    @DatabaseField(canBeNull = false)       //纬度
    private double latitudes;

    @DatabaseField(canBeNull = false)       //经度
    private double longtitudes;

    @DatabaseField(canBeNull = false)       //基站lac
    private int lac;

    @DatabaseField(canBeNull = false)       //基站cid
    private int cid;

    @DatabaseField(canBeNull = false)       //运营商
    private int mnc;

    private int isLocal = 0;//1：网络，0：本地

    private String dataId;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSearchAddresses() {
        return searchAddresses;
    }

    public void setSearchAddresses(String searchAddresses) {
        this.searchAddresses = searchAddresses;
    }

    public Double getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(Double latitudes) {
        this.latitudes = latitudes;
    }

    public Double getLongtitudes() {
        return longtitudes;
    }

    public void setLongtitudes(Double longtitudes) {
        this.longtitudes = longtitudes;
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

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }
}
