package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eren on 2017/6/27.
 * <p/>
 * 创建朋友圈数据库表实例
 */

@DatabaseTable(tableName = "t_friend_circle")
public class FriendCircleContentBean {

    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false)
    private String FriendContent;

    public FriendCircleContentBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFriendContent() {
        return FriendContent;
    }

    public void setFriendContent(String friendContent) {
        FriendContent = friendContent;
    }
}
