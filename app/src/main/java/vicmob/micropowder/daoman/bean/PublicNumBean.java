package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eren on 2017/6/27.
 * <p/>
 * 创建漂流瓶数据库表实例
 */

@DatabaseTable(tableName="t_public_num_z")
public class PublicNumBean {
    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false)
    private String PublicNumName;

    public PublicNumBean() {
    }

    public String getPublicNumName() {
        return PublicNumName;
    }

    public void setPublicNumName(String publicNumName) {
        PublicNumName = publicNumName;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


}
