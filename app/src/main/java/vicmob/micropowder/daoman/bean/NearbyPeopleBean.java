package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eren on 2017/6/29.
 * <p/>
 * 创建附近人数据库表实例
 */

@DatabaseTable(tableName = "t_nearby_people")
public class NearbyPeopleBean {

    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false)
    private String NearbyContent;

    public NearbyPeopleBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNearbyContent() {
        return NearbyContent;
    }

    public void setNearbyContent(String nearbyContent) {
        NearbyContent = nearbyContent;
    }
}
