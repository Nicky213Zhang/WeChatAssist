package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author: Twisted
 * created on: 2017/7/20 15:28
 * description:
 */
@DatabaseTable(tableName = "t_send_message")
public class SendMessageBean {
    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;
    @DatabaseField(canBeNull = false)
    private String messageText;

    public SendMessageBean() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


}
