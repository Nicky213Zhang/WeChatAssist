package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eren on 2017/7/3.
 * <p/>
 * 创建城市号码段的数据库实例
 */
@DatabaseTable(tableName = "demo")
public class CityPhoneBean {

    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String opera;

    @DatabaseField(canBeNull = false)
    private String area;

    public CityPhoneBean() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpera() {
        return opera;
    }

    public void setOpera(String opera) {
        this.opera = opera;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
