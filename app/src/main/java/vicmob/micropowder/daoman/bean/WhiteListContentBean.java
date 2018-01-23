package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eren on 2017/6/29.
 * <p/>
 * 白名单数据库表实例
 */

@DatabaseTable(tableName = "t_white_list")
public class WhiteListContentBean {


    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;

    public WhiteListContentBean() {
    }

    @DatabaseField(canBeNull = false)
    private String WhiteContent;

    @DatabaseField(canBeNull = true)
    private String Name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getWhiteContent() {
        return WhiteContent;
    }

    public void setWhiteContent(String whiteContent) {
        WhiteContent = whiteContent;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}

