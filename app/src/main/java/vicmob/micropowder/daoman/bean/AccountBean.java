package vicmob.micropowder.daoman.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Twisted on 2017/7/3.
 */
@DatabaseTable(tableName = "t_account")
public class AccountBean {
    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;
    @DatabaseField(canBeNull = false)
    private String UserName;
    @DatabaseField(canBeNull = false)
    private String PassWord;

    public AccountBean(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
