package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.AutoReplyBean;

/**
 * Created by Twisted on 2017/7/4.
 */

public class AutoReplyDao {
    private DBHelper mDBHelper;
    private Dao<AutoReplyBean,Integer> mAutoReplyDao;

    public AutoReplyDao (Context context){
        try {
            mDBHelper=new DBHelper(context);
            mAutoReplyDao=mDBHelper.getDao(AutoReplyBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add (AutoReplyBean autoReplyBean){
        try {
            mAutoReplyDao.create(autoReplyBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条数据
     */
    public void delete(AutoReplyBean autoReplyBean){
        try {
            mAutoReplyDao.delete(autoReplyBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有记录
     */
    public List<AutoReplyBean> queryForAll(){
        List<AutoReplyBean> autoReply=new ArrayList<>();
        try {
            autoReply=mAutoReplyDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autoReply;
    }
}
