package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Twisted on 2017/7/4.
 */
@DatabaseTable (tableName = "t_auto_reply")
public class AutoReplyBean {
    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;
    @DatabaseField(canBeNull = false)
    private  String KeyWord;
    @DatabaseField(canBeNull = false)
    private  String Content;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String keyWord) {
        KeyWord = keyWord;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
