package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.FriendCircleContentBean;

/**
 * Created by Eren on 2017/6/28.
 * <p>
 * 朋友圈数据的增删改查
 */
public class FriendCircleDao {

    private DBHelper mDbHelper;
    private Dao<FriendCircleContentBean, Integer> mFriendCircleDao;

    public FriendCircleDao(Context context) {
        try {
            mDbHelper = new DBHelper(context);
            mFriendCircleDao = mDbHelper.getDao(FriendCircleContentBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add(FriendCircleContentBean friendCircleContentBean) {
        try {
            mFriendCircleDao.create(friendCircleContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param friendCircleContentBean
     */
    public void delete(FriendCircleContentBean friendCircleContentBean) {
        try {
            mFriendCircleDao.delete(friendCircleContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条记录
     *
     * @param friendCircleContentBean
     */
    public void update(FriendCircleContentBean friendCircleContentBean) {
        try {
            mFriendCircleDao.update(friendCircleContentBean);
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
    public FriendCircleContentBean queryForId(int id) {
        FriendCircleContentBean friendCircle = null;
        try {
            friendCircle = mFriendCircleDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendCircle;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<FriendCircleContentBean> queryForAll() {
        List<FriendCircleContentBean> friendCircle = new ArrayList<FriendCircleContentBean>();
        try {
            friendCircle = mFriendCircleDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendCircle;
    }

    /**
     * 删除所有记录
     */
    public List<FriendCircleContentBean> deleteAll() {
        List<FriendCircleContentBean> friendCircle = new ArrayList<FriendCircleContentBean>();
        try {
            for (int i = 0; i < friendCircle.size(); i++) {
                mFriendCircleDao.delete(friendCircle.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendCircle;
    }

    /**
     * 查询特殊字段是否存在
     *
     * @param parameter
     * @return
     */
    public boolean queryOne(String parameter) {
        List<FriendCircleContentBean> mFriendCircleContent = null;
        try {
            mFriendCircleContent = mFriendCircleDao.queryBuilder().where().eq("FriendContent", parameter).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mFriendCircleContent.size() != 0 ? true : false;
    }
}
