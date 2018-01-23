package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.SendMessageBean;

/**
 * author: Twisted
 * created on: 2017/7/20 15:26
 * description:
 */

public class SendMessageDao {
    private DBHelper mDbHelper;
    private Dao<SendMessageBean, Integer> mSendMessageDao;

    public SendMessageDao(Context context) {
            mDbHelper =  new DBHelper(context);
        try {
            mSendMessageDao = mDbHelper.getDao(SendMessageBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * 添加一条数据
     */
    public void add(SendMessageBean sendMessageBean) {
        try {
            mSendMessageDao.create(sendMessageBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param
     */
    public void delete(SendMessageBean sendMessageBean) {
        try {
            mSendMessageDao.delete(sendMessageBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条记录
     *
     * @param
     */
    public void update(SendMessageBean sendMessageBean) {
        try {
            mSendMessageDao.update(sendMessageBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    public SendMessageBean queryForId(int id) {
        SendMessageBean sendMessage = null;
        try {
            sendMessage = mSendMessageDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sendMessage;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<SendMessageBean> queryForAll() {
        List<SendMessageBean> mSendMessageList = new ArrayList<SendMessageBean>();
        try {
            mSendMessageList = mSendMessageDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mSendMessageList;
    }

    /**
     * 删除所有记录
     */
    public List<SendMessageBean> deleteAll() {
        List<SendMessageBean> mSendMessage = new ArrayList<SendMessageBean>();
        try {
            for (int i = 0; i < mSendMessage.size(); i++) {
                mSendMessageDao.delete(mSendMessage.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mSendMessage;
    }

    /**
     * 查询特殊字段是否存在
     *
     * @param parameter
     * @return
     */
    public boolean queryOne(String parameter) {
        List<SendMessageBean> mSendMessageBeanList = null;
        try {
            mSendMessageBeanList = mSendMessageDao.queryBuilder().where().eq("messageText", parameter).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mSendMessageBeanList.size() != 0 ? true : false;
    }
}
